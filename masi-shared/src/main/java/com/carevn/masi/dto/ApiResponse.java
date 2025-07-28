package com.carevn.masi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
public class ApiResponse<T> {
    private List<T> data;
    private Long totalRecord;

    public ApiResponse(List<T> data, Long totalRecord) {
        this.data = data;
        this.totalRecord = totalRecord;
    }

    public ApiResponse() {
    }

    public static <T> Mono<ApiResponse<T>> from(Mono<List<T>> data, Mono<Long> totalRecord) {
        return Mono.zip(data, totalRecord, ApiResponse::new);
    }

    public static <T> Mono<ApiResponse<T>> from(Flux<T> data, Mono<Long> totalRecord) {
        return Mono.zip(data.collectList(), totalRecord, ApiResponse::new);
    }

}