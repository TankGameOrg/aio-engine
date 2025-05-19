import {positionToString} from "./position.js";

function subEntryToText(subentry) {
    if (subentry?.fallen_action) {
        const variant = subentry.fallen_action.variant;
        if (variant === "MOVE") {
            return `Fallen moves to ${positionToString(subentry.target_position)}`;
        } else if (variant === "SHOOT") {
            return `Fallen shoots at ${positionToString(subentry.target_position)}`;
        } else if (variant === "REMAIN") {
            return "Fallen remains";
        } else {
            return `Fallen takes action: ${variant}`
        }
    } else {
        return "Could not resolve subentry";
    }
}

export default function entryToText(entry) {
    if (entry?.subject?.name && entry?.action) {
        if (entry.action === "Shoot") {
            return `${entry.subject.name} shoots at ${positionToString(entry.target_position)} ${entry.dice_roll !== undefined ? `(${entry.dice_roll}${entry.total_damage === entry.dice_roll ? "" : `; ${entry.total_damage} total`} damage)` : ""}`;
        } else if (entry.action === "Move") {
            return `${entry.subject.name} moves to ${positionToString(entry.target_position)}`;
        } else if (entry.action === "Specialize") {
            return `${entry.subject.name} specializes in ${entry.target_specialty.name}`;
        } else if (entry.action === "Mine") {
            return `${entry.subject.name} mines and finds ${entry.scrap} scrap`;
        } else if (entry.action === "Repair") {
            return `${entry.subject.name} repairs unit on ${positionToString(entry.target_position)}`;
        } else if (entry.action === "Upgrade") {
            return `${entry.subject.name} upgrades ${entry.target_boon.name}`;
        } else if (entry.action === "Accept Sponsorship") {
            return `${entry.subject.name} accepts sponsorship from ${entry.target_player.name}`;
        } else if (entry.action === "Offer Sponsorship") {
            return `${entry.subject.name} offers sponsorship to ${entry.target_player.name}`;
        } else if (entry.action === "Bless Patron") {
            return `${entry.subject.name} blesses ${entry?.target_player?.name}`;
        } else if (entry.action === "Fallen Move") {
            return `${entry.subject.name} compels the fallen to move to ${positionToString(entry?.target_position)}`;
        } else if (entry.action === "Fallen Shoot") {
            return `${entry.subject.name} compels the fallen to shoot at ${positionToString(entry?.target_position)}`;
        } else if (entry.action === "Fallen Remain") {
            return `${entry.subject.name} compels the fallen to remain`;
        } else {
            return `${entry.subject.name} takes action: ${entry.action}`;
        }
    } else if (entry?.tick) {
        let text =  `Start of day ${entry.tick}`;
        const subentries = entry?.subentries?.elements;
        if (subentries !== undefined && subentries !== 0) {
            return (
                <>
                    <span>{text}</span>
                    { subentries.map((entry, index) =>
                        <>
                            <br/>
                            <span id={`${index}`}>{subEntryToText(entry)}</span>
                        </>
                    )}
                </>
            );
        }
        return <span>{text}</span>;

    } else {
        return "Unknown entry localization";
    }
}
