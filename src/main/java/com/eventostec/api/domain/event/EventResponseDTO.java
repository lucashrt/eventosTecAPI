package com.eventostec.api.domain.event;

import java.sql.Date;
import java.util.UUID;

public record EventResponseDTO (UUID id, String title, String description, Date date, String city, String state, Boolean remote, String img_url, String event_url) {

}
