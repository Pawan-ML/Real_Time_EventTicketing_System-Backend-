package oop.eventticketingsystem.model;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.tickets.TicketPool;

public class Model {

    private static Configuration config;
    private static TicketPool ticketPool;

    public static void setConfiguration(Configuration currentConfig) {
        config = currentConfig;
    }

    public static synchronized Configuration getConfiguration(){
        if(config == null){
            try{
                config = Configuration.loadConfig();
            } catch (RuntimeException e){
                System.out.println("Error while loading configuration");
                throw new RuntimeException(e);
            }
        }
        return config;
    }

    public static synchronized TicketPool getTicketPool(){
        if(ticketPool == null){
            ticketPool = new TicketPool();
        }
        return ticketPool;
    }
}
