package com.carevn.masi.sdk.dto;

public record CallDetail(String calldate, String caller, String callee, String did, String extension, String type,
                         String status, String callid, int duration, int billsec, String note) {

}