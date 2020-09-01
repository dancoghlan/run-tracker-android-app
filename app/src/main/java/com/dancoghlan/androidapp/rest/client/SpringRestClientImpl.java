package com.dancoghlan.androidapp.rest.client;

import com.dancoghlan.androidapp.model.RunContext;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpringRestClientImpl implements RestClient {
    private final UriComponentsBuilder uriComponentsBuilder;
    private final RestTemplate restTemplate;

    public SpringRestClientImpl() {
        this.restTemplate = new RestTemplate(true);
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        int connectionTimeoutSeconds = 10;
        factory.setConnectTimeout(connectionTimeoutSeconds * 1000);
        factory.setReadTimeout(connectionTimeoutSeconds * 1000);
        this.uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("192.168.0.164")
                .port(8080)
                .path("/rest");
    }

    @Override
    public void save(RunContext runContext) {
        // Set HTTP headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Create URL
        String uri = uriComponentsBuilder
                .path("/save")
                .build()
                .toUriString();

        // Send REST request
        HttpEntity<RunContext> request = new HttpEntity<>(runContext, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, request, String.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode != HttpStatus.OK) {
//            throw new RuntimeException("Received error response [" + statusCode + "]");
        }
    }

    @Override
    public List<RunContext> getAll() throws ResourceAccessException {
        // Create URL
        String uri = uriComponentsBuilder
                .path("/getAll")
                .build()
                .toUriString();

        // Send REST request
        ResponseEntity<RunContext[]> responseEntity;
        responseEntity = restTemplate.getForEntity(uri, RunContext[].class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode != HttpStatus.OK) {
//            throw new RuntimeException("Received error response [" + statusCode + "]");
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public void delete(long id) {
        // Set HTTP headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Create URL
        String uri = uriComponentsBuilder
                .path("/delete")
                .queryParam("id", id)
                .build()
                .toUriString();

        // Send REST request
        HttpEntity<RunContext> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode != HttpStatus.OK) {
//            throw new RuntimeException("Received error response [" + statusCode + "]");
        }
    }

    @Override
    public Double getTotalDistance() {
        // Create URL
        String uri = uriComponentsBuilder
                .path("/getTotalDistance")
                .build()
                .toUriString();

        // Send REST request
        ResponseEntity<Double> responseEntity = restTemplate.getForEntity(uri, Double.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode != HttpStatus.OK) {
//            throw new RuntimeException("Received error response [" + statusCode + "]");
            return 0d;
        }
        return responseEntity.getBody();
    }

}
