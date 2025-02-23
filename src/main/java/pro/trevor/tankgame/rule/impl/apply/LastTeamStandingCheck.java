package pro.trevor.tankgame.rule.impl.apply;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.apply.Apply;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LastTeamStandingCheck implements Apply {
    @Override
    public Optional<LogEntry> apply(State state) {
        List<Tank> tanks = state.getBoard().gatherUnits(Tank.class)
                .filter((tank) -> tank.has(Attribute.TEAM))
                .toList();

        Set<String> teams = tanks.stream()
                .map(((tank) -> tank.getUnsafe(Attribute.TEAM)))
                .collect(Collectors.toSet());

        int numberOfTeams = teams.size();
        if (numberOfTeams == 0) {
            state.put(Attribute.WINNER, "Nobody");
            state.put(Attribute.RUNNING, false);
        } else if (numberOfTeams == 1) {
            String winningTeam = teams.stream().findFirst().get();
            List<PlayerRef> winners = new ArrayList<>(state.getPlayers().stream()
                    .filter(((player) -> player.get(Attribute.TEAM).map((team) -> team.equals(winningTeam)).orElse(false)))
                    .map(Player::toRef).toList());
            for (Tank tank : tanks) {
                if (tank.has(Attribute.SPONSOR)) {
                    winners.add(tank.getUnsafe(Attribute.SPONSOR));
                }
            }

            StringBuilder winnerString = new StringBuilder(winners.getFirst().getName());
            for (int i = 1; i < winners.size(); i++) {
                winnerString.append(", ").append(winners.get(i));
            }

            state.put(Attribute.WINNER, winnerString.toString());
            state.put(Attribute.RUNNING, false);
        }

        return Optional.empty();
    }
}
