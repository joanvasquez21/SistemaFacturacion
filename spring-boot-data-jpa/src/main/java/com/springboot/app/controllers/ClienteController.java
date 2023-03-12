package com.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.dao.IClienteDAO;
import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.service.IClienteService;
import com.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	//1.- Paginador, primero obtenemos la pagina actual
	// 2.- Crear el objeto pageable
	// 3.- Invocacion al service findall pero paginable 
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value="/ver/{id}")
	 public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash ) {
		 Cliente cliente = clienteService.fetchByIdWithFacturas(id);		//findOne(id);;
		 if(cliente==null) {
			 flash.addFlashAttribute("error", "El cliente no existe en la bd");
			 return "redirect:/listar";
		 }
		 model.put("cliente", cliente );
		 model.put("titulo", "Informacion del cliente: " + cliente.getNombre());
		return "ver";
	 }
	
	 @RequestMapping(value="listar", method=RequestMethod.GET)
	 public String listar(@RequestParam(name="page", defaultValue="0") int page,  Model model) {
		 
		 Pageable pageRequest = PageRequest.of(page, 4);
		 Page<Cliente> clientes = clienteService.findAll(pageRequest);
		 PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		 
		 model.addAttribute("page", pageRender);
		 model.addAttribute("titulo", "Listado de clientes");
		 model.addAttribute("clientes",clientes);
		 return "listar";
	 }
	 
	 @RequestMapping(value="/form")
	 public String crear(Map<String, Object> model ) {
		 Cliente cliente = new Cliente();
		 model.put("cliente", cliente);
		 model.put("titulo", "Formulario");
		 return "form";	 
	 }
	 
	 @RequestMapping(value="/form", method = RequestMethod.POST)
	 public String guardar(@Valid Cliente cliente,BindingResult result, Model model, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Listado de clientes");
			return "form";
		}
		clienteService.save(cliente);
		 status.setComplete();
		 return "redirect:listar";
	 }
	 
	 @RequestMapping(value="/form/{id}")
	 public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		 Cliente cliente = null;
		 if(id > 0) {
			cliente = clienteService.findOne(id); 
		 }else {
				return "redirect:/listar";
		 }
		 model.put("cliente", cliente);
		 model.put("titulo", "Editar cliente");
		 return "form";
	 }
	 
	 @RequestMapping(value="/eliminar/{id}")
	 public String eliminar(@PathVariable(value="id") Long id) {
		 if(id > 0) {
			 clienteService.delete(id);
		 }
		 
		 return "redirect:/listar";
	 }
	 
	 
	 
	 
	 
	 
}
