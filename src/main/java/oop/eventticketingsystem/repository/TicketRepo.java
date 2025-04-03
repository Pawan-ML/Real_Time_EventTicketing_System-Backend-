package oop.eventticketingsystem.repository;

import oop.eventticketingsystem.model.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {
}
