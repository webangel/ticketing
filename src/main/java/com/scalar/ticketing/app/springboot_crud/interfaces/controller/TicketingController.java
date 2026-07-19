package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.TicketService;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.TicketRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.TicketResponseDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.mapper.TicketMapper;

@RestController
@RequestMapping("/api/tickets")
public class TicketingController {
    private final TicketService ticketingService;

    public TicketingController(TicketService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @PostMapping("/join")
    public ResponseEntity<TicketResponseDTO> joinQueue(@RequestBody TicketRequestDTO request) {
        return new ResponseEntity<>(
                TicketMapper.toResponse(ticketingService.joinQueue(request.userId(), request.eventId())),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{ticketId}/purchase")
    public ResponseEntity<TicketResponseDTO> attemptPurchase(@PathVariable String ticketId) {
        return ResponseEntity.ok(TicketMapper.toResponse(ticketingService.attemptPurchase(ticketId)));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable String ticketId) {
        return ticketingService.getTicketById(ticketId)
                .map(ticket -> ResponseEntity.ok(TicketMapper.toResponse(ticket)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(TicketMapper.toResponseList(ticketingService.getAllTickets()));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByEventId(@PathVariable String eventId) {
        return ResponseEntity.ok(TicketMapper.toResponseList(ticketingService.getTicketsByEventId(eventId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(TicketMapper.toResponseList(ticketingService.getTicketsByUserId(userId)));
    }

    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<TicketResponseDTO> cancelTicket(@PathVariable String ticketId) {
        return ResponseEntity.ok(TicketMapper.toResponse(ticketingService.cancelTicket(ticketId)));
    }
}