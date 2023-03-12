package com.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.ItemFactura;
import com.springboot.app.models.entity.Producto;
import com.springboot.app.models.service.IClienteService;
//Es importante mantener el objeto factura en una session, hasta que se procese el formulario y se envie al metodo guardar @SessionA..
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash) {
		//1.- Buscar factura por id
		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);   //clienteService.findFacturaById(id);
		//2.- Validad si la factura es null
		if(factura == null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: " .concat(factura.getDescripcion()));
		
		
		return "factura/ver";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {
		// 1.- Buscamos el id del cliente
		Cliente cliente = clienteService.findOne(clienteId);
		// 2.- Condicion si cliente.id es nulo redirigir al listado
		if (cliente == null) {
			flash.addFlashAttribute("error", "No se ha encontrado ningun cliente");
			return "redirect:/listar";
		}
		// 3.- Creamos la factura, estara mapeado al formulario
		Factura factura = new Factura();
		// 4.- Relacionamos una factura con un cliente
		factura.setCliente(cliente);
		// 5.- A traves del model pasamos la factura a la vista
		model.put("factura", factura);
		model.put("titulo", "Crear factura");

		return "factura/form";
	} 
	
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		return clienteService.findByNombreLikeIgnoreCase("%"+term+"%");
	}
	
	@PostMapping("/form")
	public String guardar(Factura factura, 
		@RequestParam(name="item_id[]", required=false) Long[] itemId,  
		@RequestParam(name="cantidad[]", required=false) Integer[] cantidad,
		RedirectAttributes flash, SessionStatus status) {
		
		for(int i=0; i < itemId.length; i++) {
			//Por cada linea de la factura, obtendremos el producto por el id que lo obtenemos a traves del arreglo
			Producto producto = clienteService.findProductoById(itemId[i]);
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID:" + itemId[i].toString()+ " , cantidad: " + cantidad[i].toString());
		}
		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con exito");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		Factura factura = clienteService.findFacturaById(id);
		if(factura!= null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con exito");
			return "redirect:/ver/"+ factura.getCliente().getId() ;
		}
		//Si es null
		flash.addFlashAttribute("error", "La factura no existe en la base de datos");
		return "redirect:/listar";
	}
	

}
