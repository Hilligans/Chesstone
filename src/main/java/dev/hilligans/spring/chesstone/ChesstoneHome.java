package dev.hilligans.spring.chesstone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ChesstoneHome {

    @GetMapping("/chesstone")
    public String chesstone(HttpServletRequest request) {

        for(Cookie cookie : request.getCookies()) {
            if("chesstone_token".equals(cookie.getName())) {
                System.out.println("Cookie " + cookie.getValue());
            }
        }

        return "chesstone"; // it is your home.html in your /resources/static folder.
    }

}