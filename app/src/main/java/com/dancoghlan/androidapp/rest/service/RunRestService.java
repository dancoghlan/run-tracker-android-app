package com.dancoghlan.androidapp.rest.service;

import com.dancoghlan.androidapp.model.RunContext;

import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public interface RunRestService {

    void save(RunContext run);

    List<RunContext> getAll() throws ResourceAccessException;

    Double getTotalDistance();

    void delete(long id);

}
