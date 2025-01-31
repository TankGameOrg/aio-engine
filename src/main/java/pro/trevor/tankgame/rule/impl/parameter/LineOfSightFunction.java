package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.util.Position;

public interface LineOfSightFunction {

    boolean inLineOfSight(State state, Position source, Position target);

}
