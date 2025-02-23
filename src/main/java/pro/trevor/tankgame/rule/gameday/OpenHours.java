package pro.trevor.tankgame.rule.gameday;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@JsonType(name = "OpenHours")
public class OpenHours implements IJsonObject {

    private final Set<DaySpec> openHours;
    private final Set<Holiday> holidays;

    public OpenHours() {
        this.openHours = new HashSet<>();
        this.holidays = new HashSet<>();
    }

    public OpenHours(JSONObject json) {
        this.openHours = new HashSet<>();
        this.holidays = new HashSet<>();
        JSONArray openHoursJsonArray = json.getJSONArray("open_hours");
        for (int i = 0; i < openHoursJsonArray.length(); ++i) {
            JSONObject daySpecJson = openHoursJsonArray.getJSONObject(i);
            this.openHours.add(new DaySpec(daySpecJson));
        }
        JSONArray holidaysJsonArray = json.getJSONArray("holidays");
        for (int i = 0; i < holidaysJsonArray.length(); ++i) {
            JSONObject holidayJson = holidaysJsonArray.getJSONObject(i);
            this.holidays.add(new Holiday(holidayJson));
        }
    }

    public void addDaySpec(DaySpec daySpec) {
        this.openHours.add(daySpec);
    }

    public void addHoliday(Holiday holiday) {
        this.holidays.add(holiday);
    }

    public boolean isOpen(Calendar calendar) {
        for (Holiday holiday : holidays) {
            if (holiday.isHoliday(calendar)) {
                return false;
            }
        }

        for (DaySpec daySpec : openHours) {
            if (daySpec.isOpen(calendar)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public JSONObject toJson() {
        JSONObject output = new JSONObject();

        JSONArray openHoursArray = new JSONArray();
        for (DaySpec daySpec : openHours) {
            openHoursArray.put(daySpec.toJson());
        }
        output.put("open_hours", openHoursArray);

        JSONArray holidaysArray = new JSONArray();
        for (Holiday holiday : holidays) {
            holidaysArray.put(holiday.toJson());
        }
        output.put("holidays", holidaysArray);

        return output;
    }
}
