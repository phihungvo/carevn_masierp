package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.service.dto.ContractDTO;
import com.masi.sale.service.dto.EmployeeDTO;
import com.masi.sale.service.dto.OrderDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Order.
 */
@Table("masi_order")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private OrderStatus status;

    @NotNull(message = "must not be null")
    @Column("order_code")
    private String orderCode;

    @NotNull(message = "must not be null")
    @Column("number_order")
    private Integer numberOrder;

    @NotNull(message = "must not be null")
    @Column("date_order")
    private LocalDate dateOrder;

    @NotNull(message = "must not be null")
    @Column("contract_id")
    private UUID contractId;

    @NotNull(message = "must not be null")
    @Column("package_type")
    private String packageType;
    @Transient
    @jakarta.persistence.Transient
    private Collection<QualityIndex> qualityIndexes;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private String quantity;

    @NotNull(message = "must not be null")
    @Column("protein")
    private String protein;

    @NotNull(message = "must not be null")
    @Column("humidity")
    private String humidity;

    @NotNull(message = "must not be null")
    @Column("ashing")
    private String ashing;

    @NotNull(message = "must not be null")
    @Column("fat")
    private String fat;

    @NotNull(message = "must not be null")
    @Column("salt")
    private String salt;

    @NotNull(message = "must not be null")
    @Column("tvn")
    private String tvn;

    @NotNull(message = "must not be null")
    @Column("impurities")
    private String impurities;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("finish_date")
    private LocalDate finishDate;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private UUID createdBy;

    @Column("updated_by")
    private UUID updatedBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<OrderReview> orderReviews = new HashSet<>();

    @JsonIgnoreProperties(value = {}, allowSetters = true)
    @Transient
    private Contract contract;

    @JsonIgnoreProperties(value = {}, allowSetters = true)
    @Transient
    private ContractDTO contractDTO;

    @Transient
    private EmployeeDTO createdByDTO;

    //////////////////// 16-09 /////////////////

    @Column("delivery_term_from")
    private LocalDate deliveryTermFrom;

    @Column("delivery_term_to")
    private LocalDate deliveryTermTo;

    @Column("pay_term")
    private String payTerm;

    @Column("pay_condition")
    private String payCondition ;

    @Column("delivery_location")
    private String deliveryLocation;

    @Column("monetary_unit")
    private String monetaryUnit;

//    @Column("exchange_rate")
    private Float exchangeRate;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Order id(UUID id) {
        this.setId(id);
        return this;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public Order orderCode(String orderCode) {
        this.setOrderCode(orderCode);
        return this;
    }

    public Order numberOrder(Integer numberOrder) {
        this.setNumberOrder(numberOrder);
        return this;
    }

    public Order dateOrder(LocalDate dateOrder) {
        this.setDateOrder(dateOrder);
        return this;
    }

    public Order contractId(UUID contractId) {
        this.setContractId(contractId);
        return this;
    }

    public Order packageType(String packageType) {
        this.setPackageType(packageType);
        return this;
    }

    public Order quantity(String quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public Order protein(String protein) {
        this.setProtein(protein);
        return this;
    }

    public Order humidity(String humidity) {
        this.setHumidity(humidity);
        return this;
    }

    public Order ashing(String ashing) {
        this.setAshing(ashing);
        return this;
    }

    public Order fat(String fat) {
        this.setFat(fat);
        return this;
    }

    public Order salt(String salt) {
        this.setSalt(salt);
        return this;
    }

    public Order tvn(String tvn) {
        this.setTvn(tvn);
        return this;
    }

    public Order impurities(String impurities) {
        this.setImpurities(impurities);
        return this;
    }

    public Order note(String note) {
        this.setNote(note);
        return this;
    }

    public Order finishDate(LocalDate finishDate) {
        this.setFinishDate(finishDate);
        return this;
    }

    public Order lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public Order createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Order isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Order setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Order orderReviews(Set<OrderReview> orderReviews) {
        this.setOrderReviews(orderReviews);
        return this;
    }

    public Order addOrderReview(OrderReview orderReview) {
        this.orderReviews.add(orderReview);
        orderReview.setOrder(this);
        return this;
    }

    public Order removeOrderReview(OrderReview orderReview) {
        this.orderReviews.remove(orderReview);
        orderReview.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    public OrderDTO toDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(this.id);
        orderDTO.setStatus(this.status);
        orderDTO.setOrderCode(this.orderCode);
        orderDTO.setNumberOrder(this.numberOrder);
        orderDTO.setDateOrder(this.dateOrder);
        orderDTO.setContractId(this.contractId);
        orderDTO.setPackageType(this.packageType);
        orderDTO.setQuantity(this.quantity);
        orderDTO.setProtein(this.protein);
        orderDTO.setHumidity(this.humidity);
        orderDTO.setAshing(this.ashing);
        orderDTO.setFat(this.fat);
        orderDTO.setSalt(this.salt);
        orderDTO.setTvn(this.tvn);
        orderDTO.setImpurities(this.impurities);
        orderDTO.setNote(this.note);
        orderDTO.setFinishDate(this.finishDate);
        orderDTO.setLastUpdated(this.lastUpdated);
        orderDTO.setCreatedDate(this.createdDate);
        orderDTO.setIsDeleted(this.isDeleted);
        orderDTO.setCreatedBy(this.createdBy);

        orderDTO.setDeliveryTermFrom(this.deliveryTermFrom);
        orderDTO.setDeliveryTermTo(this.deliveryTermTo);
        orderDTO.setPayTerm(this.payTerm);
        orderDTO.setPayCondition(this.payCondition);
        orderDTO.setDeliveryLocation(this.deliveryLocation);
        orderDTO.setMonetaryUnit(this.monetaryUnit);
//        orderDTO.setExchangeRate(this.exchangeRate);

        orderDTO.setContract(
                this.contract == null || this.contract.getId() == null ? null : this.contract.toBriefDTO());
        return orderDTO;
    }
}
