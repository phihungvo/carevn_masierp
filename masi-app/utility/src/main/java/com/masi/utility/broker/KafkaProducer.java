package com.masi.utility.broker;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;

public class KafkaProducer implements Supplier<String> {

    @Override
    public String get() {
        return "kakfa_producer";
    }
}
