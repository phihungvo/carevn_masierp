package com.carevn.masi;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
//        "fromDate": "2024-12-10",
//                "zonedFromDate": "2024-12-10T00:00:00Z",
//                "toDate": "2024-12-12",
        var from = LocalDate.of(2024, 12, 10);
        var to = LocalDate.of(2024, 12, 12);

       var totalDayOff = ChronoUnit.DAYS.between(from, to);
        System.out.println(totalDayOff);

    }
}