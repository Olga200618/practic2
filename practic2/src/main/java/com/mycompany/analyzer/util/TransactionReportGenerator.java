package com.mycompany.analyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TransactionReportGenerator {

    // 1 символ = 1000 грн
    private static final int SCALE = 1000; 
    private static final char VISUAL_CHAR = '█'; // Використовуємо інший символ для кращої візуалізації

    public static String generateTextReport(List<Transaction> transactions) {
        StringBuilder report = new StringBuilder();

        report.append("=================================================================\n");
        report.append("              ЗВІТ ПРО АНАЛІЗ ФІНАНСОВИХ ТРАНЗАКЦІЙ\n");
        report.append("=================================================================\n\n");

        // Сумарні витрати по категоріях
        report.append("## 📊 Сумарні Витрати по Категоріях (1 " + VISUAL_CHAR + " = " + SCALE + " грн)\n");
        Map<String, BigDecimal> categoryTotals = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        categoryTotals.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    String category = entry.getKey();
                    BigDecimal total = entry.getValue();
                    String visualization = visualizeAmount(total);
                    report.append(String.format("%-20s: %10.2f грн | %s\n", category, total, visualization));
                });

        report.append("\n" + "---" + "\n\n");

        // Сумарні витрати по місяцях
        report.append("## 📅 Сумарні Витрати по Місяцях (1 " + VISUAL_CHAR + " = " + SCALE + " грн)\n");
        Map<Month, BigDecimal> monthTotals = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().getMonth(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        monthTotals.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String monthName = entry.getKey().getDisplayName(TextStyle.FULL, new Locale("uk", "UA"));
                    BigDecimal total = entry.getValue();
                    String visualization = visualizeAmount(total);
                    report.append(String.format("%-20s: %10.2f грн | %s\n", monthName, total, visualization));
                });
        
        report.append("\n=================================================================");

        return report.toString();
    }

    private static String visualizeAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            // Можемо візуалізувати від'ємні як "[-]" або просто пропустити
            return ""; 
        }
        
        // Округлення до найближчого цілого для кількості символів
        int numberOfSymbols = amount.divide(new BigDecimal(SCALE), 0, RoundingMode.HALF_UP).intValue();
        
        return String.valueOf(VISUAL_CHAR).repeat(Math.min(50, Math.max(0, numberOfSymbols))); // Обмеження 50 символами
    }
}