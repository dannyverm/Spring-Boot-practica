package com.jpa.app.database.models.services;

import java.util.List;

import com.jpa.app.database.models.dao.IClienteDao;
import com.jpa.app.database.models.dao.IFacturaDao;
import com.jpa.app.database.models.dao.IProductoDao;
import com.jpa.app.database.models.entity.Cliente;
import com.jpa.app.database.models.entity.Factura;
import com.jpa.app.database.models.entity.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiseImpl implements IClienteService {

    @Autowired
    IClienteDao clienteDao;

    @Autowired
    IProductoDao productoDao;

    @Autowired
    IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public Cliente findOne(Long id) {
        return clienteDao.findById(id).orElse(null) ;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombre(String term) {
        
        //return productoDao.findByNombre(term);
        //----dannyverm: Los dos return retornan lo mismo
        return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
       
        facturaDao.save(factura);        
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {
        // TODO Auto-generated method stub
        facturaDao.deleteById(id);
    }
    
}
