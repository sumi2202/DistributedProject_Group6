import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Client {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Authenticate user
            Database database = new Database();
            boolean isAuthenticated = database.authenticate("customerId", "firstName", "lastName", "email");
            if (!isAuthenticated) {
                System.out.println("Authentication failed");
                return;
            }
            // SUB socket to subscribe to messages from the Updates Server
            ZMQ.Socket clientSubscriber = context.createSocket(SocketType.SUB);
            clientSubscriber.connect("tcp://localhost:5556");
            // Subscribe to all messages (empty byte array)
            clientSubscriber.subscribe(new byte[0]);

            while (true) {
                // Accessing messages from the Updates Server
                String receivedUpdates = clientSubscriber.recvStr(0);
                System.out.println("Received: " + receivedUpdates);
            }
        }
    }
}
