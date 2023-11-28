import java.util.Scanner;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Client {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // SUB socket to subscribe to messages from the Updates Server
            ZMQ.Socket clientSubscriber = context.createSocket(SocketType.SUB);
            clientSubscriber.connect("tcp://localhost:5556");

            // socket to send user subscription preferences to the Restaurant Server
            ZMQ.Socket subscriptionSender = context.createSocket(SocketType.DEALER);
            subscriptionSender.connect("tcp://localhost:5559");

            // Scanner for user input
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Authenticate user
                System.out.println("Hello, Welcome to FoodPlug, your number 1 food ordering service:");
                System.out.println("Enter customer ID:");
                String customerId = scanner.nextLine();
                System.out.println("Enter first name:");
                String firstName = scanner.nextLine();
                System.out.println("Enter last name:");
                String lastName = scanner.nextLine();
                System.out.println("Enter email:");
                String email = scanner.nextLine();

                // authenticating the user based on the database
                Database database = new Database();
                boolean isAuthenticated = database.authenticate(customerId, firstName, lastName, email);
                if (!isAuthenticated) {
                    System.out.println("Authentication failed");
                    continue;
                }

                // Send subscription preferences to the Restaurant Server
                System.out.println("Enter restaurants to subscribe to (comma-separated):");
                String[] restaurants = scanner.nextLine().split(",");
                subscriptionSender.send(String.join(",", restaurants));

                // Accessing messages from the Updates Server
                String receivedUpdates = clientSubscriber.recvStr(0);
                System.out.println("Received: " + receivedUpdates);
            }
        }
    }
}
