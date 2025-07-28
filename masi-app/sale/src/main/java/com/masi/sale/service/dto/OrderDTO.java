package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.Material;
import com.masi.sale.domain.Order;
import com.masi.sale.domain.enumeration.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A DTO for the {@link com.masi.sale.domain.Order} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus status;

    @NotNull(message = "must not be null")
    private String orderCode;

    // @NotNull(message = "must not be null")
    @Builder.Default
    private Integer numberOrder = 0;

    // @NotNull(message = "must not be null")
    @Builder.Default
    private LocalDate dateOrder = LocalDate.now();

    private Collection<CreateQuantityIndexDto> qualityIndexes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = { ManufactureOrderDTO.class })
    private LinkedList<ManufactureOrderDTO> manufactureOrders;

    @NotNull(message = "must not be null")
    private UUID contractId;

    @NotNull(message = "must not be null")
    private String packageType;

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String quantity = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String protein = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String humidity = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String ashing = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String fat = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String salt = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String tvn = "0";

    // @NotNull(message = "must not be null")
    @Builder.Default
    private String impurities = "0";

    private String note;

    @NotNull(message = "must not be null")
    private LocalDate finishDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = { ContractDTO.class })
    private ContractDTO contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = { OrderReviewDTO.class })
    private Collection<OrderReviewDTO> orderReviews;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate waitUntil;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = { EmployeeDTO.class })
    private Collection<EmployeeDTO> employees;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID createdBy;

    ////////////// 16-07 //////////////

    private LocalDate deliveryTermFrom;

    private LocalDate deliveryTermTo;

    private String payTerm;

    private String payCondition;

    private String deliveryLocation;

    private String monetaryUnit;

    private float exchangeRate = 0;

    private Collection<UUID> contractMaterialUse;

    public void addManufactureOrder(ManufactureOrderDTO manufactureOrder) {
        if (Objects.isNull(this.manufactureOrders)) {
            this.manufactureOrders = new LinkedList<>();
        }
        this.manufactureOrders
                .add(manufactureOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
                "id='" + getId() + "'" +
                ", status='" + getStatus() + "'" +
                ", orderCode='" + getOrderCode() + "'" +
                ", numberOrder=" + getNumberOrder() +
                ", dateOrder='" + getDateOrder() + "'" +
                ", contractId='" + getContractId() + "'" +
                ", packageType='" + getPackageType() + "'" +
                ", quantity='" + getQuantity() + "'" +
                ", protein='" + getProtein() + "'" +
                ", humidity='" + getHumidity() + "'" +
                ", ashing='" + getAshing() + "'" +
                ", fat='" + getFat() + "'" +
                ", salt='" + getSalt() + "'" +
                ", tvn='" + getTvn() + "'" +
                ", impurities='" + getImpurities() + "'" +
                ", note='" + getNote() + "'" +
                ", finishDate='" + getFinishDate() + "'" +
                ", lastUpdated='" + getLastUpdated() + "'" +
                ", createdDate='" + getCreatedDate() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                "}";
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(this.status);
        order.setOrderCode(this.orderCode);
        order.setNumberOrder(this.numberOrder);
        order.setDateOrder(this.dateOrder);
        order.setContractId(this.contractId);
        order.setPackageType(this.packageType);
        order.setQuantity(this.quantity);
        order.setProtein(this.protein);
        order.setHumidity(this.humidity);
        order.setAshing(this.ashing);
        order.setFat(this.fat);
        order.setSalt(this.salt);
        order.setTvn(this.tvn);
        order.setImpurities(this.impurities);
        order.setNote(this.note);
        order.setFinishDate(this.finishDate);
        order.setLastUpdated(ZonedDateTime.now());
        order.setCreatedDate(ZonedDateTime.now());
        order.setIsDeleted(false);

        order.setDeliveryTermFrom(this.deliveryTermFrom);
        order.setDeliveryTermTo(this.deliveryTermTo);
        order.setPayTerm(this.payTerm);
        order.setPayCondition(this.payCondition);
        order.setDeliveryLocation(this.deliveryLocation);
        order.setMonetaryUnit(this.monetaryUnit);
//        order.setExchangeRate(this.exchangeRate);
        return order;
    }

    public void applyChanges(Order order) {
        if (Objects.isNull(order)) {
            return;
        }
        order.setOrderCode(this.orderCode);
        order.setNumberOrder(this.numberOrder);
        order.setDateOrder(this.dateOrder);
        order.setContractId(this.contractId);
        order.setPackageType(this.packageType);
        order.setQuantity(this.quantity);
        order.setProtein(this.protein);
        order.setHumidity(this.humidity);
        order.setAshing(this.ashing);
        order.setFat(this.fat);
        order.setSalt(this.salt);
        order.setTvn(this.tvn);
        order.setImpurities(this.impurities);
        order.setNote(this.note);
        order.setFinishDate(this.finishDate);
        order.setLastUpdated(ZonedDateTime.now());
        order.setCompany(this.company);
        order.setCreatedBy(this.createdBy);

        order.setDeliveryTermFrom(this.deliveryTermFrom);
        order.setDeliveryTermTo(this.deliveryTermTo);
        order.setPayTerm(this.payTerm);
        order.setPayCondition(this.payCondition);
        order.setDeliveryLocation(this.deliveryLocation);
        order.setMonetaryUnit(this.monetaryUnit);
//        order.setExchangeRate(this.exchangeRate);

    }
}
