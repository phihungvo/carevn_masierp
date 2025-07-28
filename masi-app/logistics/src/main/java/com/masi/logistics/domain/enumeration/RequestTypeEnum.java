package com.masi.logistics.domain.enumeration;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The RequestTypeEnum enumeration.
 */
public enum RequestTypeEnum implements PaymentRequestHandler{
    PAYMENT {
        @Override
        public Mono<String> generateCodePrefix() {
            String requestCode = "DNTT" + RequestTypeEnum.getCurrentMonthDayString() + "/";
            return Mono.just(requestCode);
        }
    },
    ADVANCEMENT {
        @Override
        public Mono<String> generateCodePrefix() {
            String requestCode = "DNTU" + RequestTypeEnum.getCurrentMonthDayString() + "/";
            return Mono.just(requestCode);
        }
    },
    REIMBURSEMENT {
        @Override
        public Mono<String> generateCodePrefix() {
            String requestCode = "DNHU" + RequestTypeEnum.getCurrentMonthDayString() + "/";
            return Mono.just(requestCode);
        }
    };
    private static String getCurrentMonthDayString() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return currentDate.format(formatter);
    }
}
