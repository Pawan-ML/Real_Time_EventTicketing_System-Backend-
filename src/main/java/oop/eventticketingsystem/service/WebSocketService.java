package oop.eventticketingsystem.service;

import oop.eventticketingsystem.model.tickets.TicketUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {


    @Autowired
    public SimpMessagingTemplate messagingTemplate;


    public WebSocketService() {}


    @MessageMapping("/tickets")
    @SendTo("/topic/tickets")
    public synchronized void broadcastMessage(int availableTickets, int totalSold, int totalReleased) {
        try {
            TicketUpdate updateMessage = new TicketUpdate(availableTickets, totalSold, totalReleased);
            messagingTemplate.convertAndSend("/topic/tickets", updateMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while sending message: " + e.getMessage());
        }
    }


}
