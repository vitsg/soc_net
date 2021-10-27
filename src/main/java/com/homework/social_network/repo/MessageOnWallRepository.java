package com.homework.social_network.repo;

import com.homework.social_network.entity.message.MessageOnWall;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageOnWallRepository extends PagingAndSortingRepository<MessageOnWall, UUID> {

    @Query(value = "select m from MessageOnWall m where m.destinationId = :targetId and m.sendDate <= :startLoadTime")
    List<MessageOnWall> getMessagesOnWall(UUID targetId, LocalDateTime startLoadTime, Pageable pageable);

    @Query(value = "select count(m) from MessageOnWall m where m.destinationId = :targetId and m.sendDate <= :startLoadTime")
    long getMessagesOnWallCount(UUID targetId, LocalDateTime startLoadTime);
}
