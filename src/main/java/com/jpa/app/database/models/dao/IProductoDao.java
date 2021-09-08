package com.jpa.app.database.models.dao;

import java.util.List;

import com.jpa.app.database.models.entity.Producto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IProductoDao extends CrudRepository<Producto, Long>{
    
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByNombre(String term);

    //Otra forma de implementar el metodo find pero respetano la convencion 
    //en este caso findByNombreLikeIgnoreCase donde busca por nombre y utiliza Like de sql
    //e ignora mayusculas y minusculas
    public List<Producto> findByNombreLikeIgnoreCase(String term);

}
