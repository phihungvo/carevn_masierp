package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncomingInvoice} and its DTO {@link IncomingInvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface IncomingInvoiceMapper extends EntityMapper<IncomingInvoiceDTO, IncomingInvoice> {
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierId")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    @Mapping(target = "paymentRequest", source = "paymentRequest", qualifiedByName = "paymentRequestId")
    @Mapping(target = "reimbursement", source = "reimbursement", qualifiedByName = "reimbursementId")
    @Mapping(target = "supplierContract", source = "supplierContract", qualifiedByName = "contractId")
    IncomingInvoiceDTO toDto(IncomingInvoice incomingInvoice);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SuppliersDTO toSupplierDTO(Suppliers suppliers);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CurrencyDTO toCurrencyDTO(Currency currency);

    @Named("paymentRequestId")
    @BeanMapping(ignoreByDefault = false)
    PaymentRequestDTO toPaymentRequestDTO(PaymentRequest paymentRequest);

    @Named("reimbursementId")
    @BeanMapping(ignoreByDefault = false)
    PaymentRequestDTO toReimbursementDTO(PaymentRequest paymentRequest);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = false)
    SupplierContractDTO toSupplierContractDTO(SupplierContract supplierContract);
}
