package com.eventostec.api.service;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public CouponResponseDTO addCouponToEvent(UUID event_id, CouponRequestDTO data) {
        Event event =  eventRepository.findById(event_id)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setDiscount(data.discount());
        newCoupon.setCode(data.code());
        newCoupon.setValid(data.valid());
        newCoupon.setEvent(event);

        couponRepository.save(newCoupon);
        
        return new CouponResponseDTO(
            newCoupon.getCode(),
            newCoupon.getDiscount(),
            newCoupon.getValid(),
            event.getId()
        );
    }

    public List<Coupon> consultCoupons(UUID eventId, LocalDateTime now) {
        return couponRepository.findByEventIdAndValidAfter(eventId, now);
    }

    public Coupon deleteCoupon(UUID coupon_id) {
        Coupon coupon = couponRepository.findById(coupon_id)
            .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        couponRepository.delete(coupon);
        return coupon;
    }
}
