package com.sadhakphanse.expense_tracker_web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private User getLoggedInUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @GetMapping("/")
    public String showDashboard(Model model, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        List<Expense> allEntries = expenseRepository.findByUser(currentUser);

        List<Expense> expenses = allEntries.stream()
            .filter(e -> e.getAmount() >= 0)
            .collect(Collectors.toList());
        
        List<Expense> incomes = allEntries.stream()
            .filter(e -> e.getAmount() < 0)
            .collect(Collectors.toList());

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalIncome = incomes.stream().mapToDouble(e -> Math.abs(e.getAmount())).sum();
        double balance = totalIncome - totalExpenses;

        model.addAttribute("expenses", expenses);
        model.addAttribute("incomes", incomes);
        model.addAttribute("newExpense", new Expense());
        model.addAttribute("newIncome", new Expense());
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("balance", balance);
        model.addAttribute("username", principal.getName());

        return "index";
    }

    @PostMapping("/addExpense")
    public String addExpense(@ModelAttribute Expense newExpense, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        newExpense.setUser(currentUser);
        expenseRepository.save(newExpense);
        return "redirect:/";
    }

    @PostMapping("/addIncome")
    public String addIncome(@ModelAttribute Expense newIncome, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        newIncome.setUser(currentUser);
        newIncome.setAmount(Math.abs(newIncome.getAmount()) * -1);
        expenseRepository.save(newIncome);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        expenseRepository.findById(id)
            .filter(expense -> expense.getUser().equals(currentUser))
            .ifPresent(expense -> expenseRepository.deleteById(id));
        return "redirect:/";
    }

    @GetMapping("/export/excel")
    public void exportExpensesToExcel(HttpServletResponse response, Principal principal) throws IOException {
        User currentUser = getLoggedInUser(principal);
        List<Expense> expenses = expenseRepository.findByUser(currentUser).stream()
            .filter(e -> e.getAmount() >= 0)
            .collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"expenses.xlsx\"");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expenses");
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Date", "Category", "Amount (₹)", "Description"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(expense.getExpense_date()));
                row.createCell(1).setCellValue(expense.getCategory());
                row.createCell(2).setCellValue(expense.getAmount());
                row.createCell(3).setCellValue(expense.getDescription() != null ? expense.getDescription() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/api/generate-summary")
    @ResponseBody
    public String getAiSummary(Principal principal) {
        
        User currentUser = getLoggedInUser(principal);
        List<Expense> allEntries = expenseRepository.findByUser(currentUser);
        List<Expense> expenses = allEntries.stream().filter(e -> e.getAmount() >= 0).collect(Collectors.toList());
        List<Expense> incomes = allEntries.stream().filter(e -> e.getAmount() < 0).collect(Collectors.toList());

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are a friendly personal finance assistant. Write a concise and encouraging summary paragraph based on the following data for the user named ").append(principal.getName()).append(".\n");
        promptBuilder.append("Address the user by their name. First, summarize their sources of income with descriptions. Then, summarize their expenditures with descriptions. Keep it brief and in a single paragraph format.\n\n");
        promptBuilder.append("Incomes:\n");
        if (incomes.isEmpty()) {
            promptBuilder.append("- No income recorded.\n");
        } else {
            incomes.forEach(inc -> promptBuilder.append("- ₹").append(Math.abs(inc.getAmount())).append(" from ").append(inc.getCategory()).append(": ").append(inc.getDescription() != null ? inc.getDescription() : "No description").append("\n"));
        }
        promptBuilder.append("\nExpenses:\n");
        if (expenses.isEmpty()) {
            promptBuilder.append("- No expenses recorded.\n");
        } else {
            expenses.forEach(exp -> promptBuilder.append("- ₹").append(exp.getAmount()).append(" on ").append(exp.getCategory()).append(": ").append(exp.getDescription() != null ? exp.getDescription() : "No description").append("\n"));
        }

        
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-05-20:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        GeminiRequest requestPayload = new GeminiRequest(promptBuilder.toString());
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Failed to communicate with the AI service.\"}";
        }
    }
}