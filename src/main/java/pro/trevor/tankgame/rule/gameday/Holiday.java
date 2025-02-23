package pro.trevor.tankgame.rule.gameday;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Codec;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.Calendar;
import java.util.Objects;

@JsonType(name = "Holiday")
public class Holiday implements IJsonObject {

    private final int day;
    private final int month;
    private final int year;

    public Holiday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Holiday(JSONObject json) {
        this.day = json.getInt("day");
        this.month = json.getInt("month");
        this.year = json.getInt("year");
    }

    public boolean isHoliday(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Holiday holiday)) return false;
        return day == holiday.day && month == holiday.month && year == holiday.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    @Override
    public JSONObject toJson() {
        JSONObject output = new JSONObject();

        output.put("day", day);
        output.put("month", month);
        output.put("year", year);
        output.put("class", Codec.typeFromClass(getClass()));

        return output;
    }
}
