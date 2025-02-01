import {positionToString} from "./position.js";

export default function entryToText(entry) {
    if (entry?.subject?.name && entry.action) {
        if (entry.action === "Shoot") {
            return `${entry.subject.name} shoots at ${positionToString(entry.target_position)} ${entry.dice_roll !== undefined ? `(${entry.dice_roll}${entry.total_damage === entry.dice_roll ? "" : `; ${entry.total_damage} total`} damage)` : ""}`;
        } else if (entry.action === "Move") {
            return `${entry.subject.name} moves to ${positionToString(entry.target_position)}`;
        } else if (entry.action === "Specialize") {
            return `${entry.subject.name} specializes in ${entry.target_specialty.variant}`;
        } else if (entry.action === "Mine") {
            return `${entry.subject.name} mines and finds ${entry.scrap} scrap`;
        } else if (entry.action === "Repair") {
            return `${entry.subject.name} repairs unit on ${positionToString(entry.target_position)}`;
        } else if (entry.action === "Donate") {
            return `${entry.subject.name} donates ${entry.scrap} to unit on ${positionToString(entry.target_position)}`;
        } else {
            return `${entry.subject.name} takes action: ${entry.action}`;
        }
    } else if (entry?.tick) {
        return `Start of day ${entry.tick}`;
    } else {
        return "Unknown entry localization";
    }
}
