package pro.trevor.tankgame.rule.impl.ruleset;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.ActionRule;
import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.action.Description;
import pro.trevor.tankgame.rule.action.Predicate;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.Parameter;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;
import pro.trevor.tankgame.rule.apply.TargetApplyRule;
import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;
import pro.trevor.tankgame.rule.impl.action.Mine;
import pro.trevor.tankgame.rule.impl.action.Move;
import pro.trevor.tankgame.rule.impl.action.Repair;
import pro.trevor.tankgame.rule.impl.action.Shoot;
import pro.trevor.tankgame.rule.impl.action.specialize.Specialize;
import pro.trevor.tankgame.rule.impl.apply.ModifyAttribute;
import pro.trevor.tankgame.rule.impl.handle.*;
import pro.trevor.tankgame.rule.impl.parameter.MovePositionSupplier;
import pro.trevor.tankgame.rule.impl.parameter.RepairPositionSupplier;
import pro.trevor.tankgame.rule.impl.parameter.ShootPositionSupplier;
import pro.trevor.tankgame.rule.impl.parameter.SpecialtySupplier;
import pro.trevor.tankgame.rule.impl.predicate.*;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.util.LineOfSight;

import java.util.List;

public class DefaultRulesetRegister implements RulesetRegister {

    @Override
    public String getIdentifier() {
        return "TestRuleset";
    }

    @Override
    public void registerPlayerRules(Ruleset ruleset) {
        ActionRuleset actionRuleset = ruleset.getPlayerActionRuleset();
        actionRuleset.add(
                new Description("Mine", "Has a chance to obtain a scrap; the chance is greater if standing on a bigger scrap heap"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankCanActPreondition()), List.of()), new Mine()));
        actionRuleset.add(
                new Description("Move", "Move your tank to a new position on the game board"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankCanActPreondition(), new PlayerTankHasAttributePrecondition(Attribute.SPEED)), List.of()),
                        new Move(), new Parameter<>("Position", Attribute.TARGET_POSITION, new MovePositionSupplier())));
        actionRuleset.add(
                new Description("Shoot", "Shoot at a position to damage its occupant"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankCanActPreondition(), new PlayerTankHasAttributePrecondition(Attribute.RANGE)), List.of()),
                        new Shoot(ruleset), new Parameter<>("Target", Attribute.TARGET_POSITION, new ShootPositionSupplier(LineOfSight::hasLineOfSight))));
        actionRuleset.add(
                new Description("Specialize", "Once per game, hone your tank to a specialized style of combat"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasNoSpecialtyPrecondition(), new PlayerTankCanActPreondition()), List.of()),
                        new Specialize(), new Parameter<>("Specialty", Attribute.TARGET_SPECIALTY, new SpecialtySupplier()))
        );
        actionRuleset.add(
                new Description("Repair", "Spend two scrap to repair a target tank, wall, or bridge within range for two durability"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankCanActPreondition(), new PlayerTankHasScrapPrecondition(2)), List.of()),
                        new Repair(2, 2), new Parameter<>("Target", Attribute.TARGET_POSITION, new RepairPositionSupplier())));
    }

    @Override
    public void registerTickRules(Ruleset ruleset) {
        ApplyRuleset tickRuleset = ruleset.getTickRuleset();
        tickRuleset.add(new TargetApplyRule<>(new ModifyAttribute<>(Attribute.CAN_ACT, ((state, target) -> true)), Tank.class));
    }

    @Override
    public void registerConditionalRules(Ruleset ruleset) {
        ApplyRuleset tickRuleset = ruleset.getTickRuleset();
    }

    @Override
    public void registerInvariantRules(Ruleset ruleset) {
        ApplyRuleset invariantRuleset = ruleset.getInvariantRuleset();
    }

    @Override
    public void registerDamageHandlers(List<Damage> damageHandlers) {
        damageHandlers.add(new Damage(new HasDurabilityPredicate().and(new IsTankPredicate()), new DamageDurabilityHandle(new int[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6})));
        damageHandlers.add(new Damage(new HasDurabilityPredicate(), new DamageDurabilityHandle()));
    }

    @Override
    public void registerDestroyHandlers(List<Destroy> destroysHandlers) {
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsTankPredicate()), new DestroyTankHandle(2)));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate(), new DestroyEntityHandle()));
    }
}
