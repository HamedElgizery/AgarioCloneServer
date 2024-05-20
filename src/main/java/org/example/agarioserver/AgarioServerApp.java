package org.example.agarioserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.agarioserver.PlayerStatus;
import org.example.agarioserver.AgarioPackets.FoodEaten;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgarioServerApp {
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;
    final private Server server;
    HashMap<String, FoodPacket> food = new HashMap<>();
    ScheduledExecutorService foodScheduler;

    public AgarioServerApp() throws IOException {
        server = new Server(100000, 1000000);
        server.start();
        server.bind(54555, 54777);
        server.getKryo().register(PlayerStatus.class);
        server.getKryo().register(FoodPacket.class);
        server.getKryo().register(FoodEaten.class);
        server.getKryo().register(ArrayList.class);
        server.getKryo().register(FoodRequest.class);
        server.getKryo().register(PlayerDead.class);
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof FoodRequest) {
                    System.out.println("RECEIVED!");
                    for (Map.Entry<String, FoodPacket> set :
                            food.entrySet()) {
                        //System.out.println("Sending " + set.getValue().entityId);
                        connection.sendTCP(set.getValue());
                    }
                    System.out.println("Sent all food");
                }
                if (object instanceof PlayerStatus playerStatus) {
                    broadcastAgarioPacket(playerStatus);
                }
                if (object instanceof FoodEaten foodEaten) {
                    try {
                        food.remove(foodEaten.entityId);
                        broadcastFoodEaten(foodEaten);
                    } catch (Exception e) {
                        // Catches the problem that could happen if two players ate it at the same time
                        e.printStackTrace();
                    }
                }
                if (object instanceof PlayerDead playerDead) {
                    try {
                        broadcastPlayerDead(playerDead);
                    } catch (Exception e) {
                        // Catches the problem that could happen if two players ate it at the same time
                        e.printStackTrace();
                    }
                }
            }

        });
        setUpFoodService();
    }
    private void setUpFoodService() {
        // Send food every 0.1 second (adds 10 food per second)
        foodScheduler = Executors.newScheduledThreadPool(1);
        foodScheduler.scheduleAtFixedRate(() -> {
//            System.out.println("Sending food!");
            FoodPacket foodPacket = FoodPacket.randomize();
            food.put(foodPacket.entityId, foodPacket);
            server.sendToAllTCP(foodPacket);
        }, 0, 100, TimeUnit.MILLISECONDS); // Sends update every second
    }
    private void broadcastPlayerDead(PlayerDead playerDead) {
        server.sendToAllTCP(playerDead);
    }
    private void broadcastFoodEaten(FoodEaten foodEaten) {
        server.sendToAllTCP(foodEaten);
    }
    private void broadcastAgarioPacket(PlayerStatus playerStatus) {
        server.sendToAllTCP(playerStatus);
    }

    public static void main(String[] args) throws IOException {
        new AgarioServerApp();

    }
}
