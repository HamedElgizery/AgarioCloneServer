package org.example.agarioserver;

import org.example.agarioserver.FoodPacket;

import java.io.Serializable;

public class PlayerStatus implements Serializable {
    public String t;
    public String entityId;
    public double x;
    public double y;
    public double radius;
    public double colorR;
    public double colorG;
    public double colorB;
    //public FoodPacket foodEntity;

    public PlayerStatus () {}
    public PlayerStatus(String type, String entityId) {
        this.t = type;
        this.entityId = entityId;
    }
    public PlayerStatus(String type, String entityId, double x, double y, double radius, double r, double g, double b) {
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
