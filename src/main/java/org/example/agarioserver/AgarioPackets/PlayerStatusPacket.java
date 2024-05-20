package org.example.agarioserver.AgarioPackets;

import java.io.Serializable;

public class PlayerStatusPacket implements Serializable {
    public String t;
    public String entityId;
    public double x;
    public double y;
    public double radius;
    public double colorR;
    public double colorG;
    public double colorB;
    //public FoodPacket foodEntity;

    public PlayerStatusPacket() {}
    public PlayerStatusPacket(String type, String entityId) {
        this.t = type;
        this.entityId = entityId;
    }
    public PlayerStatusPacket(String type, String entityId, double x, double y, double radius, double r, double g, double b) {
        this.t = type;
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        colorR = r;
        colorG = g;
        colorB = b;
    }

}
