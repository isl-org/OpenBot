const { object, string, union, literal } = require('valibot');

const buildSchema = object({
    prompt: string(),
    level: union([
        literal("beginner"),
        literal("intermediate"),
        literal("advanced"),
    ]),
});

const insightSchema = object({
    image: string(),
    xml: string(),
    level: union([
        literal("beginner"),
        literal("intermediate"),
        literal("advanced"),
    ]),
});

module.exports = { buildSchema, insightSchema };
