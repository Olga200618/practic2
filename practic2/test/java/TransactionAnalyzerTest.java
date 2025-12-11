package com.mycompany.analyzer;

import com.opencsv.exceptions.CsvValidationException;
import com.mycompany.analyzer.util.TransactionAnalyzer;
import com.mycompany.analyzer.util.TransactionCSVReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    private static List<Transaction> transactions;
    private static final String TEST_CSV_PATH = "src/test/resources/test_transactions.csv";

    @BeforeAll
    static void setup() throws IOException, CsvValidationException {
        transactions = TransactionCSVReader.readTransactions(TEST_CSV_PATH);
    }

    @Test
    void readTransactions_ShouldReturnCorrectNumberOfTransactions() {
        assertEquals(15, transactions.size(), "Має бути 15 транзакцій у тестовому файлі.");
        
        assertEquals(new BigDecimal("5000.00"), transactions.get(1).getAmount());
    }

    @Test
    void findTop10LargestExpenses_ShouldReturn10LargestTransactionsSorted() {
        List<Transaction> top10 = TransactionAnalyzer.findTop10LargestExpenses(transactions);
        
        assertEquals(10, top10.size(), "Має повернути рівно 10 найбільших транзакцій.");
        
        assertEquals(new BigDecimal("12000.00"), top10.get(0).getAmount());
    }
    
    @Test
    void findLargestAndSmallestExpense_ShouldReturnCorrectTransactionsInPeriod() {
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 10); 

        Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, startDate, endDate);
        Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, startDate, endDate);
        
        assertEquals(new BigDecimal("12000.00"), largest.get().getAmount());
        
        assertEquals(new BigDecimal("150.00"), smallest.get().getAmount());
    }
}