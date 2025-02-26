package MyApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LotDisplay extends JFrame {
    private JTable lotTable;
    private DefaultTableModel tableModel;
    private LotDBAccess lotDBAccess;
    
    public LotDisplay() {
        setTitle("Lot Listings");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        lotDBAccess = new LotDBAccess();
        
        // Table Setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Location", "Price", "Size", "Status"}, 0);
        lotTable = new JTable(tableModel);
        add(new JScrollPane(lotTable), BorderLayout.CENTER);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sortByPriceBtn = new JButton("Sort by Price");
        JButton sortBySizeBtn = new JButton("Sort by Size");
        JButton backButton = new JButton("Back to Menu");
        JButton viewTransactionsBtn = new JButton("View Transactions"); // Added button to view transactions
        JButton addLotBtn = new JButton("Add Lot"); // Placeholder for adding a new lot
        JButton deleteLotBtn = new JButton("Delete Lot"); // Deletes selected lot
        
        sortByPriceBtn.addActionListener(e -> updateTable(lotDBAccess.getLotsSortedByPrice()));
        sortBySizeBtn.addActionListener(e -> updateTable(lotDBAccess.getLotsSortedBySize()));
        
        backButton.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });
        
        viewTransactionsBtn.addActionListener(e -> {
            new TransactionHistoryDisplay().setVisible(true); // Opens transaction history window
        });
        
        addLotBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Add Lot feature coming soon!"); // Placeholder for adding lots
        });
        
        deleteLotBtn.addActionListener(e -> deleteSelectedLot()); // Calls method to delete selected lot
        
        // Adding buttons based on user role
        buttonPanel.add(sortByPriceBtn);
        buttonPanel.add(sortBySizeBtn);
        buttonPanel.add(backButton);
        buttonPanel.add(viewTransactionsBtn);
        
        if (UserSession.isAdmin()) { // Checks if the user is an admin
            buttonPanel.add(addLotBtn);
            buttonPanel.add(deleteLotBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadLots(); // Load initial data
    }
    
    private void loadLots() {
        try {
            updateTable(lotDBAccess.getAllLots());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading lots: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable(List<Lot> lots) {
        tableModel.setRowCount(0); // Clear table
        
        if (lots != null) {
            for (Lot l : lots) {
                if (l != null) {
                    tableModel.addRow(new Object[]{l.getId(), l.getLocation(), l.getPrice(), l.getSize(), l.status()});
                }
            }
        }
    }
    
    private void deleteSelectedLot() {
        int selectedRow = lotTable.getSelectedRow(); // Get selected row
        if (selectedRow >= 0) {
            int lotId = (int) tableModel.getValueAt(selectedRow, 0); // Get lot ID from table
            lotDBAccess.deleteLot(lotId); // Call method to delete lot
            loadLots(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lot to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LotDisplay().setVisible(true));
    }
}
