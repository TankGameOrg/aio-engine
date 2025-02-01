package pro.trevor.tankgame.rule.impl.action;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.IRandom;
import pro.trevor.tankgame.util.MathUtil;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.util.Random;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Mine implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        PlayerRef subject = maybeSubject.get();
        Optional<Tank> maybeTank = state.getTankForPlayerRef(subject);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject with a corresponding tank");
        }

        Tank tank = maybeTank.get();
        Position position = tank.getPosition();

        Set<Position> mines = new HashSet<>();

        if (MathUtil.isMine(state, position)) {
            MathUtil.findAllConnectedMines(mines, state, position);
        }
        int numberOfMines = mines.size();

        int numerator;
        int denominator;

        switch (numberOfMines) {
            case 0 -> {
                numerator = 1;
                denominator = 4;
            }
            case 1 -> {
                numerator = 1;
                denominator = 3;
            }
            default -> {
                numerator = numberOfMines - 1;
                denominator = numberOfMines;
            }
        }

        IRandom random = state.getOrElse(Attribute.RANDOM, new Random(System.currentTimeMillis()));
        boolean gainScrap = random.nextInt(denominator) < numerator;
        state.put(Attribute.RANDOM, random);

        tank.put(Attribute.CAN_ACT, false);
        if (gainScrap) {
            tank.put(Attribute.SCRAP, tank.getOrElse(Attribute.SCRAP, 0) + 1);
            entry.put(Attribute.SCRAP, 1);
        } else {
            entry.put(Attribute.SCRAP, 0);
        }

        return Error.NONE;
    }
}
