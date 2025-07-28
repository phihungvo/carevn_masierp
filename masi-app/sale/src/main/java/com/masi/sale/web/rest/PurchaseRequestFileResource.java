package com.masi.sale.web.rest;

import com.masi.sale.repository.PurchaseRequestFileRepository;
import com.masi.sale.service.PurchaseRequestFileService;
import com.masi.sale.service.dto.PurchaseRequestFileDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.sale.domain.PurchaseRequestFile}.
 */
@RestController
@RequestMapping("/api/purchase-request-files")
public class PurchaseRequestFileResource {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRequestFileResource.class);

    private static final String ENTITY_NAME = "masiSalePurchaseRequestFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseRequestFileService purchaseRequestFileService;


    public PurchaseRequestFileResource(
        PurchaseRequestFileService purchaseRequestFileService    ) {
        this.purchaseRequestFileService = purchaseRequestFileService;
    }

    @Operation(summary = "Truyền vào uuid cuả file để trả về file")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String,Object>>> getPurchaseRequestFile(@PathVariable("id") UUID id) {
        log.debug("REST request to get PurchaseRequestFile : {}", id);
        Mono<PurchaseRequestFileDTO> purchaseRequestFileDTO = purchaseRequestFileService.findOne(id);
        return purchaseRequestFileDTO.handle((purchaseRequestFile, sink) -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(purchaseRequestFile.getFilePath());
                String[] fileNameSplited = purchaseRequestFile.getFilePath().replace('\\', '/').split("/");
                String fileName = fileNameSplited[fileNameSplited.length - 1];
                Map<String, Object> map = Map.of("file", new InputStreamResource(fileInputStream).getContentAsByteArray(), "fileName", fileName);
                sink.next(ResponseEntity.ok()
                    .body(map));
            } catch (Exception e) {
                sink.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e));
            }
        });
    }

}
