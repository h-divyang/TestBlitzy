package com.catering.service.common;

import java.util.List;

/**
 * Provides functionality to map and convert between objects of different types,
 * typically between entities and DTOs. Additionally, supports conversion of
 * lists containing such objects.
 */
public interface ModelMapperService {

	/**
	 * Converts the source object of type S to an object of type D.
	 *
	 * @param <S> The source type of the object to be converted.
	 * @param <D> The destination type of the object to be returned.
	 * @param source The object of type S that needs to be converted.
	 * @param destinationType The class of the destination type D to which the source object will be converted.
	 * @return The converted object of type D.
	 */
	<S, D> D convertEntityAndDto(S source, Class<D> destinationType);

	/**
	 * Converts a list of objects of type S to a list of objects of type D.
	 *
	 * @param <S> The source type of the objects in the input list to be converted.
	 * @param <D> The destination type of the objects in the output list.
	 * @param source The list of objects of type S to be converted.
	 * @param destinationType The class of the destination type D to which the objects in the source list will be converted.
	 * @return A list of converted objects of type D.
	 */
	<S, D> List<D> convertListEntityAndListDto(List<S> source, Class<D> destinationType);

}