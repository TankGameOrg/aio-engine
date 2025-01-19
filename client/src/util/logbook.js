
export default function entryToText(entry) {
    if (entry?.subject?.name && entry.action) {
        return `${entry.subject.name} takes action ${entry.action}`
    } else if (entry?.tick) {
        return `Start of day ${entry.tick}`
    } else {
        return "Unknown entry"
    }
}
