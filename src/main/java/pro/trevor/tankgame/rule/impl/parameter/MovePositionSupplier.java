package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
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
import java.util.Optional;

public class MovePositionSupplier implements AvailableParameterSupplier<Position> {
    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        List<Position> positions = List.of();

        Optional<Tank> maybeTank = state.getTankForPlayerRef(player.toRef());
        if (maybeTank.isPresent()) {
            Tank tank = maybeTank.get();
            int speed = tank.getOrElse(Attribute.SPEED, 0);
            positions = new ArrayList<>(MathUtil.allPossibleMoves(state.getBoard(), tank.getPosition(), speed));
            positions.remove(tank.getPosition());
        }

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, positions);
    }
}
