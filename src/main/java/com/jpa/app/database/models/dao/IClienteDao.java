package com.jpa.app.database.models.dao;

import com.jpa.app.database.models.entity.Cliente;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long> {
    
}
