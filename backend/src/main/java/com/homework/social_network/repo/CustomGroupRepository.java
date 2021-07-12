package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomGroupRepository {

    List<Group> getAllGroups(int start, int limit);

    List<Group> findAllByIds(List<UUID> ids);
}
