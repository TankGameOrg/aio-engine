package pro.trevor.tankgame.rule;

import pro.trevor.tankgame.rule.action.ActionRuleset;
import pro.trevor.tankgame.rule.apply.ApplyRuleset;

public interface RulesetRegister {

    String getIdentifier();
    void registerPlayerRules(ActionRuleset actionRuleset);
    void registerTickRules(ApplyRuleset tickRuleset);
    void registerConditionalRules(ApplyRuleset conditionalRuleset);
    void registerInvariantRules(ApplyRuleset invariantRuleset);

}
