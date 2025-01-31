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

import java.util.Set;

public class ShootPositionSupplier implements AvailableParameterSupplier<Position> {

    private final LineOfSightFunction lineOfSight;

    public ShootPositionSupplier(LineOfSightFunction lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        Tank tank = state.getTankForPlayerRef(player.toRef()).get();
        Set<Position> spacesInRange = MathUtil.getSpacesInRange(state.getBoard(), tank.getPosition(), tank.getOrElse(Attribute.RANGE, 0));

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, spacesInRange.stream()
                .filter((position) -> lineOfSight.inLineOfSight(state, tank.getPosition(), position))
                .toList());
    }
}
