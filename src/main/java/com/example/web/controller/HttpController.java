package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.main.controllers.MainController;


@Validated
@RestController
public class HttpController {

    @Autowired
    private MainController mainController;
    
    @GetMapping("/populate")
    public ResponseEntity<Object> populateDatabase() {
        mainController.populateDatabaseWithSampleData();
        return new ResponseEntity<>("Base de datos poblada", HttpStatus.OK);
    }

}
