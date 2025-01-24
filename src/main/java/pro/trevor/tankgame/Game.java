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

    private final String rulesetIdentifier;
    private final Ruleset ruleset;
    private State state;

    public Game(RulesetRegister register) {
        this.rulesetIdentifier = register.getIdentifier();
        this.ruleset = new Ruleset(register);
        this.state = null;
    }

    public Game(RulesetRegister register, State state) {
        this.rulesetIdentifier = register.getIdentifier();
        this.ruleset = new Ruleset(register);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getRulesetIdentifier() {
        return rulesetIdentifier;
    }

    public JSONObject ingestEntry(LogEntry entry) {
        if (entry.get(Attribute.TICK).isPresent()) {
            tick();
            return jsonSuccess();
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

        rule.apply(state, actionEntry);

        return jsonSuccess();
    }

    public void tick() {
        ruleset.getTickRuleset().stream().forEach((tickRule) -> {
            tickRule.apply(state);
            enforceInvariants();
        });
        state.put(Attribute.TICK, state.getOrElse(Attribute.TICK, 0) + 1);
    }

    public void checkConditions() {
        ruleset.getConditionalRuleset().stream().forEach((conditionRule) -> {
            conditionRule.apply(state);
            enforceInvariants();
        });
    }

    public void enforceInvariants() {
        ruleset.getInvariantRuleset().stream().forEach((invariantRule) -> {
            invariantRule.apply(state);
        });
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

            JSONArray parametersArray = new JSONArray();
            for (Parameter<?> parameter : rule.getParameters()) {
                parametersArray.put(parameter.possibleParameters(state, player).toJson().put("name", parameter.getName()));
            }
            possibleAction.put("parameters", parametersArray);

            possibleActionsArray.put(possibleAction);
        }

        return response.put("possible_actions", possibleActionsArray);
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
