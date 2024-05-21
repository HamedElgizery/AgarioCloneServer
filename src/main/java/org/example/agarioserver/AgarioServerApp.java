package org.example.agarioserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import org.example.agarioserver.AgarioEvents.DeadPlayerEvent;
import org.example.agarioserver.AgarioEvents.EatenFoodEvent;
import org.example.agarioserver.AgarioPackets.FoodPacket;
import org.example.agarioserver.AgarioPackets.PlayerStatusPacket;
import org.example.agarioserver.AgarioRequests.FoodRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgarioServerApp {
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;
    final private Server server;
    HashMap<String, FoodPacket> food = new HashMap<>(); // store food in map
    ScheduledExecutorService foodScheduler;

    public AgarioServerApp() throws IOException {
        server = new Server(1000000, 1000000);
        server.start();

        server.bind(54555, 54777);
        server.getKryo().register(PlayerStatusPacket.class);
        server.getKryo().register(FoodPacket.class);
        server.getKryo().register(EatenFoodEvent.class);
        server.getKryo().register(FoodRequest.class);
        server.getKryo().register(DeadPlayerEvent.class);
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof FoodRequest) {
                    for (Map.Entry<String, FoodPacket> set :
                            food.entrySet()) {
                        connection.sendTCP(set.getValue());
                    }
                }
                if (object instanceof PlayerStatusPacket playerStatusPacket) {
                    broadcastAgarioPacket(playerStatusPacket);
                }
                if (object instanceof EatenFoodEvent eatenFoodEvent) {
                    try {
                        food.remove(eatenFoodEvent.entityId);
                        broadcastFoodEaten(eatenFoodEvent);
                    } catch (Exception e) {
                        // Catches the problem that could happen if two players ate it at the same time
                        e.printStackTrace();
                    }
                }
                if (object instanceof DeadPlayerEvent deadPlayerEvent) {
                    try {
                        broadcastPlayerDead(deadPlayerEvent);
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
            if (food.size() < 4000) {
                FoodPacket foodPacket = FoodPacket.randomize();
                food.put(foodPacket.entityId, foodPacket);
                server.sendToAllTCP(foodPacket);
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // Sends update every second
    }
    private void broadcastPlayerDead(DeadPlayerEvent deadPlayerEvent) {
        server.sendToAllTCP(deadPlayerEvent);
    }
    private void broadcastFoodEaten(EatenFoodEvent eatenFoodEvent) {
        server.sendToAllTCP(eatenFoodEvent);
    }
    private void broadcastAgarioPacket(PlayerStatusPacket playerStatusPacket) {
        server.sendToAllTCP(playerStatusPacket);
    }

    public static void main(String[] args) throws IOException {
        new AgarioServerApp();

    }
}
