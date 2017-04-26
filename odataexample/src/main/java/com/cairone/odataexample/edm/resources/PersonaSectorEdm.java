package com.cairone.odataexample.edm.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cairone.odataexample.OdataexampleEdmProvider;
import com.cairone.odataexample.annotations.EdmEntity;
import com.cairone.odataexample.annotations.EdmEntitySet;
import com.cairone.odataexample.annotations.EdmProperty;
import com.cairone.odataexample.entities.PersonaSectorEntity;

@EdmEntity(name = "PersonaSector", key = { "id" }, namespace = OdataexampleEdmProvider.NAME_SPACE, containerName = OdataexampleEdmProvider.CONTAINER_NAME)
@EdmEntitySet("PersonasSectores")
public class PersonaSectorEdm {

	@EdmProperty(name="id", nullable = false)
	private Integer id = null;
	
	@EdmProperty(name="nombre", nullable = false, maxLength=100)
	private String nombre = null;

	@EdmProperty(name="fechaIngreso", nullable = false)
	private LocalDate fechaIngreso = null;
	
	public PersonaSectorEdm() {}

	public PersonaSectorEdm(Integer id, String nombre, LocalDate fechaIngreso) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso;
	}
	
	public PersonaSectorEdm(PersonaSectorEntity personaSectorEntity) {
		this(personaSectorEntity.getSector().getId(), personaSectorEntity.getSector().getNombre(), personaSectorEntity.getFechaIngreso());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public static List<PersonaSectorEdm> crearLista(Iterable<PersonaSectorEntity> personaSectorEntities) {
		
		List<PersonaSectorEdm> lista = new ArrayList<PersonaSectorEdm>();
		
		for(PersonaSectorEntity personaSectorEntity : personaSectorEntities) {
			lista.add(new PersonaSectorEdm(personaSectorEntity));
		}
		
		return lista;
	}
}
