package pro.trevor.tankgame.attribute;

import pro.trevor.tankgame.state.board.Board;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.state.meta.Council;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.IRandom;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Attribute<E> {

    private static final Set<Attribute<?>> ATTRIBUTES = new HashSet<>();

    // Element attributes
    public static final Attribute<Position> POSITION = new Attribute<>("POSITION", Position.class);
    public static final Attribute<Integer> DAYS_REMAINING = new Attribute<>("DAYS_REMAINING", Integer.class);

    // Tank attributes
    public static final Attribute<PlayerRef> PLAYER_REF = new Attribute<>("PLAYER_REF", PlayerRef.class);
    public static final Attribute<Integer> GOLD = new Attribute<>("GOLD", Integer.class);
    public static final Attribute<Integer> ACTION_POINTS = new Attribute<>("ACTIONS", Integer.class);
    public static final Attribute<Integer> MAX_ACTION_POINTS = new Attribute<>("MAX_ACTIONS", Integer.class);
    public static final Attribute<Integer> RANGE = new Attribute<>("RANGE", Integer.class);
    public static final Attribute<Integer> SPEED = new Attribute<>("SPEED", Integer.class);
    public static final Attribute<Integer> PREVIOUS_SPEED = new Attribute<>("PREVIOUS_SPEED", Integer.class);
    public static final Attribute<Integer> BOUNTY = new Attribute<>("BOUNTY", Integer.class);
    public static final Attribute<Boolean> SLOWED = new Attribute<>("SLOWED", Boolean.class);
    public static final Attribute<Boolean> HASTENED = new Attribute<>("HASTENED", Boolean.class);

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
    public static final Attribute<ListEntity> PLAYERS = new Attribute<>("PLAYERS", ListEntity.class); // AttributeList<Player>
    public static final Attribute<Council> COUNCIL = new Attribute<>("COUNCIL", Council.class);
    public static final Attribute<Board> BOARD = new Attribute<>("BOARD", Board.class);
    public static final Attribute<IRandom> RANDOM = new Attribute<>("RANDOM", IRandom.class);

    // Council attributes
    public static final Attribute<ListEntity> COUNCILLORS = new Attribute<>("COUNCILLORS", ListEntity.class); // AttributeList<Player>
    public static final Attribute<ListEntity> SENATORS = new Attribute<>("SENATORS", ListEntity.class); // AttributeList<Player>
    public static final Attribute<Integer> COFFER = new Attribute<>("COFFER", Integer.class);
    public static final Attribute<Boolean> CAN_BOUNTY = new Attribute<>("CAN_BOUNTY", Boolean.class);
    public static final Attribute<Integer> ARMISTICE = new Attribute<>("ARMISTICE", Integer.class);
    public static final Attribute<Integer> MAX_ARMISTICE = new Attribute<>("MAX_ARMISTICE", Integer.class);

    // Player attributes
    public static final Attribute<String> NAME = new Attribute<>("NAME", String.class);
    public static final Attribute<String> TEAM = new Attribute<>("TEAM", String.class);
    public static final Attribute<Integer> POWER = new Attribute<>("POWER", Integer.class);
    public static final Attribute<Long> GLOBAL_COOLDOWN_END_TIME = new Attribute<>("GLOBAL_COOLDOWN_END_TIME", Long.class);

    // Log entry attributes
    public static final Attribute<Position> TARGET_POSITION = new Attribute<>("TARGET_POSITION", Position.class);
    public static final Attribute<PlayerRef> TARGET_PLAYER = new Attribute<>("TARGET_PLAYER", PlayerRef.class);
    public static final Attribute<Integer> DONATION = new Attribute<>("DONATION", Integer.class);
    public static final Attribute<Boolean> HIT = new Attribute<>("HIT", Boolean.class);
    public static final Attribute<Long> TIMESTAMP = new Attribute<>("TIMESTAMP", Long.class);
    public static final Attribute<Integer> DAY = new Attribute<>("DAY", Integer.class);
    public static final Attribute<PlayerRef> SUBJECT = new Attribute<>("SUBJECT", PlayerRef.class);
    public static final Attribute<String> ACTION = new Attribute<>("ACTION", String.class);

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