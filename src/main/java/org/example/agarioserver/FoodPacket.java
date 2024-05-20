package org.example.agarioserver;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class FoodPacket implements Serializable {
    String entityId;
    public double x;
    public double y;
    public double colorR;
    public double colorG;
    public double colorB;
    public FoodPacket() {}
    public static FoodPacket randomize() {
        FoodPacket foodEntity = new FoodPacket();
        foodEntity.entityId = Utility.getRandomId();
        Point2D position = Utility.getRandomPosition();
        foodEntity.x =  position.getX();
        foodEntity.y =  position.getY();
        Color color = Utility.getRandomColor();
        foodEntity.colorR = color.getRed();
        foodEntity.colorG = color.getGreen();
        foodEntity.colorB = color.getBlue();
        return foodEntity;
    }
}
