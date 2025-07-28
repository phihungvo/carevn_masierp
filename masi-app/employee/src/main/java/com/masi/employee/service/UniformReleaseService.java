package com.masi.employee.service;

import com.masi.employee.domain.*;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformReleaseDTO;
import com.masi.employee.service.dto.UniformReleaseGetListDTO;
import com.masi.employee.service.dto.UniformStockReleaseDTO;
import com.masi.employee.service.mapper.*;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link UniformRelease}.
 */
@Service
@Transactional
public class UniformReleaseService {
    private static final String ENTITY_NAME = "masiEmployeeUniformRelease";

    private static final Logger log = LoggerFactory.getLogger(UniformReleaseService.class);

    private final UniformReleaseRepository uniformReleaseRepository;

    private final UniformReleaseMapper uniformReleaseMapper;

    private final UniformReturnMapper uniformReturnMapper;

    private final UniformRepository uniformRepository;

    private final UniformFormDetailMapper uniformFormDetailMapper;
    private final UniformMapper uniformMapper;

    private final UniformFormDetailRepository uniformFormDetailRepository;

    private final UniformStockRepository uniformStockRepository;
    private UniformReturnService uniformReturnService;

    private DocumentSequenceService documentSequenceService;
    private final UniformReturnRepository uniformReturnRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final LogisticClient logisticClient;

    @Autowired
    public void setUniformReturnService(@Lazy UniformReturnService uniformReturnService) {
        this.uniformReturnService = uniformReturnService;
    }

    @Autowired
    public void setDocumentSequenceService(@Lazy DocumentSequenceService documentSequenceService) {
        this.documentSequenceService = documentSequenceService;
    }

    @Lazy
    public UniformReleaseService(UniformReleaseRepository uniformReleaseRepository,
                                 UniformReleaseMapper uniformReleaseMapper, UniformReturnMapper uniformReturnMapper, UniformFormDetailMapper uniformFormDetailMapper, UniformMapper uniformMapper,
                                 UniformFormDetailRepository uniformFormDetailRepository, UniformStockRepository uniformStockRepository,
                                 UniformRepository uniformRepository, UniformReturnRepository uniformReturnRepository, EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, LogisticClient logisticClient) {
        this.uniformReleaseRepository = uniformReleaseRepository;
        this.uniformReleaseMapper = uniformReleaseMapper;
        this.uniformReturnMapper = uniformReturnMapper;
        this.uniformMapper = uniformMapper;
        this.uniformRepository = uniformRepository;
        this.uniformFormDetailMapper = uniformFormDetailMapper;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformStockRepository = uniformStockRepository;
        this.uniformReturnRepository = uniformReturnRepository;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.logisticClient = logisticClient;
    }

    /**
     * Save a uniformRelease.
     *
     * @param uniformReleaseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformReleaseDTO> save(UniformReleaseDTO uniformReleaseDTO) {
        log.debug("Request to save UniformRelease : {}", uniformReleaseDTO);
        return uniformReleaseRepository.save(uniformReleaseMapper.toEntity(uniformReleaseDTO))
                .map(uniformReleaseMapper::toDto);
    }

    public Mono<UniformReleaseDTO> handleCreateReleaseStock(UniformStockReleaseDTO uniformStockReleaseDTO) {
        log.debug("Request to save UniformRelease z: {}", uniformStockReleaseDTO);

        if (!uniformStockReleaseDTO.getIsReturned()) {
            uniformStockReleaseDTO.setRemaining(uniformStockReleaseDTO.getQuantity());
        }
        var uniformReleaseCode = documentSequenceService.findOrInitSequence(ENTITY_NAME,
                uniformStockReleaseDTO.getCompany());
        log.debug("uniform release code: {}", uniformStockReleaseDTO);
        return uniformReleaseCode.flatMap(sequence -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
            LocalDate date = LocalDate.now(); // or any other LocalDate instance
            String formattedDate = date.format(formatter);
            var seqFormat = String.format("/%d", sequence);
            if (sequence < 10000) {
                seqFormat = String.format("/%04d", sequence);
            }
            var code = "XK" + formattedDate + seqFormat;
            uniformStockReleaseDTO.setCode(code);
            if (uniformStockReleaseDTO.getIsReturned()) {
                uniformStockReleaseDTO.setRemaining(0);
            }
            return uniformReleaseRepository.save(uniformReleaseMapper.toEntity(uniformStockReleaseDTO.toReleaseDto()))
                    .map(uniformReleaseMapper::toDto).flatMap(release -> {
                        var detailsEntity = uniformFormDetailMapper
                                .toEntity(uniformStockReleaseDTO.toDetailDto(release));
                        return uniformFormDetailRepository.saveAll(detailsEntity)
                                .collectList()
                                .flatMap(detail -> {
                                    var detailDto = detail.stream().map(uniformFormDetailMapper::toDto)
                                            .collect(Collectors.toList());

                                    return calculateUniformStockRelease(
                                            detailDto, release.getCompany(),
                                            release.getId(), release.getWarehouseId())
                                            .then(Mono.defer(() -> {
                                                if (uniformStockReleaseDTO.getIsReturned()) {
                                                    var uniformReturnDto = uniformStockReleaseDTO.toReturnDto();
                                                    return uniformReturnService.fastReturn(uniformReturnDto)
                                                            .thenReturn(release);
                                                } else {
                                                    return Mono.empty();
                                                }
                                            }))
                                            .thenReturn(release);
                                });
                    }).doOnError(e -> {
                        log.error("Error when create release stock", e);
                    });
        });
    }

    /**
     * Update a uniformRelease.
     *
     * @param uniformReleaseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformReleaseDTO> update(UniformReleaseDTO uniformReleaseDTO) {
        log.debug("Request to update UniformRelease : {}", uniformReleaseDTO);
        return uniformReleaseRepository
                .save(uniformReleaseMapper.toEntity(uniformReleaseDTO).setIsPersisted())
                .map(uniformReleaseMapper::toDto);
    }

    /**
     * Partially update a uniformRelease.
     *
     * @param uniformReleaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformReleaseDTO> partialUpdate(UniformReleaseDTO uniformReleaseDTO) {
        log.debug("Request to partially update UniformRelease : {}", uniformReleaseDTO);

        return uniformReleaseRepository
                .findById(uniformReleaseDTO.getId())
                .map(existingUniformRelease -> {
                    uniformReleaseMapper.partialUpdate(existingUniformRelease, uniformReleaseDTO);

                    return existingUniformRelease;
                })
                .flatMap(uniformReleaseRepository::save)
                .map(uniformReleaseMapper::toDto);
    }

    /**
     * Get all the uniformReleases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformReleaseDTO> findAll(Pageable pageable, UniformReleaseGetListDTO uniformReleaseGetListDTO) {
        log.debug("Request to get all UniformReleases");
        if (uniformReleaseGetListDTO.getUniformId() != null && !uniformReleaseGetListDTO.getUniformId().isEmpty()) {
            return uniformRepository.getIdAndNameUniformByCompanyAndDeleteAtIsNull(
                    uniformReleaseGetListDTO.getCompany(), uniformReleaseGetListDTO.getUniformId())
                    .collectList()
                    .flatMapMany(uniforms -> uniformReleaseRepository.findAllWithQuery(pageable,
                            uniformReleaseGetListDTO, uniforms)
                            .map(uniformReleaseMapper::toDto));
        } else {
            log.info("Request to get all UniformReleases 1232 312");

            return uniformReleaseRepository.findAllWithQuery(pageable,
                    uniformReleaseGetListDTO, null)
                    .map(uniformReleaseMapper::toDto);
        }
    }

    /**
     * Returns the number of uniformReleases available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return uniformReleaseRepository.count();
    }

    public Mono<Long> countAllByQuery(UniformReleaseGetListDTO uniformReleaseGetListDTO) {
        if (uniformReleaseGetListDTO.getUniformId() != null && !uniformReleaseGetListDTO.getUniformId().isEmpty()) {
            return uniformRepository.getIdAndNameUniformByCompanyAndDeleteAtIsNull(
                    uniformReleaseGetListDTO.getCompany(), uniformReleaseGetListDTO.getUniformId())
                    .collectList()
                    .flatMap(uniforms -> uniformReleaseRepository.countAllByQuery(uniformReleaseGetListDTO, uniforms));
        } else {
            return uniformReleaseRepository.countAllByQuery(uniformReleaseGetListDTO, null);
        }
    }

    /**
     * Get one uniformRelease by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformReleaseDTO> findOne(UUID id) {
        log.debug("Request to get UniformRelease : {}", id);
        return uniformReleaseRepository.findById(id)
            .map(uniformReleaseMapper::toDto)
            .flatMap(release -> employeeRepository.findById(release.getEmployeeId()).map(employeeMapper::toDto).flatMap(employee -> {
                release.setEmployee(employee);
                return uniformFormDetailRepository.findAllByUniformReleaseIdAndCompany(id, release.getCompany()).collectList().flatMap(details -> {
                    var list = details.stream().map(uniformFormDetailMapper::toDto).collect(Collectors.toSet());
                    var listUniformId = details.stream().map(UniformFormDetail::getUniformId).toList();
                    return uniformRepository.getIdAndNameUniformByCompanyAndDeleteAtIsNull(release.getCompany(), listUniformId)
                        .collectList()
                        .flatMap(uniforms -> uniformReturnRepository.findByUniformReleaseId(id, release.getCompany(), uniforms)
                            .collectList()
                            .flatMap(uniformReturns -> {
                                var listReturn = uniformReturns.stream().map(uniformReturnMapper::toDto).collect(Collectors.toSet());
                                release.setUniformReturn(listReturn);
                                list.forEach(detail -> {
                                    var uniform = uniforms.stream().filter(u -> u.getId() != null && u.getId().equals(detail.getUniformId())).findFirst().orElse(null);
                                    detail.setUniform(uniformMapper.toDto(uniform));
                                });
                                release.setUniformFormDetails(list);
                                return logisticClient.getUniformReleaseResponse(release).map(response -> {
                                    release.getUniformFormDetails().forEach(detail -> {
                                        if(response.getUomMap().containsKey(detail.getUomId())){
                                            detail.setUomName(response.getUomMap().get(detail.getUomId()).getName());
                                        }
                                    });

                                    if (response.getWarehouseMap().containsKey(release.getWarehouseId())) {
                                        release.setWarehouse(response.getWarehouseMap().get(release.getWarehouseId()));
                                    }

                                    return release;
                                });
                            }));
                });
            }));
    }

    /**
     * Delete the uniformRelease by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformRelease : {}", id);
        return uniformReleaseRepository.deleteById(id);
    }

    public Mono<Void> calculateUniformStockRelease(List<UniformFormDetailDTO> uniformFormDetailDTO, String company,
            UUID id, UUID warehouseId) {
        return uniformFormDetailRepository.findByUniformReleaseNotDelete(id).collectList().flatMap(details -> {
            var listUniformId = details.stream().map(UniformFormDetail::getUniformId).collect(Collectors.toList());
            return uniformStockRepository.getListUniformStockNotDeleteByListUniformId(listUniformId,
                    company, warehouseId)
                    .collectList()
                    .flatMap(existingStocks -> {
                        var stockMap = existingStocks.stream()
                                .collect(Collectors.toMap(UniformStock::getUniformId, stock -> stock));

                        List<UniformStock> stocksToUpdate = new ArrayList<>();

                        for (var detail : details) {
                            var uniformId = detail.getUniformId();
                            Integer quantity = detail.getQuantity();

                            UniformStock stock = stockMap.get(uniformId);
                            if (stock != null && stock.getStock() >= quantity) {
                                // Cập nhật số lượng tồn kho hiện tại
                                stock.setStock(stock.getStock() - quantity);
                                stock.setIsPersisted();
                                stocksToUpdate.add(stock);

                            } else {
                                // Tạo mới mục tồn kho
                                String stockNotEnough = (stock != null ? stock.toString() : "");
                                return Mono
                                        .error(new BadRequestAlertException(stockNotEnough, "UNIFORM_STOCK",
                                                "NOT_ENOUGH"));
                            }
                        }

                        return uniformStockRepository.saveAll(stocksToUpdate).then();
                    });
        });
    }
}
