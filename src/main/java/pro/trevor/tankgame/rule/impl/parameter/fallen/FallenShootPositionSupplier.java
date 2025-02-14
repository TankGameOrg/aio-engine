package pro.trevor.tankgame.rule.impl.parameter.fallen;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.rule.impl.parameter.LineOfSightFunction;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.MathUtil;
import pro.trevor.tankgame.util.Position;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FallenShootPositionSupplier implements AvailableParameterSupplier<Position> {

    private final LineOfSightFunction lineOfSight;

    public FallenShootPositionSupplier(LineOfSightFunction lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        Optional<PseudoTank> maybeTank = state.getBoard().gather(PseudoTank.class).findAny();
        if (maybeTank.isEmpty()) {
            return new DiscreteValueBound<>(Attribute.TARGET_POSITION, List.of());
        }
        PseudoTank tank = maybeTank.get();

        Set<Position> spacesInRange = MathUtil.getSpacesInRange(state.getBoard(), tank.getPosition(), tank.getOrElse(Attribute.RANGE, 0));

        return new DiscreteValueBound<>(Attribute.TARGET_POSITION, spacesInRange.stream()
                .filter((position) -> lineOfSight.inLineOfSight(state, tank.getPosition(), position))
                .toList());
    }
}
