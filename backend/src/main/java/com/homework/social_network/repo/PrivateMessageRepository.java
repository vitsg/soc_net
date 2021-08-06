package com.homework.social_network.repo;

import com.homework.social_network.entity.message.PrivateMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PrivateMessageRepository extends PagingAndSortingRepository<PrivateMessage, UUID> {

    @Query(value = "select m from PrivateMessage m where (m.sourceId = :currentAccountId and m.destinationId = :targetId) or " +
            "(m.destinationId = :currentAccountId and m.sourceId = :targetId) and m.createDate <= :startLoadTime")
    List<PrivateMessage> getPrivateMessages(UUID currentAccountId, UUID targetId, LocalDateTime startLoadTime, Pageable pageable);

    @Modifying
    @Query("update PrivateMessage m set m.accepted = true where m.id in :messageIds")
    void acceptMessages(List<UUID> messageIds);

    List<PrivateMessage> getByDestinationIdAndAccepted(UUID destinationId, boolean accepted);
}
