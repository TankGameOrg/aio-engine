package pro.trevor.tankgame.web.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
