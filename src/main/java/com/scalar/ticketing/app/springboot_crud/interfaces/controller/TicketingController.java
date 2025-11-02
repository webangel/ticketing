package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.TicketService;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.TicketRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.TicketResponseDTO;

@RestController
@RequestMapping("/api/tickets")
public class TicketingController {
    private final TicketService ticketingService;

    public TicketingController(TicketService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @PostMapping("/join")
    public TicketResponseDTO joinQueue(@RequestBody TicketRequestDTO request) {
        return TicketResponseDTO.fromDomain(ticketingService.joinQueue(request.userId(), request.eventId()));
    }

    @PostMapping("/{ticketId}/purchase")
    public TicketResponseDTO attemptPurchase(@PathVariable String ticketId) {
        return TicketResponseDTO.fromDomain(ticketingService.attemptPurchase(ticketId));
    }
}