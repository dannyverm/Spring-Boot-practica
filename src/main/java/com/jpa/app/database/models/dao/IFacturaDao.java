package com.jpa.app.database.models.dao;

import com.jpa.app.database.models.entity.Factura;

import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura,Long> {
    
}
