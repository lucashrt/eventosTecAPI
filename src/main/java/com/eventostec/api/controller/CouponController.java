package com.eventostec.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.service.CouponService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{event_id}")
    public ResponseEntity<CouponResponseDTO> addCouponToEvent(@PathVariable UUID event_id, @RequestBody CouponRequestDTO data) {
        return ResponseEntity.ok(this.couponService.addCouponToEvent(event_id, data));
    }

    @DeleteMapping("/delete/{coupon_id}")
    public ResponseEntity<Coupon> deleteCoupon(@PathVariable UUID coupon_id) {
        return ResponseEntity.ok(this.couponService.deleteCoupon(coupon_id));
    }
}