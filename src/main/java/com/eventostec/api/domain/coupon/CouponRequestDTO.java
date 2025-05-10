package com.eventostec.api.domain.coupon;

import java.time.LocalDateTime;

public record CouponRequestDTO(Integer discount, String code, LocalDateTime valid) {

}