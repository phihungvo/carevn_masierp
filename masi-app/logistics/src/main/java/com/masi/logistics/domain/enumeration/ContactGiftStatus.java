package com.masi.logistics.domain.enumeration;

/**
 * The ContactGiftStatus enumeration.
 */
public enum ContactGiftStatus {
    SENT, // đã gửi
    DELIVERED, // đã giao
    ACCEPTED, // đã chấp nhận
    REJECTED, // đã từ chối
    IN_TRANSIT, // đang vận chuyển
    DAMAGED, // bị hỏng
    LOST, // bị mất
    PENDING, // chờ xử lý
}
