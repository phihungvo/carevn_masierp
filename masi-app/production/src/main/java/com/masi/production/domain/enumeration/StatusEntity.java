package com.masi.production.domain.enumeration;

import com.masi.production.service.mapper.AdditiveMaterialChecklistMapper;
import lombok.Getter;

import java.util.Optional;

@Getter
public enum StatusEntity {
//    NEW(2, "Mới"),
    WAITING_APPROVED(4, "Chờ duyệt"),
    PROCESSING(5, "Đang xử lý"),
    REJECTED(6, "Từ chối"),
    CANCELLED(8, "Hủy"),
    APPROVED(10, "Đã duyệt"),
    PENDING(12, "Chờ xử lý"),
    COMPLETE_PRODUCTION(14, "Hoàn thành sản xuất"),

    MATERIAL(20, "Nguyên liệu"),


    NEW(19, "Mới"),
    ADDITIVES(18, "Phụ gia"),
    PRODUCTION(24, "Sản xuất"),
    PACKAGING(26, "Đóng gói"),
    PACKED_COMPLETED(32, "Nhập lô"),
    SHIPPED(34, "Nhập kho"),
    COMPLETED(36, "Hoàn thành"),
    WAREHOUSED(38, "Đã nhập kho"),;


    private final int value;
    private final String vietnameseName;

    StatusEntity(int value, String vietnameseName) {
        this.value = value;
        this.vietnameseName = vietnameseName;
    }

    public static StatusEntity fromValue(int value) {
        for (StatusEntity status : StatusEntity.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static String getVietnameseNameFromValue(int value) {
        for (StatusEntity status : StatusEntity.values()) {
            if (status.value == value) {
                return status.getVietnameseName();
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static Optional<StatusEntity> getNextStatus(StatusEntity current, Integer isSkip) {
        StatusEntity[] values = StatusEntity.values();
        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] == current) {
                return Optional.of(values[i + (isSkip)]);
            }
        }
        return Optional.empty();
    }
}
