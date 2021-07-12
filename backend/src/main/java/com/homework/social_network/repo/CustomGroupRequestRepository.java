package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupRequest;

import java.util.List;
import java.util.UUID;

public interface CustomGroupRequestRepository {

    List<UUID> findByOwnerAccountId(UUID ownerAccountId, int start, int limit);
}
