package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;

import java.util.List;

public interface CustomGroupRepository {

    List<Group> getAllGroups(int start, int limit);
}
