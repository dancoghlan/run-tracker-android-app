package com.dancoghlan.androidapp.util;

import com.dancoghlan.androidapp.model.Pace;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.rest.serialize.DurationDeserializer;
import com.dancoghlan.androidapp.rest.serialize.DurationSerializer;
import com.dancoghlan.androidapp.rest.serialize.PaceDeserializer;
import com.dancoghlan.androidapp.rest.serialize.PaceSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.joda.time.Duration;

import java.util.List;

public class RunContextObjectMapper {
    private final ObjectMapper objectMapper;

    public RunContextObjectMapper() {
        this.objectMapper = createObjectMapper();
    }

    public RunContext jsonToRunContext(String json) {
        try {
            return objectMapper.readValue(json, RunContext.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RunContext[] jsonToRunContextArray(String json) {
        try {
            return objectMapper.readValue(json, RunContext[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String writeValueAsString(List<RunContext> runContexts) {
        try {
            return objectMapper.writeValueAsString(runContexts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String writeValueAsString(RunContext runContext) {
        try {
            return objectMapper.writeValueAsString(runContext);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer(Duration.class, new DurationSerializer());
        module.addSerializer(Pace.class, new PaceSerializer());
        module.addDeserializer(Duration.class, new DurationDeserializer());
        module.addDeserializer(Pace.class, new PaceDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

}
