package pro.trevor.tankgame.rule.action;

import pro.trevor.tankgame.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ActionRuleset {

    private final Map<Description, ActionRule> rules;

    public ActionRuleset() {
        rules = new HashMap<>();
    }

    public void add(Description description, ActionRule rule) {
        rules.put(description, rule);
    }

    public void add(String name, String description, ActionRule rule) {
        rules.put(new Description(name, description), rule);
    }

    public void add(String name, ActionRule rule) {
        rules.put(new Description(name, ""), rule);
    }

    public Optional<Pair<Description, ActionRule>> get(String name) {
        Optional<Description> description =  rules.keySet()
                .stream()
                .filter((d) -> d.name().equals(name))
                .findFirst();

        return description.map(value -> Pair.of(value, rules.get(value)));
    }

    public Set<Pair<Description, ActionRule>> getAllRules() {
        return rules.keySet().stream()
                .map(((description) -> Pair.of(description, rules.get(description))))
                .collect(Collectors.toSet());
    }

}
