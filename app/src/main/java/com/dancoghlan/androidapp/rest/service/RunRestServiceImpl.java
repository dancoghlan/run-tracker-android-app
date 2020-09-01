package com.dancoghlan.androidapp.rest.service;

import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.rest.client.RestClient;
import com.dancoghlan.androidapp.rest.client.SpringRestClientImpl;

import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public class RunRestServiceImpl implements RunRestService {
    private final RestClient restClient;

    public RunRestServiceImpl() {
        this.restClient = new SpringRestClientImpl();

    }

    @Override
    public void save(RunContext run) {
        restClient.save(run);
    }

    @Override
    public List<RunContext> getAll() throws ResourceAccessException {
        return restClient.getAll();
    }

    @Override
    public Double getTotalDistance() {
        return restClient.getTotalDistance();
    }

    @Override
    public void delete(long id) {
        restClient.delete(id);
    }


}
