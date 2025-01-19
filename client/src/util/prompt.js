export default function promptMatches(message, default_, expected) {
    return prompt(message, default_).toLowerCase() === expected.toLowerCase();
}