package com.masi.logistics.repository;

import com.masi.logistics.domain.IncomingInvoice;

import java.util.List;
import java.util.UUID;

import com.masi.logistics.service.dto.IncomingInvoiceDTO;
import com.masi.logistics.service.dto.IncomingInvoiceQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IncomingInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomingInvoiceRepository extends ReactiveCrudRepository<IncomingInvoice, UUID>, IncomingInvoiceRepositoryInternal {
    Flux<IncomingInvoice> findAllBy(Pageable pageable);

    @Override
    <S extends IncomingInvoice> Mono<S> save(S entity);

    @Override
    Flux<IncomingInvoice> findAll();

    @Override
    Mono<IncomingInvoice> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE incoming_invoice SET status = :status WHERE id = :id AND company = :company  AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Integer> updateStatus(UUID id, String status, UUID company);

    // update document_id by id
    @Modifying
    @Query("UPDATE incoming_invoice SET document_id = :documentId WHERE id IN (:id) AND company = :company  AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Integer> updateDocumentId(List<UUID> id, UUID documentId, String company);

    @Modifying
    @Query("UPDATE incoming_invoice SET reimbursement_id = :reimbursementId WHERE id IN (:id) AND company = :company  AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Integer> updateReimbursementId(List<UUID> id, UUID reimbursementId, String company);

    // set null document_id by id
    @Modifying
    @Query("UPDATE incoming_invoice SET document_id = NULL WHERE document_id = :documentId AND company = :company  AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Integer> setNullDocumentId(UUID documentId, String company);

    // set null reimbursement by id
    @Modifying
    @Query("UPDATE incoming_invoice SET reimbursement = NULL WHERE reimbursement_id = :reimbursementId AND company = :company  AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Integer> setNullReimbursementId(UUID reimbursementId, String company);

    Flux<IncomingInvoice> findAllByInventoryInId(UUID id);

    @Query("SELECT * FROM incoming_invoice WHERE supplier_contract_id = :id AND deleted_at IS NULL AND deleted_by IS NULL AND company = :company")
    Flux<IncomingInvoiceDTO> findAllBySupplierContractId(UUID id, String company);
}

interface IncomingInvoiceRepositoryInternal {
    <S extends IncomingInvoice> Mono<S> save(S entity);

    Flux<IncomingInvoice> findAllBy(Pageable pageable);

    Flux<IncomingInvoice> findAll();

    Mono<IncomingInvoice> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IncomingInvoice> findAllBy(Pageable pageable, Criteria criteria);
    Flux<IncomingInvoice> findAllBy(IncomingInvoiceQuery query, Pageable pageable);
    Mono<Long> countAllBy(IncomingInvoiceQuery query);
}
