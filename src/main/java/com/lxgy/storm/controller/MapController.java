package com.lxgy.storm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gryant
 */
@Controller
public class MapController {

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map() {
        return "map";
    }

}
