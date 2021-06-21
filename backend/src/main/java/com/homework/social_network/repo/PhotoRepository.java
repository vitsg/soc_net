package com.homework.social_network.repo;

import com.homework.social_network.entity.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    Photo getByOwnerIdAndMainIsTrue(UUID ownerId);

    List<Photo> getByOwnerIdAndMainIsFalse(UUID ownerId, Pageable pageable);

    void deleteAllByIdIn(List<UUID> ids);

    int countByOwnerIdAndMainIsFalse(UUID ownerId);
}
