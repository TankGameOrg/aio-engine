
export default function entryToText(entry) {
    if (entry?.subject?.name && entry.action) {
        if (entry.action === "shoot") {
            return `${entry.subject.name} shoots at ${entry.target_position}`;
        } else if (entry.action === "move") {
            return `${entry.subject.name} moves to ${entry.target_position}`;
        } else {
            return `${entry.subject.name} takes action: ${entry.action}`;
        }
    } else if (entry?.tick) {
        return `Start of day ${entry.tick}`;
    } else {
        return "Unknown entry";
    }
}
