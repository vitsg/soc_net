package com.homework.social_network.entity.message;

import com.homework.social_network.entity.BaseEntity;
import com.homework.social_network.enums.MessageOnWallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages_on_wall")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageOnWall extends BaseEntity {

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "destination_id")
    private UUID destinationId;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageOnWallType messageOnWallType;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;
}
