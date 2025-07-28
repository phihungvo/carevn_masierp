package com.masi.sale.domain.enumeration;

/**
 * The OrderStatus enumeration.
 */
// Bao gồm các trạng thái sau:
// ●	Đợi duyệt
// ●	Đã duyệt
// ●	Từ chối
// ●	Đã hủy

// Khi đơn đặt hàng bị từ chối, sẽ có các giải pháp sau:
// ●	Không duyệt
// ●	Chờ

public enum OrderStatus {
    WAITING_APPROVAL, // đợi duyệt
    APPROVED, // đã duyệt
    NOT_APPROVE,  // không duyệt
    WAITING, // chờ
    REJECTED, // từ chối
    DELETED, // dont care
    NEW, // mới tạo
    CANCELLED, // huỷ
    NOT_DELIVERED,
    IN_PROGRESS_DELIVERED,
    FINISHED,


}
