package src;//responsible for publish-subscribe mechanism , sends all updates to the restaurant
//acts as middleman between the restaurant server and the client  

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;


public class UpdatesServer {
   public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // PULL socket that receives notifications from RestaurantServer
            ZMQ.Socket restaurantNotification = context.createSocket(SocketType.PULL);
            restaurantNotification.bind("tcp://*:5557");

            // PUB socket that publishes messages to clients
            ZMQ.Socket clientMessenger = context.createSocket(SocketType.PUB);
            clientMessenger.bind("tcp://*:5556");

            // PUSH socket to pushing menus to Restaurant Server
            ZMQ.Socket restaurantMenu = context.createSocket(SocketType.PUSH);
            restaurantMenu.bind("tcp://*:5558");


            while (true) {
                // send notifications from RestaurantServer to client
                String notification = restaurantNotification.recvStr(0);
                clientMessenger.send(notification);

                // sends received menus from Restaurant Server to clients
                String menu = restaurantMenu.recvStr(0);
                clientMessenger.send(menu);
            }
        }
    } 

    
}
