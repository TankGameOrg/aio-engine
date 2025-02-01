package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.MathUtil;
import pro.trevor.tankgame.util.Position;

import java.util.List;
import java.util.Set;

public class RepairPositionSupplier implements AvailableParameterSupplier<Position> {

    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        Tank tank = state.getTankForPlayerRef(player.toRef()).get();
        Set<Position> positions = MathUtil.allAdjacentMovablePositions(state.getBoard(), tank.getPosition());
        positions.add(tank.getPosition());

        List<Position> legalRepairs = positions.stream()
                .filter((position) -> ((AttributeEntity) state.getBoard().getUnit(tank.getPosition()).get()).has(Attribute.DURABILITY))
                .toList();

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, legalRepairs);
    }
}
