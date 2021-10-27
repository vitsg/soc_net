package com.homework.social_network.repo;

import com.homework.social_network.entity.Photo;

import java.util.List;
import java.util.UUID;

public interface CustomRepository {

    List<Photo> getByOwnerIdAndMainIsFalse(UUID ownerId, int start, int limit);
}
