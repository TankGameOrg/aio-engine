package pro.trevor.tankgame.attribute;

import pro.trevor.tankgame.rule.impl.action.fallen.ActionType;
import pro.trevor.tankgame.rule.impl.action.specialize.Specialty;
import pro.trevor.tankgame.rule.impl.action.upgrade.Boon;
import pro.trevor.tankgame.state.board.Board;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.state.meta.Council;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.IRandom;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Attribute<E> {

    private static final Set<Attribute<?>> ATTRIBUTES = new HashSet<>();

    // Element attributes
    public static final Attribute<Position> POSITION = new Attribute<>("POSITION", Position.class);

    // Tank attributes
    public static final Attribute<PlayerRef> PLAYER_REF = new Attribute<>("PLAYER_REF", PlayerRef.class);
    public static final Attribute<Integer> SCRAP = new Attribute<>("SCRAP", Integer.class);
    public static final Attribute<Boolean> CAN_ACT = new Attribute<>("CAN_ACT", Boolean.class);
    public static final Attribute<Integer> RANGE = new Attribute<>("RANGE", Integer.class);
    public static final Attribute<Integer> SPEED = new Attribute<>("SPEED", Integer.class);
    public static final Attribute<Integer> DAMAGE_MODIFIER = new Attribute<>("DAMAGE_MODIFIER", Integer.class);
    public static final Attribute<Integer> DEFENSE_MODIFIER = new Attribute<>("DEFENSE_MODIFIER", Integer.class);
    public static final Attribute<Specialty> SPECIALTY = new Attribute<>("SPECIALTY", Specialty.class);
    public static final Attribute<Boon> BOON = new Attribute<>("BOON", Boon.class);
    public static final Attribute<PlayerRef> SPONSOR = new Attribute<>("SPONSOR", PlayerRef.class);
    public static final Attribute<ListEntity> OFFERED_SPONSORS = new Attribute<>("OFFERED_SPONSORS", ListEntity.class); // ListEntity<PlayerRef>

    //  PseudoTank attributes
    public static final Attribute<ListEntity> ACTION_OPTIONS = new Attribute<>("ACTION_OPTIONS", ListEntity.class); // ListEntity<LogEntry>

    // Durability attributes
    public static final Attribute<Integer> DURABILITY = new Attribute<>("DURABILITY", Integer.class);
    public static final Attribute<Integer> MAX_DURABILITY = new Attribute<>("MAX_DURABILITY", Integer.class);
    public static final Attribute<Boolean> DESTROYED = new Attribute<>("DESTROYED", Boolean.class);

    // Floor attributes
    public static final Attribute<Integer> REGENERATION = new Attribute<>("REGENERATION", Integer.class);
    public static final Attribute<Integer> DAMAGE = new Attribute<>("DAMAGE", Integer.class);

    // State attributes
    public static final Attribute<Integer> TICK = new Attribute<>("TICK", Integer.class);
    public static final Attribute<Boolean> RUNNING = new Attribute<>("RUNNING", Boolean.class);
    public static final Attribute<String> WINNER = new Attribute<>("WINNER", String.class);
    public static final Attribute<ListEntity> PLAYERS = new Attribute<>("PLAYERS", ListEntity.class); // ListEntity<Player>
    public static final Attribute<Council> COUNCIL = new Attribute<>("COUNCIL", Council.class);
    public static final Attribute<Board> BOARD = new Attribute<>("BOARD", Board.class);
    public static final Attribute<IRandom> RANDOM = new Attribute<>("RANDOM", IRandom.class);

    // Council attributes
    public static final Attribute<ListEntity> COUNCILLORS = new Attribute<>("COUNCILLORS", ListEntity.class); // ListEntity<Player>

    // Player attributes
    public static final Attribute<String> NAME = new Attribute<>("NAME", String.class);
    public static final Attribute<String> TEAM = new Attribute<>("TEAM", String.class);
    public static final Attribute<PlayerRef> SPONSORED_PLAYER = new Attribute<>("SPONSORED_PLAYER", PlayerRef.class);
    public static final Attribute<Boolean> IS_SPONSOR = new Attribute<>("IS_SPONSOR", Boolean.class);
    public static final Attribute<Boolean> CAN_BLESS = new Attribute<>("CAN_BLESS", Boolean.class);
    public static final Attribute<Boolean> HAS_COMPELLED_FALLEN = new Attribute<>("HAS_COMPELLED_FALLEN", Boolean.class);

    // Log entry attributes
    public static final Attribute<Boolean> SUCCESS = new Attribute<>("SUCCESS", Boolean.class);
    public static final Attribute<ListEntity> SUBENTRIES = new Attribute<>("SUBENTRIES", ListEntity.class); // ListEntity<LogEntry>
    public static final Attribute<String> ACTION = new Attribute<>("ACTION", String.class);
    public static final Attribute<PlayerRef> SUBJECT = new Attribute<>("SUBJECT", PlayerRef.class);
    public static final Attribute<Position> TARGET_POSITION = new Attribute<>("TARGET_POSITION", Position.class);
    public static final Attribute<PlayerRef> TARGET_PLAYER = new Attribute<>("TARGET_PLAYER", PlayerRef.class);
    public static final Attribute<Specialty> TARGET_SPECIALTY = new Attribute<>("TARGET_SPECIALTY", Specialty.class);
    public static final Attribute<Boon> TARGET_BOON = new Attribute<>("TARGET_BOON", Boon.class);
    public static final Attribute<Integer> DICE_ROLL = new Attribute<>("DICE_ROLL", Integer.class);
    public static final Attribute<Integer> TOTAL_DAMAGE = new Attribute<>("TOTAL_DAMAGE", Integer.class);
    public static final Attribute<ActionType> FALLEN_ACTION = new Attribute<>("FALLEN_ACTION", ActionType.class);

    // Random number generation attributes
    public static final Attribute<Long> RNG_SEED = new Attribute<>("RNG_SEED", Long.class);

    private final String attributeName;
    private final Class<E> attributeClass;

    public Attribute(String name, Class<E> attributeClass) {
        this.attributeName = name;
        this.attributeClass = attributeClass;

        if (ATTRIBUTES.contains(this)) {
            throw new IllegalArgumentException("Attribute '" + name + "' is already defined");
        } else {
            ATTRIBUTES.add(this);
        }
    }

    public String getName() {
        return attributeName;
    }

    public Class<E> getAttributeClass() {
        return attributeClass;
    }

    public static Optional<Attribute<?>> fromString(String attributeString) {
        return ATTRIBUTES.stream().filter((attr) -> attr.attributeName.equals(attributeString)).findAny();
    }

    public static <T> Optional<Attribute<T>> fromString(String attributeString, Class<T> type) {
        return ATTRIBUTES.stream()
                .filter((attr) -> attr.attributeName.equals(attributeString) && attr.attributeClass.isAssignableFrom(type))
                .map((attr) -> (Attribute<T>) attr)
                .findAny();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Attribute<?> attribute)) return false;
        return Objects.equals(attributeName, attribute.attributeName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributeName);
    }
}
