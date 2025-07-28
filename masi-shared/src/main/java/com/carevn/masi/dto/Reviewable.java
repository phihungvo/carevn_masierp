package com.carevn.masi.dto;

import java.util.UUID;

public interface Reviewable<T> {
     UUID getDocumentId();
     public void addReview(T review);
}
