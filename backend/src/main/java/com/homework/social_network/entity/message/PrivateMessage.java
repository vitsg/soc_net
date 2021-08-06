package com.homework.social_network.entity.message;


import com.homework.social_network.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "private_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateMessage extends BaseEntity {

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "destination_id")
    private UUID destinationId;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "accepted")
    private boolean accepted;
}
