package com.springboot.app.models.dao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.springboot.app.models.entity.Cliente;


public interface IClienteDAO extends PagingAndSortingRepository<Cliente, Long >, CrudRepository<Cliente, Long>{

	@Query("select c from Cliente c left join fetch  c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);
	
}
