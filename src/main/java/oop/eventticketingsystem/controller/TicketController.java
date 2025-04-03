package oop.eventticketingsystem.controller;

import oop.eventticketingsystem.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ticketingSystem")
public class TicketController {

    @Autowired
    TicketingService ticketService;

    @PostMapping("/start-system")
    public ResponseEntity<String> startTicketingSystem(){
        ticketService.startTicketingService();
        return ResponseEntity.ok("Ticketing service started");
    }

    @PostMapping("/stop-system")
    public ResponseEntity<String> stopTicketingSystem() throws InterruptedException {
        ticketService.stopTicketingService();
        return ResponseEntity.ok("Ticketing service stopped");
    }


}
