package com.eventostec.api.domain.event;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public record EventRequestDTO(String title, String description, LocalDateTime date, String city, String uf, Boolean remote, MultipartFile image, String event_url) {

}