package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    DataHandler dataHandler = new DataHandler();

    @RequestMapping(path = "/set/{key}/{content}/{ttl}")
    void set(@PathVariable String key, @PathVariable String content, @PathVariable long ttl) {
        dataHandler.setData(key, content, ttl);
    }

    @RequestMapping(path = "/set/{key}/{content}")
    void set(@PathVariable String key, @PathVariable String content) {
        dataHandler.setData(key, content);//default ttl is 5 seconds ttl is low for demonstration
    }

    @RequestMapping(path = "/get/{key}")
    String get(@PathVariable String key) {
        return dataHandler.getData(key);
    }

    @RequestMapping(path = "/remove/{key}")
    void remove(@PathVariable String key) {
        dataHandler.remove(key);
    }
}
