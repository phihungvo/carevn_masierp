package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.service.EmployeeProfileService;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.dto.EmployeeProfileQuery;
import com.masi.employee.service.dto.EmployeeProfileXlsx;
import com.masi.employee.service.dto.reponse.ListEmployeeDepartmentDTOReponse;
import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.masi.employee.domain.EmployeeProfile}.
 */
@RestController
@RequestMapping("/api/employee-profiles")
public class EmployeeProfileResource {

    private static final Logger log = LoggerFactory.getLogger(EmployeeProfileResource.class);

    private static final String ENTITY_NAME = "masiEmployeeEmployeeProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeProfileService employeeProfileService;

    private final EmployeeProfileRepository employeeProfileRepository;
    private final FileClient fileClient;

    public EmployeeProfileResource(EmployeeProfileService employeeProfileService, EmployeeProfileRepository employeeProfileRepository, FileClient fileClient) {
        this.employeeProfileService = employeeProfileService;
        this.employeeProfileRepository = employeeProfileRepository;
        this.fileClient = fileClient;
    }

    @PutMapping("recalculate-code")
    public Mono<ResponseEntity<Void>> recalculateCode(@RequestParam("password") String password) {
        if ("itbee".equals(password)) {
            return employeeProfileService.reEvaluateCode().then(Mono.just(ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "employeeProfile.recalculate.success", "")).build()));
        }
        return Mono.just(ResponseEntity.badRequest().headers(HeaderUtil.createAlert(applicationName, "employeeProfile.recalculate.fail", "")).build());
    }

    @PostMapping("")
    public Mono<ResponseEntity<EmployeeProfileDTO>> createEmployeeProfile(@Valid @RequestBody EmployeeProfileDTO employeeProfileDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeeProfile : {}", employeeProfileDTO);
        employeeProfileDTO.setId(UUID.randomUUID());
        return employeeProfileService.save(employeeProfileDTO).handle((result, sink) -> {
            try {
                sink.next(ResponseEntity.created(new URI("/api/employee-profiles/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result));
            } catch (URISyntaxException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    @GetMapping("{id}/contract")
    public Mono<ResponseEntity<InputStreamResource>> getContract(@PathVariable("id") UUID id) {
        log.debug("REST request to get contract of EmployeeProfile : {}", id);
        return employeeProfileService.exportContract(id).flatMap(contractFileName -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(contractFileName);
                InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"HDLD.docx\"");

                return Mono.just(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));

            } catch (FileNotFoundException e) {
                return Mono.error(new RuntimeException(e));
            }
        });


    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<EmployeeProfileDTO>> partialUpdateEmployeeProfile(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody EmployeeProfileDTO employeeProfileDTO) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeProfile partially : {}, {}", id, employeeProfileDTO);
        employeeProfileDTO.setId(id);

        return employeeProfileRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }

            Mono<EmployeeProfileDTO> result = employeeProfileService.partialUpdate(employeeProfileDTO);

            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString())).body(res));
        });
    }

    @PatchMapping(value = "/sync-account-status")
    public Mono<Void> syncAccountStatus() {
        return employeeProfileService.syncAccountStatus();
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<EmployeeProfileDTO>>> getAllEmployeeProfiles(@org.springdoc.core.annotations.ParameterObject Pageable pageable, EmployeeProfileQuery query) {
        log.debug("REST request to get a page of EmployeeProfiles");
        return SecurityUtils.getCompanyId().flatMap(companyId -> {
            return Mono.just(query);
        }).then(employeeProfileService.countAllByQuery(query).zipWith(employeeProfileService.findAllByQuery(query, pageable).collectList()).map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))));
    }

    @GetMapping("/list")
    public Mono<ResponseEntity<List<EmployeeProfileDTO>>> getAllEmployeeProfilesInList(@RequestParam("ids") List<UUID> ids) {
        return employeeProfileService.findByInIds(ids).map(employeeProfileDTOS -> ResponseEntity.ok().body(employeeProfileDTOS));
    }

    /**
     * {@code GET  /employee-profiles/:id} : get the "id" employeeProfile.
     *
     * @param id the id of the employeeProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the employeeProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeProfileDTO>> getEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to get EmployeeProfile : {}", id);
        Mono<EmployeeProfileDTO> employeeProfileDTO = employeeProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeProfileDTO);
    }

    /**
     * {@code DELETE  /employee-profiles/:id} : delete the "id" employeeProfile.
     *
     * @param id the id of the employeeProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to delete EmployeeProfile : {}", id);
        return employeeProfileService.delete(id).then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()));
    }

    @Operation(summary = "Tìm theo Căn cước, không có trả 404")
    @GetMapping("/citizen-id/{idCard}")
    public Mono<ResponseEntity<EmployeeProfileDTO>> getEmployeeProfileByCitizenId(@PathVariable("idCard") String idCard) {
        log.debug("REST request to get EmployeeProfile by citizen id : {}", idCard);
        return employeeProfileService.findByCitizenId(idCard).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Tìm theo Mã số thuế, không có trả 404")
    @GetMapping("/tax-code/{taxId}")
    public Mono<ResponseEntity<EmployeeProfileDTO>> getEmployeeProfileByTaxCode(@PathVariable("taxId") String taxId) {
        log.debug("REST request to get EmployeeProfile by tax code : {}", taxId);
        return employeeProfileService.findByTaxCode(taxId).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Tìm theo Số tài khoản ngân hàng, không có trả 404")
    @GetMapping("/bank-number/{bankCode}")
    public Mono<ResponseEntity<EmployeeProfileDTO>> getEmployeeProfileByBankCode(@PathVariable("bankCode") String bankCode) {
        log.debug("REST request to get EmployeeProfile by bank code : {}", bankCode);
        return employeeProfileService.findByBankCode(bankCode).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Vô hiệu profile")
    @PatchMapping("/{id}/disable")
    public Mono<ResponseEntity<EmployeeProfileDTO>> disableEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to disable EmployeeProfile : {}", id);
        return employeeProfileService.disableProfile(id).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Kích hoạt profile")
    @PatchMapping("/{id}/enable")
    public Mono<ResponseEntity<EmployeeProfileDTO>> enableEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to enable EmployeeProfile : {}", id);
        return employeeProfileService.activeProfile(id).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Xác nhận nghỉ việc")
    @PatchMapping("/{id}/confirm-leave")
    public Mono<ResponseEntity<EmployeeProfileDTO>> confirmLeaveEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to confirm leave EmployeeProfile : {}", id);
        return employeeProfileService.confirmLeave(id).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Operation(summary = "Làm việc lại sau nghỉ việc")
    @PatchMapping("/{id}/back-to-work")
    public Mono<ResponseEntity<EmployeeProfileDTO>> backToWorkEmployeeProfile(@PathVariable("id") UUID id) {
        log.debug("REST request to back to work EmployeeProfile : {}", id);
        return employeeProfileService.confirmRetire(id).map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO)).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


    @Operation(summary = "Xuất file excel")
    @GetMapping("/export-xlsx")
    public Mono<ResponseEntity<InputStreamResource>> exportEmployeeProfilesXlsx(EmployeeProfileQuery query, @ParameterObject Pageable pageable) {
        log.debug("REST request to export xlsx EmployeeProfiles");
        return SecurityUtils.getUserJWTDetail().flatMap(e -> {
            query.setCompany(e.getCompanyId());
            return Mono.just(query);
        }).then(employeeProfileService.exportXLSX(query, pageable).flatMap(xlsxFileName -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(xlsxFileName);
                InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", "Danh sach nhan vien.xlsx"));
                return Mono.just(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
            } catch (FileNotFoundException e) {
                return Mono.error(new RuntimeException(e));
            }
        }));
    }

    @Operation(summary = "Lấy mẫu file excel")
    @GetMapping("/xlsx-template")
    public Mono<ResponseEntity<InputStreamResource>> templateEmployeeProfilesXlsx() {
        return employeeProfileService.getImportTemplate().flatMap(stream -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", "Mau nhap nhan vien.xlsx"));
            return Mono.just(new ResponseEntity<>(stream, headers, HttpStatus.OK));
        });
    }

    @Operation(summary = "Cập nhật mẫu excel")
    @PostMapping("/update-xlsx")
    public Mono<ResponseEntity<Void>> updateEmployeeProfiles(@RequestBody Mono<FilePart> fileParts) {
        log.debug("REST request to update EmployeeProfiles");
        return employeeProfileService.uploadImportFile(fileParts).then(Mono.just(ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "employeeProfile.import.success", "")).build()));
    }


    @Operation(summary = "Import file xlsx")
    @PostMapping("/import/{fileId}")
    public Mono<ResponseEntity<List<EmployeeProfileXlsx>>> importEmployeeProfiles(@PathVariable("fileId") UUID fileId) {
        log.debug("REST request to import EmployeeProfiles");
        return fileClient.getWithBase64(fileId).flatMap(f -> {
            return employeeProfileService.importXLSX(f.getBase64())
                .collectList()
                .flatMap(employeeProfileDTOS -> {
                    return Mono.just(ResponseEntity.ok().body(employeeProfileDTOS));
                });
        });

    }


    @Operation(summary = "Get review by id")
    @GetMapping("/list-department")
    public Mono<ResponseEntity<ListEmployeeDepartmentDTOReponse>> getListTeam() {
        Mono<ListEmployeeDepartmentDTOReponse> getlist = employeeProfileService.getListDepartment();
        return ResponseUtil.wrapOrNotFound(getlist);
    }

    @Operation(summary = "Kích hoạt máy chấm công")
    @PatchMapping("/{id}/enable-timekeeping-device")
    public Mono<ResponseEntity<EmployeeProfileDTO>> enableTimkeepingDevice(@PathVariable("id") UUID id) {
        log.debug("REST request to enable EmployeeProfile : {}", id);
        return employeeProfileService.activeTimeKeepingDevice(id)
                .map(employeeProfileDTO -> ResponseEntity.ok().body(employeeProfileDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/search-name-code")
    public Mono<ResponseEntity<List<UUID>>> getAllEmployeeProfilesBySea(
            @RequestParam(value = "search", required = false) String search) {
        EmployeeProfileQuery employeeProfileQuery = new EmployeeProfileQuery();
        employeeProfileQuery.setSearch(search);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        return employeeProfileService
                .findAllByQuery(employeeProfileQuery,pageable)
                .map(EmployeeProfileDTO::getId)
                .collectList()
                .map(ids -> ResponseEntity.ok().body(ids))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

}
