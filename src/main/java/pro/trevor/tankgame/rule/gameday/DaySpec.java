package pro.trevor.tankgame.rule.gameday;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Codec;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.Calendar;
import java.util.Objects;

@JsonType(name = "DaySpec")
public class DaySpec implements IJsonObject {

    // The day of the week for which this specification applies; see Calendar.DAY_OF_WEEK
    private final int dayOfWeek;

    // The localized open time in minutes from 00:00
    private final int localizedOpenTime;

    // The localized close time in minutes from 00:00; must be greater than localizedOpenTime
    private final int localizedCloseTime;

    public DaySpec(int dayOfWeek, int localizedOpenTime, int localizedCloseTime) {
        assert localizedOpenTime < localizedCloseTime;

        this.dayOfWeek = dayOfWeek;
        this.localizedOpenTime = localizedOpenTime;
        this.localizedCloseTime = localizedCloseTime;
    }

    public DaySpec(JSONObject json) {
        this.dayOfWeek = json.getInt("day");
        this.localizedOpenTime = json.getInt("open");
        this.localizedCloseTime = json.getInt("close");
        assert localizedOpenTime < localizedCloseTime;
    }

    public boolean isOpen(Calendar calendar) {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int currentMinute = hours * 60 + minutes;
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        return currentMinute >= localizedOpenTime && currentMinute < localizedCloseTime && currentDay == dayOfWeek;

    }

    @Override
    public JSONObject toJson() {
        JSONObject output = new JSONObject();

        output.put("day", dayOfWeek);
        output.put("open", localizedOpenTime);
        output.put("close", localizedCloseTime);
        output.put("class", Codec.typeFromClass(getClass()));

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DaySpec daySpec)) return false;
        return dayOfWeek == daySpec.dayOfWeek && localizedOpenTime == daySpec.localizedOpenTime && localizedCloseTime == daySpec.localizedCloseTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, localizedOpenTime, localizedCloseTime);
    }
}
