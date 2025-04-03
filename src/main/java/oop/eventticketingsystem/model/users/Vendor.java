package oop.eventticketingsystem.model.users;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.Model;
import oop.eventticketingsystem.model.tickets.TicketPool;
import oop.eventticketingsystem.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Vendor implements Runnable{

    private final int ticketReleaseRate;
    private final Configuration configuration = Model.getConfiguration();
    private final TicketPool ticketPool = Model.getTicketPool();
//    private WebSocketService webSocketService = new WebSocketService();
//    @Autowired
    private WebSocketService webSocketService;

    public Vendor() {
        this.ticketReleaseRate = configuration.getTicketReleaseRate();
    }

    @Autowired
    public Vendor(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        this.ticketReleaseRate = configuration.getTicketReleaseRate();
    }

    @Override
    public void run(){
        for (int i = 0; i < ticketReleaseRate; i++) {
            ticketPool.addTickets();
            int totalTickets = TicketPool.getTotalTicketsCreated();
            int soldTickets = TicketPool.getTotalTicketsSold();
            int availableTickets = TicketPool.getAvailableTickets();
            try{
                webSocketService.broadcastMessage(availableTickets,soldTickets,totalTickets);
            } catch (Exception e){
//                e.printStackTrace();
                continue;
            }
        }

    }
}
