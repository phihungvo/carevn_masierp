package com.masi.employee.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformStock;
import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.repository.UniformStockRepository;
import com.masi.employee.service.dto.UniformDTO;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.dto.UniformStockDTO;
import com.masi.employee.service.dto.UomDTO;
import com.masi.employee.service.mapper.UniformStockMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.UniformStock}.
 */
@Service
@Transactional
public class UniformStockService {

    private static final Logger log = LoggerFactory.getLogger(UniformStockService.class);

    private final UniformStockRepository uniformStockRepository;
    private final UniformFormDetailRepository uniformFormDetailRepository;
    private final LogisticClient logisticClient;
    private final UniformStockMapper uniformStockMapper;

    public UniformStockService(UniformStockRepository uniformStockRepository, UniformStockMapper uniformStockMapper, UniformFormDetailRepository uniformFormDetailRepository, LogisticClient logisticClient) {
        this.uniformStockRepository = uniformStockRepository;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformStockMapper = uniformStockMapper;
        this.logisticClient = logisticClient;
    }

    /**
     * Save a uniformStock.
     *
     * @param uniformStockDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformStockDTO> save(UniformStockDTO uniformStockDTO) {
        log.debug("Request to save UniformStock : {}", uniformStockDTO);
        return uniformStockRepository.save(uniformStockMapper.toEntity(uniformStockDTO)).map(uniformStockMapper::toDto);
    }

    /**
     * Update a uniformStock.
     *
     * @param uniformStockDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformStockDTO> update(UniformStockDTO uniformStockDTO) {
        log.debug("Request to update UniformStock : {}", uniformStockDTO);
        return uniformStockRepository.save(uniformStockMapper.toEntity(uniformStockDTO).setIsPersisted()).map(uniformStockMapper::toDto);
    }

    /**
     * Partially update a uniformStock.
     *
     * @param uniformStockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformStockDTO> partialUpdate(UniformStockDTO uniformStockDTO) {
        log.debug("Request to partially update UniformStock : {}", uniformStockDTO);

        return uniformStockRepository.findById(uniformStockDTO.getId()).map(existingUniformStock -> {
            uniformStockMapper.partialUpdate(existingUniformStock, uniformStockDTO);

            return existingUniformStock;
        }).flatMap(uniformStockRepository::save).map(uniformStockMapper::toDto);
    }

    /**
     * Get all the uniformStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformStockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformStocks");
        return uniformStockRepository.findAllBy(pageable).map(uniformStockMapper::toDto);
    }

    /**
     * Returns the number of uniformStocks available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return uniformStockRepository.count();
    }

    public Mono<Void> returnUniform(UUID uniformId, Integer quantity) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return uniformStockRepository.findOneByUniformIdAndCompany(uniformId, user.getCompanyId()).flatMap(stock -> {
                stock.setStock(stock.getStock() + quantity);
                return uniformStockRepository.save(stock.setIsPersisted()).then();
            });
        });
    }

    public Mono<Void> releaseUniform(UUID uniformId, Integer quantity) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return uniformStockRepository.findOneByUniformIdAndCompany(uniformId, user.getCompanyId()).flatMap(stock -> {
                stock.setStock(stock.getStock() - quantity);
                if (stock.getStock() < 0) {
                    return Mono.error(new BadRequestAlertException("Stock is not enough", "UniformStock", "stocknotenough"));
                }
                return uniformStockRepository.save(stock.setIsPersisted()).then();
            });
        });
    }

    /**
     * Get one uniformStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformStockDTO> findOne(UUID id) {
        log.debug("Request to get UniformStock : {}", id);
        return uniformStockRepository.findById(id).map(uniformStockMapper::toDto);
    }

    /**
     * Delete the uniformStock by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformStock : {}", id);
        return uniformStockRepository.deleteById(id);
    }

    public Mono<Void> calculateUniformStockCreate(List<UniformFormDetailDTO> uniformFormDetailDTO, String company, UUID id, UUID warehouseId) {
        return uniformFormDetailRepository.findByUniformOrder(id).switchIfEmpty(Mono.error(new BadRequestAlertException("detail not found", "UniformFormDetail", "NOT_FOUND"))).collectList().flatMap(details -> {
            var listUniformId = details.stream().map(UniformFormDetail::getUniformId).collect(Collectors.toList());
            return uniformStockRepository.getListUniformStockNotDeleteByListUniformId(listUniformId, company, warehouseId).collectList().flatMap(existingStocks -> {
                var stockMap = existingStocks.stream().collect(Collectors.toMap(stock -> stock.getUniformId(), stock -> stock));

                List<UniformStock> stocksToUpdate = new ArrayList<>();
                List<UniformStock> stocksToSave = new ArrayList<>();

                for (var detail : details) {
                    var uniformId = detail.getUniformId();
                    Integer quantity = detail.getQuantity();

                    UniformStock stock = stockMap.get(uniformId);
                    if (stock != null) {
                        // Cập nhật số lượng tồn kho hiện tại
                        stock.setStock(stock.getStock() + quantity);
                        stock.setIsPersisted();
                        stocksToUpdate.add(stock);

                    } else {
                        // Tạo mới mục tồn kho
                        stock = new UniformStock();
                        stock.setUniformId(detail.getUniformId());
                        stock.setStock(quantity);
                        stocksToSave.add(stock);
                    }
                }

                return uniformStockRepository.saveAll(stocksToSave).then(uniformStockRepository.saveAll(stocksToUpdate).then());
            });
        });
    }

    public Mono<Void> calculateUniformStockCreateWithOrderStock(String company, UUID id, UUID warehouseId) {
        return uniformFormDetailRepository.findByUniformStockOrderNotDelete(id, company).switchIfEmpty(Mono.error(new BadRequestAlertException("detail not found", "UniformFormDetail", "NOT_FOUND"))).collectList().flatMap(details -> {
            var listUniformId = details.stream().map(UniformFormDetail::getUniformId).collect(Collectors.toList());
            return uniformStockRepository.getListUniformStockNotDeleteByListUniformId(listUniformId, company, warehouseId).collectList().flatMap(existingStocks -> {
                var stockMap = existingStocks.stream().collect(Collectors.toMap(stock -> stock.getUniformId(), stock -> stock));

                List<UniformStock> stocksToUpdate = new ArrayList<>();
                List<UniformStock> stocksToSave = new ArrayList<>();

                for (var detail : details) {
                    var uniformId = detail.getUniformId();
                    Integer quantity = detail.getQuantity();

                    UniformStock stock = stockMap.get(uniformId);
                    if (stock != null) {
                        // Cập nhật số lượng tồn kho hiện tại
                        stock.setStock(stock.getStock() + quantity);
                        stock.setIsPersisted();
                        stocksToUpdate.add(stock);

                    } else {
                        // Tạo mới mục tồn kho
                        stock = new UniformStock();
                        stock.setUniformId(detail.getUniformId());
                        stock.setStock(quantity);
                        stock.setWarehouseId(warehouseId);
                        stocksToSave.add(stock);
                    }
                }

                return uniformStockRepository.saveAll(stocksToSave).then(uniformStockRepository.saveAll(stocksToUpdate).then());
            });
        });
    }

    public Flux<UniformStockDTO> findAllByQuery(Pageable pageable, String company, String status) {
        return uniformStockRepository.findAllByQuery(pageable, company, status).map(uniformStockMapper::toDto).flatMap(stock -> {
            return logisticClient.getWarehouseById(stock.getWarehouseId()).map(warehouse -> {
                stock.setWarehouseName(warehouse.getName());
                return stock;
            });
        }).collectList().flatMap(t -> {
            var uomIds = t.stream().map(s -> s.getUniform().getUomId()).toList();
            return logisticClient.getUomByListIds(uomIds).collectList().map(emps -> {
                var empMap = emps.stream().collect(Collectors.toMap(UomDTO::getId, Function.identity()));
                t.forEach(c -> {
                    c.getUniform().setUomDTO(empMap.get(c.getUniform().getUomId()));
                });
                return t;
            });
        }).flatMapMany(Flux::fromIterable);
    }

    public Mono<Long> countAllByQuery(String company, String status) {
        return uniformStockRepository.countAllByQuery(company, status);
    }

}
