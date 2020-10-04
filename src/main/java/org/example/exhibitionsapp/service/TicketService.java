package org.example.exhibitionsapp.service;

import org.example.exhibitionsapp.entity.Ticket;
import org.example.exhibitionsapp.entity.TicketStatus;
import org.example.exhibitionsapp.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    Logger logger = LoggerFactory.getLogger(TicketService.class);
    @Autowired
    TicketRepository ticketRepository;

    public void addNewTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Transactional
    public boolean setTicketInactive(Long id) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if (ticketOptional.isEmpty()) {
            logger.debug("Searching incorrect ticket number: " + id);
            return false;
        }
        Ticket ticket = ticketOptional.get();
        if (ticket.getTicketStatus().equals(TicketStatus.TICKET_REFUNDED)) {
            logger.debug("Attemp to inactivate ticket already refunded: " + ticket.getId());
            return false;
        }
        ticket.setTicketStatus(TicketStatus.TICKET_REFUNDED);
        ticketRepository.save(ticket);
        logger.debug("Ticket inactivated.");
        return true;
    }

    public boolean setTicketCancelled(Ticket ticket){
        if (ticket.getTicketStatus().equals(TicketStatus.TICKET_ACTIVE)) {
            ticket.setTicketStatus(TicketStatus.TICKET_WAITING_REFUND);
            ticketRepository.save(ticket);
            logger.info("Inactivated ticket: " + ticket);
            return true;
        }
        logger.info("Failed to inactivate ticket: " + ticket);
        return false;
    }

    @Transactional
    public void cancellGroupOfTickets(List<Ticket> ticketList) {
        for (Ticket t: ticketList) {
            setTicketCancelled(t);
        }
    }

    public List<Ticket> findTicketsByUserId(Long id) {
        return ticketRepository.findAllByCustomer_Id(id);
    }

    public List<Ticket> findTicketsByExhibitionId(Long id){
        return ticketRepository.findAllByExhibition_Id(id);
    }

    public List<Ticket> findTicketsByStatus(String status){
        TicketStatus ticketStatus = TicketStatus.valueOf(status);
        return ticketRepository.findAllByTicketStatus(ticketStatus);
    }




}
