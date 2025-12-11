package com.mycompany.analyzer;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value 
@Builder 
public class Transaction {
    String id;
    LocalDate date;
    BigDecimal amount;
    String category;
    String description;
}