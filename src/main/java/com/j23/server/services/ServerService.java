package com.j23.server.services;

import java.util.Collection;

import com.j23.server.models.Server;

public interface ServerService {
    Server create(Server server);
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
}
