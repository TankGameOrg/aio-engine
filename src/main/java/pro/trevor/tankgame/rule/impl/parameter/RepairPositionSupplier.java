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

import java.util.ArrayList;
import java.util.List;

public class RepairPositionSupplier implements AvailableParameterSupplier<Position> {

    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        Tank tank = state.getTankForPlayerRef(player.toRef()).get();
        Position[] positions = MathUtil.allAdjacentPositions(tank.getPosition());

        List<Position> legalPositions = new ArrayList<>();
        legalPositions.add(tank.getPosition());

        for (Position position : positions) {
            if (state.getBoard().isValidPosition(position) &&
                    state.getBoard().getUnit(position).get() instanceof AttributeEntity entity &&
                    entity.has(Attribute.DURABILITY)) {
                legalPositions.add(position);
            }
        }

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, legalPositions);
    }
}
