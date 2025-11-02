package com.scalar.ticketing.app.springboot_crud.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.PaymentMethod;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.PaymentStatus;

import lombok.Data;

@Data
public class Payment {
    private Long id;
    private Ticket ticket;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime paymentDate;
}
