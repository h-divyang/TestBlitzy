package com.catering.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for making HTTP client requests using Java's {@link HttpClient}.
 * 
 * <p>This class provides a simple method to perform HTTP GET requests and parse 
 * the response into a specified type using Jackson's {@link ObjectMapper}.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * String url = "https://api.example.com/resource";
 * MyClass response = HttpClientUtils.get(url, MyClass.class, new ObjectMapper());
 * }</pre>
 *
 * <p>This class is non-instantiable and uses a single instance of {@link HttpClient}
 * to handle all HTTP requests.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientUtils {

	/**
	 * Singleton instance of {@link HttpClient} used for making HTTP requests.
	 */
	private static final HttpClient httpClient = HttpClient.newBuilder().build();

	/**
	 * Performs an HTTP GET request to the specified URL and maps the response to the given class type.
	 *
	 * @param <T> The type of the response object.
	 * @param url The URL to which the GET request will be made.
	 * @param clazz The class type to map the response body to.
	 * @param objectMapper An instance of {@link ObjectMapper} for parsing JSON responses. If null, a new instance will be created.
	 * @return The response body parsed into the specified type, or {@code null} if the HTTP response status is not 200 (OK).
	 * @throws IOException If an I/O error occurs during the HTTP request or JSON parsing.
	 * @throws InterruptedException If the HTTP request is interrupted.
	 * @throws IllegalArgumentException If the URL is malformed or the response body cannot be parsed into the specified class.
	 */
	public static <T> T get(String url, Class<T> clazz, ObjectMapper objectMapper) throws IOException, InterruptedException {
		if (Objects.isNull(objectMapper)) {
			objectMapper = new ObjectMapper();
		}
		HttpRequest httpRequest = HttpRequest
				.newBuilder(URI.create(url))
				.GET()
				.build();
		HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		if (HttpStatus.OK.value() == response.statusCode()) {
			return objectMapper.readValue(response.body(), clazz);
		}
		return null;
	}

}