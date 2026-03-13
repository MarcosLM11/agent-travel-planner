package com.marcos.myagenttravelplannerapp.memory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "traveler_context")
class TravelerContextEntity {

    @Id
    @Column(name = "context_id")
    private String contextId;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected TravelerContextEntity() {}

    TravelerContextEntity(String contextId, String payload, LocalDateTime updatedAt) {
        this.contextId = contextId;
        this.payload = payload;
        this.updatedAt = updatedAt;
    }

    String getPayload() { return payload; }
}