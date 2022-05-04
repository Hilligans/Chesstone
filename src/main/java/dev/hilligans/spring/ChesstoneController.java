package dev.hilligans.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ChesstoneController {

    @RequestMapping("/chesstone")
    public String chesstone() {
        return "chesstone.html"; //defect-details.html page name to open it
    }
}
