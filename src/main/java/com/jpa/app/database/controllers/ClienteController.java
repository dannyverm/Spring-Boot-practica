package com.jpa.app.database.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.jpa.app.database.controllers.paginator.PageRender;
import com.jpa.app.database.models.entity.Cliente;
import com.jpa.app.database.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes(names = "cliente")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageR = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageR);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("cliente", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @GetMapping(value = "/form")
    public String form(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("titulo", "Guardar nuevo cliente");
        model.addAttribute("cliente", cliente);
        return "form";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {
        
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);//clienteService.findOne(id);
        
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }

    @PostMapping(value = "/form")
    public String guardar(Cliente cliente, SessionStatus status, RedirectAttributes flash,
            @RequestParam("file") MultipartFile foto) {
        if (!foto.isEmpty()) {

            // String rootPath="C://temp//uploads";
            Path rootPath = Paths.get("uploads").resolve(foto.getOriginalFilename());
            Path rootAdsolutPath = rootPath.toAbsolutePath();

            try {
                // byte[] bytes=foto.getBytes();
                // Path rutaCompleta=Paths.get(rootPath+"//"+foto.getOriginalFilename());
                // Files.write(rutaCompleta, bytes);
                flash.addFlashAttribute("info", "Ha subido correctamente '" + foto.getOriginalFilename() + "'");
                Files.copy(foto.getInputStream(), rootAdsolutPath);
                cliente.setFoto(foto.getOriginalFilename());
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", "Se han guardado los datos");
        return "redirect:/listar";
    }

    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Cliente cli = null;

        if (id > 0) {
            cli = clienteService.findOne(id);
        } else {
            flash.addFlashAttribute("success", "El registro se a editado correctamente");
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cli);
        model.addAttribute("titulo", "Editar Cliente");
        return "form";
    }

    @GetMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        if (id > 0) {
            Cliente cli = clienteService.findOne(id);
            clienteService.delete(id);
            Path rooPath = Paths.get("uploads").resolve(cli.getFoto()).toAbsolutePath();
            File archivo = rooPath.toFile();
            if (archivo.exists() && archivo.canRead()) {
                archivo.delete();
            }

        }
        return "redirect:/listar";
    }

}
