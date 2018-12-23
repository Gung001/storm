package com.lxgy.storm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * springboot integrate hbase
 * https://github.com/SpringForAll/spring-boot-starter-hbase#%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F
 * @author Gryant
 */
@Controller
public class MapController {

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map() {
        return "map";
    }

}
