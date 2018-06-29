package org.ccs.rosters.controller;

import org.ccs.rosters.manager.impl.TransportationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3000)
@RestController
@RequestMapping("api/v1/transportation")
public class TransportationController {

    @Autowired
    private TransportationManager transportationManager;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<String> getTransportationAutomation() {
        return new ResponseEntity<>(transportationManager.automateTransportation(), HttpStatus.OK);
    }

}
