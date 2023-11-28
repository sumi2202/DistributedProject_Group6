import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class RestaurantServer {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // PUSH socket that pushes notifications to the Publish-Subscribe Server
            ZMQ.Socket pushUpdates = context.createSocket(SocketType.PUSH);
            pushUpdates.connect("tcp://localhost:5557");

            // PUB socket that publishes menus to the Publish-Subscribe Server
            ZMQ.Socket publishMenus = context.createSocket(SocketType.PUB);
            publishMenus.connect("tcp://localhost:5558");

            // Send notifications to the Updates Server
            for (int i = 0; i < 10; i++) {
                String notifications = "Notification " + i;
                pushUpdates.send(notifications);
                // Send menus to the Updates Server
                String menu = "Menu " + i;
                publishMenus.send(menu);
            }
        }
    }
}
