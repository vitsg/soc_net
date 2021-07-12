package com.homework.social_network.repo;

import java.util.List;
import java.util.UUID;

public interface CustomFriendRequestRepository {

    List<UUID> getFriendIds(UUID ownerAccountId, int start, int limit);

    List<UUID> getInputFriendsIds(UUID ownerAccountId, int start, int limit);

    List<UUID> getOutputFriendIds(UUID ownerAccountId, int start, int limit);
}
