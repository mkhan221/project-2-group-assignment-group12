public class WishlistOperationsState extends BaseState {
    private static WishlistOperationsState instance;

    private WishlistOperationsState() { }

    public static WishlistOperationsState instance() {
        if (instance == null)
            instance = new WishlistOperationsState();
        return instance;
    }

    @Override
    public void run() {
        WarehouseContext context = WarehouseContext.instance();
        WarehouseBL warehouse = context.getWarehouse();

        System.out.println("\n--- WISHLIST OPERATIONS MENU ---");
        System.out.println("1. View Wishlist");
        System.out.println("2. Add Item to Wishlist");
        System.out.println("3. Remove Item from Wishlist");
        System.out.println("4. Return to Client Menu");

        String input = context.getToken("Enter option: ");
        switch (input) {
            case "1":
                System.out.println("Viewing wishlist for client " + context.getClientID());
                warehouse.displayWishlist(context.getClientID());
                break;
            case "2":
                String productId = context.getToken("Enter Product ID to add: ");
                int qty = 0;
                try {
                    qty = Integer.parseInt(context.getToken("Enter quantity: "));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity.");
                    break;
                }
                boolean added = warehouse.addItemToWishlist(context.getClientID(), productId, qty);
                System.out.println(added ? "Item added to wishlist." : "Failed to add item.");
                break;
            case "3":
                System.out.println("Feature not implemented yet (Remove Item).");
                break;
            case "4":
                System.out.println("Returning to Client Menu...");
                context.changeState(0); // Correct transition index
                return;
            default:
                System.out.println("Invalid option.");
        }

        // loop back to keep running until user exits
        run();
    }
}
