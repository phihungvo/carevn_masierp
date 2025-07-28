package com.masi.sale.domain.enumeration;

/**
 * The ContractStatus enumeration.
 */
public enum ContractStatus {
    DRAFT,
    WAITING_APPROVAL,
    APPROVED,
    FINISHED,
    WAITING_LIQUIDATION,
    LIQUIDATED,
    LIQUIDATE_CANCELLED,
    CANCELLED,
    DELETED,
    REJECTED
}
