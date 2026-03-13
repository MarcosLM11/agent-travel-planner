package com.marcos.myagenttravelplannerapp.memory;

import com.embabel.agent.core.Context;
import com.embabel.agent.spi.support.InMemoryContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqliteContextRepositoryTest {

    @Mock
    private TravelerContextJpaRepository jpaRepository;

    private SqliteContextRepository repository;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        repository = new SqliteContextRepository(jpaRepository, mapper);
    }

    @Test
    void saveAndLoadRoundTrip() {
        TravelerProfile profile = new TravelerProfile(
                "default", List.of("history", "gastronomy"), BudgetLevel.MEDIUM, TravelPace.MODERATE,
                List.of("vegetarian"), "", "es", List.of("Rome"), List.of(), null
        );
        InMemoryContext context = new InMemoryContext("user:default");
        context.addObject(profile);

        ArgumentCaptor<TravelerContextEntity> captor = ArgumentCaptor.forClass(TravelerContextEntity.class);
        when(jpaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        repository.save(context);

        verify(jpaRepository).save(captor.capture());
        TravelerContextEntity saved = captor.getValue();
        when(jpaRepository.findById("user:default")).thenReturn(Optional.of(saved));

        Context loaded = repository.findById("user:default");
        assertNotNull(loaded);
        TravelerProfile loadedProfile = loaded.last(TravelerProfile.class);
        assertNotNull(loadedProfile);
        assertEquals("default", loadedProfile.userId());
        assertEquals(List.of("history", "gastronomy"), loadedProfile.cumulativeInterests());
        assertEquals(BudgetLevel.MEDIUM, loadedProfile.preferredBudget());
        assertEquals(List.of("Rome"), loadedProfile.visitedDestinations());
    }

    @Test
    void findByIdMissingReturnsNull() {
        when(jpaRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertNull(repository.findById("nonexistent"));
    }

    @Test
    void findByIdCorruptedPayloadReturnsNull() {
        TravelerContextEntity corrupted = new TravelerContextEntity("user:bad", "{ invalid json !!!", null);
        when(jpaRepository.findById("user:bad")).thenReturn(Optional.of(corrupted));

        assertNull(repository.findById("user:bad"));
    }

    @Test
    void createReturnsContextWithNonBlankId() {
        Context context = repository.create();

        assertNotNull(context);
        assertNotNull(context.getId());
    }

    @Test
    void createWithIdReturnsContextWithGivenId() {
        Context context = repository.createWithId("user:test");

        assertEquals("user:test", context.getId());
    }
}