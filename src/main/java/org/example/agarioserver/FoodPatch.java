package org.example.agarioserver;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodPatch implements Serializable {
    ArrayList<FoodPacket> food = new ArrayList<>();
    public FoodPatch() {}
    public FoodPatch(int N) {
        for (int i = 0; i < N; ++i) {
            food.add(FoodPacket.randomize());
        }
    }
}
