package com.masi.sale.web.rest;

import com.masi.sale.repository.QualityIndexRepository;
import com.masi.sale.service.QualityIndexService;
import com.masi.sale.service.dto.QualityIndexDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link com.masi.sale.domain.QualityIndex}.
 */
@RestController
@RequestMapping("/api/quality-indices")
public class QualityIndexResource {

    private static final Logger log = LoggerFactory.getLogger(QualityIndexResource.class);

    private static final String ENTITY_NAME = "masiSaleQualityIndex";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualityIndexService qualityIndexService;

    private final QualityIndexRepository qualityIndexRepository;

    public QualityIndexResource(QualityIndexService qualityIndexService,
            QualityIndexRepository qualityIndexRepository) {
        this.qualityIndexService = qualityIndexService;
        this.qualityIndexRepository = qualityIndexRepository;
    }

    @GetMapping(value = "name", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<String>> getAllQualityIndexNames() {
        log.debug("REST request to get a page of QualityIndices");
        return qualityIndexService.findAllByDistinctName();

    }
}
