package dev.iuredev.HelpDeskAPI.tickets.repository;

import dev.iuredev.HelpDeskAPI.tickets.model.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Long> {
}
