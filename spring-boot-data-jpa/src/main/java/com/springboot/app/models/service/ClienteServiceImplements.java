package com.springboot.app.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IClienteDAO;
import com.springboot.app.models.dao.IFacturaDAO;
import com.springboot.app.models.dao.IProductoDAO;
import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImplements implements IClienteService{

	@Autowired
	private IClienteDAO clienteDAO;
	
	@Autowired
	private IProductoDAO productoDAO;
	
	@Autowired
	private IFacturaDAO facturaDAO;
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDAO.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {

		clienteDAO.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return clienteDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		clienteDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		
		return clienteDAO.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		// TODO Auto-generated method stub
		return productoDAO.findByNombre(term);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombreLikeIgnoreCase(String term) {
		// TODO Auto-generated method stub
		return productoDAO.findByNombreLikeIgnoreCase(term);
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDAO.save(factura);
	}

	@Override
	@Transactional(readOnly=true)
	public Producto findProductoById(Long id) {
		// TODO Auto-generated method stub
		return productoDAO.findById(id).orElse(null);
	
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public void deleteFactura(Long id) {

		facturaDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		// TODO Auto-generated method stub
		return facturaDAO.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		// TODO Auto-generated method stub
		return clienteDAO.fetchByIdWithFacturas(id);
	}
	
	
	
}
