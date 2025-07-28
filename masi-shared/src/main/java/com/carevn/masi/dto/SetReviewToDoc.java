package com.carevn.masi.dto;

@FunctionalInterface
public interface SetReviewToDoc<T extends Reviewable<V>, V> {
    void apply(T docs, V review);
}
