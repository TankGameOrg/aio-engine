package pro.trevor.tankgame.rule.impl.ruleset;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.ActionRule;
import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.action.Description;
import pro.trevor.tankgame.rule.action.Predicate;
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
import pro.trevor.tankgame.rule.impl.action.sponsor.AcceptSponsorship;
import pro.trevor.tankgame.rule.impl.action.sponsor.Bless;
import pro.trevor.tankgame.rule.impl.action.sponsor.OfferSponsorship;
import pro.trevor.tankgame.rule.impl.action.upgrade.Upgrade;
import pro.trevor.tankgame.rule.impl.apply.ModifyAttribute;
import pro.trevor.tankgame.rule.impl.handle.*;
import pro.trevor.tankgame.rule.impl.parameter.*;
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
        // Player with tank rules
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
                new Description("Repair", "Spend two scrap to repair a target tank, wall, or bridge within range for two durability"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasScrapPrecondition(2), new PlayerTankCanActPreondition()), List.of()),
                        new Repair(2, 2), new Parameter<>("Target", Attribute.TARGET_POSITION, new RepairPositionSupplier())));
        actionRuleset.add(
                new Description("Specialize", "Sped four scrap to hone your tank to a specialized style of combat"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasScrapPrecondition(4), new PlayerTankCanActPreondition()), List.of()),
                        new Specialize(4), new Parameter<>("Specialty", Attribute.TARGET_SPECIALTY, new SpecialtySupplier()))
        );
        actionRuleset.add(
                new Description("Upgrade", "Once per game, upgrade an attribute of your tank"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasNoBoonPrecondition(), new PlayerTankHasScrapPrecondition(6), new PlayerTankCanActPreondition()), List.of()),
                        new Upgrade(), new Parameter<>("Boon", Attribute.TARGET_BOON, new BoonSupplier()))
        );
        actionRuleset.add(
                new Description("Accept Sponsorship", "Accept the sponsorship of a councilor; they may offer you certain benefits"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasNoSponsorPrecondition(), new PlayerTankHasSponsorOfferPrecondition()), List.of()),
                        new AcceptSponsorship(), new Parameter<>("Player", Attribute.TARGET_PLAYER, new AcceptSponsorshipSupplier()))
        );

        // Player without tank rules
        actionRuleset.add(
                new Description("Offer Sponsorship", "Offer to sponsor a tank; you lose your team alignment but win if the chosen tank wins"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsAbsentPrecondition(), new CouncilorHasNoPatronPrecondition()), List.of()),
                        new OfferSponsorship(), new Parameter<>("Player", Attribute.TARGET_PLAYER, new OfferSponsorshipSupplier()))
        );
        actionRuleset.add(
                new Description("Bless Patron", "Bless your patron with an extra action today "),
                new ActionRule(new Predicate(List.of(new PlayerTankIsAbsentPrecondition(), new CouncilorHasPatronPrecondition(), new CouncilorHasPatronWithTank()), List.of()), new Bless())

        );
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
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate(), new DestroyEntityHandle()));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsTankPredicate()), new DestroyTankHandle(2)));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsWallPredicate()), new ExpandScrapHeapHandle()));
    }
}
