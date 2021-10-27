package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID>, CustomGroupRepository, JpaSpecificationExecutor<Group> {

    Group getById(UUID id);

    @Query("select g from Group g left join fetch g.photo where g.id = :id")
    Group getByIdWithPhoto(UUID id);

}
