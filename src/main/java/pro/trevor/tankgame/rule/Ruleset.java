package pro.trevor.tankgame.rule;

import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;
import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;

import java.util.ArrayList;
import java.util.List;

public class Ruleset {

    private final ActionRuleset playerActionRuleset;
    private final ApplyRuleset tickRuleset;
    private final ApplyRuleset conditionalRuleset;
    private final ApplyRuleset invariantRuleset;

    private final List<Damage> damageHandlers;
    private final List<Destroy> destroyHandlers;

    public Ruleset() {
        this.playerActionRuleset = new ActionRuleset();
        this.tickRuleset = new ApplyRuleset();
        this.conditionalRuleset = new ApplyRuleset();
        this.invariantRuleset = new ApplyRuleset();

        this.damageHandlers = new ArrayList<>();
        this.destroyHandlers = new ArrayList<>();
    }

    public Ruleset(RulesetRegister register) {
        this();
        register.registerPlayerRules(this);
        register.registerTickRules(this);
        register.registerConditionalRules(this);
        register.registerInvariantRules(this);
        register.registerDamageHandlers(this.damageHandlers);
        register.registerDestroyHandlers(this.destroyHandlers);
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

    public List<Damage> getDamageHandlers() {
        return damageHandlers;
    }

    public List<Destroy> getDestroyHandlers() {
        return destroyHandlers;
    }
}
