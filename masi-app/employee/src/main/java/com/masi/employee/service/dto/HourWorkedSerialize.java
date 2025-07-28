package com.masi.employee.service.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class HourWorkedSerialize extends StdSerializer<Float> {

    protected HourWorkedSerialize(Class<Float> t) {
        super(t);
    }
    protected HourWorkedSerialize() {
        super(Float.class);
    }
    @Override
    public void serialize(Float arg0, JsonGenerator arg1, SerializerProvider arg2) {
        if (arg0 < 0) {
            try {
                arg1.writeNull();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (arg0 % 1 == 0) {
                    arg1.writeNumber(arg0.intValue());
                } else {
                    arg1.writeNumber(arg0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
