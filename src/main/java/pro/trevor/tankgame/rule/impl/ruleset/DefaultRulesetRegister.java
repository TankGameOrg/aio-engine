package pro.trevor.tankgame.rule.impl.ruleset;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.ActionRule;
import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.action.Description;
import pro.trevor.tankgame.rule.action.Predicate;
import pro.trevor.tankgame.rule.action.parameter.Parameter;
import pro.trevor.tankgame.rule.apply.ApplyRule;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;
import pro.trevor.tankgame.rule.apply.TargetApplyRule;
import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;
import pro.trevor.tankgame.rule.impl.action.Mine;
import pro.trevor.tankgame.rule.impl.action.Move;
import pro.trevor.tankgame.rule.impl.action.Repair;
import pro.trevor.tankgame.rule.impl.action.Shoot;
import pro.trevor.tankgame.rule.impl.action.fallen.MoveFallen;
import pro.trevor.tankgame.rule.impl.action.fallen.RemainFallen;
import pro.trevor.tankgame.rule.impl.action.fallen.ShootFallen;
import pro.trevor.tankgame.rule.impl.action.specialize.AttributeModifier;
import pro.trevor.tankgame.rule.impl.action.specialize.Specialize;
import pro.trevor.tankgame.rule.impl.action.specialize.Specialty;
import pro.trevor.tankgame.rule.impl.action.sponsor.AcceptSponsorship;
import pro.trevor.tankgame.rule.impl.action.sponsor.Bless;
import pro.trevor.tankgame.rule.impl.action.sponsor.OfferSponsorship;
import pro.trevor.tankgame.rule.impl.action.upgrade.Boon;
import pro.trevor.tankgame.rule.impl.action.upgrade.Upgrade;
import pro.trevor.tankgame.rule.impl.apply.ApplyRandomActionOption;
import pro.trevor.tankgame.rule.impl.apply.ModifyAttribute;
import pro.trevor.tankgame.rule.impl.apply.LastTeamStandingCheck;
import pro.trevor.tankgame.rule.impl.handle.*;
import pro.trevor.tankgame.rule.impl.parameter.*;
import pro.trevor.tankgame.rule.impl.parameter.fallen.FallenMovePositionSupplier;
import pro.trevor.tankgame.rule.impl.parameter.fallen.FallenShootPositionSupplier;
import pro.trevor.tankgame.rule.impl.predicate.*;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.LineOfSight;

import java.util.Collections;
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

        Specialty offenseSpec = new Specialty("Offense", new AttributeModifier(Attribute.DAMAGE_MODIFIER, 1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1));
        Specialty defenseSpec = new Specialty("Defense", new AttributeModifier(Attribute.DAMAGE_MODIFIER, -1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, 2));
        Specialty scoutSpec = new Specialty("Scout", new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1), new AttributeModifier(Attribute.SPEED, 1), new AttributeModifier(Attribute.RANGE, 1));
        Specialty meleeSpec = new Specialty("Melee", new AttributeModifier(Attribute.DAMAGE_MODIFIER, 1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1), new AttributeModifier(Attribute.SPEED, 1), new AttributeModifier(Attribute.RANGE, -1));
        List<Specialty> specialties = List.of(offenseSpec, defenseSpec, scoutSpec, meleeSpec);
        actionRuleset.add(
                new Description("Specialize", "Spend four scrap to hone your tank to a specialized style of combat"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasScrapPrecondition(4), new PlayerTankCanActPreondition()), List.of()),
                        new Specialize(4, specialties), new Parameter<>("Specialty", Attribute.TARGET_SPECIALTY, new SpecialtySupplier(specialties))
                )
        );

        Boon attackBoon = new Boon("Attack", Attribute.DAMAGE_MODIFIER, 1);
        Boon defenseBoon = new Boon("Defence", Attribute.DEFENSE_MODIFIER, 1);
        Boon rangeBoon = new Boon("Range", Attribute.RANGE, 1);
        Boon speedBoon = new Boon("Speed", Attribute.SPEED, 1);
        List<Boon> boons = List.of(attackBoon, defenseBoon, rangeBoon, speedBoon);
        actionRuleset.add(
                new Description("Upgrade", "Once per game, spend six scrap to upgrade an attribute of your tank"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition(), new PlayerTankHasNoBoonPrecondition(), new PlayerTankHasScrapPrecondition(6), new PlayerTankCanActPreondition()), List.of()),
                        new Upgrade(boons), new Parameter<>("Boon", Attribute.TARGET_BOON, new BoonSupplier(boons))
                )
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
                new Description("Bless Patron", "Bless your patron with an extra action today"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsAbsentPrecondition(), new CouncilorHasPatronPrecondition(), new CouncilorHasPatronWithTankPrecondition(), new CouncilorCanBlessPrecondition()), List.of()), new Bless())
        );
        actionRuleset.add(
                new Description("Fallen Shoot", "Compel the fallen tank to shoot at a position"),
                new ActionRule(new Predicate(List.of(new FallenExistsPrecondition(), new PlayerTankIsAbsentPrecondition(), new CouncilorHasNoPatronPrecondition(), new CouncilorCanCompelFallenPrecondition()), List.of()), new ShootFallen(),
                        new Parameter<>("Target", Attribute.TARGET_POSITION, new FallenShootPositionSupplier(LineOfSight::hasLineOfSight)))
        );
        actionRuleset.add(
                new Description("Fallen Move", "Compel the fallen to move to a new position"),
                new ActionRule(new Predicate(List.of(new FallenExistsPrecondition(), new PlayerTankIsAbsentPrecondition(), new CouncilorHasNoPatronPrecondition(), new CouncilorCanCompelFallenPrecondition()), List.of()), new MoveFallen(),
                        new Parameter<>("Target", Attribute.TARGET_POSITION, new FallenMovePositionSupplier()))
        );
        actionRuleset.add(
                new Description("Fallen Remain", "Compel the fallen to do nothing"),
                new ActionRule(new Predicate(List.of(new FallenExistsPrecondition(), new PlayerTankIsAbsentPrecondition(), new CouncilorHasNoPatronPrecondition(), new CouncilorCanCompelFallenPrecondition()), List.of()), new RemainFallen())
        );
    }

    @Override
    public void registerTickRules(Ruleset ruleset) {
        ApplyRuleset tickRuleset = ruleset.getTickRuleset();
        tickRuleset.add(new ApplyRule(new ApplyRandomActionOption(ruleset, LineOfSight::hasLineOfSight)));
        tickRuleset.add(new TargetApplyRule<>(new ModifyAttribute<>(Attribute.CAN_ACT, ((state, target) -> true)), Tank.class));
        tickRuleset.add(new TargetApplyRule<>(new ModifyAttribute<>(Attribute.HAS_COMPELLED_FALLEN, ((state, target) -> false)), Player.class));
    }

    @Override
    public void registerConditionalRules(Ruleset ruleset) {
        ApplyRuleset conditionalRuleset = ruleset.getConditionalRuleset();
        conditionalRuleset.add(new ApplyRule(new LastTeamStandingCheck()));
    }

    @Override
    public void registerDamageHandlers(List<Damage> damageHandlers) {
        damageHandlers.add(new Damage(new HasDurabilityPredicate().and(new IsTankPredicate()), new DamageDurabilityHandle(0, new int[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6})));
        damageHandlers.add(new Damage(new HasDurabilityPredicate().and(new IsPseudoTankPredicate()), new DamageDurabilityHandle(0, new int[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6})));
        damageHandlers.add(new Damage(new HasDurabilityPredicate(), new DamageDurabilityHandle(0)));
    }

    @Override
    public void registerDestroyHandlers(List<Destroy> destroysHandlers) {
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate(), new DestroyEntityHandle()));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsTankPredicate()), new DestroyTankHandle(2)));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsPseudoTankPredicate()), new DestroyTankHandle(2)));
        destroysHandlers.add(new Destroy(new HasZeroDurabilityPredicate().and(new IsWallPredicate()), new ExpandScrapHeapHandle()));
    }
}
