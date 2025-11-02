package com.scalar.ticketing.app.springboot_crud;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;

@SpringBootTest
class SpringbootCrudApplicationTests {

	@Autowired
    private EventRepository eventRepository;
    
    @Test
    void shouldSaveAndRetrieveEvent() {
        Event event = Event.create("prueba x", null, null, 0, 0, null, null

        );
        
        Event saved = eventRepository.save(event);
        Optional<Event> found = eventRepository.findById("event1");
        
        assertTrue(found.isPresent());
        assertEquals("Concierto", found.get().getName());
    }

}
