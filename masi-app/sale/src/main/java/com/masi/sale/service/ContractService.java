package com.masi.sale.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.sale.domain.*;
import com.masi.sale.domain.ContractFile;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.domain.enumeration.ContractType;
import com.masi.sale.repository.*;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.dto.reponse.ContractFileReponse;
import com.masi.sale.service.dto.reponse.ContractTotalReponse;
import com.masi.sale.service.dto.request.ApprovedContractRequest;
import com.masi.sale.service.dto.request.ConsentToReview;
import com.masi.sale.service.dto.request.RefusalOfReview;
import com.masi.sale.service.mapper.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.service.web.client.FileClient;
import com.masi.sale.service.web.client.LogisticClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.Contract}.
 */
@Service
@Transactional
public class ContractService {


    //Default
    private final List<ContractStatus> nonDeletableStatuses = Arrays.asList(ContractStatus.APPROVED, ContractStatus.LIQUIDATED, ContractStatus.CANCELLED);
    private final List<ContractStatus> ApprovedStatuses = Arrays.asList(ContractStatus.WAITING_APPROVAL, ContractStatus.APPROVED, ContractStatus.CANCELLED);
    private static final Logger log = LoggerFactory.getLogger(ContractService.class);
    private final FileClient fileClient;
    //

    private final ContractRepository contractRepository;

    private final RequestApprovalService requestApprovalService;

    private final CustomerRepository customerRepository;

    private final ContractFileService contractFileService;

    private final ContractFileRepository contractFileRepository;

    private final ContractMaterialMapper contractMaterialMapper;

    private final ContractProductMapper contractProductMapper;

    private final ContractMapper contractMapper;

    private final MaterialRepository materialRepository;

    private final ContractMaterialRepository contractMaterialRepository;

    private final EmployeeClient employeeClient;

    private final TemplateContractRepository templateContractRepository;

    private final MaterialMapper materialMapper;

    private final ContractProductRepository contractProductRepository;

    private final ProductRepository productRepository;

    private final LogisticClient logisticClient;

    public ContractService(FileClient fileClient, ContractRepository contractRepository, RequestApprovalService requestApprovalService, CustomerRepository customerRepository, ContractMapper contractMapper, ContractFileRepository contractFileRepository, ContractFileService contractFileService, ContractMaterialMapper contractMaterialMapper, ContractProductMapper contractProductMapper, MaterialRepository materialRepository, ContractMaterialRepository contractMaterialRepository, EmployeeClient employeeClient, TemplateContractRepository templateContractRepository, MaterialMapper materialMapper, ContractProductRepository contractProductRepository, ProductRepository productRepository, LogisticClient logisticClient) {
        this.contractRepository = contractRepository;
        this.requestApprovalService = requestApprovalService;
        this.customerRepository = customerRepository;
        this.contractMapper = contractMapper;
        this.contractFileRepository = contractFileRepository;
        this.contractFileService = contractFileService;
        this.contractMaterialMapper = contractMaterialMapper;
        this.contractProductMapper = contractProductMapper;
        this.materialRepository = materialRepository;
        this.contractMaterialRepository = contractMaterialRepository;
        this.employeeClient = employeeClient;
        this.templateContractRepository = templateContractRepository;
        this.fileClient = fileClient;
        this.materialMapper = materialMapper;
        this.contractProductRepository = contractProductRepository;
        this.productRepository = productRepository;
        this.logisticClient = logisticClient;
    }

    public Mono<List<ContractMaterialDTO>> findAllContractMaterialByIds(List<UUID> ids) {
        return contractMaterialRepository.findAllByIds(ids).map(ContractMaterial::toDto).collectList();
    }

    public Mono<Void> backUpAndSetNewStatus(UUID id, ContractStatus status) {
        return contractRepository.findById(id).flatMap(contract -> {
            contract.setOldStatus(contract.getStatus());
            contract.status(status);
            contract.setLastUpdated(ZonedDateTime.now());
            return contractRepository.save(contract.setIsPersisted()).then();
        });
    }

    public Mono<Void> restoreOldStatus(UUID id) {
        return contractRepository.findById(id).flatMap(contract -> {
            contract.setStatus(contract.getOldStatus());
            contract.setLastUpdated(ZonedDateTime.now());
            return contractRepository.save(contract.setIsPersisted()).then();
        });
    }

    public Mono<Void> setContractStatus(UUID id, ContractStatus status) {
        return contractRepository.findById(id).flatMap(contract -> {
//            if (nonDeletableStatuses.contains(contract.getStatus())) {
//                return Mono.error(new BadRequestAlertException("Cannot update contract with status: " + contract.getStatus(), "", ""));
//            }
            return contractRepository.changeStatusByIdAndStatus(id, status, ZonedDateTime.now()).then();
        });
    }

    @Transactional
    public Mono<ContractDTO> save(ContractDTO contractDTO) throws IOException {
        log.debug("Request to save Contract : {}", contractDTO);

        contractDTO.setCreatedDate(ZonedDateTime.now());
        contractDTO.setLastUpdated(ZonedDateTime.now());
        contractDTO.setIsDeleted(false);
        contractDTO.setIsActive(true);
        if (ContractStatus.DELETED.equals(contractDTO.getStatus())) {
            contractDTO.setIsActive(false);
        }

        return contractRepository.save(contractMapper.toEntity(contractDTO)).flatMap(savedContract -> {

            // Phần xử lý ContractProduct đã được tắt
        /*
        Flux<ContractProductDTO> contractProductFlux = Flux.fromIterable(contractDTO.getContractProductDTOS()).flatMap(contractProductDTO -> {
            contractProductDTO.setId(UUID.randomUUID());
            contractProductDTO.setIdContract(savedContract.getId());

            return contractProductRepository.save(contractProductMapper.toEntity(contractProductDTO));
        }).map(ContractProduct::toDto);
        */

            // Kiểm tra và lưu ContractMaterialDTO
            Flux<ContractMaterialDTO> contractMaterialsFlux = Flux.fromIterable(contractDTO.getContractMaterialDTOS()).flatMap(contractMaterialDTO -> {
                contractMaterialDTO.setId(UUID.randomUUID());
                contractMaterialDTO.setIdContract(savedContract.getId());

                if (contractMaterialDTO.getNameMaterialNew() != null && !contractMaterialDTO.getNameMaterialNew().isEmpty()) {
                    MaterialDTO materialDTO = new MaterialDTO();
                    materialDTO.setId(UUID.randomUUID());
                    materialDTO.setName(contractMaterialDTO.getNameMaterialNew());
                    materialDTO.setNameEn(contractMaterialDTO.getNameMaterialNewEn());

                    return materialRepository.save(materialMapper.toEntity(materialDTO)).flatMap(material -> {
                        contractMaterialDTO.setIdMaterial(material.getId());
                        return contractMaterialRepository.save(contractMaterialMapper.toEntity(contractMaterialDTO));
                    });
                }

                return contractMaterialRepository.save(contractMaterialMapper.toEntity(contractMaterialDTO));
            }).map(ContractMaterial::toDto);

            // Bỏ qua phần xử lý ContractProduct và chỉ xử lý ContractMaterial
            return contractMaterialsFlux.collectList().flatMap(contractMaterialDTOS -> {
//                if (StringUtils.isBlank(contractDTO.getContractFile().getContractFile()) && StringUtils.isBlank(contractDTO.getContractFile().getAppendixFile())) {
//                    return Mono.just(contractMapper.toDto(savedContract));
//                }

                // Lưu các tệp hợp đồng (contract files) nếu có
                return contractFileService.saveContractFiles(contractDTO).thenReturn(savedContract).map(contractMapper::toDto);
            });
        });
    }

    public Mono<MaterialDTO> saveMaterial(MaterialDTO materialDTO) {
        return materialRepository.save(materialMapper.toEntity(materialDTO)).map(materialMapper::toDto);
    }


    /**
     * Update a contract.
     *
     * @param contractDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContractDTO> update(ContractDTO contractDTO) {
        log.debug("Request to update Contract : {}", contractDTO);
        return contractRepository.save(contractMapper.toEntity(contractDTO).setIsPersisted()).map(contractMapper::toDto);
    }

    /**
     * Partially update a contract.
     *
     * @param contractDTO the entity to update partially.
     * @return the persisted entity.
     */

    @Transactional
    public Mono<ContractDTO> partialUpdate(ContractDTO contractDTO) {
        log.debug("Request to partially update Contract : {}", contractDTO);
        if (ContractStatus.DELETED.equals(contractDTO.getStatus())) {
            contractDTO.setIsActive(false);
        }
        return contractRepository.findByIdAndIsActive(contractDTO.getId(), true).flatMap(existingContract -> {
//            if (nonDeletableStatuses.contains(existingContract.getStatus())) {
//                return Mono.error(new BadRequestAlertException("Cannot update contract with status: " + existingContract.getStatus(), "", ""));
//            }

            // Cập nhật thông tin hợp đồng
            ContractStatus contractStatus = existingContract.getStatus();
            contractDTO.applyUpdate(existingContract);
            existingContract.setLastUpdated(ZonedDateTime.now());
            existingContract.setIsPersisted();

            if (contractDTO.getStatus().equals(ContractStatus.DELETED)) {
                existingContract.setStatus(contractStatus);
                existingContract.setIsActive(false);
            }

            return contractRepository.save(existingContract).flatMap(savedContract -> {
                // Xóa các sản phẩm cũ
                return contractProductRepository.deleteByIdContract(savedContract.getId()).then(Mono.just(savedContract)).flatMap(contract -> {
                    // Lưu các sản phẩm mới
/*                    Flux<ContractProductDTO> contractProductFlux = Flux.fromIterable(contractDTO.getContractProductDTOS()).flatMap(contractProductDTO -> {
                        contractProductDTO.setId(UUID.randomUUID());
                        contractProductDTO.setIdContract(savedContract.getId());
                        return contractProductRepository.save(contractProductMapper.toEntity(contractProductDTO)).thenReturn(contractProductDTO);
                    });*/

                    // Xóa các vật liệu cũ
                    return contractMaterialRepository.deleteByIdContract(savedContract.getId()).then(Mono.just(savedContract)).flatMap(contract1 -> {
                        // Lưu các vật liệu mới
                        Flux<ContractMaterialDTO> contractMaterialsFlux = Flux.fromIterable(contractDTO.getContractMaterialDTOS()).flatMap(contractMaterialDTO -> {
                            contractMaterialDTO.setId(UUID.randomUUID());
                            contractMaterialDTO.setIdContract(savedContract.getId());

                            if (contractMaterialDTO.getNameMaterialNew() != null && !contractMaterialDTO.getNameMaterialNew().isEmpty()) {
                                MaterialDTO materialDTO = new MaterialDTO();
                                materialDTO.setId(UUID.randomUUID());
                                materialDTO.setName(contractMaterialDTO.getNameMaterialNew());

                                return materialRepository.save(materialMapper.toEntity(materialDTO)).flatMap(material -> {
                                    contractMaterialDTO.setIdMaterial(material.getId());
                                    return contractMaterialRepository.save(contractMaterialMapper.toEntity(contractMaterialDTO)).thenReturn(contractMaterialDTO);
                                });
                            }

                            return contractMaterialRepository.save(contractMaterialMapper.toEntity(contractMaterialDTO)).thenReturn(contractMaterialDTO);
                        });

                        // Kết hợp kết quả từ các Flux và lưu các tệp hợp đồng nếu có
                        return contractMaterialsFlux.collectList().flatMap(contractMaterialDTOS -> {
//                            if (StringUtils.isBlank(contractDTO.getContractFile().getContractFile()) && StringUtils.isBlank(contractDTO.getContractFile().getAppendixFile())) {
//                                return Mono.just(contractMapper.toDto(savedContract));
//                            }

//                            if (contractDTO.getContractFile().getContractFileNew() == null ||  contractDTO.getContractFile().getContractFileNew().isEmpty()) {
//                                return Mono.just(contractMapper.toDto(savedContract));
//                            }

                            return contractFileService.saveContractFiles(contractDTO).thenReturn(savedContract).map(contractMapper::toDto);
                        });
                    });
                });
            });
        });
    }


    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAllBy(pageable).map(contractMapper::toDto);
    }

    /**
     * Returns the number of contracts available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return contractRepository.count();
    }

    /**
     * Get one contract by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ContractDTO> findOne(UUID id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findByIdAndIsActive(id, true).map(contractMapper::toDto)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Contract not found", "", "")))
            .zipWith(contractFileRepository.findTop3ByContractIdOrderByLastUpdatedDesc(id).collectList())
            .flatMap(tuple -> {
                ContractDTO contractDTO = tuple.getT1();
                Map<UUID, FileAttachmentDTO> files = mapFileAttachment(tuple);
                contractDTO.setFileAttachments(new ArrayList<>(files.values()));
                return fileClient.getFileAttachmentsByListIds(new ArrayList<>(files.keySet())).collectList().map(fileAttachments -> {
                    fileAttachments.forEach(fileAttachment -> {
                        files.get(fileAttachment.getId()).copyForm(fileAttachment);
                    });
                    return contractDTO;
                });
            })
            .flatMap(dto -> employeeClient.getEmployee(dto.getContractOwner())
                .switchIfEmpty(Mono.just(new EmployeeDTO())).map(employee -> {
                    dto.setEmployee(employee);
                    return dto;
                }))
            .flatMap(dto -> contractMaterialRepository.findAllByIdContract(dto.getId())
                .switchIfEmpty(Mono.just(new ContractMaterialFull()))
                .map(ContractMaterialFull::toDto)
                .collectList()
                .map(materialDTOs -> {
                    dto.setContractMaterialDTOS(materialDTOs);
                    return dto;
                }))
            .flatMap(dto -> {
                List<UUID> itemIds = dto.getContractMaterialDTOS().stream()
                    .map(ContractMaterialDTO::getItemId)
                    .filter(Objects::nonNull)
                    .toList();

                if (itemIds.isEmpty()) {
                    return Mono.just(dto);
                }
                return logisticClient.getItemByListIds(itemIds)
                    .collectList()
                    .flatMap(items -> {
                        Map<UUID, ItemDTO> item = items.stream()
                            .collect(Collectors.toMap(ItemDTO::getId, Function.identity()));
                        dto.getContractMaterialDTOS().forEach(c -> {
                            c.setItemDTO(item.get(c.getItemId()));
                        });
                        return Mono.just(dto);
                    });
            })
//                .flatMap(dto -> contractProductRepository.findAllByIdContract(dto.getId()).map(ContractProductFull::toDto).collectList().map(productDto -> {
//                    dto.setContractProductDTOS(productDto);
//                    return dto;
//                }))
            .flatMap(dto -> customerRepository.findById(dto.getCustomerId())
                .switchIfEmpty(Mono.just(new Customer())).map(Customer::toDto).map(customerDTO -> {
                    dto.setCustomer(customerDTO);
                    return dto;
                }))
            .flatMap(dto -> {
                if (StringUtils.isNotBlank(dto.getApprovalSignFile())) {
                    return fileClient.getFileAttachment(UUID.fromString(dto.getApprovalSignFile()))
                        .map(fileAttachmentDTO -> {
                            dto.setApprovalSignFileAttachment(fileAttachmentDTO);
                            return dto;
                        });
                }

                dto.getContractMaterialDTOS().forEach(c -> {
                    try {
                        if (c.getItemDTO() != null) {
                            System.out.println("Have Item");
                        } else {
                            c.setItemDTO(ItemDTO.builder()
                                .id(UUID.randomUUID())
                                .name("test")
                                .percentProtein(60f)
                                .build());
                        }
                    } catch (Exception e) {
                        c.setItemDTO(ItemDTO.builder()
                            .id(UUID.randomUUID())
                            .name("test")
                            .percentProtein(60f)
                            .build());
                    }
                });
                return Mono.just(dto);
            })
            ;
    }


    private static @NotNull Map<UUID, FileAttachmentDTO> mapFileAttachment(Tuple2<ContractDTO, List<ContractFile>> tuple) {
        List<ContractFile> contractFiles = tuple.getT2();
        Map<UUID, FileAttachmentDTO> files = new HashMap<>();
        contractFiles.forEach(f -> {
            if (StringUtils.isNotBlank(f.getContractFilePath())) {
                files.put(UUID.fromString(f.getContractFilePath()), FileAttachmentDTO.builder().type("contract").id(UUID.fromString(f.getContractFilePath())).build());
            }
            if (StringUtils.isNotBlank(f.getContractIndexFilePath())) {
                files.put(UUID.fromString(f.getContractIndexFilePath()), FileAttachmentDTO.builder().type("appendix").id(UUID.fromString(f.getContractIndexFilePath())).build());
            }
        });
        return files;
    }


    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
            if (nonDeletableStatuses.contains(contract.getStatus())) {
                return Mono.error(new BadRequestAlertException("Cannot delete contract with status: " + contract.getStatus(), "", ""));
            }
            ZonedDateTime lastUpdate = ZonedDateTime.now();

            return contractRepository.changeIsActiveByIdAndUpdateLast(id, lastUpdate, false);
        });
    }

    public Mono<Integer> recoverContract(UUID id) {
        log.debug("Request to  recover Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
            if (contract.getStatus().equals(ContractStatus.DELETED)) {
                return Mono.error(new BadRequestAlertException("Cannot recover contract with status: " + contract.getStatus(), "", ""));
            }
            ZonedDateTime lastUpdate = ZonedDateTime.now();
            return contractRepository.changeIsActiveByIdAndUpdateLast(id, lastUpdate, true).thenReturn(1);
        });
    }

    public Mono<Integer> proposeReviewContract(UUID id) {
        log.debug("Request to Propose Review Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
            if (nonDeletableStatuses.contains(contract.getStatus())) {
                return Mono.error(new BadRequestAlertException("Cannot Propose Review contract with status: " + contract.getStatus(), "", ""));
            }
            ZonedDateTime lastUpdate = ZonedDateTime.now();
            return contractRepository.changeStatusByIdAndStatus(id, ContractStatus.WAITING_APPROVAL, lastUpdate).thenReturn(1);
        });
    }

    public Mono<Integer> approvedContract(UUID id, ApprovedContractRequest approvedContractRequest) {
        log.debug("Request to Approved Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
            if (!ApprovedStatuses.contains(approvedContractRequest.getContractStatus()) && !contract.getStatus().equals(ContractStatus.WAITING_APPROVAL)) {
                return Mono.error(new BadRequestAlertException("Cannot Approved contract with status: " + contract.getStatus(), "", ""));
            }
            ZonedDateTime lastUpdate = ZonedDateTime.now();
            contract.setApprovalSignFileName(approvedContractRequest.getApprovalSignName());
            contract.setApprovalSignFile(approvedContractRequest.getApprovalSign());
            contract.setReviewAt(ZonedDateTime.now());
            contract.setReviewBy(approvedContractRequest.getReviewId());
            if (StringUtils.isNotBlank(approvedContractRequest.getRejectNote())) {
                contract.setRejectNote(approvedContractRequest.getRejectNote());
            }
            return
                SecurityUtils.getUserJWTDetail().flatMap(user -> {
                    contract.setReviewBy(user.getUserId());
                    return Mono.just(contract);
                }).then(
                    contractRepository.save(contract.setIsPersisted()).flatMap(savedContract -> contractRepository.changeStatusByIdAndStatus(id, approvedContractRequest.getContractStatus(), lastUpdate).thenReturn(1))
                )
                ;
        });
    }

    public Mono<Integer> liqidatedReviewContract(UUID id) {
        return contractRepository.findById(id).flatMap(contract -> {
//            if (contract.getStatus().equals(ContractStatus.APPROVED)) {
            ZonedDateTime lastUpdate = ZonedDateTime.now();
            return contractRepository.changeStatusByIdAndStatus(id, ContractStatus.WAITING_LIQUIDATION, lastUpdate).thenReturn(1);
//            }
//            return Mono.error(new BadRequestAlertException("Cannot Liqidated Review contract with status: " + contract.getStatus(), "", ""));

        });
    }

    public Mono<Void> maskAsCompleted(UUID id) {
        return contractRepository.findById(id).flatMap(contract -> {
            return contractRepository.changeStatusByIdAndStatus(id, ContractStatus.FINISHED, ZonedDateTime.now()).then();
        });
    }

    public Mono<Integer> liqidatedConsentContract(UUID id, ConsentToReview consentToReview) {
        log.debug("Request to Liqidated Consent Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
            if (contract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION)) {
                ZonedDateTime lastUpdate = ZonedDateTime.now();
                try {
                    if (StringUtils.isNotBlank(consentToReview.getApprovalSign())) {


                        return contractRepository.consentToReviewContract(id, consentToReview.getApprovalSign(), lastUpdate).thenReturn(1);

                    }
                } catch (Exception e) {
                    return Mono.error(new BadRequestAlertException("Cannot upload file: " + e.getMessage(), "file", "uploaderror"));
                }

            }
            return Mono.error(new BadRequestAlertException("Cannot Liqidated Consent contract with status: " + contract.getStatus() + " or Cannot upload file", "", ""));

        });
    }

    public Mono<Integer> liqidatedRefusalContract(UUID id, RefusalOfReview refusalOfReview) {
        log.debug("Request to Liqidated Review Contract : {}", id);
        return contractRepository.findById(id).flatMap(contract -> {
//            if (contract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION) && contract.getContractValidTo().isBefore(LocalDate.now())) {
            if (contract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION)) {
                ZonedDateTime lastUpdate = ZonedDateTime.now();
                return contractRepository.refusalOfReviewContract(id, refusalOfReview.getRejectNote(), lastUpdate).thenReturn(1);

            }
            return Mono.error(new BadRequestAlertException("Cannot Liqidated Review contract with status: " + contract.getStatus() + "OR Not Time ContractValid", "", ""));

        });
    }

    /*    public Flux<ContractDTO> findAllByFilter(Pageable pageable, ContractRO ro) {
            log.debug("Request to get all Contract by query: {}", ro);

            return contractRepository.findAllByFilter(pageable, ro).map(Contract::toBriefDTO);
        }*/
    public Flux<ContractDTO> findAllByFilter(Pageable pageable, ContractRO ro, Boolean needTotal) {
        log.debug("Request to get all Contract by query: {}", ro);

        return contractRepository.findAllByFilter(pageable, ro).map(Contract::toBriefDTO).collectList()

            .flatMapMany(contractDTOList -> {

                if (needTotal) {
                    BigDecimal totalContractValue = contractDTOList.stream().map(ContractDTO::getContractTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    ContractDTO summaryContract = new ContractDTO();
                    summaryContract.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    summaryContract.setContractName("Tổng hợp đồng");
                    summaryContract.setContractTotal(totalContractValue);
                    summaryContract.setCustomerId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    contractDTOList.add(summaryContract);
                    return Flux.fromIterable(contractDTOList);
                }
                return Flux.fromIterable(contractDTOList);

            });
    }


    public Mono<Long> countAllByFilter(ContractRO ro) {
        log.debug("Request to count all Contract by query: {}", ro);
        return contractRepository.countByFilter(ro);
    }


    public Mono<List<ContractFileReponse>> findAllFileByContractId(UUID id) {
        return contractFileRepository.findTop3ByContractIdOrderByLastUpdatedDesc(id).collectList().flatMapMany(Flux::fromIterable).map(contractFile -> {
            String contractName = null;
            String appendixName = null;

            if (contractFile.getContractFilePath() != null && !contractFile.getContractFilePath().isEmpty()) {
                contractName = contractFile.getContractFilePath();
            }

            if (contractFile.getContractIndexFilePath() != null && !contractFile.getContractIndexFilePath().isEmpty()) {
                appendixName = contractFile.getContractIndexFilePath();
            }

            return new ContractFileReponse(contractName, appendixName);
        }).collectList();
    }


    @Transactional
    public Mono<Void> cleanupOldDeletedContracts(ZonedDateTime thirtyDaysAgo) {
        return contractRepository.updateInactiveContracts(thirtyDaysAgo).then();
    }

// boolean checkDate(ContractDTO contractDTO) {
//     if (contractDTO.getDeliveryTermFrom() != null && contractDTO.getDeliveryTermTo() != null) {
//         if (contractDTO.getDeliveryTermFrom().isBefore(contractDTO.getContractValidFrom()) ||
//             contractDTO.getDeliveryTermTo().isAfter(contractDTO.getContractValidTo()) ||
//             isPayTermEndDateBeforeContractStart(contractDTO)) {

//             return false;
//         }
//     }
//     return true;
// }

// private boolean isPayTermEndDateBeforeContractStart(ContractDTO contractDTO) {
//     long payTermDays = (long) Math.floor(contractDTO.getPayTerm());
//     LocalDate payTermEndDate = contractDTO.getDeliveryTermFrom().plusDays(payTermDays);
//     double payTermFraction = contractDTO.getPayTerm() - payTermDays;
//     if (payTermFraction > 0) {
//         long extraDays = (long) Math.round(payTermFraction * 24);
//         payTermEndDate = payTermEndDate.plusDays(extraDays / 24);
//     }
//     return payTermEndDate.isAfter(contractDTO.getContractValidTo());
// }
//////////////////////////////////////////////////////////////////////////////////////////////

    public Flux<String> getNameFilesByCompany(String companyId) {
        System.out.println("\n\n\n\n\n\n\n " + companyId);
        return templateContractRepository.findAllByCompany(companyId).map(TemplateContract::getNameFile);
    }

    public Mono<Long> countAllMaterialsByFilter(String company) {
        log.debug("RequesttocountallContractbyquery:{}", company);
        return materialRepository.countProductsByFilter(company);
    }

    public Flux<MaterialDTO> findAllMaterialsByFilter(String company) {
        return materialRepository.findAllProductsByFilter(company).map(Material::toDTO);
    }

    public Mono<Long> countAllProductsByFilter(String company) {
        log.debug("RequesttocountallContractbyquery:{}", company);
        return productRepository.countProductsByFilter(company);
    }

    public Flux<ProductDTO> findAllProductsByFilter(String company) {
        return productRepository.findAllProductsByFilter(company).map(Product::toDTO);
    }
}
