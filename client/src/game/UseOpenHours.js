import {useEffect, useState} from "react";
import {fetchOpenHours} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";

const daysOfTheWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

function zeroPadTime(time) {
    if (time < 10 ) {
        return `0${time}`;
    } else {
        return `${time}`;
    }
}

function daySpecToString(spec) {
    const dayOfWeek = daysOfTheWeek[spec.day - 1];
    const openHour = Math.floor(spec.open / 60);
    const openMinute = spec.open % 60;
    const closeHour = Math.floor(spec.close / 60);
    const closeMinute = spec.close % 60;
    return `${dayOfWeek} ${openHour}:${zeroPadTime(openMinute)} - ${closeHour}:${zeroPadTime(closeMinute)}`;
}


function useOpenHours(uuid) {

    const [openHours, setOpenHours] = useState([]);
    const [holidays, setHolidays] = useState([]);

    useEffect(() => {
        fetchOpenHours(SERVER_URL, uuid)
            .then((res) => res.json())
            .then((data) => {
                setOpenHours(data.open_hours);
                setHolidays(data.holidays);
            });
    }, []);

    const currentTime = new Date();
    const day = currentTime.getDay();
    const month = currentTime.getMonth();
    const year = currentTime.getFullYear();

    for (const holiday of holidays) {
        if (day === holiday.day && month === holiday.month && year === holiday.year) {
            return [false, false, 0, openHours, holidays];
        }
    }

    if (openHours.length === 0) {
        return [true, true, 0, [], holidays];
    }

    const dayOfWeek = currentTime.getDay() + 1;
    const hours = currentTime.getHours();
    const minutes = currentTime.getMinutes();
    const totalMinutes = hours * 60 + minutes;

    let isOpen = false;
    let opensToday = false;
    let timeToUpcoming = Infinity;
    for (const daySpec of openHours) {
        if (daySpec.day === dayOfWeek) {
            if (totalMinutes >= daySpec.open &&  totalMinutes < daySpec.close) {
                isOpen = true;
                opensToday = true;
                timeToUpcoming = 0;
                break;
            }
            const timeToThisOpening = daySpec.open - totalMinutes;
            if (timeToThisOpening >= 0 && timeToThisOpening < timeToUpcoming) {
                opensToday = true;
                timeToUpcoming = timeToThisOpening;
            }
        }
    }

    return [isOpen, opensToday, timeToUpcoming, openHours, holidays];
}

export default useOpenHours;

export { daySpecToString };
