package pro.trevor.tankgame.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.trevor.tankgame.rule.RulesetRegistry;
import pro.trevor.tankgame.rule.impl.ruleset.DefaultRulesetRegister;

@SpringBootApplication
public class ServerMain {

    public static void main(String[] args) {
        RulesetRegistry.REGISTRY.register(new DefaultRulesetRegister());
        SpringApplication.run(ServerMain.class, args);
    }

}
