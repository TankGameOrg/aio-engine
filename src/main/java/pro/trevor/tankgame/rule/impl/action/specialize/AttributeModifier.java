package pro.trevor.tankgame.rule.impl.action.specialize;

import pro.trevor.tankgame.attribute.Attribute;

public record AttributeModifier(Attribute<Integer> attribute, Integer modifier) {
}
