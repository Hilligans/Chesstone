package dev.hilligans.spring.chesstone;

import dev.hilligans.spring.SocketTextHandler;
import dev.hilligans.spring.chesstone.CreateGameData;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CreateGame {

    @GetMapping(value = "/chesstone/create", produces = "application/json")
    public CreateGameData create(@RequestParam String mode, HttpServletResponse response) {
        System.out.println(mode);
        return new CreateGameData("123454");
    }
}