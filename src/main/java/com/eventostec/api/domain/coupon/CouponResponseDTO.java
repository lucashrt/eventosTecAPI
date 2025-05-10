package com.eventostec.api.domain.coupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponseDTO(String code, Integer discount, LocalDateTime valid, UUID event_id) {

}
