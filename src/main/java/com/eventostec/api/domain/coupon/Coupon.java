package com.eventostec.api.domain.coupon;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.eventostec.api.domain.event.Event;

@Table(name = "coupon")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;

    private Integer discount;
    private String code;
    private Date valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}