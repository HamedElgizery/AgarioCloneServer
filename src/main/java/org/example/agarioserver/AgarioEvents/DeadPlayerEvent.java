package org.example.agarioserver.AgarioEvents;

import java.io.Serializable;

public class DeadPlayerEvent implements Serializable {
    String entityId;
    public DeadPlayerEvent() {}
    public DeadPlayerEvent(String id) {
        entityId = id;
    }
}
