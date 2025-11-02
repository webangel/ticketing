package com.scalar.ticketing.app.springboot_crud.application.service;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;


public interface TicketService {
    // Unirse a la cola
    Ticket joinQueue(String userId,  String eventId);

    // Intento de comprrar
    Ticket attemptPurchase(String ticketId);
    
    //generar ticket
    String generateTicketId(String userId);
}
