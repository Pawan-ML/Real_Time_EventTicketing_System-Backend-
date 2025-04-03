package oop.eventticketingsystem.model.tickets;

import lombok.Getter;

import java.util.Calendar;

@Getter
public class TicketUpdate {

    private int availableTickets;
    private int totalTicketsSold;
    private int totalTicketsReleased;

    public TicketUpdate(int availableTickets, int ticketsSold, int ticketsReleased) {
        this.availableTickets = availableTickets;
        this.totalTicketsSold = ticketsSold;
        this.totalTicketsReleased = ticketsReleased;
    }

    public String getTicketTime(){
        Calendar calendar = Calendar.getInstance();
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = Integer.toString(calendar.get(Calendar.MINUTE));
        String seconds = Integer.toString(calendar.get(Calendar.SECOND));
        return hour + ":" + minutes + ":" + seconds;
    }



}
