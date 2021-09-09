package com.jpa.app.database.models.dao;

import com.jpa.app.database.models.entity.Cliente;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long> {
 
    //Si existe un cliente que no tenga factura el sistema lanzara un error 
    //diciendo que no existe el cliente porque hacemos una consulta a dos tablas
    //para resolver este inconveniente usaremos left join
    @Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
    public Cliente fetchByIdWithFacturas(Long id);
}
