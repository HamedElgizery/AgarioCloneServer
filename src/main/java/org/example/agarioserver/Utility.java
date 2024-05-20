package org.example.agarioserver;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class Utility {
    static double minZoom = 0.05, maxZoom = 1.0;

    private Utility(){}
    public static final Random rng = new Random();
    public static Color getRandomColor() {
        int randNumber = Math.abs(rng.nextInt());
        int r = randNumber % 256;
        int g = (randNumber >> 8) % 256;
        int b = (randNumber >> 16) % 256;
        return Color.rgb(r, g, b);
    }

    public static String getRandomId() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    public static Point2D getRandomPosition() {
        return new Point2D(rng.nextInt(AgarioServerApp.MAP_WIDTH), rng.nextInt(AgarioServerApp.MAP_HEIGHT));
    }
}
