import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Expense {
    String date, category, description;
    double amount;

    public Expense(String date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }
}

public class ExpenseTrackerGUI extends JFrame {
    private ArrayList<Expense> expenses = new ArrayList<>();
    private DefaultTableModel tableModel;

    private JTextField dateField, categoryField, descField, amountField;

    public ExpenseTrackerGUI() {
        setTitle("Expense Tracker");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Input Panel ----
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        dateField = new JTextField();
        categoryField = new JTextField();
        descField = new JTextField();
        amountField = new JTextField();

        inputPanel.add(new JLabel("Date (dd-mm-yyyy):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JButton addBtn = new JButton("Add Expense");
        inputPanel.add(addBtn);

        JButton totalBtn = new JButton("View Total");
        inputPanel.add(totalBtn);

        add(inputPanel, BorderLayout.NORTH);

        // ---- Table Panel ----
        tableModel = new DefaultTableModel(new String[]{"Date", "Category", "Description", "Amount"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ---- Bottom Panel ----
        JPanel bottomPanel = new JPanel();

        JTextField filterField = new JTextField(10);
        JButton filterBtn = new JButton("Filter by Category");
        JButton exitBtn = new JButton("Exit");

        bottomPanel.add(new JLabel("Category:"));
        bottomPanel.add(filterField);
        bottomPanel.add(filterBtn);
        bottomPanel.add(exitBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---- Button Actions ----
        addBtn.addActionListener(e -> addExpense());
        totalBtn.addActionListener(e -> showTotal());
        filterBtn.addActionListener(e -> filterByCategory(filterField.getText()));
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void addExpense() {
        String date = dateField.getText();
        String category = categoryField.getText();
        String desc = descField.getText();
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense expense = new Expense(date, category, desc, amount);
        expenses.add(expense);

        tableModel.addRow(new Object[]{date, category, desc, amount});

        dateField.setText("");
        categoryField.setText("");
        descField.setText("");
        amountField.setText("");
    }

    private void showTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        JOptionPane.showMessageDialog(this, "Total Expenses: ₹" + total, "Total", JOptionPane.INFORMATION_MESSAGE);
    }

    private void filterByCategory(String category) {
        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a category to filter!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Expenses in " + category + ":\n");
        double total = 0;
        boolean found = false;

        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase(category)) {
                sb.append(e.date).append(" | ").append(e.description).append(" | ₹").append(e.amount).append("\n");
                total += e.amount;
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "No expenses found in this category.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            sb.append("\nTotal in ").append(category).append(": ₹").append(total);
            JOptionPane.showMessageDialog(this, sb.toString(), "Category Expenses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
