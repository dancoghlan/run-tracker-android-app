package com.dancoghlan.androidapp.rest.serialize;

import com.dancoghlan.androidapp.model.Pace;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PaceSerializer extends StdSerializer<Pace> {

    public PaceSerializer() {
        this(null);
    }

    public PaceSerializer(Class<Pace> t) {
        super(t);
    }

    @Override
    public void serialize(Pace pace, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(pace.toString());
    }

}
