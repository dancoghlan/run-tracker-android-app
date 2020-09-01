package com.dancoghlan.androidapp.rest.client;

import com.dancoghlan.androidapp.model.RunContext;

import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public interface RestClient {

    void save(RunContext runContext);

    List<RunContext> getAll() throws ResourceAccessException;

    void delete(long id);

    Double getTotalDistance();

}
