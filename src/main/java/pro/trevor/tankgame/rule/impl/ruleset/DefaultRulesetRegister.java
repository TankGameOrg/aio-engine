package pro.trevor.tankgame.rule.impl.ruleset;

import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.ActionRule;
import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.action.Description;
import pro.trevor.tankgame.rule.action.Predicate;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;
import pro.trevor.tankgame.rule.impl.action.ExampleAction;

public class DefaultRulesetRegister implements RulesetRegister {

    @Override
    public void registerPlayerRules(ActionRuleset actionRuleset) {
        actionRuleset.add(
                new Description("ExampleAction", "An example rule that applies only if the player has a tank on the board"),
                new ActionRule(new Predicate(), new ExampleAction()));
    }

    @Override
    public void registerTickRules(ApplyRuleset tickRuleset) {

    }

    @Override
    public void registerConditionalRules(ApplyRuleset conditionalRuleset) {

    }

    @Override
    public void registerInvariantRules(ApplyRuleset invariantRuleset) {

    }
}
