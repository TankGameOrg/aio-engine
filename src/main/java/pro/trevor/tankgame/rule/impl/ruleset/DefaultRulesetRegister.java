package pro.trevor.tankgame.rule.impl.ruleset;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.action.ActionRule;
import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.action.Description;
import pro.trevor.tankgame.rule.action.Predicate;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.Parameter;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;
import pro.trevor.tankgame.rule.apply.TargetApplyRule;
import pro.trevor.tankgame.rule.impl.action.ExampleAction;
import pro.trevor.tankgame.rule.impl.action.Move;
import pro.trevor.tankgame.rule.impl.apply.ModifyAttribute;
import pro.trevor.tankgame.rule.impl.parameter.MovePositionSupplier;
import pro.trevor.tankgame.rule.impl.predicate.PlayerTankCanActCondition;
import pro.trevor.tankgame.rule.impl.predicate.PlayerTankHasAttributeCondition;
import pro.trevor.tankgame.rule.impl.predicate.PlayerTankIsPresentPrecondition;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.util.Position;

import java.util.List;

public class DefaultRulesetRegister implements RulesetRegister {

    @Override
    public String getIdentifier() {
        return "TestRuleset";
    }

    @Override
    public void registerPlayerRules(ActionRuleset actionRuleset) {
        actionRuleset.add(
                new Description("ExampleAction", "An example rule that applies only if the player has a tank on the board; adds the specified amount of gold to the player's tank"),
                new ActionRule(new Predicate(List.of(), List.of(new PlayerTankCanActCondition())), new ExampleAction(), new Parameter<>("Gold", Attribute.GOLD, (state, player) -> new DiscreteValueBound<>(Attribute.GOLD, 0, 1 ,2))));
        actionRuleset.add(
                new Description("Move", "Move your tank to a new position on the game board"),
                new ActionRule(new Predicate(List.of(new PlayerTankIsPresentPrecondition()), List.of(new PlayerTankHasAttributeCondition(Attribute.SPEED), new PlayerTankCanActCondition())), new Move(),
                        new Parameter<>("Position", Attribute.TARGET_POSITION, new MovePositionSupplier())));
    }

    @Override
    public void registerTickRules(ApplyRuleset tickRuleset) {
        tickRuleset.add(new TargetApplyRule<>(new ModifyAttribute<>(Attribute.CAN_ACT, ((state, target) -> true)), Tank.class));
    }

    @Override
    public void registerConditionalRules(ApplyRuleset conditionalRuleset) {

    }

    @Override
    public void registerInvariantRules(ApplyRuleset invariantRuleset) {
        invariantRuleset.add(new TargetApplyRule<>((state, target) -> target.put(Attribute.ACTION_POINTS, Math.min(5, target.getOrElse(Attribute.ACTION_POINTS, 0))), Tank.class));
    }
}
