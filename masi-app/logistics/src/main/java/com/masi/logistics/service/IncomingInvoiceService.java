package com.masi.logistics.service;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.InvoiceSupplies;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.InvoiceSuppliesCriteria;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import com.masi.logistics.domain.criteria.RelatedCostsCriteria;
import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import com.masi.logistics.domain.enumeration.IncomingInvoiceType;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.mapper.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.IncomingInvoice}.
 */
@Service
@Transactional
@AllArgsConstructor
public class IncomingInvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingInvoiceService.class);

    private final IncomingInvoiceRepository incomingInvoiceRepository;

    private final IncomingInvoiceMapper incomingInvoiceMapper;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final InvoiceSuppliesRepository invoiceSuppliesRepository;
    private final InvoiceSuppliesMapper invoiceSuppliesMapper;
    private final RequestApprovalRepository requestApprovalRepository;
    private final RequestApprovalMapper requestApprovalMapper;
    private final RelatedCostsMapper relatedCostsMapper;
    private final SuppliersRepository suppliersRepository;
    private final RelatedCostsRepository relatedCostsRepository;
    private final InventoriesService inventoriesService;
    private final EmployeeClient employeeClient;
    private final InventoriesRepository inventoriesRepository;
    private final InventoriesMapper inventoriesMapper;


    private Mono<IncomingInvoiceQuery> resolveQuery(IncomingInvoiceQuery query) {
        return SecurityUtils.getUserJWTDetail()
                .map(user -> {
//                    if (user.isHasAbove(AuthoritiesConstants.DIRECTOR) || user.getGroupId().equals("SALE")) {
//                        //Bộ phận Kế toán, Giám đốc, Tổng Giám đốc thấy được tất cả các chứng từ
//                        return query;
//                    }
//                    if (user.isHasAbove(AuthoritiesConstants.DEPARTMENT_MANAGER)) {
//                        //Trưởng phòng chỉ thấy được chứng từ của phòng mình
//                        query.setDepartment(user.getGroupId());
//                        return query;
//                    }
//                    query.setCompanyId(user.getCompanyId());
//                    //Nhân viên chỉ thấy được chứng từ của mình
//                    query.setCreatedBy(user.getUserId().toString());
                    query.setCompanyId(user.getCompanyId());
                    return query;
                });
    }


    /**
     * Save a incomingInvoice.
     *
     * @param incomingInvoiceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IncomingInvoiceDTO> save(IncomingInvoiceDTO incomingInvoiceDTO) {
        LOG.debug("Request to save IncomingInvoice : {}", incomingInvoiceDTO);
        return documentCodeSequenceService.getByDocumentType(IncomingInvoice.ENTITY_NAME)
                        .flatMap(documentCodeSequence -> {
                            LocalDate currentDate = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                            String formattedDate = currentDate.format(formatter);
                            var codeNext = documentCodeSequence.getNextAndIncrement();
                            var codeType = incomingInvoiceDTO.getInvoiceType().equals(IncomingInvoiceType.IMPORT_INVOICE) ? "HDNK" : "HDDV";
                            var fullCode = codeType + formattedDate + "/" + codeNext;
                            incomingInvoiceDTO.setInvoiceNo(fullCode);
                            var invoiceSuppliesEntities = new ArrayList<>(invoiceSuppliesMapper.toEntity(new ArrayList<>(incomingInvoiceDTO.getInvoiceSupplies())));
                            invoiceSuppliesEntities.forEach(invoiceSupplies -> {
                                invoiceSupplies.setInvoiceId(incomingInvoiceDTO.getId());
                            });
                            if (incomingInvoiceDTO.getIsInvoice()){
                                incomingInvoiceDTO.setStatus(IncomingInvoiceStatus.PAID);
                            }
                            //incomingInvoiceDTO.setInvoiceNo(documentCodeSequence.getNextAndIncrement());
                            var entity = incomingInvoiceMapper.toEntity(incomingInvoiceDTO);
                            entity.setAttachments(incomingInvoiceDTO.getAttachments());
                            return incomingInvoiceRepository
                                    .save(entity)
                                    .map(incomingInvoiceMapper::toDto)
                                    .flatMap(incomingInvoiceDTO1 -> documentCodeSequenceService
                                            .updateSequence(documentCodeSequence)
                                            .then(Mono.just(incomingInvoiceDTO1))).flatMap(ic -> {
                                                return invoiceSuppliesRepository.saveAll(invoiceSuppliesEntities).collectList().flatMap(invoiceSupplies -> {
                                                    ic.setInvoiceSupplies(invoiceSuppliesMapper.toDto(new ArrayList<>(invoiceSupplies)));
                                                    if (ic.getInvoiceType().equals(IncomingInvoiceType.IMPORT_INVOICE)) {
                                                        List<RelatedCostsDTO> relatedCostsDTO =
                                                            incomingInvoiceDTO.getRelatedCosts() != null ?
                                                                new ArrayList<>(incomingInvoiceDTO.getRelatedCosts())
                                                                : new ArrayList<>();
                                                        var headerRelatedCosts = new RelatedCostsDTO();
                                                        headerRelatedCosts.setInvoiceId(ic.getId());
                                                        headerRelatedCosts.setNote(ic.getNote());
                                                        relatedCostsDTO.add(headerRelatedCosts);
                                                        var entityRelatedCosts = relatedCostsMapper.toEntity(relatedCostsDTO);
                                                        return relatedCostsRepository.saveAll(entityRelatedCosts)
                                                            .map(relatedCostsMapper::toDto)
                                                            .collectList()
                                                            .flatMap(relatedCosts -> {
                                                                ic.setRelatedCosts(new ArrayList<>(relatedCosts));
                                                                Mono<IncomingInvoiceDTO> saveInventories = saveInventories(incomingInvoiceDTO, ic);
                                                                if (saveInventories != null) return saveInventories;
                                                                return Mono.just(ic);
                                                            });
                                                    }
                                                    Mono<IncomingInvoiceDTO> saveInventories = saveInventories(incomingInvoiceDTO, ic);
                                                    if (saveInventories != null) return saveInventories;
                                                    return Mono.just(ic);
                                                });
                                });
                        });
    }

    private @Nullable Mono<IncomingInvoiceDTO> saveInventories(IncomingInvoiceDTO incomingInvoiceDTO, IncomingInvoiceDTO ic) {
        var inventoriesIds = incomingInvoiceDTO.getInventoryIds();
        if (inventoriesIds != null && !inventoriesIds.isEmpty()) {
            InventoriesCriteria inventoriesCriteria = getInventoriesCriteriaByIdsIn(inventoriesIds);
            return inventoriesRepository.findByCriteria(inventoriesCriteria, null)
                .collectList()
                .defaultIfEmpty(new ArrayList<>())
                .flatMap(inventories -> {
                    if (inventories.isEmpty()) {
                        return Mono.just(ic);
                    }

                    inventories.forEach(inventoriesDTO -> {
                        inventoriesDTO.setInvoiceId(ic.getId());
                        inventoriesDTO.setIsPersisted();
                    });
                    return inventoriesRepository.saveAll(inventories)
                        .collectList()
                        .flatMap(inventories1 -> {
                            ic.setInventories(new ArrayList<>(inventoriesMapper.toDto(new ArrayList<>(inventories1))));
                            return Mono.just(ic);
                        });

                });
        }
        return null;
    }

    private static InventoriesCriteria getInventoriesCriteriaByIdsIn(Collection<UUID> inventoriesIds) {
        InventoriesCriteria inventoriesCriteria = new InventoriesCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        inventoriesCriteria.setIsDeleted(isDeleted);
        uuidFilter.setIn(new ArrayList<>(inventoriesIds));
        inventoriesCriteria.setId(uuidFilter);
        return inventoriesCriteria;
    }

    private static InventoriesCriteria getInventoriesCriteriaByInvoiceIdsIn(Collection<UUID> invoiceIds) {
        InventoriesCriteria inventoriesCriteria = new InventoriesCriteria();
        UUIDFilter invoiceIdsFilter = new UUIDFilter();
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        inventoriesCriteria.setIsDeleted(isDeleted);
        invoiceIdsFilter.setIn(new ArrayList<>(invoiceIds));
        inventoriesCriteria.setInvoiceId(invoiceIdsFilter);
        return inventoriesCriteria;
    }

    // save list incoming invoice:
    public Flux<IncomingInvoiceDTO> saveAll(List<IncomingInvoiceDTO> incomingInvoiceDTOList, UUID paymentRequestId, UUID supplierId, boolean isPaymentRequest) {
        return documentCodeSequenceService.getByDocumentType(IncomingInvoice.ENTITY_NAME)
            .flatMap(documentCodeSequence -> {
                return Flux.fromIterable(incomingInvoiceDTOList)
                    .flatMap(incomingInvoiceDTO -> {
                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                        String formattedDate = currentDate.format(formatter);
                        var codeNext = documentCodeSequence.getNextAndIncrement();
                        var codeType = "HDDV";
                        var fullCode = codeType + formattedDate + "/" + codeNext;
                        incomingInvoiceDTO.setInvoiceNo(fullCode);
                        incomingInvoiceDTO.setInvoiceType(IncomingInvoiceType.INVOICE);
                        incomingInvoiceDTO.setStatus(IncomingInvoiceStatus.NEW);
                        var totalAmount = incomingInvoiceDTO.getTotalAmount() == null ? BigDecimal.ZERO : incomingInvoiceDTO.getTotalAmount();
                        incomingInvoiceDTO.setGrandTotal(totalAmount);
                        incomingInvoiceDTO.setContent(incomingInvoiceDTO.getContent() == null ? incomingInvoiceDTO.getNote() == null ? "" : incomingInvoiceDTO.getNote() : incomingInvoiceDTO.getContent());
                        incomingInvoiceDTO.setInvoiceDate(ZonedDateTime.now());

                        var entity = incomingInvoiceMapper.toEntity(incomingInvoiceDTO);
                        entity.setAttachments(incomingInvoiceDTO.getAttachments());
                        if (isPaymentRequest) {
                            entity.setDocumentId(paymentRequestId);
                            //entity.setStatus(IncomingInvoiceStatus.PAID);
                        }
                        else
                        {
                            entity.setReimbursementId(supplierId);
                        }
                        entity.setSupplierId(supplierId);
                        return Mono.just(entity);
                    })
                    .collectList()
                    .flatMap(listInvoice -> {
                        return incomingInvoiceRepository.saveAll(listInvoice)
                            .collectList()
                            .flatMap(incomingInvoices -> {
                                return documentCodeSequenceService.updateSequence(documentCodeSequence)
                                    .then(Mono.just(incomingInvoices));
                            });
                    });
            })
            .flatMapMany(Flux::fromIterable)
            .map(incomingInvoiceMapper::toDto);
    }

    /**
     * Partially update a incomingInvoice.
     *
     * @param incomingInvoiceDTO the entity to update partially.
     * @return the persisted entity.
     */

    public Mono<IncomingInvoiceDTO> partialUpdate(IncomingInvoiceDTO incomingInvoiceDTO) {
        LOG.debug("Request to partially update IncomingInvoice : {}", incomingInvoiceDTO);
        incomingInvoiceDTO.setCreatedAt(null);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return incomingInvoiceRepository
                .findById(incomingInvoiceDTO.getId())
                .flatMap(existingIncomingInvoice -> {
                    if (incomingInvoiceDTO.getInvoiceSupplies() == null || incomingInvoiceDTO.getInvoiceSupplies().isEmpty()) {
                        return Mono.error(new BadRequestAlertException("invoice supplies is required", "incomingInvoice", "invoiceSuppliesRequired"));
                    }
                    if (incomingInvoiceDTO.getIsInvoice()){
                        incomingInvoiceDTO.setStatus(IncomingInvoiceStatus.PAID);
                    }
                    else
                    {
                        incomingInvoiceDTO.setStatus(IncomingInvoiceStatus.NEW);
                    }
                    incomingInvoiceDTO.setInvoiceNo(existingIncomingInvoice.getInvoiceNo());
                    incomingInvoiceMapper.partialUpdate(existingIncomingInvoice, incomingInvoiceDTO);
                    existingIncomingInvoice.setIsPersisted();
                    return Mono.just(existingIncomingInvoice);
                })
                .flatMap(incomingInvoiceRepository::save)
                .map(incomingInvoiceMapper::toDto).flatMap(ic -> {

                    List<InvoiceSuppliesDTO> invoiceSuppliesDTO =new ArrayList<>(incomingInvoiceDTO.getInvoiceSupplies());
                    List<InvoiceSupplies> invoiceSuppliesNew = invoiceSuppliesMapper.toEntity(invoiceSuppliesDTO);
                    invoiceSuppliesNew.forEach(invoiceSupply -> {
                        invoiceSupply.setInvoiceId(incomingInvoiceDTO.getId());
                    });

                    return invoiceSuppliesRepository.deleteByInvoiceId(incomingInvoiceDTO.getId(), login.getCompanyId(), login.getUserId())
                        .then(
                            invoiceSuppliesRepository.saveAll(invoiceSuppliesNew)
                                .collectList()
                                .flatMap(invoiceSupplies -> {
                                    ic.setInvoiceSupplies(invoiceSuppliesMapper.toDto(new ArrayList<>(invoiceSupplies)));

                                    if(ic.getInvoiceType().equals(IncomingInvoiceType.IMPORT_INVOICE))
                                    {

                                        List<RelatedCostsDTO> relatedCostsDTO = incomingInvoiceDTO.getRelatedCosts() != null ? new ArrayList<>(incomingInvoiceDTO.getRelatedCosts()): new ArrayList<>();
                                        var entityRelatedCosts = relatedCostsMapper.toEntity(relatedCostsDTO);
                                        entityRelatedCosts.forEach(relatedCost -> {
                                            relatedCost.setInvoiceId(ic.getId());
                                        });
                                        return relatedCostsRepository.removeAllByInvoiceIdAndCompanyAndIsDeletedIsFalse(ic.getId(), login.getCompanyId(), login.getUserId().toString())
                                            .then(relatedCostsRepository.saveAll(entityRelatedCosts)
                                                .map(relatedCostsMapper::toDto)
                                                .collectList()
                                                .flatMap(relatedCosts -> {
                                                    ic.setRelatedCosts(new ArrayList<>(relatedCosts));

                                                    return handleUpdateInventories(incomingInvoiceDTO, ic);
                                                }));
                                    }

                                    return handleUpdateInventories(incomingInvoiceDTO, ic);
                                }));
                });
        });
    }

    private @NotNull Mono<IncomingInvoiceDTO> handleUpdateInventories(IncomingInvoiceDTO incomingInvoiceDTO, IncomingInvoiceDTO ic) {
        return inventoriesRepository.findByCriteria(getInventoriesCriteriaByInvoiceIdsIn(List.of(ic.getId())), null)
            .collectList()
            .flatMap(inventoriesInDb -> {
                var inventoriesIds = incomingInvoiceDTO.getInventoryIds();
                var idsInDb = inventoriesInDb.stream().map(Inventories::getId).collect(Collectors.toSet());
                var idsInDto = new HashSet<>(inventoriesIds);

                var idsOnlyInDb = inventoriesIds.stream()
                    .filter(idsInDb::contains)
                    .toList();

                var idsOnlyInDto = inventoriesIds.stream()
                    .filter(id -> !idsInDb.contains(id) && idsInDto.contains(id))
                    .toList();

                if (idsOnlyInDb.isEmpty()) {
                    idsOnlyInDb = new ArrayList<>(List.of(UUID.fromString("00000000-0000-0000-0000-000000000000")));
                }

                // remove inventories only in db
                return inventoriesRepository.clearInvoiceIdByInvoiceIds(idsOnlyInDb)
                    .then(Mono.defer(() -> {
                        if (idsOnlyInDto.isEmpty()) {
                            return Mono.just(ic);
                        }
                        return inventoriesRepository.findByCriteria(getInventoriesCriteriaByIdsIn(idsOnlyInDto), null)
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>())
                            .flatMap(inventories -> {
                                if (inventories.isEmpty()) {
                                    return Mono.just(ic);
                                }

                                inventories.forEach(inventoriesDTO -> {
                                    inventoriesDTO.setInvoiceId(ic.getId());
                                    inventoriesDTO.setIsPersisted();
                                });
                                return inventoriesRepository.saveAll(inventories)
                                    .collectList()
                                    .flatMap(inventories1 -> {
                                        ic.setInventories(new ArrayList<>(inventoriesMapper.toDto(new ArrayList<>(inventories1))));
                                        return Mono.just(ic);
                                    });

                            });
                    }));
            });
    }

    /**
     * Get all the incomingInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IncomingInvoiceDTO> findAll(IncomingInvoiceQuery query, Pageable pageable) {
    LOG.debug("Request to get all IncomingInvoices");

        // Kiểm tra và xử lý criteria.getCode()
        Mono<IncomingInvoiceQuery> updatedCriteriaMono;
        if (query.getSearch() != null && !query.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(query.getSearch())
                .collectList()
                .flatMap(employeeIds -> {
                    if (employeeIds.isEmpty()) {
                        LOG.warn("No employees found for the given code, skipping filter.");
                        return Mono.just(query);
                    }

                    query.setEmployeeIds(new ArrayList<>(employeeIds));
                    return Mono.just(query);
                });
        } else {
            updatedCriteriaMono = Mono.just(query);
        }

    return updatedCriteriaMono.flatMapMany(updateQuery -> {
        return resolveQuery(updateQuery)
            .flatMapMany(query1 -> incomingInvoiceRepository.findAllBy(query1, pageable))
            .collectList()
            .map(incomingInvoiceMapper::toDto)
            .flatMapMany(ic -> SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
                var criteria = getInvoiceSuppliesCriteria(ic);
                HashSet<UUID> listEmployeeIds = new HashSet<>();
                ic.forEach(incomingInvoiceDTO -> {
                    listEmployeeIds.add(incomingInvoiceDTO.getEmployeeId());
                    try {
                        UUID createdBy = UUID.fromString(incomingInvoiceDTO.getCreatedBy());
                        listEmployeeIds.add(createdBy);
                    }
                    catch (IllegalArgumentException ignored) {
                    }
                });
                return employeeClient.getEmployeesByListIds(new ArrayList<>(listEmployeeIds))
                    .collectList()
                    .flatMapMany(employees -> {
                        HashMap<UUID, EmployeeDTO> employeeMap = new HashMap<>();
                        employeeMap = employees.stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll);
                        HashMap<UUID, EmployeeDTO> finalEmployeeMap = employeeMap;
                        ic.forEach(incomingInvoiceDTO -> {
                            EmployeeDTO employeeDTO = finalEmployeeMap.get(incomingInvoiceDTO.getEmployeeId());
                            if (employeeDTO != null) {
                                incomingInvoiceDTO.setEmployee(employeeDTO);
                            }
                            try {
                                UUID createdBy = UUID.fromString(incomingInvoiceDTO.getCreatedBy());
                                EmployeeDTO createdByEmployee = finalEmployeeMap.get(createdBy);
                                if (createdByEmployee != null) {
                                    incomingInvoiceDTO.setCreatedByEmployee(createdByEmployee);
                                }
                            }
                            catch (IllegalArgumentException ignored) {

                            }
                        });
                        return getIncomingInvoiceDTOFlux(ic, criteria)
                            .collectList()
                            .flatMapMany(icDTOs -> {
                                var invoiceIds = icDTOs.stream().map(IncomingInvoiceDTO::getId).toList();
                                var inventoriesCriteria = getInventoriesCriteriaByInvoiceIdsIn(invoiceIds);
                                return inventoriesRepository.findByCriteria(inventoriesCriteria, null)
                                    .collectList()
                                    .defaultIfEmpty(new ArrayList<>())
                                    .flatMapMany(inventories -> {
                                        icDTOs.forEach(incomingInvoiceDTO -> {
                                            var inventoriesDTO = inventories.stream()
                                                .filter(inventories1 -> inventories1.getInvoiceId() != null && inventories1.getInvoiceId().equals(incomingInvoiceDTO.getId()))
                                                .toList();
                                            if (!inventoriesDTO.isEmpty()) {
                                                incomingInvoiceDTO.setInventories(new ArrayList<>(inventoriesMapper.toDto(new ArrayList<>(inventoriesDTO))));
                                            }
                                        });
                                        return Flux.fromIterable(icDTOs);
                                    });
                            });
                    });
            }));
    });
}

    private static InvoiceSuppliesCriteria getInvoiceSuppliesCriteria(List<IncomingInvoiceDTO> ic) {
        var criteria = new InvoiceSuppliesCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        var listICIds = ic.stream().map(IncomingInvoiceDTO::getId).toList();
        uuidFilter.setIn(listICIds);
        criteria.setInvoiceId(uuidFilter);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        criteria.setIsDeleted(isDeleted);
        return criteria;
    }

    private @NotNull Flux<IncomingInvoiceDTO> getIncomingInvoiceDTOFlux(List<IncomingInvoiceDTO> ic, InvoiceSuppliesCriteria criteria) {
        return invoiceSuppliesRepository.findByCriteria(criteria, null)
            .collectList()
            .defaultIfEmpty(new ArrayList<>())
            .flatMapMany(invoiceSupplies -> {
                var mapInvoiceSupplies = invoiceSupplies.stream().collect(Collectors.groupingBy(InvoiceSupplies::getInvoiceId));
                ic.forEach(incomingInvoiceDTO -> {
                    var invoiceSuppliesDTO = mapInvoiceSupplies.get(incomingInvoiceDTO.getId());
                    if (invoiceSuppliesDTO != null) {
                        incomingInvoiceDTO.setInvoiceSupplies(invoiceSuppliesMapper.toDto(new ArrayList<>(invoiceSuppliesDTO)));
                    }
                });
                return relatedCostsRepository.findByCriteria(getRelatedCostsCriteria(ic), null)
                    .collectList()
                    .flatMapMany(relatedCosts -> {
                        ic.forEach(incomingInvoiceDTO -> {
                            relatedCosts.forEach(relatedCost -> {
                                if (incomingInvoiceDTO.getId().equals(relatedCost.getInvoiceId())) {
                                    if (incomingInvoiceDTO.getRelatedCosts() == null) {
                                        incomingInvoiceDTO.setRelatedCosts(new ArrayList<>());
                                    }
                                    incomingInvoiceDTO.getRelatedCosts().add(relatedCostsMapper.toDto(relatedCost));
                                }
                            });
                        });
                        return Flux.fromIterable(ic);
                    });
            });
    }

    private static RelatedCostsCriteria getRelatedCostsCriteria(List<IncomingInvoiceDTO> ic) {
        List<UUID> ids = ic.stream().map(IncomingInvoiceDTO::getId).toList();
        var relatedCostsCriteria = new RelatedCostsCriteria();
        UUIDFilter invoiceIdFilter = new UUIDFilter();
        invoiceIdFilter.setIn(ids);
        relatedCostsCriteria.setInvoiceId(invoiceIdFilter);
        BooleanFilter isDeletedFilter = new BooleanFilter();
        isDeletedFilter.setEquals(false);
        relatedCostsCriteria.setIsDeleted(isDeletedFilter);
        return relatedCostsCriteria;
    }

    /**
     * Returns the number of incomingInvoices available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll(IncomingInvoiceQuery query) {
        LOG.debug("Request to count all IncomingInvoices");
        Mono<IncomingInvoiceQuery> updatedCriteriaMono;
        if (query.getSearch() != null && !query.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(query.getSearch())
                .collectList()
                .flatMap(employeeIds -> {
                    if (employeeIds.isEmpty()) {
                        LOG.warn("No employees found for the given code, skipping filter.");
                        return Mono.just(query);
                    }

                    query.setEmployeeIds(new ArrayList<>(employeeIds));
                    return Mono.just(query);
                });
        } else {
            updatedCriteriaMono = Mono.just(query);
        }
        return updatedCriteriaMono.flatMap(query1 -> resolveQuery(query1)
            .flatMap(incomingInvoiceRepository::countAllBy)
        );
    }

    /**
     * Get one incomingInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IncomingInvoiceDTO> findOne(UUID id) {
        LOG.debug("Request to get IncomingInvoice : {}", id);
        return incomingInvoiceRepository.findById(id)
            .map(incomingInvoiceMapper::toDto)
            .flatMap((ic -> {
                var criteria = new InvoiceSuppliesCriteria();
                UUIDFilter uuidFilter = new UUIDFilter();
                uuidFilter.setEquals(id);
                criteria.setInvoiceId(uuidFilter);
                BooleanFilter isDeletedFilter = new BooleanFilter();
                isDeletedFilter.setEquals(false);
                criteria.setIsDeleted(isDeletedFilter);
                return invoiceSuppliesRepository.findByCriteria(criteria, null)
                    .collectList()
                    .flatMap(invoiceSupplies -> {
                        ic.setInvoiceSupplies(invoiceSuppliesMapper.toDto(new ArrayList<>(invoiceSupplies)));
                        var employeeIds = new HashSet<UUID>();
                        employeeIds.add(ic.getEmployeeId());
                        try {
                            UUID createdBy = UUID.fromString(ic.getCreatedBy());
                            employeeIds.add(createdBy);
                        } catch (IllegalArgumentException ignored) {
                        }
                        if (ic.getInvoiceType().equals(IncomingInvoiceType.IMPORT_INVOICE)) {
                            var relatedCostsCriteria = getRelatedCostsCriteria(List.of(ic));
                            return relatedCostsRepository.findByCriteria(relatedCostsCriteria, null)
                                .collectList()
                                .flatMap(relatedCosts -> {
                                    ic.setRelatedCosts(relatedCostsMapper.toDto(new ArrayList<>(relatedCosts)));
                                    return getEmployeeClient(ic, employeeIds);
                                });
                        }
                        return getEmployeeClient(ic, employeeIds);
                    });
            }));
    }

    private @NotNull Mono<IncomingInvoiceDTO> getEmployeeClient(IncomingInvoiceDTO ic, HashSet<UUID> employeeIds) {
        return employeeClient.getEmployeesByListIds(new ArrayList<>(employeeIds))
            .collectList()
            .flatMap(employees -> {
                HashMap<UUID, EmployeeDTO> employeeMap = new HashMap<>();
                employeeMap = employees.stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll);
                EmployeeDTO employeeDTO = employeeMap.get(ic.getEmployeeId());
                if (employeeDTO != null) {
                    ic.setEmployee(employeeDTO);
                }
                try {
                    UUID createdBy = UUID.fromString(ic.getCreatedBy());
                    EmployeeDTO createdByEmployee = employeeMap.get(createdBy);
                    if (createdByEmployee != null) {
                        ic.setCreatedByEmployee(createdByEmployee);
                    }
                } catch (IllegalArgumentException ignored) {
                }
                return getInventoriesByOneInvoiceId(ic);
            });
    }

    private @NotNull Mono<IncomingInvoiceDTO> getInventoriesByOneInvoiceId(IncomingInvoiceDTO ic) {
        return inventoriesRepository
            .findByCriteria(getInventoriesCriteriaByInvoiceIdsIn(List.of(ic.getId())), null)
            .collectList()
            .flatMap(inventories -> {
                if (inventories.isEmpty()) {
                    return Mono.just(ic);
                }
                ic.setInventories(new ArrayList<>(inventoriesMapper.toDto(new ArrayList<>(inventories))));
                return Mono.just(ic);
            });
    }

    /**
     * Delete the incomingInvoice by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete IncomingInvoice : {}", id);
        return SecurityUtils.getUserJWTDetail()
            .flatMap(login -> incomingInvoiceRepository.findById(id)
                .flatMap(incomingInvoice -> {
                    if (!(incomingInvoice.getStatus().equals(IncomingInvoiceStatus.NEW) || incomingInvoice.getStatus().equals(IncomingInvoiceStatus.CANCELLED))) {
                        return Mono.error(new BadRequestAlertException("Can not delete", "incomingInvoice", "canNotDelete"));
                    }
                    return incomingInvoice.deleteAsync()
                        .then(incomingInvoiceRepository.save(incomingInvoice.setIsPersisted()));
                })
                .then(invoiceSuppliesRepository.deleteByInvoiceId(id, login.getCompanyId(), login.getUserId()))
            );
    }

    // api gửi xét duyệt hoá đơn đầu vào:
    public Mono<IncomingInvoiceDTO> sendForApproval(UUID id, List<RequestApprovalDTO> requestApprovalDTOList) {
        var requestApprovalEntities = requestApprovalMapper.toEntity(new ArrayList<>(requestApprovalDTOList));
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return incomingInvoiceRepository.findById(id)
                .flatMap(incomingInvoice -> {
                    if (!incomingInvoice.getStatus().equals(IncomingInvoiceStatus.NEW)) {
                        return Mono.error(new BadRequestAlertException("Can not send for approval", "incomingInvoice", "canNotSendForApproval"));
                    }

                    incomingInvoice.setStatus(IncomingInvoiceStatus.WAITING);
                    incomingInvoice.setIsPersisted();
                    return incomingInvoiceRepository.save(incomingInvoice);
                }).map(incomingInvoiceMapper::toDto).flatMap(ic -> {
                    return requestApprovalRepository.saveAll(requestApprovalEntities)
                        .collectList()
                        .flatMap(requestApprovals -> {
                            ic.setRequestApprovals(requestApprovalMapper.toDto(new ArrayList<>(requestApprovals)));
                            return Mono.just(ic);
                        });
                });
        });
    }

    // api duyệt hoá đơn đầu vào:
    public Mono<IncomingInvoiceDTO> approve(UUID invoiceId, RequestApprovalDTO requestApprovalDTO) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return incomingInvoiceRepository.findById(invoiceId).switchIfEmpty(Mono.error(new BadRequestAlertException("Not found", "incomingInvoice", "notFound")))
                .flatMap(incomingInvoice -> {
                    if (!incomingInvoice.getStatus().equals(IncomingInvoiceStatus.WAITING)) {
                        return Mono.error(new BadRequestAlertException("can not approve because not in waiting", "incomingInvoice", "INVALID_STATUS"));
                    }

                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(invoiceId)
                        .collectList()
                        .flatMap(requestApprovals -> {
                            var myRequestApproval = requestApprovals.stream().filter(requestApproval -> requestApproval.getEmployeeId().equals(requestApprovalDTO.getEmployeeId())).findFirst().orElse(null);
                            if (myRequestApproval == null) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not in approver list", "incomingInvoice", "INVALID_APPROVER"));
                            }
                            requestApprovals.remove(myRequestApproval);

                            myRequestApproval.approvedSign(requestApprovalDTO.getApprovedSign());
                            myRequestApproval.approvedSignName(requestApprovalDTO.getApprovedSignName());
                            myRequestApproval.result(true);
                            myRequestApproval.setIsPersisted();
                            var isAllApproved = requestApprovals.stream().allMatch(x -> x.getResult().equals(true));
                            if (isAllApproved) {
                                incomingInvoice.setStatus(IncomingInvoiceStatus.APPROVED);
                                incomingInvoice.setIsPersisted();
                                return requestApprovalRepository.save(myRequestApproval).then(incomingInvoiceRepository.save(incomingInvoice));
                            }
                            return requestApprovalRepository.save(myRequestApproval).then(Mono.just(incomingInvoice));
                        });
                }).map(incomingInvoiceMapper::toDto);
        });
    }

    // api từ chối hoá đơn đầu vào:
    public Mono<IncomingInvoiceDTO> reject(UUID invoiceId, RequestApprovalDTO requestApprovalDTO) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return incomingInvoiceRepository.findById(invoiceId)
                .flatMap(incomingInvoice -> {
                    if (!incomingInvoice.getStatus().equals(IncomingInvoiceStatus.WAITING)) {
                        return Mono.error(new BadRequestAlertException("canNotReject because not in waiting", "incomingInvoice", "INVALID_STATUS"));
                    }

                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(invoiceId).collectList().flatMap(docs -> {
                        var myRequestApproval = docs.stream().filter(requestApproval -> requestApproval.getEmployeeId().equals(requestApprovalDTO.getEmployeeId())).findFirst().orElse(null);
                        if (myRequestApproval == null) {
                            return Mono.error(new BadRequestAlertException("Can not reject because not in approver list", "incomingInvoice", "INVALID_APPROVER"));
                        }

                        myRequestApproval.rejectNote(requestApprovalDTO.getRejectNote());
                        myRequestApproval.result(false);
                        myRequestApproval.setIsPersisted();
                        incomingInvoice.setStatus(IncomingInvoiceStatus.REJECTED);
                        incomingInvoice.setIsPersisted();
                        return requestApprovalRepository.save(myRequestApproval).then(incomingInvoiceRepository.save(incomingInvoice));
                    });
                }).map(incomingInvoiceMapper::toDto);
        });
    }

    // cancel incoming invoice:
    public Mono<IncomingInvoiceDTO> cancel(UUID invoiceId) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return incomingInvoiceRepository.findById(invoiceId)
                .flatMap(incomingInvoice -> {
                    incomingInvoice.setStatus(IncomingInvoiceStatus.CANCELLED);
                    incomingInvoice.setIsPersisted();
                    return incomingInvoiceRepository.save(incomingInvoice);
                }).map(incomingInvoiceMapper::toDto);
        });
    }

    public Mono<String> export(List<IncomingInvoiceDTO> incomingInvoiceDTOS){
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Danh sách hoá đơn đầu vào");

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"STT", "Số hoá đơn", "Ngày PS", "Ngày hoá đơn", "Khách hàng", "Số tiền", "VAT", "Tổng tiền", "Đã thanh toán", "Diễn giải", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            var headerData = Flux.fromIterable(incomingInvoiceDTOS).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            return headerData.flatMap(data -> {
                data.forEach(invoiceDTO -> {
                    var createdByName = "SYSTEM";
                    var createdByCode = "SYSTEM";
                    if(invoiceDTO.getEmployee() != null) {
                        createdByName = invoiceDTO.getEmployee().getFullName() == null ? "" : invoiceDTO.getEmployee().getFullName();
                        createdByCode = invoiceDTO.getEmployee().getEmployeeCode() == null ? "" : invoiceDTO.getEmployee().getEmployeeCode();
                    }
                    var employee = createdByCode + " - " + createdByName;
                    Row row = orderSheet.createRow(rowNum.get());
                    row.createCell(0).setCellValue(rowNum.getAndIncrement());
                    row.createCell(1).setCellValue(invoiceDTO.getInvoiceNo());
                    // ngay phat sinh
                    var createdAt = invoiceDTO.getCreatedAt().withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).format(formatter);
                    row.createCell(2).setCellValue(createdAt);
                    // ngay hoa don
                    var invoiceDate = invoiceDTO.getInvoiceDate() == null ? "" : invoiceDTO.getInvoiceDate().withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).format(formatter);
                    row.createCell(3).setCellValue(invoiceDate);
                    // khach hang
                    var supplier = invoiceDTO.getSuppliers() == null ? "" : invoiceDTO.getSuppliers().getName();
                    row.createCell(4).setCellValue(supplier);
                    // so tien
                    var totalAmount = invoiceDTO.getTotalAmount() == null ? "0" : (invoiceDTO.getTotalAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(invoiceDTO.getTotalAmount().intValue()) : String.valueOf(invoiceDTO.getTotalAmount()));
                    row.createCell(5).setCellValue(totalAmount);
                    // vat
                    var totalVat = invoiceDTO.getTotalVat() == null ? "0" : (BigDecimal.valueOf(invoiceDTO.getTotalVat()).stripTrailingZeros().scale() <= 0 ? String.valueOf(new BigDecimal(invoiceDTO.getTotalVat()).intValue()) : String.valueOf(invoiceDTO.getTotalVat()));
                    row.createCell(6).setCellValue(totalVat);
                    // tong tien
                    var grandTotal = invoiceDTO.getGrandTotal() == null ? "0" : (invoiceDTO.getGrandTotal().stripTrailingZeros().scale() <= 0 ? String.valueOf(invoiceDTO.getGrandTotal().intValue()) : String.valueOf(invoiceDTO.getGrandTotal()));
                    row.createCell(7).setCellValue(grandTotal);

                    // Đã thanh toán
                    var paidAmount = getPaidAmount(invoiceDTO);
                    row.createCell(8).setCellValue(paidAmount);

                    // Diễn giải
                    var description = invoiceDTO.getContent() == null ? "" : invoiceDTO.getContent();
                    row.createCell(9).setCellValue(description);
                    // Trạng thái
                    var status = invoiceDTO.getStatus() == null ? "" : invoiceDTO.getStatus().toVietnameseName();
                    row.createCell(10).setCellValue(status);


                });
                for(int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {

                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private static String getPaidAmount(IncomingInvoiceDTO invoiceDTO) {
        var paymentRequest = invoiceDTO.getPaymentRequest();
        var paidAmount = "0";
        if (paymentRequest != null) {
            paidAmount = paymentRequest.getTotalAmount() == null ? "0" :
                (paymentRequest.getTotalAmount().stripTrailingZeros().scale() <= 0 ?
                    String.valueOf(paymentRequest.getTotalAmount().intValue()) :
                    String.valueOf(paymentRequest.getTotalAmount()));
        }
        return paidAmount;
    }
}
