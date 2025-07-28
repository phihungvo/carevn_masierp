package com.masi.employee.service;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.repository.UniformReleaseRepository;
import com.masi.employee.repository.UniformReturnRepository;
import com.masi.employee.service.dto.UniformReturnDTO;
import com.masi.employee.service.mapper.UniformReturnMapper;

import java.time.ZonedDateTime;
import java.util.*;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
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
 * {@link com.masi.employee.domain.UniformReturn}.
 */
@Service
@Transactional()
public class UniformReturnService {

    private static final Logger log = LoggerFactory.getLogger(UniformReturnService.class);

    private final UniformReturnRepository uniformReturnRepository;

    private final UniformReleaseRepository uniformReleaseRepository;

    private UniformReleaseService uniformReleaseService;

    private final UniformFormDetailRepository uniformFormDetailRepository;

    private final UniformReturnMapper uniformReturnMapper;

    public final UniformStockService uniformStockService;

    @Autowired
    public void setUniformReleaseService(UniformReleaseService uniformReleaseService) {
        this.uniformReleaseService = uniformReleaseService;
    }

    @Lazy
    public UniformReturnService(UniformReturnRepository uniformReturnRepository,
                                UniformReleaseRepository uniformReleaseRepository, UniformReleaseService uniformReleaseService,
                                UniformReturnMapper uniformReturnMapper, UniformFormDetailRepository uniformFormDetailRepository, UniformStockService uniformStockService) {
        this.uniformReturnRepository = uniformReturnRepository;
        this.uniformReturnMapper = uniformReturnMapper;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformReleaseRepository = uniformReleaseRepository;
        this.uniformReleaseService = uniformReleaseService;
        this.uniformStockService = uniformStockService;
    }


    public Mono<UniformReturn> fastReturn(UniformReturnDTO uniformReturnDTO) {
        var entity = uniformReturnMapper.toEntity(uniformReturnDTO);
        entity.setUniformReleaseId(uniformReturnDTO.getUniformReleaseId());
        entity.setId(UUID.randomUUID());
        var listDetailEntity = Arrays.stream(uniformReturnDTO.getReturnDetails())
            .map(detail -> UniformFormDetail.builder()
                .quantity(detail.quantity())
                .uniformId(detail.uniformId())
                .uniformReturnId(entity.getId())
                .build()
            ).toList();
        return uniformReturnRepository.save(entity)// lưu thông tin hoàn ứng
            .flatMap(savedEntity -> {
                var updateStockFlux = Flux.fromIterable(listDetailEntity)
                    .flatMap(detail -> {
                        return uniformStockService.returnUniform(detail.getUniformId(), detail.getQuantity());
                    }); // cập nhật số lượng trong kho
                return uniformFormDetailRepository // lưu thông tin chi tiết hoàn ứng
                    .saveAll(listDetailEntity)
                    .collectList().zipWith(updateStockFlux.collectList())
                    .map(list -> savedEntity);
            })
            ;
    }

    public Mono<UniformReturnDTO> save(UniformReturnDTO uniformReturnDTO) {
        log.debug("Request to save UniformReturn : {}", uniformReturnDTO);
        var entity = uniformReturnMapper.toEntity(uniformReturnDTO);
        // tìm xuất ứng gần nhất của nhân viên
        var releaseBeforeMono = uniformReleaseRepository.findLatestReleaseForEmployee(entity.getEmployeeId()).switchIfEmpty(Mono.error(new BadRequestAlertException("No release found for employee", "uniformRelease", "notfound")));

        var saveMono = releaseBeforeMono.flatMap(releaseBefore -> {
            releaseBefore.setIsReturned(true); // đánh dấu đã hoàn ứng
            releaseBefore.setIsPersisted();
            var listDetailEntity = Arrays.stream(uniformReturnDTO.getReturnDetails())
                .map(detail -> UniformFormDetail.builder()
                    .quantity(uniformReturnDTO.getQuantity())
                    .uniformId(detail.uniformId())
                    .uniformReturnId(entity.getId())
                    .build()
                ).toList();
            return uniformReleaseRepository.save(releaseBefore) // lưu lại thông tin xuất ứng
                .flatMap(release -> {
                    return uniformReturnRepository.save(entity)// lưu thông tin hoàn ứng
                        .flatMap(savedEntity -> {
                            var updateStockFlux = Flux.fromIterable(listDetailEntity)
                                .flatMap(detail -> {
                                    return uniformStockService.returnUniform(detail.getUniformId(), detail.getQuantity());
                                }); // cập nhật số lượng trong kho
                            return uniformFormDetailRepository // lưu thông tin chi tiết hoàn ứng
                                .saveAll(listDetailEntity)
                                .collectList().zipWith(updateStockFlux.collectList())
                                .map(list -> savedEntity);
                        });
                });
        });


        return saveMono.map(uniformReturnMapper::toDto);
    }

    public Mono<UniformReturnDTO> saveV2(UniformReturnDTO uniformReturnDTO) {
        var entity = uniformReturnMapper.toEntity(uniformReturnDTO);
        // tìm xuất ứng gần nhất của nhân viên
        if (uniformReturnDTO.getReturnDetails() == null || uniformReturnDTO.getReturnDetails().length == 0) {
            return Mono.error(new BadRequestAlertException("No return detail found", "uniformReturn", "noReturnDetail"));
        }
        var wantReturnId = uniformReturnDTO.getReturnDetails()[0].uniformId();
        var releaseBeforeMono = uniformReleaseRepository.findOldestReleaseForEmployee(entity.getEmployeeId(), wantReturnId).switchIfEmpty(Mono.error(new BadRequestAlertException("No release found for employee", "uniformRelease", "notfound")));

        var saveMono = releaseBeforeMono.collectList().flatMap(releaseBefores -> {
            // thac nuước
            List<UniformRelease> edited = new ArrayList<>();
            int total = uniformReturnDTO.calTotalQuantity();
            uniformReturnDTO.setQuantity(total);
            entity.setQuantity(total);
            for (UniformRelease releaseBefore : releaseBefores) {
                if (total <= 0) break;
                int quantity = releaseBefore.getRemaining();
                if (quantity <= 0) continue;
                int returnAmount = Math.min(quantity, total);
                releaseBefore.setRemaining(quantity - returnAmount);
                if (releaseBefore.getRemaining() == 0) {
                    releaseBefore.setIsReturned(true);
                }
                edited.add(releaseBefore.setIsPersisted());
                total -= returnAmount;
            }
            if (total > 0) {
                log.error("Return too much {}", total);
                return Mono.error(new BadRequestAlertException("Not enough uniform to return", "uniformRelease", "returnTooMuch"));
            }


            var listDetailEntity = Arrays.stream(uniformReturnDTO.getReturnDetails())
                .map(detail -> UniformFormDetail.builder()
                    .quantity(uniformReturnDTO.getQuantity())
                    .uniformId(detail.uniformId())
                    .uniformReturnId(entity.getId())
                    .build()
                ).toList();

            return uniformReleaseRepository.saveAll(edited)
                .collectList()
                .flatMap(release -> {
                    return uniformReturnRepository.save(entity)// lưu thông tin hoàn ứng
                        .flatMap(savedEntity -> {
                            var updateStockFlux = Flux.fromIterable(listDetailEntity)
                                .flatMap(detail -> {
                                    return uniformStockService.returnUniform(detail.getUniformId(), detail.getQuantity());
                                }); // cập nhật số lượng trong kho
                            return uniformFormDetailRepository //à lưu thông tin chi tiết hon ứng
                                .saveAll(listDetailEntity)
                                .collectList().zipWith(updateStockFlux.collectList())
                                .map(list -> savedEntity);
                        });
                });
        });


        return saveMono.map(uniformReturnMapper::toDto);
    }

    public Mono<UniformReturnDTO> saveV3(UniformReturnDTO uniformReturnDTO) {
        log.debug("Request to savev3 UniformReturn : {}", uniformReturnDTO);
        var entity = uniformReturnMapper.toEntity(uniformReturnDTO);
        entity.setUniformReleaseId(uniformReturnDTO.getUniformReleaseId());
        // tìm đơn xuất ứng tương ứng
        var listDetailEntity = Arrays.stream(uniformReturnDTO.getReturnDetails())
            .map(detail -> UniformFormDetail.builder()
                .quantity(detail.quantity())
                .uniformId(detail.uniformId())
                .uniformReturnId(entity.getId())
                .build()
            ).toList();
        return uniformReleaseRepository
            .findNotDeletedById(entity.getUniformReleaseId(), entity.getCompany())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("No release found for employee", "uniformRelease", "notfound"))).flatMap(uniformRelease -> {
                // tìm đơn chi tiết xuất ứng
                return uniformFormDetailRepository.findByUniformReleaseNotDelete(entity.getUniformReleaseId(),entity.getCompany()).collectList().flatMap(uniformReleaseDetails -> {
                    // update list detail
                    for(var item : uniformReleaseDetails){
                    var returnedQuantity =  (item.getQuantityChange() != null ? item.getQuantityChange() : 0);
                        var detailItem = listDetailEntity.stream().filter(detail -> detail.getUniformId().equals(item.getUniformId())).findFirst().orElse(null);
                        if(detailItem != null){
                            var newQuantity = returnedQuantity + detailItem.getQuantity();
                            if(newQuantity > item.getQuantity()){
                                // return an error:
                                return Mono.error(new BadRequestAlertException("Return too much", "uniformRelease", "returnTooMuch"));
                            }
                            item.setQuantityChange(newQuantity);
                            item.setUpdateAt(ZonedDateTime.now());
                            item.setUpdateBy(uniformReturnDTO.getCreateBy());
                            item.setIsPersisted();
                        }
                    }
                    var isReturnAll = uniformReleaseDetails.stream().allMatch(detail -> detail.getQuantity().equals(detail.getQuantityChange()));
                    if(isReturnAll){
                        uniformRelease.setRemaining(0);
                        uniformRelease.setIsReturned(true);
                    }
                    else{
                        var sumReturned = uniformReleaseDetails.stream().mapToInt(UniformFormDetail::getQuantityChange).sum();
                        uniformRelease.setRemaining(uniformRelease.getQuantity() - sumReturned);
                    }
                    uniformRelease.setUpdateAt(ZonedDateTime.now());
                    uniformRelease.setUpdateBy(uniformReturnDTO.getCreateBy());
                    uniformRelease.setIsPersisted();
                    // save return, return detail and update release, release detail
                    return uniformReleaseRepository.save(uniformRelease)
                        .flatMap(savedRelease -> {
                            return uniformReturnRepository.save(entity)
                                .flatMap(savedEntity -> {
                                    var updateStockFlux = Flux.fromIterable(listDetailEntity)
                                        .flatMap(detail -> {
                                            return uniformStockService.returnUniform(detail.getUniformId(), detail.getQuantity());
                                        });
                                    return uniformFormDetailRepository.saveAll(listDetailEntity)
                                        .collectList().zipWith(updateStockFlux.collectList())
                                        .map(list -> savedEntity).zipWith(
                                            uniformFormDetailRepository.saveAll(uniformReleaseDetails).collectList()
                                        ).then(Mono.just(savedEntity).map(uniformReturnMapper::toDto));
                                });
                        });
                });
            });

    }

    public Mono<Long> countTotalRemainingUniform(UUID employeeId, UUID uniformId) {
        return uniformReleaseRepository.countTotalRemaining(employeeId, uniformId);
    }

    /**
     * Update a uniformReturn.
     *
     * @param uniformReturnDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformReturnDTO> update(UniformReturnDTO uniformReturnDTO) {
        log.debug("Request to update UniformReturn : {}", uniformReturnDTO);
        return uniformReturnRepository
            .save(uniformReturnMapper.toEntity(uniformReturnDTO).setIsPersisted())
            .map(uniformReturnMapper::toDto);
    }

    /**
     * Partially update a uniformReturn.
     *
     * @param uniformReturnDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformReturnDTO> partialUpdate(UniformReturnDTO uniformReturnDTO) {
        log.debug("Request to partially update UniformReturn : {}", uniformReturnDTO);

        return uniformReturnRepository
            .findById(uniformReturnDTO.getId())
            .map(existingUniformReturn -> {
                uniformReturnMapper.partialUpdate(existingUniformReturn, uniformReturnDTO);

                return existingUniformReturn;
            })
            .flatMap(uniformReturnRepository::save)
            .map(uniformReturnMapper::toDto);
    }

    /**
     * Get all the uniformReturns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformReturnDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformReturns");
        return uniformReturnRepository.findAllBy(pageable).map(uniformReturnMapper::toDto);
    }

    /**
     * Returns the number of uniformReturns available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return uniformReturnRepository.count();
    }

    /**
     * Get one uniformReturn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformReturnDTO> findOne(UUID id) {
        log.debug("Request to get UniformReturn : {}", id);
        return uniformReturnRepository.findById(id).map(uniformReturnMapper::toDto);
    }

    /**
     * Delete the uniformReturn by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformReturn : {}", id);
        return uniformReturnRepository.deleteById(id);
    }
}
