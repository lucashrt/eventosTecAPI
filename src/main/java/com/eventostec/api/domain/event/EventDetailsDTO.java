package com.eventostec.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;


public record EventDetailsDTO (UUID id, String title, String description, LocalDateTime date, String city, String uf, Boolean remote, String img_url, String event_urL, List<CouponDTO> coupons) {

    public record CouponDTO (String code, Integer discount, LocalDateTime valid) {}

}
