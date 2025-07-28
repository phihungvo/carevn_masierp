package com.masi.logistics.domain.enumeration;

import reactor.core.publisher.Mono;

public interface PaymentRequestHandler {
    Mono<String> generateCodePrefix();
}
