package com.eventostec.api.repositories;

import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventostec.api.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

        @Query("SELECT e FROM Event e LEFT JOIN e.address a WHERE e.date >= :currentDate")
        public Page<Event> findUpComingEvents(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);

        @Query("SELECT e FROM Event e " +
        "LEFT JOIN e.address a " +
        "WHERE (:title = '' OR e.title LIKE CONCAT('%', :title, '%')) " +
        "AND (:city = '' OR a.city LIKE CONCAT('%', :city, '%')) " +
        "AND (:uf = '' OR a.uf LIKE CONCAT('%', :uf, '%')) " +
        "AND e.date BETWEEN :startDate AND :endDate")
        public Page<Event> findFilteredEvents(@Param("title") String title,
                                        @Param("city") String city,
                                        @Param("uf") String uf,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        Pageable pageable);
}