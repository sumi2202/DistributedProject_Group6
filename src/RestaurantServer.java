import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            // ROUTER socket to receive subscription preferences from clients
            ZMQ.Socket clientSubscriptions = context.createSocket(SocketType.ROUTER);
            clientSubscriptions.bind("tcp://*:5559");

             // List of restaurants that the client is subscribed to
            List<String> subscribedRestaurants = new ArrayList<>();

            while (true){
                //get subscription choice from the clients 
                String clientMessage = clientSubscriptions.recvStr(0);
                subscribedRestaurants = Arrays.asList(clientMessage.split(","));

                 // Send notifications to the Updates Server
            for (int i = 0; i < 10; i++) {
                String restaurantName = "Restaurant" + i;
                if (subscribedRestaurants.contains(restaurantName)) {
                    String notifications = restaurantName + ": Notification " + i;                
                pushUpdates.send(notifications);
                // Send menus to the Updates Server
                String menu = "Menu " + i;
                publishMenus.send(menu);
            }

           
            }
        }
    }
}
}
