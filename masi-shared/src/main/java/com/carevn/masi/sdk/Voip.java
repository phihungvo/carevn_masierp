package com.carevn.masi.sdk;

import com.carevn.masi.sdk.dto.CallDetail;
import com.carevn.masi.sdk.dto.CallHistoryQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface Voip {
    Mono<String> getAccessToken();

    Mono<Collection<CallDetail>> getCallHistory(CallHistoryQuery query);
}
