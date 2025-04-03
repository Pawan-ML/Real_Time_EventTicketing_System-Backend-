package oop.eventticketingsystem.model.tickets;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.Model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;
import static java.util.Collections.synchronizedList;

public class TicketPool implements TicketHandling{



    private static int totalTicketsCreated = 0;
    private static int totalTicketsSold = 0;
    public static List<Ticket> ticketPool = synchronizedList(new ArrayList<Ticket>());
    private final static Configuration configuration = Model.getConfiguration();
    private static int totalTickets = configuration.getNumberOfTickets();
    private static final int maxCapacity = configuration.getMaxTicketCapacity();
    private boolean limitReached = false;
    private boolean capacityReached = false;
    private boolean waitingForTicket = false;

    public TicketPool() {
        totalTickets = configuration.getNumberOfTickets();
    }

    public static int getTotalTicketsCreated() {
        return totalTicketsCreated;
    }

    public static int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public static int getAvailableTickets() {
        return totalTicketsCreated - totalTicketsSold;
    }


    @Override
    public synchronized int[] addTickets() {
        while (ticketPool.size() >= maxCapacity || limitReached) { // Condition to wait
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!limitReached) {
            if (ticketPool.size() < maxCapacity) {
                capacityReached = false;
            }
            if (totalTicketsCreated >= totalTickets) {
                System.out.println("Maximum number of tickets reached.");
                limitReached = true;
                return null;
            }
            if (ticketPool.size() >= maxCapacity && !capacityReached) {
                System.out.println("Max Capacity Reached, waiting for tickets to be sold...");
                capacityReached = true;
                return null;
            }
            Ticket ticket = new Ticket();
            ticketPool.add(ticket);
            totalTicketsCreated++;
            System.out.printf("""
                    \nTotal Tickets released: %d
                    Total Tickets sold: %d
                    Tickets Remaining: %d\n""", totalTicketsCreated, totalTicketsSold, totalTicketsCreated - totalTicketsSold);
            notifyAll();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public synchronized void removeTickets() {
        while (ticketPool.isEmpty() && totalTicketsSold < totalTickets) { // Condition to wait
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (ticketPool.size() > 0) {
            waitingForTicket = false;
        }
        if(ticketPool.isEmpty() && totalTicketsSold == totalTickets){
            System.out.println("\nAll tickets have been sold out!");
            System.out.println("Exiting System... Have a nice day!");
            exit(0);
        } else if (ticketPool.isEmpty() && !waitingForTicket) {
            System.out.println("Waiting for tickets to be released.");
            waitingForTicket = true;
            return;
        }
        if (ticketPool.isEmpty() && waitingForTicket) {
            return;
        }
        totalTicketsSold++;
        ticketPool.remove(ticketPool.size() - 1);
        System.out.printf("""
                \nTotal Tickets released: %d
                Total Tickets sold: %d
                Tickets Remaining: %d\n""", totalTicketsCreated, totalTicketsSold, totalTicketsCreated - totalTicketsSold);
        notifyAll();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
