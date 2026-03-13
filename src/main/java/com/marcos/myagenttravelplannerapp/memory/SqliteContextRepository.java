package com.marcos.myagenttravelplannerapp.memory;

import com.embabel.agent.core.Context;
import com.embabel.agent.spi.ContextRepository;
import com.embabel.agent.spi.support.InMemoryContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SqliteContextRepository implements ContextRepository {

    private static final Logger log = LoggerFactory.getLogger(SqliteContextRepository.class);

    private final TravelerContextJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public SqliteContextRepository(TravelerContextJpaRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @NotNull
    @Override
    public Context create() {
        return new InMemoryContext(UUID.randomUUID().toString());
    }

    @NotNull
    @Override
    public Context createWithId(@NotNull String contextId) {
        return new InMemoryContext(contextId);
    }

    @Override
    public Context findById(@NotNull String contextId) {
        return jpaRepository.findById(contextId)
                .map(entity -> deserialize(contextId, entity.getPayload()))
                .orElse(null);
    }

    @NotNull
    @Override
    public Context save(Context context) {
        TravelerProfile profile = context.last(TravelerProfile.class);
        if (profile == null) {
            return context;
        }
        try {
            String payload = objectMapper.writeValueAsString(profile);
            jpaRepository.save(new TravelerContextEntity(context.getId(), payload, LocalDateTime.now()));
        } catch (IOException e) {
            log.warn("Failed to save traveler profile for context '{}': {}", context.getId(), e.getMessage());
        }
        return context;
    }

    @Override
    public void delete(Context context) {
        jpaRepository.deleteById(context.getId());
    }

    private Context deserialize(String contextId, String payload) {
        try {
            TravelerProfile profile = objectMapper.readValue(payload, TravelerProfile.class);
            InMemoryContext context = new InMemoryContext(contextId);
            context.addObject(profile);
            return context;
        } catch (IOException e) {
            log.warn("Failed to deserialize traveler profile for context '{}': {}", contextId, e.getMessage());
            return null;
        }
    }
}