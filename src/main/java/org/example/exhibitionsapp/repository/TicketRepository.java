package org.example.exhibitionsapp.repository;

import org.example.exhibitionsapp.entity.Ticket;
import org.example.exhibitionsapp.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAll();
    List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus);
    List<Ticket> findAllByExhibition_Id(Long exhibitionId);
    List<Ticket> findAllByCustomer_Id(Long id);
    Optional<Ticket> findById(Long id);
    List<Ticket> findAllByCustomer_Email(String email);
}
