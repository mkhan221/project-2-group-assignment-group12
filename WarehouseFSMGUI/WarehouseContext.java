import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class WarehouseContext {
    private static WarehouseContext context;
    private static int currentState;

    private static BaseState[] states;
    private int[][] nextState;

    private WarehouseBL warehouse;
    private int currentLogin; // 0=Manager, 1=Clerk, 2=Client
    private String clientID;

    // ===== Added new constant for Wishlist State =====
    public static final int CLERK_STATE = 0;
    public static final int CLIENT_STATE = 1;
    public static final int LOGIN_STATE = 2;
    public static final int MANAGER_STATE = 3;
    public static final int WISHLIST_STATE = 4; // ✅ new state index

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private WarehouseContext() {
        System.out.println("Initializing Warehouse Context...");

        warehouse = new WarehouseBL();

        // ===== Updated from 4 → 5 to include WishlistOperationsState =====
        states = new BaseState[5];
        states[CLERK_STATE] = ClerkMenuState.instance();
        states[CLIENT_STATE] = ClientMenuState.instance();
        states[LOGIN_STATE] = LoginState.instance();
        states[MANAGER_STATE] = ManagerMenuState.instance();
        states[WISHLIST_STATE] = WishlistOperationsState.instance(); // ✅ new state registered

        // ===== Define transition table for all 5 states =====
        nextState = new int[5][5];

        // Clerk transitions
        nextState[CLERK_STATE][0] = LOGIN_STATE;
        nextState[CLERK_STATE][1] = CLIENT_STATE;
        nextState[CLERK_STATE][2] = -2;
        nextState[CLERK_STATE][3] = MANAGER_STATE;
        nextState[CLERK_STATE][4] = -2;

        // Client transitions
        nextState[CLIENT_STATE][0] = LOGIN_STATE;    // Logout → Login
        nextState[CLIENT_STATE][1] = CLERK_STATE;    // Clerk
        nextState[CLIENT_STATE][2] = -2;             // Error
        nextState[CLIENT_STATE][3] = MANAGER_STATE;  // Manager
        nextState[CLIENT_STATE][4] = WISHLIST_STATE; // ✅ Go to Wishlist

        // Login transitions
        nextState[LOGIN_STATE][0] = CLERK_STATE;
        nextState[LOGIN_STATE][1] = CLIENT_STATE;
        nextState[LOGIN_STATE][2] = MANAGER_STATE;
        nextState[LOGIN_STATE][3] = -1; // exit
        nextState[LOGIN_STATE][4] = -2;

        // Manager transitions
        nextState[MANAGER_STATE][0] = LOGIN_STATE;   // Logout -> Login
        nextState[MANAGER_STATE][1] = CLERK_STATE;   // Become Clerk
        nextState[MANAGER_STATE][2] = -2;            // Error
        nextState[MANAGER_STATE][3] = -1;            // Exit
        nextState[MANAGER_STATE][4] = -2;

        // Wishlist transitions
        nextState[WISHLIST_STATE][0] = CLIENT_STATE; // ✅ Return to Client Menu
        nextState[WISHLIST_STATE][1] = -2;
        nextState[WISHLIST_STATE][2] = -2;
        nextState[WISHLIST_STATE][3] = -2;
        nextState[WISHLIST_STATE][4] = -2;

        currentState = LOGIN_STATE;
    }

    public static WarehouseContext instance() {
        if (context == null) {
            context = new WarehouseContext();
        }
        return context;
    }

    public void changeState(int transition) {
        int next = nextState[currentState][transition];

        if (next == -2) {
            System.out.println("Error has occurred. Terminating...");
            terminate();
        } else if (next == -1) {
            terminate();
        } else {
            currentState = next;
            states[currentState].run();
        }
    }

    private void terminate() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    public void setLogin(int role) {
        currentLogin = role;
    }

    public int getLogin() {
        return currentLogin;
    }

    public WarehouseBL getWarehouse() {
        return warehouse;
    }

    public void setClientID(String id) {
        clientID = id;
    }

    public String getClientID() {
        return clientID;
    }

    public void process() {
        while (true) {
            states[currentState].run();
        }
    }

    public static void main(String[] args) {
        WarehouseContext.instance().process();
    }
}
