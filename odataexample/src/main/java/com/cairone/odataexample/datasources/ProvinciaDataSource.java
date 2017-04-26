package com.cairone.odataexample.datasources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.cairone.odataexample.dtos.ProvinciaFrmDto;
import com.cairone.odataexample.dtos.validators.ProvinciaFrmDtoValidator;
import com.cairone.odataexample.edm.resources.ProvinciaEdm;
import com.cairone.odataexample.entities.ProvinciaEntity;
import com.cairone.odataexample.interfaces.DataSource;
import com.cairone.odataexample.interfaces.DataSourceProvider;
import com.cairone.odataexample.services.ProvinciaService;
import com.cairone.odataexample.utils.SQLExceptionParser;
import com.cairone.odataexample.utils.ValidatorUtil;

@Component
public class ProvinciaDataSource implements DataSourceProvider, DataSource {

	private static final String ENTITY_SET_NAME = "Provincias";
	
	@Autowired private ProvinciaService provinciaService = null;
	@Autowired private ProvinciaFrmDtoValidator provinciaFrmDtoValidator = null;

	@Autowired
	private MessageSource messageSource = null;
	
	@Override
	public Object create(Object entity) throws ODataException {

		if(entity instanceof ProvinciaEdm) {
			
			ProvinciaEdm provinciaEdm = (ProvinciaEdm) entity;
			ProvinciaFrmDto provinciaFrmDto = new ProvinciaFrmDto(provinciaEdm);

			try {
				ValidatorUtil.validate(provinciaFrmDtoValidator, messageSource, provinciaFrmDto);
				ProvinciaEntity provinciaEntity = provinciaService.nuevo(provinciaFrmDto);
				return new ProvinciaEdm(provinciaEntity);
			} catch (Exception e) {
				String message = SQLExceptionParser.parse(e);
				throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
		}
		
		throw new ODataApplicationException("LOS DATOS NO CORRESPONDEN A LA ENTIDAD PROVINCIA", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
	}

	@Override
	public Object update(Map<String, UriParameter> keyPredicateMap, Object entity, List<String> propertiesInJSON, boolean isPut) throws ODataException {

    	if(entity instanceof ProvinciaEdm) {
    		
    		ProvinciaEdm provincia = (ProvinciaEdm) entity;
    		ProvinciaFrmDto provinciaFrmDto;
    		
        	Integer provinciaID = Integer.valueOf( keyPredicateMap.get("id").getText() );
        	Integer paisID = Integer.valueOf( keyPredicateMap.get("paisId").getText() );
        	
    		if(isPut) {
    			provinciaFrmDto = new ProvinciaFrmDto(provincia);
    			provinciaFrmDto.setId(provinciaID);
    			provinciaFrmDto.setPaisID(paisID);
    		} else {
	    		ProvinciaEntity provinciaEntity = provinciaService.buscarPorID(paisID, provinciaID);
	    		
	    		if(provinciaEntity == null) {
	    			throw new ODataApplicationException(
	    					String.format("LA PROVINCIA CON ID (PAIS=%s,PROVINCIA=%s) NO EXITE", provincia.getPaisId(), provincia.getId()), HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
	    		}
	    		
	    		// *** CAMPO << NOMBRE >>
	    		
	    		if(propertiesInJSON.contains("nombre")) {
	    			provinciaEntity.setNombre(provincia.getNombre() == null || provincia.getNombre().trim().isEmpty() ? null : provincia.getNombre().trim().toUpperCase());
	    		}
	    		
	    		provinciaFrmDto = new ProvinciaFrmDto(provinciaEntity);
    		}
    		
			try {
				ValidatorUtil.validate(provinciaFrmDtoValidator, messageSource, provinciaFrmDto);
				return new ProvinciaEdm( provinciaService.actualizar(provinciaFrmDto) );
			} catch (Exception e) {
				String message = SQLExceptionParser.parse(e);
				throw new ODataApplicationException(message, HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
    	}
    	
    	throw new ODataApplicationException("LOS DATOS NO CORRESPONDEN A LA ENTIDAD PROVINCIA", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
	}

	@Override
	public Object delete(Map<String, UriParameter> keyPredicateMap) throws ODataException {

    	Integer provinciaID = Integer.valueOf( keyPredicateMap.get("id").getText() );
    	Integer paisID = Integer.valueOf( keyPredicateMap.get("paisId").getText() );
    	
    	try {
			provinciaService.borrar(paisID, provinciaID);
		} catch (Exception e) {
			throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}
    	
    	return null;
	}

	@Override
	public String isSuitableFor() {
		return ENTITY_SET_NAME;
	}

	@Override
	public DataSource getDataSource() {
		return this;
	}

	@Override
	public Object readFromKey(Map<String, UriParameter> keyPredicateMap) throws ODataException {
		
		Integer provinciaID = Integer.valueOf( keyPredicateMap.get("id").getText() );
    	Integer paisID = Integer.valueOf( keyPredicateMap.get("paisId").getText() );
    	
    	ProvinciaEntity provinciaEntity = provinciaService.buscarPorID(paisID, provinciaID);
    	ProvinciaEdm provinciaEdm = provinciaEntity == null ? null : new ProvinciaEdm(provinciaEntity);
    	
    	return provinciaEdm;
	}

	@Override
	public Iterable<?> readAll(OrderByOption orderByOption) throws ODataException {

		List<Sort.Order> orderByList = new ArrayList<Sort.Order>();
		
		if(orderByOption != null) {
			orderByOption.getOrders().forEach(orderByItem -> {
				
				Expression expression = orderByItem.getExpression();
				if(expression instanceof Member){
					
					UriInfoResource resourcePath = ((Member)expression).getResourcePath();
					UriResource uriResource = resourcePath.getUriResourceParts().get(0);
					
				    if (uriResource instanceof UriResourcePrimitiveProperty) {
				    	EdmProperty edmProperty = ((UriResourcePrimitiveProperty)uriResource).getProperty();
						Direction direction = orderByItem.isDescending() ? Direction.DESC : Direction.ASC;
						String property = edmProperty.getName();
						orderByList.add(new Order(direction, property));
				    }
				}
				
			});
		}
		
		return provinciaService.ejecutarConsulta(null, orderByList).stream().map(e -> new ProvinciaEdm(e)).collect(Collectors.toList());
	}
}
