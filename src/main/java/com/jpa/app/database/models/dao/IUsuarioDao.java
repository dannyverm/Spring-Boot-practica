package com.jpa.app.database.models.dao;

import com.jpa.app.database.models.entity.Usuario;

import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    
    public Usuario findByUsername(String username);
}
