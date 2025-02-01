import {positionToString} from "./position.js";

function codeToLocale(attribute) {
    if (attribute === undefined || attribute === null) {
        return undefined;
    }

    return attribute.substring(1);
}

const ENGLISH_LOCALE = {
    SCRAP: "Scrap",
    SPEED: "Speed",
    RANGE: "Range",
    PLAYER_REF: "Player",
    DURABILITY: "Durability",
    DAMAGE_MODIFIER: "Damage Modifier",
    DEFENSE_MODIFIER: "Defense Modifier",
    SPECIALTY: "Specialty",
    TEAM: "Team",
    GLORY: "Glory",
};

export function objectToLocalizedObject(locale, object) {
    const output = {};
    Object.keys(object)
        .filter((key) => Object.keys(locale).indexOf(codeToLocale(key)) > -1)
        .map((key) => {
            if (object[key]) {
                output[locale[codeToLocale(key)]] = object[key];
            }
        });
    return output;
}

export function objectToArray(object) {
    return Object.keys(object).map((key) => ({key: key, value: object[key]}));
}

export function codeObjectToString(object) {
    let type = typeof object;
    if (type === "number" || type === "boolean" || type === "string") {
        return `${object}`;
    } else if (type === "object") {
        type = object.class;
        if (type === "Position") {
            return positionToString(object);
        } else if (type === "PlayerRef") {
            return object.name;
        } else if (type === "Specialty") {
            return object.variant;
        }
    }
    return `Unhandled type ${type}`;
}

export { ENGLISH_LOCALE };
