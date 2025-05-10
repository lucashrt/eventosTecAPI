package com.eventostec.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO (UUID id, String title, String description, LocalDateTime date, String city, String uf, Boolean remote, String img_url, String event_url) {

}
