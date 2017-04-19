package com.cairone.odataexample.edm.resources;

import com.cairone.odataexample.OdataexampleEdmProvider;
import com.cairone.odataexample.annotations.EdmEntity;
import com.cairone.odataexample.annotations.EdmEntitySet;
import com.cairone.odataexample.annotations.EdmNavigationProperty;
import com.cairone.odataexample.annotations.EdmProperty;
import com.cairone.odataexample.annotations.ODataJPAEntity;
import com.cairone.odataexample.annotations.ODataJPAProperty;

@EdmEntity(name = "Localidad", key = { "paisId", "provinciaId", "localidadId" }, namespace = OdataexampleEdmProvider.NAME_SPACE, containerName = OdataexampleEdmProvider.CONTAINER_NAME)
@EdmEntitySet("Localidades")
@ODataJPAEntity("com.cairone.odataexample.entities.LocalidadEntity")
public class LocalidadEdm {

	@EdmProperty(name="paisId", nullable = false) @ODataJPAProperty("provincia.pais.id")
	private Integer paisId = null;
	
	@EdmProperty(name="provinciaId", nullable = false) @ODataJPAProperty("provincia.id")
	private Integer provinciaId = null;
	
	@EdmProperty(name="localidadId", nullable = false) @ODataJPAProperty("id")
	private Integer localidadId = null;

	@EdmNavigationProperty(name="provincia")
	private ProvinciaEdm provincia = null;

	@EdmProperty(name="nombre", nullable = false, maxLength=100)
	private String nombre = null;
	
	@EdmProperty(name="cp", nullable = false)
	private Integer cp = null;
	
	@EdmProperty(name="prefijo", nullable = false)
	private Integer prefijo = null;
	
	public LocalidadEdm() {}

	public LocalidadEdm(Integer localidadId, ProvinciaEdm provincia, String nombre, Integer cp, Integer prefijo) {
		super();
		this.paisId = provincia == null ? null : provincia.getPaisId();
		this.provinciaId = provincia == null ? null : provincia.getId();
		this.localidadId = localidadId;
		this.provincia = provincia;
		this.nombre = nombre;
		this.cp = cp;
		this.prefijo = prefijo;
	}

	public Integer getPaisId() {
		return paisId;
	}

	public void setPaisId(Integer paisId) {
		this.paisId = paisId;
	}

	public Integer getProvinciaId() {
		return provinciaId;
	}

	public void setProvinciaId(Integer provinciaId) {
		this.provinciaId = provinciaId;
	}

	public Integer getLocalidadId() {
		return localidadId;
	}

	public void setLocalidadId(Integer localidadId) {
		this.localidadId = localidadId;
	}

	public ProvinciaEdm getProvincia() {
		return provincia;
	}

	public void setProvincia(ProvinciaEdm provincia) {
		this.provincia = provincia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCp() {
		return cp;
	}

	public void setCp(Integer cp) {
		this.cp = cp;
	}

	public Integer getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(Integer prefijo) {
		this.prefijo = prefijo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localidadId == null) ? 0 : localidadId.hashCode());
		result = prime * result + ((paisId == null) ? 0 : paisId.hashCode());
		result = prime * result
				+ ((provinciaId == null) ? 0 : provinciaId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalidadEdm other = (LocalidadEdm) obj;
		if (localidadId == null) {
			if (other.localidadId != null)
				return false;
		} else if (!localidadId.equals(other.localidadId))
			return false;
		if (paisId == null) {
			if (other.paisId != null)
				return false;
		} else if (!paisId.equals(other.paisId))
			return false;
		if (provinciaId == null) {
			if (other.provinciaId != null)
				return false;
		} else if (!provinciaId.equals(other.provinciaId))
			return false;
		return true;
	}
	
}