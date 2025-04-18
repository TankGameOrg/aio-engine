package pro.trevor.tankgame.attribute;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;
import pro.trevor.tankgame.util.MathUtil;

import java.util.*;
import java.util.stream.Stream;

@JsonType(name = "ListEntity")
public class ListEntity<T> implements Collection<T>, Entity, IJsonObject {

    private final List<T> elements;

    public ListEntity() {
        this.elements = new ArrayList<>();
    }

    public ListEntity(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public ListEntity(JSONObject json) {
        this.elements = new ArrayList<>();
        JSONArray array = json.optJSONArray("elements");
        for (int i = 0; i < array.length(); ++i) {
            Object fromJson = array.get(i);
            if (fromJson instanceof JSONObject jsonObject) {
                elements.add((T) Codec.decodeJson(jsonObject));
            } else {
                // Assume we are working with a primitive or String
                elements.add((T) fromJson);
            }
        }
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int indexOf(T element) {
        return elements.indexOf(element);
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    public T get(int index) {
        return elements.get(index);
    }

    public boolean add(T element) {
        return elements.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(elements).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return elements.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elements.retainAll(c);
    }

    public void add(T element, int index) {
        elements.add(index, element);
    }

    public T remove(int index) {
        return elements.remove(index);
    }

    public void clear() {
        elements.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <U> U[] toArray(U[] a) {
        return elements.toArray(a);
    }

    @Override
    public String toString() {
        return MathUtil.toString(elements);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        elements.forEach((e) -> {
            if (e instanceof IJsonObject jsonObject) {
                array.put(jsonObject.toJson());
            } else {
                array.put(e);
            }
        });
        json.put("elements", array);
        return json;
    }

    public Stream<Object> gather() {
        Stream<Object> result = Stream.empty();
        for (Object value : elements) {
            if (value instanceof IJsonObject) {
                result = Stream.concat(result, Stream.of(value));
            }
            if (value instanceof Entity entity) {
                result = Stream.concat(result, entity.gather());
            }
        }
        return result;
    }
}
