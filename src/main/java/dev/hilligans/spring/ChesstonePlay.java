package dev.hilligans.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class ChesstonePlay {

   // @GetMapping("/chesstone/play/{id}")
    public String createGame(@PathVariable("id") String id) {
        System.out.println(id);
        return "chesstone.html"; //defect-details.html page name to open it
    }
}
