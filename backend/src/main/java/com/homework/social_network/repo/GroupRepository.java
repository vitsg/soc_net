package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID>, CustomGroupRepository {

    Group getById(UUID id);

}
