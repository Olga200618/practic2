package com.mycompany.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<Transaction> readTransactions(String filePath) throws IOException, CsvValidationException {
        List<Transaction> transactions = new ArrayList<>();

        // withSkipLines(1) пропускає рядок заголовка
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build()) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length < 5) continue;
                
                try {
                    Transaction transaction = Transaction.builder()
                            .id(nextLine[0])
                            .date(LocalDate.parse(nextLine[1], DATE_FORMATTER))
                            .amount(new BigDecimal(nextLine[2]))
                            .category(nextLine[3])
                            .description(nextLine[4])
                            .build();
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.err.println("Помилка парсингу рядка CSV: " + String.join(",", nextLine) + ". Помилка: " + e.getMessage());
                }
            }
        }
        return transactions;
    }
}