package org.example.agarioserver;

import java.io.Serializable;

public class PlayerDead implements Serializable {
    String entityId;
    public PlayerDead() {}
    public PlayerDead(String id) {
        entityId = id;
    }
}
