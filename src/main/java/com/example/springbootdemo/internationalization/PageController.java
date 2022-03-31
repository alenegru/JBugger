package com.example.springbootdemo.internationalization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */

@Controller
public class PageController {
    @RequestMapping(value = "/international")
    public String getInternationalPage() {
        return "international";
    }
}
