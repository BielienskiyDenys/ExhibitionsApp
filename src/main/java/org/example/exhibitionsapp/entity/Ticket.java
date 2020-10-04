package org.example.exhibitionsapp.entity;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Exhibition exhibition;
    @ManyToOne
    private User customer;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus = TicketStatus.TICKET_ACTIVE;
    private int quantity = 1;

    public Ticket() {
    }

    public Ticket(Exhibition exhibition, User customer, int quantity) {
        this.exhibition = exhibition;
        this.customer = customer;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", exhibition=" + exhibition.getId() +
                ", customer=" + customer.getId() +
                ", ticketStatus=" + ticketStatus +
                ", quantity=" + quantity +
                '}';
    }
}
