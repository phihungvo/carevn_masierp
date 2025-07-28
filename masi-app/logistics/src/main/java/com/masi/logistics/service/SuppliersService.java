package com.masi.logistics.service;

import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.SupplierGroupDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.service.mapper.*;

import java.util.ArrayList;
import java.util.UUID;

import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Suppliers}.
 */
@Service
@Transactional
public class SuppliersService {

    private static final Logger log = LoggerFactory.getLogger(SuppliersService.class);

    private final SuppliersRepository suppliersRepository;
    private final SupplierDetailRepository supplierDetailRepository;
    private final SupplierGroupRepository supplierGroupRepository;

    private final SuppliersMapper suppliersMapper;
    private final SupplierDetailMapper supplierDetailMapper;
    private final SupplierGroupMapper supplierGroupMapper;
    private final ContactMapper contactMapper;
    private final ContactGiftMapper contactGiftsMapper;
    private final SupplierContractMapper supplierContractMapper;
    private final ContactRepository contactRepository;
    private final ContactGiftRepository contactGiftRepository;
    private final SupplierContractRepository supplierContractRepository;

    private ContactService contactService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Lazy
    public SuppliersService(SuppliersRepository suppliersRepository, SupplierDetailRepository supplierDetailRepository, SupplierGroupRepository supplierGroupRepository, SuppliersMapper suppliersMapper, SupplierDetailMapper supplierDetailMapper, SupplierGroupMapper supplierGroupMapper, ContactMapper contactMapper, ContactGiftMapper contactGiftsMapper, SupplierContractMapper supplierContractMapper, ContactRepository contactRepository, ContactGiftRepository contactGiftRepository, SupplierContractRepository supplierContractRepository) {
        this.suppliersRepository = suppliersRepository;
        this.supplierDetailRepository = supplierDetailRepository;
        this.supplierGroupRepository = supplierGroupRepository;
        this.suppliersMapper = suppliersMapper;
        this.supplierDetailMapper = supplierDetailMapper;
        this.supplierGroupMapper = supplierGroupMapper;
        this.contactMapper = contactMapper;
        this.contactGiftsMapper = contactGiftsMapper;
        this.supplierContractMapper = supplierContractMapper;
        this.contactRepository = contactRepository;
        this.contactGiftRepository = contactGiftRepository;
        this.supplierContractRepository = supplierContractRepository;
    }

    /**
     * Save a suppliers.
     *
     * @param suppliersDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliersDTO> save(SuppliersDTO suppliersDTO) {
        log.debug("Request to save Suppliers : {}", suppliersDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return suppliersRepository.countByCode(suppliersDTO.getCode(), userJWTDetail.getCompanyId()).flatMap(count ->{
               if (count > 0) {
                   return Mono.error(new BadRequestAlertException("Code already exists", "Suppliers", "codeexists"));
               }
               else {
                   return suppliersRepository.save(suppliersMapper.toEntity(suppliersDTO)).map(suppliersMapper::toDto).flatMap(s -> {
                       var listContact = suppliersDTO.getContacts();
                       listContact.forEach(c -> c.setSupplierId(s.getId()));
                       var listContactEntity = contactMapper.toEntity(new ArrayList<>(listContact));
                       return contactRepository.saveAll(listContactEntity).collectList().map(contactMapper::toDto).flatMap(c -> {
                           s.setContacts(new ArrayList<>(c));
                           return Mono.just(s);
                       });
                   });
               }
            });
        });
    }

    /**
     * Update a suppliers.
     *
     * @param suppliersDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliersDTO> update(SuppliersDTO suppliersDTO) {
        log.debug("Request to update Suppliers : {}", suppliersDTO);
        return suppliersRepository.save(suppliersMapper.toEntity(suppliersDTO).setIsPersisted()).map(suppliersMapper::toDto);
    }

    /**
     * Partially update a suppliers.
     *
     * @param suppliersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SuppliersDTO> partialUpdate(SuppliersDTO suppliersDTO) {
        log.debug("Request to partially update Suppliers : {}", suppliersDTO);
        return SecurityUtils.getUserJWTDetail()
            .flatMap(login -> suppliersRepository.findById(suppliersDTO.getId())
                .flatMap(existingSuppliers -> {
                    suppliersMapper.partialUpdate(existingSuppliers, suppliersDTO);
                    existingSuppliers.setIsPersisted();
                    return contactRepository.deleteBySupplierId(existingSuppliers.getId(), login.getUserId(), login.getCompanyId())
                        .then(suppliersRepository.save(existingSuppliers))
                        .map(suppliersMapper::toDto)
                        .flatMap(s -> {
                            var listContact = suppliersDTO.getContacts();
                            listContact.forEach(c -> c.setSupplierId(s.getId()));
                            var listContactEntity = contactMapper.toEntity(new ArrayList<>(listContact));
                            return contactRepository.saveAll(listContactEntity)
                                .map(contactMapper::toDto)
                                .collectList()
                                .flatMap(contactList -> {
                                    return Flux.fromIterable(contactList)
                                        .flatMap(contact -> contactGiftRepository.deletedByContactId(contact.getId(), login.getUserId(), login.getCompanyId()))
                                        .then(Mono.defer(() -> {
                                            var listContactGift = suppliersDTO.listContactGiftsInContact().stream().toList();
                                            listContactGift.forEach(c -> c.setContactId(c.getContactId()));
                                            return contactGiftRepository.saveAll(contactGiftsMapper.toEntity(new ArrayList<>(suppliersDTO.listContactGiftsInContact())))
                                                .collectList()
                                                .map(contactGiftsMapper::toDto)
                                                .flatMap(contactGifts -> {
                                                    s.setContacts(new ArrayList<>(contactList));
                                                    contactList.forEach(cl -> {
                                                        var gift = contactGifts.stream().filter(g -> g.getContactId().equals(cl.getId())).toList();
                                                        if (!gift.isEmpty()) {
                                                            cl.setContactGifts(new ArrayList<>(gift));
                                                        }
                                                    });
                                                    return Mono.just(s);
                                                });
                                        }));
                                });
                        });
                }));


    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SuppliersDTO> findAll(Pageable pageable, String search, Boolean status) {
        log.debug("Request to get all Suppliers");
        return SecurityUtils.getUserJWTDetail().flatMapMany(userJWTDetail -> {
            return suppliersRepository.findAllBy(pageable, search, status, userJWTDetail.getCompanyId()).map(suppliersMapper::toDto).switchIfEmpty(Mono.empty()).flatMap(s -> {
                return supplierGroupRepository.findById(s.getSupplierGroupId(), s.getCompany()).map(supplierGroupMapper::toDto).flatMap(g -> {
                    s.setSupplierGroup(g);
                    return supplierDetailRepository.findAllBySupplierIdAndCompany(s.getId(), s.getCompany()).collectList().map(supplierDetailMapper::toDto).flatMap(d -> {
                        s.setSuppliesDetails(new ArrayList<>(d));
                        return Mono.just(s);
                    }).then(Mono.just(s));
                }).then(Mono.just(s));
            });
        });
    }

    /**
     * Returns the number of suppliers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return suppliersRepository.count();
    }

    public Mono<Long> countAll(String search, Boolean status) {
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return suppliersRepository.countByCriteria(search, status, userJWTDetail.getCompanyId());
        });
    }

    /**
     * Get one suppliers by id.
     *
     * @param id            the id of the entity.
     * @param companyImport
     * @return the entity.
     */
    public Mono<SuppliersDTO> getSuplierWrap(String companyImport) {
        return suppliersRepository.findByCreatedBy("SYSTEM",companyImport)
                .map(suppliersMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<SuppliersDTO> findOne(UUID id, String company) {
        log.debug("Request to get Suppliers : {}", id);
        return suppliersRepository.findById(id)
            .switchIfEmpty(Mono.empty())
            .map(suppliersMapper::toDto)
            .flatMap(s -> {
                return supplierGroupRepository.findById(s.getSupplierGroupId(), company)
                    .map(supplierGroupMapper::toDto)
                    .defaultIfEmpty(new SupplierGroupDTO())
                    .flatMap(g -> {
                        s.setSupplierGroup(g);
                        return contactService.getContactBySupplierId(s.getId(), s.getCompany())
                            .collectList()
                            .flatMap(c -> {
                                s.setContacts(new ArrayList<>(c));
                                SupplierContractCriteria criteria = new SupplierContractCriteria();
                                UUIDFilter uuidFilter = new UUIDFilter();
                                uuidFilter.setEquals(s.getId());
                                criteria.setSupplierId(uuidFilter);
                                return supplierContractRepository.findByCriteria(criteria, null)
                                    .map(supplierContractMapper::toDto)
                                    .collectList()
                                    .flatMap(contracts -> {
                                        s.setSupplierContracts(new ArrayList<>(contracts));
                                        return Mono.just(s);
                                    });
                            });
                    });
            });
    }

    /**
     * Delete the suppliers by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Suppliers : {}", id);
        return suppliersRepository.deleteById(id);
    }

    public Mono<Void> delete(UUID id, String company, String deletedBy) {
        log.debug("Request to delete Suppliers {}, {}, {}", id, company, deletedBy);
        return suppliersRepository.deleteById(id, deletedBy, company).then(supplierDetailRepository.deleteBySupplierIdAndCompany(id, company, deletedBy)).then(contactService.removeAllBySupplierId(id, UUID.fromString(deletedBy), company));
    }

    public Mono<Void> active(UUID id, Boolean isActive) {
        log.debug("Request to active Suppliers {}, {}", id, isActive);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliersRepository.updateStatus(id, isActive ,login.getUserId(), login.getCompanyId());
        });
    }

    public Mono<byte[]> exportRecordsAsCSV(String search, Boolean status) {
        log.debug("Request to export supplierExportDTO as CSV");

        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return this.findAll(null, search, status)
                .map(SuppliersDTO::toExportDTO)
                .collectList()
                .flatMap(CSVUtils::convertListToExcel);
        });
    }


}
