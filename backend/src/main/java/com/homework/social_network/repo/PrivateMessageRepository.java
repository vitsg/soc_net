package com.homework.social_network.repo;

import com.homework.social_network.entity.message.PrivateMessage;
import com.homework.social_network.projections.DialogProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PrivateMessageRepository extends PagingAndSortingRepository<PrivateMessage, UUID> {

    @Query(value = "select m from PrivateMessage m where ((m.sourceId = :currentAccountId and m.destinationId = :targetId) or " +
            "(m.destinationId = :currentAccountId and m.sourceId = :targetId)) and m.sendDate <= :startLoadTime")
    List<PrivateMessage> getPrivateMessages(UUID currentAccountId, UUID targetId, LocalDateTime startLoadTime, Pageable pageable);

    @Query(value = "select count(m) from PrivateMessage m where ((m.sourceId = :currentAccountId and m.destinationId = :targetId) or " +
            "(m.destinationId = :currentAccountId and m.sourceId = :targetId)) and m.sendDate <= :startLoadTime")
    long getPrivateMessagesCount(UUID currentAccountId, UUID targetId, LocalDateTime startLoadTime);

    @Modifying
    @Query("update PrivateMessage m set m.accepted = true where m.id in :messageIds")
    void acceptMessages(List<UUID> messageIds);

    List<PrivateMessage> getByDestinationIdAndAccepted(UUID destinationId, boolean accepted);

    long countBySourceIdAndDestinationIdAndAccepted(UUID sourceId, UUID destinationId, boolean accepted);

    @Query(nativeQuery = true, value = "select cast(b.lastMessageId as varchar), cast(b.companionId as varchar) from ( " +
            "select a.*, " +
            "row_number() over (partition by companionId order by send_date desc) as row_number " +
            "from ( " +
            "select id as lastMessageId, " +
            "case when source_id = :currentAccountId then destination_id else source_id end as companionId, " +
            "send_date " +
            "from private_messages " +
            "where source_id = :currentAccountId or destination_id = :currentAccountId " +
            ") a " +
            ") b where row_number = 1 " +
            "order by send_date desc " +
            "offset :start limit :limit ")
    List<DialogProjection> getDialogs(UUID currentAccountId, int start, int limit);

    @Query(nativeQuery = true, value = "select count(*) from ( " +
            "select a.*, " +
            "row_number() over (partition by companionId order by send_date desc) as row_number " +
            "from ( " +
            "select id, " +
            "case when source_id = :currentAccountId then destination_id else source_id end as companionId, " +
            "send_date " +
            "from private_messages " +
            "where source_id = :currentAccountId or destination_id = :currentAccountId " +
            ") a " +
            ") b where row_number = 1 "
            )
    long countDialogs(UUID currentAccountId);

    @Query(nativeQuery = true, value = "select cast(b.lastMessageId as varchar), cast(b.companionId as varchar) from ( " +
            "select a.*, " +
            "row_number() over (partition by companionId order by send_date desc) as row_number " +
            "from ( " +
            "select id as lastMessageId, " +
            "case when source_id = :currentAccountId then destination_id else source_id end as companionId, " +
            "send_date " +
            "from private_messages " +
            "where (source_id = :currentAccountId and destination_id = :targetId) or (source_id = :targetId and destination_id = :currentAccountId) " +
            ") a " +
            ") b where row_number = 1 ")
    DialogProjection getDialog(UUID currentAccountId, UUID targetId);

    List<PrivateMessage> getAllByIdIn(List<UUID> ids);
}
