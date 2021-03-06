package com.cairone.odataexample.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cairone.odataexample.dtos.ProvinciaFrmDto;
import com.cairone.odataexample.entities.PaisEntity;
import com.cairone.odataexample.entities.ProvinciaEntity;
import com.cairone.odataexample.entities.ProvinciaPKEntity;
import com.cairone.odataexample.exceptions.ServiceException;
import com.cairone.odataexample.repositories.PaisRepository;
import com.cairone.odataexample.repositories.ProvinciaRepository;

@Service
public class ProvinciaService {

	public static final String CACHE_NAME = "PROVINCIAS";

	@Autowired private PaisRepository paisRepository = null;
	@Autowired private ProvinciaRepository provinciaRepository = null;

	@Transactional(readOnly=true) @Cacheable(cacheNames=CACHE_NAME, key="#paisID + '-' + #provinciaID")
	public ProvinciaEntity buscarPorID(Integer paisID, Integer provinciaID) throws ServiceException {
		
		if(paisID == null) throw new ServiceException(ServiceException.MISSING_DATA, "EL ID DEL PAIS NO PUEDE SER NULO");
		if(provinciaID == null) throw new ServiceException(ServiceException.MISSING_DATA, "EL ID DE LA PROVINCIA NO PUEDE SER NULO");
		
		ProvinciaEntity provinciaEntity = provinciaRepository.findOne(new ProvinciaPKEntity(paisID, provinciaID));

		if(provinciaEntity == null) {
			throw new ServiceException(ServiceException.ENTITY_NOT_FOUND, String.format("NO SE ENCUENTRA LA PROVINCIA CON CLAVE [PAIS: %s,PROVINCIA: %s]", paisID, provinciaID));
		}
		
		return provinciaEntity;
	}

	@Transactional @CachePut(cacheNames=CACHE_NAME, key="#provinciaFrmDto.paisID + '-' + #provinciaFrmDto.id")
	public ProvinciaEntity nuevo(ProvinciaFrmDto provinciaFrmDto) throws ServiceException {
		
		PaisEntity paisEntity = paisRepository.findOne(provinciaFrmDto.getPaisID());
		
		if(paisEntity == null) {
			throw new ServiceException(ServiceException.ENTITY_NOT_FOUND, String.format("NO SE ENCUENTRA EL PAIS CON ID %s", provinciaFrmDto.getPaisID()));
		}
		
		ProvinciaEntity provinciaEntity = new ProvinciaEntity();
		
		provinciaEntity.setId(provinciaFrmDto.getId());
		provinciaEntity.setPais(paisEntity);
		provinciaEntity.setNombre(provinciaFrmDto.getNombre());
		
		provinciaRepository.save(provinciaEntity);
		
		return provinciaEntity;
	}

	@Transactional @CachePut(cacheNames=CACHE_NAME, key="#provinciaFrmDto.paisID + '-' + #provinciaFrmDto.id")
	public ProvinciaEntity actualizar(ProvinciaFrmDto provinciaFrmDto) throws ServiceException {
		
		if(provinciaFrmDto == null || provinciaFrmDto.getId() == null || provinciaFrmDto.getPaisID() == null) {
			throw new ServiceException(ServiceException.ENTITY_NOT_FOUND, "NO SE PUEDE IDENTIFICAR LA PROVINCIA A ACTUALIZAR");
		}
		
		ProvinciaEntity provinciaEntity = provinciaRepository.findOne(new ProvinciaPKEntity(provinciaFrmDto.getPaisID(), provinciaFrmDto.getId()));
		
		if(provinciaEntity == null) {
			throw new ServiceException(ServiceException.ENTITY_NOT_FOUND, String.format("NO SE PUEDE ENCONTRAR UNA PROVINCIA CON ID [PAIS=%s,PROVINCIA=%s]", provinciaFrmDto.getPaisID(), provinciaFrmDto.getId()));
		}
		
		provinciaEntity.setNombre(provinciaFrmDto.getNombre());
		
		provinciaRepository.save(provinciaEntity);
		
		return provinciaEntity;
	}

	@Transactional @CacheEvict(cacheNames=CACHE_NAME, key="#paisID + '-' + #provinciaID")
	public void borrar(Integer paisID, Integer provinciaID) throws ServiceException {
		
		ProvinciaEntity provinciaEntity = provinciaRepository.findOne(new ProvinciaPKEntity(paisID, provinciaID));
		
		if(provinciaEntity == null) {
			throw new ServiceException(ServiceException.ENTITY_NOT_FOUND, String.format("NO SE PUEDE ENCONTRAR UNA PROVINCIA CON ID [PAIS=%s,PROVINCIA=%s]", paisID, provinciaID));
		}
		
		provinciaRepository.delete(provinciaEntity);
	}
}
