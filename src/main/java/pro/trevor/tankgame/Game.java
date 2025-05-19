package pro.trevor.tankgame;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.*;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.parameter.Parameter;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Pair;

import java.util.*;

public class Game {

    private final RulesetRegister rulesetRegister;
    private final Ruleset ruleset;
    private State state;

    public Game(RulesetRegister register) {
        this.rulesetRegister = register;
        this.ruleset = new Ruleset(register);
        this.state = null;
    }

    public Game(RulesetRegister register, State state) {
        this.rulesetRegister = register;
        this.ruleset = new Ruleset(register);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public RulesetRegister getRulesetRegister() {
        return rulesetRegister;
    }

    public JSONObject ingestEntry(LogEntry entry) {
        if (!state.getOrElse(Attribute.RUNNING, true)) {
            return jsonError("The game is not currently running");
        }

        if (entry.get(Attribute.TICK).isPresent()) {
            List<LogEntry> entries = tick();
            JSONObject result = new JSONObject();
            result.put("error", false);
            result.put("message", "entries");

            JSONArray jsonArray = new JSONArray();
            for (LogEntry e : entries) {
                jsonArray.put(e.toJson());
            }
            result.put("entries", jsonArray);

            return result;
        } else if (entry.has(Attribute.ACTION) && entry.has(Attribute.SUBJECT)) {
            return ingestPlayerAction(entry);
        }
        return jsonError("Failed to ingest entry of unknown format");
    }

    private JSONObject ingestPlayerAction(LogEntry actionEntry) {
        PlayerRef subjectRef = actionEntry.getUnsafe(Attribute.SUBJECT);
        String actionName = actionEntry.getUnsafe(Attribute.ACTION);
        Optional<Player> maybePlayer = state.getPlayer(subjectRef);
        Optional<Pair<Description, ActionRule>> maybeAction = ruleset.getPlayerActionRuleset().get(actionName);

        if (maybePlayer.isEmpty()) {
            return jsonError("No player found for given subject: " + subjectRef);
        }

        if (maybeAction.isEmpty()) {
            return jsonError("No action rule found for given action: " + actionName);
        }

        Player subject = maybePlayer.get();
        ActionRule rule = maybeAction.get().right();

        for (Parameter<?> parameter : rule.getParameters()) {
            if (!parameter.entryHasParameter(actionEntry)) {
                return jsonError(String.format("Attribute %s not found for given action: %s", parameter.getAttribute().getName(), actionName));
            }
        }

        List<Error> preconditions = rule.getPredicate().test(state, subject);

        if (!preconditions.isEmpty()) {
            return jsonError("Failed preconditions check:\n" + preconditions.stream().map((Error::message)).reduce("", (l, r) -> l + "\n" + r));
        }

        Error conditions = rule.getPredicate().test(state, actionEntry);
        if (conditions != Error.NONE) {
            return jsonError("Failed conditions check:\n" + conditions.message());
        }

        Error error = rule.apply(state, actionEntry);

        if (error == Error.NONE) {
            checkConditions();
            return jsonSuccess();
        } else {
            return jsonError("Failed to apply:\n" + error.message());
        }

    }

    public List<LogEntry> tick() {
        List<LogEntry> entries = new ArrayList<>();
        ruleset.getTickRuleset().stream().forEach((tickRule) -> tickRule.apply(state).map(entries::add));
        checkConditions();
        state.put(Attribute.TICK, state.getOrElse(Attribute.TICK, 0) + 1);
        return entries;
    }

    public void checkConditions() {
        ruleset.getConditionalRuleset().stream().forEach((conditionRule) -> conditionRule.apply(state));
    }

    public JSONObject possibleActions(PlayerRef subjectRef) {
        Optional<Player> maybePlayer = state.getPlayer(subjectRef);

        if (maybePlayer.isEmpty()) {
            throw new IllegalStateException("No player found for given subject: " + subjectRef);
        }

        Player player = maybePlayer.get();

        JSONObject response = new JSONObject();
        response.put("error", false);
        response.put("message", "success");
        response.put("player_ref", subjectRef.toString());

        JSONArray possibleActionsArray = new JSONArray();

        for (Pair<Description, ActionRule> rulePair : ruleset.getPlayerActionRuleset().getAllRules()) {
            JSONObject possibleAction = new JSONObject();

            Description ruleDescription = rulePair.left();
            ActionRule rule = rulePair.right();

            possibleAction.put("name", ruleDescription.name());
            possibleAction.put("description", ruleDescription.description());

            List<Error> preconditionErrors = rule.getPredicate().test(state, player);
            if (preconditionErrors.stream().anyMatch((error) -> error.type() == Error.Type.PRECONDITION)) {
                continue;
            } else if (!preconditionErrors.isEmpty()) {
                possibleAction.put("error", preconditionErrors.stream()
                        .map(Error::message)
                        .reduce("",  (left, right) -> left + "; " + right).substring(2));
            } else {
                JSONArray parametersArray = new JSONArray();
                for (Parameter<?> parameter : rule.getParameters()) {
                    parametersArray.put(parameter.possibleParameters(state, player).toJson().put("name", parameter.getName()));
                }
                possibleAction.put("parameters", parametersArray);
            }
            possibleActionsArray.put(possibleAction);
        }

        return response.put("possible_actions", possibleActionsArray);
    }

    public JSONObject checkActionConditions(LogEntry entry) {
        PlayerRef subjectRef = entry.getUnsafe(Attribute.SUBJECT);
        String actionName = entry.getUnsafe(Attribute.ACTION);
        Optional<Player> maybePlayer = state.getPlayer(subjectRef);
        Optional<Pair<Description, ActionRule>> maybeAction = ruleset.getPlayerActionRuleset().get(actionName);

        if (maybePlayer.isEmpty()) {
            return jsonError("No player found for given subject: " + subjectRef);
        }
        Player player = maybePlayer.get();

        if (maybeAction.isEmpty()) {
            return jsonError("No action rule found for given action: " + actionName);
        }

        ActionRule rule = maybeAction.get().right();

        List<Error> preconditionErrors = rule.getPredicate().test(state, player);
        Error error = rule.getPredicate().test(state, entry);

        if (error == Error.NONE && preconditionErrors.isEmpty()) {
            return jsonSuccess();
        } else {
            return jsonError(error.message() + (preconditionErrors.isEmpty() ?
                    "" :
                    ("; " + preconditionErrors.stream()
                            .map(Error::message)
                            .reduce("",  (left, right) -> left + "; " + right).substring(2))));
        }
    }

    private static JSONObject jsonError(String message) {
        JSONObject response = new JSONObject();
        response.put("error", true);
        response.put("message", message);
        return response;
    }

    private static JSONObject jsonSuccess() {
        JSONObject response = new JSONObject();
        response.put("error", false);
        response.put("message", "success");
        return response;
    }

}
