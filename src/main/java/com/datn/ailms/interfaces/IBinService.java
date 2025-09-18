package com.datn.ailms.interfaces;

import com.datn.ailms.model.entities.topo_entities.Bin;

import java.util.List;
import java.util.UUID;

public interface IBinService {
    List<Bin> findAllByShelfIdNativeQuery(UUID shelfId);
}
