package com.eventostec.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponService couponService;

    public Event createEvent(EventRequestDTO data) {
        String img_url = null;

        if (data.image() != null) {
            img_url = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEvent_url(data.event_url());
        newEvent.setDate(data.date());
        newEvent.setRemote(data.remote());
        newEvent.setImg_url(img_url);

        eventRepository.save(newEvent);
        
        if (!data.remote()) {
            this.addressService.createAddress(data, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDTO> getUpComingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.eventRepository.findUpComingEvents(LocalDateTime.now(), pageable);
        return eventsPage.map (event -> new EventResponseDTO(
            event.getId(), 
            event.getTitle(), 
            event.getDescription(), 
            event.getDate(), 
            event.getAddress() != null ? event.getAddress().getCity() : "", 
            event.getAddress() != null ? event.getAddress().getUf() : "", 
            event.getRemote(), 
            event.getImg_url(), 
            event.getEvent_url()))
                .stream().toList();
    }


    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, LocalDateTime startDate, LocalDateTime endDate) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : LocalDateTime.of(1, 1, 1, 0, 0);
        endDate = (endDate != null) ? endDate : LocalDateTime.now().plus(10, ChronoUnit.YEARS);
        
        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = this.eventRepository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);
        return eventsPage.map (event -> new EventResponseDTO(
            event.getId(), 
            event.getTitle(), 
            event.getDescription(), 
            event.getDate(), 
            event.getAddress() != null ? event.getAddress().getCity() : "", 
            event.getAddress() != null ? event.getAddress().getUf() : "", 
            event.getRemote(), 
            event.getImg_url(), 
            event.getEvent_url()))
                .stream().toList();
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        List<Coupon> coupons = couponService.consultCoupons(eventId, LocalDateTime.now());

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map (coupon -> new EventDetailsDTO.CouponDTO(
                    coupon.getCode(),
                    coupon.getDiscount(),
                    coupon.getValid()))
                .collect(Collectors.toList());
        
        return new EventDetailsDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getDate(),
            event.getAddress() != null ? event.getAddress().getCity() : "",
            event.getAddress() != null ? event.getAddress().getUf() : "",
            event.getRemote(),
            event.getImg_url(),
            event.getEvent_url(),
            couponDTOs);
    }

    public Event deleteEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
        return event;
    }

    private String uploadImg(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        
        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucketName, fileName, file);
            file.delete();
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e);
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}