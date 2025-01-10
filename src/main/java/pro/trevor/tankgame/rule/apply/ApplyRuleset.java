package pro.trevor.tankgame.rule.apply;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplyRuleset {

    private final List<ApplyRule> rules;

    public ApplyRuleset() {
        this.rules = new ArrayList<>();
    }

    public ApplyRuleset(List<ApplyRule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    public ApplyRuleset(ApplyRule... rules) {
        this.rules = new ArrayList<>(Arrays.stream(rules).toList());
    }

    public void add(ApplyRule rule) {
        rules.add(rule);
    }
}
