package pro.trevor.tankgame.rule.impl.parameter.fallen;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.LineOfSight;
import pro.trevor.tankgame.util.MathUtil;
import pro.trevor.tankgame.util.Position;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FallenMovePositionSupplier implements AvailableParameterSupplier<Position> {

    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        Optional<PseudoTank> maybeTank = state.getBoard().gather(PseudoTank.class).findAny();
        if (maybeTank.isEmpty()) {
            return new DiscreteValueBound<>(Attribute.TARGET_POSITION, List.of());
        }
        PseudoTank tank = maybeTank.get();

        Set<Position> spacesInRange = MathUtil.allPossibleMoves(state.getBoard(), tank.getPosition(), tank.getOrElse(Attribute.SPEED, 0));
        spacesInRange.remove(tank.getPosition());

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, spacesInRange.stream()
                .filter((position) -> LineOfSight.hasLineOfSight(state, tank.getPosition(), position))
                .toList());
    }

}
