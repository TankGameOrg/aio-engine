package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.state.board.unit.PseudoTank;

import java.util.function.Predicate;

public class IsPseudoTankPredicate implements Predicate<AttributeEntity> {

    @Override
    public boolean test(AttributeEntity entity) {
        return entity instanceof PseudoTank;
    }

}
