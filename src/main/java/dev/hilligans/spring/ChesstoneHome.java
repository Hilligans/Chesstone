package dev.hilligans.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChesstoneHome {

    @GetMapping("/chesstone")
    public String chesstone() {


        return "chesstone"; // it is your home.html in your /resources/static folder.
    }

}