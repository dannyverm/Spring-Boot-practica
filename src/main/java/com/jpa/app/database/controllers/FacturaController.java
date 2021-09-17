package com.jpa.app.database.controllers;

import java.util.List;

import com.jpa.app.database.models.entity.Cliente;
import com.jpa.app.database.models.entity.Factura;
import com.jpa.app.database.models.entity.ItemFactura;
import com.jpa.app.database.models.entity.Producto;
import com.jpa.app.database.models.services.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/form/{clienteId}")
    public String crear(@PathVariable Long clienteId, Model model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El Cliente selecionado no existe!!!");
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Crear Factura");
        return "/factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = "application/json")
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping(value = "/form")
    public String guardaString(Factura factura, Model model,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
            SessionStatus status) {

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "No hay articulos cargados!!!");
            return "/factura/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
        }

        clienteService.saveFactura(factura);
        status.setComplete();
        flash.addFlashAttribute("success", "Factura creada con exito!!");

        return "redirect:/ver/" + factura.getCliente().getId();
    }

    @GetMapping(value="/ver/{id}")
    public String verFactura(Model model,@PathVariable Long id, RedirectAttributes flash) {
        //clienteService.findFacturaById(id);
        //el metodo de arriba hace la consulta para buscar factura e iten y producto en 6 consltas
        //mientras que en la de abajo hacemos en una sola consulta con @Query en la interface IFacturaDao
        // optimizando asi nuestra consulta
        Factura factura= clienteService.fetchFacturaByIdWhithClienteWhithItemFacturaWithProducto(id);
        
        if( factura==null){
            flash.addFlashAttribute("error", "La factura no existe");
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura del cliente");
        
        return "factura/ver";
    }
    
    @GetMapping(value="/eliminar/{id}")
    public String eliminar(@PathVariable Long id,RedirectAttributes flash) {
        Factura factura=clienteService.findFacturaById(id);
        if(factura!=null){
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "La factura ha sido eliminada!!!");
            return "redirect:/ver/"+factura.getCliente().getId();
        }
        flash.addFlashAttribute("error", "La factura no existe!");
        return "redirect:/listar";
    }
    
    
}
