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
    DURABILITY: "Durability",
    DAMAGE_MODIFIER: "Damage Modifier",
    DEFENSE_MODIFIER: "Defense Modifier",
    SPECIALTY: "Specialty",
    BOON: "Upgrade",
    TEAM: "Team",
    SPONSOR: "Sponsor",
};

function orderingByLocale(locale) {
    return [
        locale.TEAM,
        locale.SPONSOR,
        locale.DURABILITY,
        locale.SCRAP,
        locale.DAMAGE_MODIFIER,
        locale.DEFENSE_MODIFIER,
        locale.RANGE,
        locale.SPEED,
        locale.SPECIALTY,
        locale.BOON,
    ];
}

export function objectToLocalizedObject(locale, object) {
    const output = {};
    Object.keys(object)
        .filter((key) => Object.keys(locale).indexOf(codeToLocale(key)) > -1)
        .forEach((key) => {
            if (object[key] !== undefined) {
                output[locale[codeToLocale(key)]] = object[key];
            }
        })
    return output;
}

export function objectToOrderedLocalizedObject(locale, object) {
    const localizedObject = objectToLocalizedObject(locale, object);
    const ordering = orderingByLocale(locale);
    return ordering
        .filter(((localeKey) => localizedObject[localeKey] !== undefined))
        .map((localeKey) => ({key: localeKey, value: localizedObject[localeKey]}));
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
        } else if (type === "Specialty" || type === "Boon") {
            return object.name;
        }
    }
    return `Unhandled type ${type}`;
}

export { ENGLISH_LOCALE };
