package pro.trevor.tankgame.rule;

import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;

public class Ruleset {

    private final ActionRuleset playerActionRuleset;
    private final ApplyRuleset tickRuleset;
    private final ApplyRuleset conditionalRuleset;
    private final ApplyRuleset invariantRuleset;

    public Ruleset() {
        this.playerActionRuleset = new ActionRuleset();
        this.tickRuleset = new ApplyRuleset();
        this.conditionalRuleset = new ApplyRuleset();
        this.invariantRuleset = new ApplyRuleset();
    }

    public ActionRuleset getPlayerActionRuleset() {
        return playerActionRuleset;
    }

    public ApplyRuleset getTickRuleset() {
        return tickRuleset;
    }

    public ApplyRuleset getConditionalRuleset() {
        return conditionalRuleset;
    }

    public ApplyRuleset getInvariantRuleset() {
        return invariantRuleset;
    }

    public Ruleset register(RulesetRegister register) {
        register.registerPlayerRules(playerActionRuleset);
        register.registerTickRules(tickRuleset);
        register.registerConditionalRules(conditionalRuleset);
        register.registerInvariantRules(invariantRuleset);
        return this;
    }

}
