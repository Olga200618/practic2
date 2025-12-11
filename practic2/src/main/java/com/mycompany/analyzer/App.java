package com.mycompany.analyzer;

import com.opencsv.exceptions.CsvValidationException;
import com.mycompany.analyzer.util.TransactionAnalyzer;
import com.mycompany.analyzer.util.TransactionCSVReader;
import com.mycompany.analyzer.util.TransactionReportGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App {
    
    private static final String CSV_FILE_PATH = "src/test/resources/test_transactions.csv";

    public static void main(String[] args) {
        System.out.println("--- ЗАПУСК АНАЛІЗУ ВИТРАТ ---");
        
        try {
            List<Transaction> transactions = TransactionCSVReader.readTransactions(CSV_FILE_PATH);
            System.out.println(" Успішно прочитано " + transactions.size() + " транзакцій.");

            String report = TransactionReportGenerator.generateTextReport(transactions);
            System.out.println("\n" + report);

            System.out.println("\n--- ТОП-10 НАЙБІЛЬШИХ ТРАНЗАКЦІЙ ---");
            TransactionAnalyzer.findTop10LargestExpenses(transactions).forEach(t -> 
                System.out.printf("  %s: %10.2f грн, %s (%s)%n", t.getDate(), t.getAmount(), t.getCategory(), t.getDescription())
            );
      
            LocalDate startDate = LocalDate.of(2025, 2, 1);
            LocalDate endDate = LocalDate.of(2025, 3, 31);
            
            System.out.printf("\n--- АНАЛІЗ за період %s по %s ---\n", startDate, endDate);

            Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, startDate, endDate);
            Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, startDate, endDate);

            if (largest.isPresent()) {
                 System.out.printf("  Найбільша: %10.2f грн | %s | %s%n", largest.get().getAmount(), largest.get().getDate(), largest.get().getCategory());
            } else {
                 System.out.println("  Найбільшу транзакцію за період не знайдено.");
            }
            
            if (smallest.isPresent()) {
                 System.out.printf("  Найменша: %10.2f грн | %s | %s%n", smallest.get().getAmount(), smallest.get().getDate(), smallest.get().getCategory());
            } else {
                 System.out.println("  Найменшу транзакцію за період не знайдено.");
            }


        } catch (IOException | CsvValidationException e) {
            System.err.println("Критична помилка під час виконання: " + e.getMessage());
        }
        
        System.out.println("\n--- АНАЛІЗ ВИТРАТ ЗАВЕРШЕНО ---");
    }
}
