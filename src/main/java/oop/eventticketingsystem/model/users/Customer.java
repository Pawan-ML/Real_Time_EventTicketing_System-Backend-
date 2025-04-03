package oop.eventticketingsystem.model.users;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.Model;
import oop.eventticketingsystem.model.tickets.TicketPool;
import oop.eventticketingsystem.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Customer implements Runnable{

    public int customerRetrievalRate;
    private static final Configuration config = Model.getConfiguration();
//    private WebSocketService webSocketService = new WebSocketService();
//    @Autowired
    private WebSocketService webSocketService;

    public Customer() {
        this.customerRetrievalRate = config.getCustomerRetrievalRate();
    }

    @Autowired
    public Customer(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        this.customerRetrievalRate = config.getCustomerRetrievalRate();
    }
    @Override
    public void run(){
        for(int i = 0; i < customerRetrievalRate; i++){
            TicketPool ticketPool = Model.getTicketPool();
            ticketPool.removeTickets();
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
