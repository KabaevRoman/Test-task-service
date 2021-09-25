package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class Controller {

    DataHandlerService dataHandlerService = new DataHandlerService();

    @RequestMapping(path = "/set/{key}/{content}")
    String set(@PathVariable String key, @PathVariable String content) {
        return dataHandlerService.setData(key, content);//default ttl is 5 seconds ttl is low for demonstration
    }

    @RequestMapping(path = "/set/{key}/{content}/{ttl}")
    String set(@PathVariable String key, @PathVariable String content, @PathVariable long ttl) {
        return dataHandlerService.setData(key, content, ttl);
    }

    @RequestMapping(path = "/get/{key}")
    Data get(@PathVariable String key) {
        if (dataHandlerService.dataMap.containsKey(key)) {
            return dataHandlerService.getData(key);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data with key: " + key + " not found");
        }
    }

    @RequestMapping(path = "/remove/{key}")
    Data remove(@PathVariable String key) {
        if (dataHandlerService.dataMap.containsKey(key)) {
            return dataHandlerService.remove(key);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data with key: " + key + " not found");
        }
    }

    @RequestMapping(path = "/dump")
    String dump() throws IOException {
        dataHandlerService.dump();
        return "Dump was successfully created";
    }

    @RequestMapping(path = "/load")
    String load() throws IOException, ClassNotFoundException {
        dataHandlerService.load();
        return "Saved data loaded";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public CustomError handleMyCustomException(ResponseStatusException ex) {
        return new CustomError(ex.getReason(), ex.getRawStatusCode());
    }
}
