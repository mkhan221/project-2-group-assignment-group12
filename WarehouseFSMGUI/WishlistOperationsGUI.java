import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WishlistOperationsGUI extends JFrame {
    private WarehouseContext context;
    private WarehouseBL warehouse;

    public WishlistOperationsGUI(WarehouseContext context) {
        this.context = context;
        this.warehouse = context.getWarehouse();

        setTitle("Wishlist Operations");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Buttons ---
        JButton btnView = new JButton("View Wishlist");
        JButton btnAdd = new JButton("Add Item to Wishlist");
        JButton btnRemove = new JButton("Remove Item from Wishlist");
        JButton btnReturn = new JButton("Return to Client Menu");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.add(btnView);
        panel.add(btnAdd);
        panel.add(btnRemove);
        panel.add(btnReturn);

        add(panel);

        // --- Button Actions ---
        btnView.addActionListener(e -> viewWishlist());
        btnAdd.addActionListener(e -> addWishlistItem());
        btnRemove.addActionListener(e -> JOptionPane.showMessageDialog(this, "Remove not implemented yet."));
        btnReturn.addActionListener(e -> returnToClientMenu());
    }

    private void viewWishlist() {
        warehouse.displayWishlist(context.getClientID());
        JOptionPane.showMessageDialog(this, "Wishlist displayed in console for now.");
    }

    private void addWishlistItem() {
        String productId = JOptionPane.showInputDialog(this, "Enter Product ID:");
        if (productId == null || productId.isEmpty()) return;

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (qtyStr == null || qtyStr.isEmpty()) return;

        try {
            int qty = Integer.parseInt(qtyStr);
            boolean success = warehouse.addItemToWishlist(context.getClientID(), productId, qty);
            JOptionPane.showMessageDialog(this, success ? "Item added successfully!" : "Failed to add item.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }

    private void returnToClientMenu() {
        JOptionPane.showMessageDialog(this, "Returning to Client Menu...");
        new ClientMenuStateGUI(context).setVisible(true);
        this.dispose();
    }
}
