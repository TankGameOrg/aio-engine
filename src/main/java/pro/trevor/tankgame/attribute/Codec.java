package pro.trevor.tankgame.attribute;

import org.json.JSONObject;
import pro.trevor.tankgame.util.BidiMap;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;
import pro.trevor.tankgame.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Codec {

    private static final BidiMap<String, Class<?>> typeToClass = new BidiMap<>();

    static {
        for (Class<?> c : ReflectionUtil.allClassesAnnotatedWith(JsonType.class, "pro.trevor.tankgame")) {
            if (!c.isEnum()) {
                try {
                    c.getConstructor(JSONObject.class);
                } catch (NoSuchMethodException ignored) {
                    System.err.printf("No constructor found for %s(JSONObject)\n", c.getSimpleName());
                }
            }
            typeToClass.put(typeFromClass(c), c);
        }
    }

    public static JSONObject encodeJson(IJsonObject jsonObject) {
        if (jsonObject instanceof Enum<?> enumObject) {
            return jsonObject.toJson().put("variant", enumObject.name()).put("class", typeFromClass(enumObject.getClass()));
        } else {
            return jsonObject.toJson().put("class", typeFromClass(jsonObject.getClass()));
        }
    }

    public static Object decodeJson(JSONObject json) {
        String className = json.getString("class");
        Class<?> objectClass = classFromType(className);
        if (objectClass.isEnum()) {
            return Enum.valueOf((Class<Enum>) objectClass, json.getString("variant"));
        } else {
            try {
                return objectClass.getConstructor(JSONObject.class).newInstance(json);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new Error("Error constructing from JSON: no matching constructor found for class " +
                        className, e);
            } catch (InstantiationException e) {
                throw new Error("Error constructing from JSON: failed to instantiate class " + className, e);
            }
        }
    }

    public static String typeFromClass(Class<?> c) {
        JsonType annotation = c.getAnnotation(JsonType.class);
        if (annotation == null) {
            return c.getSimpleName();
        } else {
            return annotation.name();
        }
    }

    public static Class<?> classFromType(String type) {
        Class<?> c = typeToClass.getValue(type);
        if (c == null) {
            try {
                return Class.forName(type);
            } catch (ClassNotFoundException e) {
                System.err.println("Could not find class " + type);
                throw new Error(e);
            }
        } else {
            return c;
        }
    }
}
