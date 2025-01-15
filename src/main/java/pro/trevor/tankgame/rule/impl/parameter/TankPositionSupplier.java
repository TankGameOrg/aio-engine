package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.Element;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.Position;

public class TankPositionSupplier implements AvailableParameterSupplier<Position> {
    @Override
    public ParameterBound<Position> possibleParameters(State state, Player player) {
        return new DiscreteValueBound<>(Attribute.POSITION, state.gather(Tank.class).map(Element::getPosition).toList());
    }
}
