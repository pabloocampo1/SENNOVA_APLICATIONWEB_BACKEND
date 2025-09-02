package com.example.sennova.domain.port;

import com.example.sennova.domain.model.RoleModel;

public interface RolePersistencePort {
   RoleModel findByName(String nameRole);
}
