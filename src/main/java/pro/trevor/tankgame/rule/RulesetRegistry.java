package pro.trevor.tankgame.rule;

import pro.trevor.tankgame.Game;

import java.util.*;

public class RulesetRegistry {

    public static final RulesetRegistry REGISTRY = new RulesetRegistry();

    private final Map<String, RulesetRegister> RULESETS;

    private RulesetRegistry() {
        RULESETS = new HashMap<>();
    }

    public void register(RulesetRegister register) {
        if (RULESETS.containsKey(register.getIdentifier())) {
            throw new IllegalArgumentException("Ruleset identifier" + register.getIdentifier() + " is already registered");
        } else {
            RULESETS.put(register.getIdentifier(), register);
        }
    }

    public Optional<Game> createGame(String rulesetIdentifier) {
        if (!RULESETS.containsKey(rulesetIdentifier)) {
            return Optional.empty();
        }

        return Optional.of(new Game(RULESETS.get(rulesetIdentifier)));
    }

    public List<String> getAllRulesetIdentifiers() {
        return new ArrayList<>(RULESETS.keySet());
    }

}
