import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientMenuStateGUI extends JFrame {
    private WarehouseContext context;
    private WarehouseBL warehouse;
    private String clientId;

    public ClientMenuStateGUI(WarehouseContext context) {
        this.context = context;
        this.warehouse = context.getWarehouse();
        this.clientId = context.getClientID();

        setTitle("Client Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Create buttons ---
        JButton btnClientDetails = new JButton("Show Client Details");
        JButton btnProducts = new JButton("Show Products");
        JButton btnTransactions = new JButton("Show Transactions");
        JButton btnWishlist = new JButton("Open Wishlist Operations");
        JButton btnLogout = new JButton("Logout");

        // --- Add to panel ---
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.add(btnClientDetails);
        panel.add(btnProducts);
        panel.add(btnTransactions);
        panel.add(btnWishlist);
        panel.add(btnLogout);

        add(panel);

        // --- Button Actions ---
        btnClientDetails.addActionListener(e -> showClientDetails());
        btnProducts.addActionListener(e -> showProducts());
        btnTransactions.addActionListener(e -> showClientTransactions());
        btnWishlist.addActionListener(e -> openWishlist());
        btnLogout.addActionListener(e -> logout());
    }

    private void showClientDetails() {
        Client client = warehouse.getClientById(clientId);
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client not found!");
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Client ID: " + client.getId() + "\nName: " + client.getName() +
                        "\nBalance: $" + client.getBalance(),
                "Client Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showProducts() {
        JOptionPane.showMessageDialog(this, "Displaying all products in console...");
        warehouse.displayProducts();
    }

    private void showClientTransactions() {
        JOptionPane.showMessageDialog(this, "Displaying transactions in console...");
        warehouse.displayTransactionsForClient(clientId);
    }

    private void openWishlist() {
        JOptionPane.showMessageDialog(this, "Opening Wishlist GUI...");
        new WishlistOperationsGUI(context).setVisible(true);
        this.dispose(); // close current window
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "Logging out...");
        System.exit(0);
    }

    public static void main(String[] args) {
        WarehouseContext context = WarehouseContext.instance();
        context.setClientID("C1");
        SwingUtilities.invokeLater(() -> new ClientMenuStateGUI(context).setVisible(true));
    }
}
