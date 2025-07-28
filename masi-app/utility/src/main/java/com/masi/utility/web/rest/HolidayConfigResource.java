package com.masi.utility.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.utility.domain.criteria.HolidayConfigCriteria;
import com.masi.utility.repository.HolidayConfigRepository;
import com.masi.utility.service.HolidayConfigService;
import com.masi.utility.service.dto.HolidayConfigDTO;
import com.masi.utility.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.masi.utility.domain.HolidayConfig}.
 */
@RestController
@RequestMapping("/api/holiday-configs")
public class HolidayConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(HolidayConfigResource.class);

    private static final String ENTITY_NAME = "masiUtilityHolidayConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HolidayConfigService holidayConfigService;


    public HolidayConfigResource(HolidayConfigService holidayConfigService) {
        this.holidayConfigService = holidayConfigService;
    }


    @PostMapping("")
    public Mono<ResponseEntity<HolidayConfigDTO>> createHolidayConfig(@RequestBody HolidayConfigDTO holidayConfigDTO) {
        LOG.debug("REST request to save HolidayConfig : {}", holidayConfigDTO);
        if (holidayConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new holidayConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return holidayConfigService.save(holidayConfigDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/holiday-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e));
                }
            });
    }

    @GetMapping("")
    public Mono<ApiResponse<HolidayConfigDTO>> getAllHolidayConfigs(HolidayConfigCriteria criteria, Pageable pageable) {
        return ApiResponse.from(holidayConfigService.findByCriteria(criteria, pageable), holidayConfigService.countByCriteria(criteria));
    }

    @GetMapping("/by/{month}/{year}")
    public Mono<List<HolidayConfigDTO>> getAllHolidayConfigsByMonthAndYear(@PathVariable Integer month, @PathVariable Integer year) {

        return holidayConfigService.findByMonth(month, year);
    }

}
