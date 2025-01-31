package pro.trevor.tankgame.rule;

import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;

import java.util.List;

public interface RulesetRegister {

    String getIdentifier();
    void registerPlayerRules(Ruleset ruleset);
    void registerTickRules(Ruleset ruleset);
    void registerConditionalRules(Ruleset ruleset);
    void registerInvariantRules(Ruleset ruleset);
    void registerDamageHandlers(List<Damage> damageHandlers);
    void registerDestroyHandlers(List<Destroy> destroysHandlers);

}
