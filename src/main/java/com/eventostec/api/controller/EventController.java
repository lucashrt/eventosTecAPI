package com.eventostec.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.service.EventService;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> createEvent(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyyMMdd'T'HHmmss") LocalDateTime date,
            @RequestParam("city") String city,
            @RequestParam("uf") String uf,
            @RequestParam("remote") Boolean remote,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("event_url") String event_url) {
        EventRequestDTO body = new EventRequestDTO(title, description, date, city, uf, remote, image, event_url);
        return ResponseEntity.ok(this.eventService.createEvent(body));
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> allEvents = this.eventService.getUpComingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd'T'HHmmss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd'T'HHmmss") LocalDateTime endDate) {
        List<EventResponseDTO> eventsPage = this.eventService.getFilteredEvents(page, size, title, city, uf, startDate, endDate);
        return ResponseEntity.ok(eventsPage);
    }

    @GetMapping("/find/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailsDTO eventDetails = this.eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventDetails);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Event> deleteEvent(@PathVariable UUID eventId) {
        Event event = this.eventService.deleteEvent(eventId);
        return ResponseEntity.ok(event);
    }
}