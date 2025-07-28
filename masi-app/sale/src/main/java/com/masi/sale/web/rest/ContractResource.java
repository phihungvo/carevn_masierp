package com.masi.sale.web.rest;

import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.sale.domain.Contract;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.repository.ContractRepository;
import com.masi.sale.service.ContractService;
import com.masi.sale.service.RequestApprovalService;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.dto.request.ApprovedContractRequest;
import com.masi.sale.service.dto.request.ConsentToReview;
import com.masi.sale.service.dto.request.RefusalOfReview;
import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import com.carevn.masi.dto.ApiResponse;

/**
 * REST controller for managing {@link Contract}.
 */
@RestController
@RequestMapping("/api/contracts")
public class ContractResource {

    private static final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "masiSaleContract";
    private final RequestApprovalService requestApprovalService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeClient employeeClient;
    private final ContractService contractService;

    private final ContractRepository contractRepository;

    public ContractResource(RequestApprovalService requestApprovalService, EmployeeClient employeeClient, ContractService contractService, ContractRepository contractRepository) {
        this.requestApprovalService = requestApprovalService;
        this.employeeClient = employeeClient;
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }
//contracts/contract-materials/list?ids=f4cdf069-58eb-45ac-9a65-b6e3abf3f832&ids=097420f9-ace2-4b92-9dbc-e63499dafb35

    @GetMapping(value = "/contract-materials/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ContractMaterialDTO>>> getContractMaterials(@RequestParam List<UUID> ids) {
        log.debug("REST request to get a page of ContractMaterials");
        return contractService.findAllContractMaterialByIds(ids).map(list -> ResponseEntity.ok().body(list));
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param contractDTO the contractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractDTO, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     */
    @PostMapping(value = "")
    public Mono<ResponseEntity<Map<String, Object>>> createContract(@Valid @RequestBody ContractDTO contractDTO) throws IOException {
        contractDTO.setId(UUID.randomUUID());

        return contractService.save(contractDTO).handle((result, sink) -> {
            try {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Contract created successfully");
                response.put("contract", result);
                sink.next(ResponseEntity.created(new URI("/api/contracts/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(response));

            } catch (URISyntaxException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param materialDTO the materialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractDTO, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     */
    @PostMapping(value = "/material")
    public Mono<ResponseEntity<Map<String, Object>>> createMaterial(@Valid @RequestBody MaterialDTO materialDTO) throws IOException {
        materialDTO.setId(UUID.randomUUID());

        return contractService.saveMaterial(materialDTO).handle((result, sink) -> {
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "Material created successfully");
            response.put("material", result);
            sink.next(new ResponseEntity<>(response, HttpStatus.CREATED));

        });
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateContract(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody ContractDTO contractDTO) throws URISyntaxException {
        log.debug("REST request to partial update Contract partially : {}, {}", id, contractDTO);

        contractDTO.setId(id);
        return contractRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }
            Mono<ContractDTO> result = contractService.partialUpdate(contractDTO);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString())).body(Utilities.generateResponse("success", res)));
        });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ContractDTO>>> getAllContracts(@ParameterObject ContractRO ro, @ParameterObject Pageable pageable) {
        Boolean needTotal = ro.getWithSum();
        log.debug("REST request to get a page of Contracts");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            ro.setDepartment(user.getGroupId());
            return contractService.countAllByFilter(ro).zipWith(contractService.findAllByFilter(pageable, ro, needTotal).collectList().flatMap(t -> {
                var emIds = t.stream().map(ContractDTO::getContractOwner).toList();
                return employeeClient.getEmployeesByListIds(emIds).collectList().map(emps -> {
                        var empMap = emps.stream().collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));
                        t.forEach(c -> {
                            c.setEmployee(empMap.get(c.getContractOwner()));
                        });
                        return t;
                    })
                    .flatMap(a -> {
                        return requestApprovalService.getReviewsByType(t, "NORMAL", ContractDTO::addNormalReview)
                            .doOnError(e -> log.error("Error when get normal review", e))
                            .then();
                    })
                    .then(requestApprovalService.resolveReviews(t))

                    .then(Mono.just(t));
            })).map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });
    }

    @Operation(summary = "Show list contract delete")
    @GetMapping(value = "status-delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ContractDTO>>> getAllContractsStatusDeletes(@ParameterObject ContractRO ro, @ParameterObject Pageable pageable, ServerHttpRequest request) {
        ro.getContractStatusList().add(ContractStatus.DELETED);
        Boolean needTotal = false;

        return contractService.countAllByFilter(ro).zipWith(contractService.findAllByFilter(pageable, ro, needTotal).collectList().flatMap(t -> {
            var emIds = t.stream().map(ContractDTO::getContractOwner).toList();
            return employeeClient.getEmployeesByListIds(emIds).collectList().map(emps -> {
                var empMap = emps.stream().collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));
                t.forEach(c -> {
                    c.setEmployee(empMap.get(c.getContractOwner()));
                });
                return t;
            });
        })).map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
    }


    /**
     * {@code GET  /contracts/:id} : get the "id" contract.
     *
     * @param id the id of the contractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractDTO, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContractDTO>> getContract(@PathVariable("id") UUID id) {
        log.debug("REST request to get Contract : {}", id);
        Mono<ContractDTO> contractDTO = contractService.findOne(id).flatMap(c -> {
            return requestApprovalService.getReviewsByType(List.of(c), "NORMAL", ContractDTO::addNormalReview)
                .doOnError(e -> log.error("Error when get normal review", e))
                .then(Mono.just(c));
        }).flatMap(c -> {
            return requestApprovalService.resolveReviews(List.of(c)).then(Mono.just(c));
        });
        return ResponseUtil.wrapOrNotFound(contractDTO);
    }


    @Operation(summary = "Get All FileTemplate Contract to Company")
    @GetMapping("/files-template-contract")
    public Mono<ResponseEntity<List<String>>> getAllFile() {
        return SecurityUtils.getCompanyId().flatMap(user -> contractService.getNameFilesByCompany(user).collectList()
            .flatMap(fileNames -> {
                if (fileNames.isEmpty()) {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("No files found for the given company.")));
                } else {
                    return Mono.just(ResponseEntity.ok(fileNames));
                }
            })).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @param id the id of the contractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteContract(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Contract : {}", id);

        return contractService.delete(id).then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build())).map(responseEntity -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract deleted successfully");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(IllegalStateException.class, ex -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Cannot delete contract");
            responseBody.put("message", ex.getMessage());
            responseBody.put("status", "success");
            return Mono.just(ResponseEntity.badRequest().body(responseBody));
        });
    }

    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clean-old-deleted-contract")
    public Mono<ResponseEntity<Map<String, Object>>> deleteOldContract() {
        var zdt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        log.debug("REST request to delete Contract at local time" + zdt);

        return contractService.cleanupOldDeletedContracts(zdt.minusMonths(1)).then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build())).map(responseEntity -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract deleted successfully");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(IllegalStateException.class, ex -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Cannot delete contract");
            responseBody.put("message", ex.getMessage());
            responseBody.put("status", "success");
            return Mono.just(ResponseEntity.badRequest().body(responseBody));
        });
    }


    @Operation(summary = "recover contract ")
    @PatchMapping("/recover-contract/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> recoverContract(@PathVariable("id") UUID id) {
        log.debug("REST request to recover Contract : {}", id);
        return contractService.recoverContract(id).then(Mono.fromCallable(() -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract recovered successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        })).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to recover contract: " + e.getMessage(), "contract", "approved");
        });
    }

    @Operation(summary = "approved review contract ")
    @PatchMapping("/approved-review/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> proposeReviewContract(@PathVariable("id") UUID id) {
        log.debug("REST request to propose review Contract : {}", id);
        return contractService.proposeReviewContract(id).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract recovered review successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to approved contract: " + e.getMessage(), "contract", "approved");
        });
    }

    @Operation(summary = "approved contract ")
    @PatchMapping("/approved/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> approvedContract(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody ApprovedContractRequest approvedContractRequest) throws URISyntaxException {
        log.debug("REST request to approve Contract : {}", id);
        return contractService.approvedContract(id, approvedContractRequest).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract approved successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to approved contract: " + e.getMessage(), "contract", "approved");
        });
    }

    @Operation(summary = "liqidated review contract ")
    @PatchMapping("/liqidated-review/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> liqidatedReviewContract(@PathVariable("id") UUID id) {
        log.debug("REST request to liqidated review Contract : {}", id);
        return contractService.liqidatedReviewContract(id).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract liqidated review successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to liqidate contract: " + e.getMessage(), "contract", "liqidationerror");
        });
    }

    @Operation(summary = "liqidated contract if success")
    @PatchMapping("/liqidated-consent/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> liqidatedContract(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody ConsentToReview consentToReview) throws URISyntaxException {
        log.debug("REST request to Consent Contract : {}", id);
        return contractService.liqidatedConsentContract(id, consentToReview).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract Liqidated Consent successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to liqidate contract: " + e.getMessage(), "contract", "liqidationerror");
        });
    }

    @PatchMapping(value = "/{id}/mask-finished", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> maskFinishedContract(@PathVariable("id") UUID id) {
        log.debug("REST request to mask finished Contract : {}", id);
        return contractService.maskAsCompleted(id).then(Mono.fromCallable(() -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract masked successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        })).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to mask contract: " + e.getMessage(), "contract", "maskerror");
        });
    }

    @Operation(summary = "liqidated contract if fail")
    @PatchMapping("/liqidated-refusal/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> liqidatedContract(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody RefusalOfReview refusalOfReview) throws URISyntaxException {
        log.debug("REST request to Refusal Contract : {}", id);
        return contractService.liqidatedRefusalContract(id, refusalOfReview).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Contract Liqidated refusal successfully");
            responseBody.put("contractId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to liqidate contract: " + e.getMessage(), "contract", "liqidationerror");
        });

    }


    @GetMapping(value = "/list-material", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<MaterialDTO>>> getAllMaterials(@RequestParam(required = false) String search) {

        log.debug("REST request to get a page of Materials");
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            String company = String.valueOf(login.getCompanyId());
            return contractService.countAllMaterialsByFilter(company).zipWith(contractService.findAllMaterialsByFilter(company).collectList()).map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));

        });
    }


    @GetMapping(value = "/list-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductDTO>>> getAllProducts(@RequestParam(required = false) String search) {

        log.debug("REST request to get a page of Products");
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            String company = String.valueOf(login.getCompanyId());
            return contractService.countAllProductsByFilter(company)
                .zipWith(contractService.findAllProductsByFilter(company).collectList()).map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));

        });
    }

    @Operation(summary = "yeu cau xet duyet thanh ly")
    @PatchMapping(value = "/{id}/request-liquidation-review")
    public Mono<ResponseEntity<Map>> requestReview(@PathVariable UUID id, @RequestBody CreateReviewRequest createReviewRequest) {
        createReviewRequest.setDocumentId(id);
        return requestApprovalService.requestReview(createReviewRequest).flatMap(requestApprovalDTO -> {
            return contractService.backUpAndSetNewStatus(id, ContractStatus.WAITING_LIQUIDATION).then(Mono.just(ResponseEntity.ok(Map.of("ok", true))));
        });
    }

    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map>> requestNormalReview(@PathVariable UUID id, @RequestBody CreateReviewRequest createReviewRequest) {
        createReviewRequest.setDocumentId(id);
        return requestApprovalService.requestReview(createReviewRequest, "NORMAL").flatMap(requestApprovalDTO -> {
            return contractService.setContractStatus(id, ContractStatus.WAITING_APPROVAL).then(Mono.just(ResponseEntity.ok(Map.of("ok", true))));
        });
    }

    @PatchMapping(value = "/{id}/normal-review")
    public Mono<ResponseEntity<Map>> approveNormalReview(@PathVariable UUID id, @RequestBody UpdateReview updateReview) {
        updateReview.setDocumentId(id);
        return requestApprovalService.handleReview(updateReview, "NORMAL").flatMap(e -> {
            return requestApprovalService.findByDocumentId(id, "NORMAL").collectList().flatMap(requestApprovals -> {
                var isAllApproved = requestApprovals.stream().allMatch(requestApproval -> requestApproval.getResult() != null && requestApproval.getResult());
                var isOneRejected = requestApprovals.stream().anyMatch(requestApproval -> requestApproval.getResult() != null && !requestApproval.getResult());
                if (isAllApproved) {
                    return contractService.setContractStatus(e.getDocumentId(), ContractStatus.APPROVED).then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return contractService.setContractStatus(e.getDocumentId(), ContractStatus.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });
        });
    }

    @Operation(summary = "xet duyet thanh ly")
    @PatchMapping(value = "review")
    public Mono<ResponseEntity<Map>> approve(@RequestBody UpdateReview updateReview) {
        return requestApprovalService.handleReview(updateReview).flatMap(e -> {
            return requestApprovalService.findByDocumentId(e.getDocumentId()).collectList().flatMap(requestApprovals -> {
                var isAllApproved = requestApprovals.stream().allMatch(requestApproval -> requestApproval.getResult() != null && requestApproval.getResult());

                var isOneRejected = requestApprovals.stream().anyMatch(requestApproval -> requestApproval.getResult() != null && !requestApproval.getResult());
                if (isAllApproved) {
                    return contractService.setContractStatus(e.getDocumentId(), ContractStatus.LIQUIDATED).then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return contractService.restoreOldStatus(e.getDocumentId()).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });
        });
    }

}
