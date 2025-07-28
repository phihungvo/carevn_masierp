package com.masi.logistics.service;

import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.criteria.DeliveryScheduleCriteria;
import com.masi.logistics.repository.DeliveryDetailRepository;
import com.masi.logistics.repository.DeliveryScheduleRepository;
import com.masi.logistics.service.dto.CreateDeliveryDetailDto;
import com.masi.logistics.service.dto.DeliveryScheduleDTO;
import com.masi.logistics.service.mapper.DeliveryScheduleMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.DeliverySchedule}.
 */
@Service
@Transactional
public class DeliveryScheduleService {


    private static final Logger LOG = LoggerFactory.getLogger(DeliveryScheduleService.class);

    private final DeliveryScheduleRepository deliveryScheduleRepository;

    private final DeliveryScheduleMapper deliveryScheduleMapper;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final RequestApprovalService requestApprovalService;

    public DeliveryScheduleService(DeliveryScheduleRepository deliveryScheduleRepository, DeliveryScheduleMapper deliveryScheduleMapper, DocumentCodeSequenceService documentCodeSequenceService, DeliveryDetailRepository deliveryDetailRepository, RequestApprovalService requestApprovalService) {
        this.deliveryScheduleRepository = deliveryScheduleRepository;
        this.deliveryScheduleMapper = deliveryScheduleMapper;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.deliveryDetailRepository = deliveryDetailRepository;
        this.requestApprovalService = requestApprovalService;
        Mono.delay(Duration.ofMinutes(3)).then(documentCodeSequenceService.makeSureDocumentCodeSequenceExist(DeliverySchedule.ENTITY_NAME,"DC-%5d")).subscribe();// code ngu vcl
    }

    public Mono<Void> setStatus(UUID id, DeliverySchedule.Status status) {
        return deliveryScheduleRepository.findById(id)
            .flatMap(deliverySchedule -> {
                deliverySchedule.setStatus(status);
                return deliveryScheduleRepository.save(deliverySchedule.setIsPersisted()).then();
            });
    }

    /**
     * Save a deliverySchedule.
     *
     * @param deliveryScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryScheduleDTO> save(DeliveryScheduleDTO deliveryScheduleDTO) {
        return
            documentCodeSequenceService.getByDocumentType(DeliverySchedule.ENTITY_NAME)
                .flatMap(documentCodeSequence -> {
                    var entity = deliveryScheduleMapper.toEntity(deliveryScheduleDTO);
                    entity.setCode(documentCodeSequence.getNextAndIncrement());
                    return documentCodeSequenceService.updateSequence(documentCodeSequence)
                        .then(deliveryScheduleRepository.save(entity).map(deliveryScheduleMapper::toDto));
                }).flatMap(savedEntity -> {
                    var createDeliveryDetails = deliveryScheduleDTO.getCreateDeliveryDetails().stream().map(createDeliveryDetailDto -> {
                        return createDeliveryDetailDto.toEntity(savedEntity.getId());
                    }).toList();
                    return deliveryDetailRepository.saveAll(createDeliveryDetails).then(Mono.just(savedEntity));
                });

    }


    public Mono<DeliveryScheduleDTO> partialUpdate(DeliveryScheduleDTO deliveryScheduleDTO) {
        LOG.debug("Request to partially update DeliverySchedule : {}", deliveryScheduleDTO);

        return deliveryScheduleRepository
            .findById(deliveryScheduleDTO.getId())
            .<DeliverySchedule>handle((existingDeliverySchedule, sink) -> {
                var notAllowedUpdateStatus = List.of(DeliverySchedule.Status.REJECTED, DeliverySchedule.Status.APPROVED, DeliverySchedule.Status.DELIVERED);
                if (notAllowedUpdateStatus.contains(existingDeliverySchedule.getStatus())) {
                    sink.error(new BadRequestAlertException("DeliverySchedule status is not allowed to be updated", "DeliverySchedule", "statusNotAllowed"));
                    return;
                }
                deliveryScheduleDTO.applyChangeToEntity(existingDeliverySchedule);
                sink.next(existingDeliverySchedule.setIsPersisted());
            })
            .flatMap(deliveryScheduleRepository::save)
            .flatMap(savedDeliverySchedule -> {
                var createDeliveryDetails = deliveryScheduleDTO.getCreateDeliveryDetails().stream().map(createDeliveryDetailDto -> {
                    return createDeliveryDetailDto.toEntity(savedDeliverySchedule.getId());
                }).toList();
                return deliveryDetailRepository.deleteByDeliveryId(savedDeliverySchedule.getId())
                    .thenMany(deliveryDetailRepository.saveAll(createDeliveryDetails))
                    .then(Mono.just(savedDeliverySchedule));
            })
            .map(deliveryScheduleMapper::toDto);
    }

    public Mono<DeliveryScheduleDTO> markAsDelivered(UUID id) {
        return deliveryScheduleRepository.findById(id)
            .flatMap(deliverySchedule -> {
                deliverySchedule.setStatus(DeliverySchedule.Status.DELIVERED);
                // toDo: public a message to the message broker
                return deliveryScheduleRepository.save(deliverySchedule.setIsPersisted()).map(deliveryScheduleMapper::toDto);
            });
    }

    /**
     * Find deliverySchedules by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<DeliveryScheduleDTO>> findByCriteria(DeliveryScheduleCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all DeliverySchedules by Criteria");
        return deliveryScheduleRepository.findByCriteria(criteria, pageable).map(deliveryScheduleMapper::toDto).collectList().flatMap(deliveryScheduleDTO -> {
            var map = new HashMap<UUID, DeliveryScheduleDTO>();
            deliveryScheduleDTO.forEach(deliverySchedule -> {
                map.put(deliverySchedule.getId(), deliverySchedule);
            });
            return requestApprovalService.findByDocumentIdIn(map.keySet()).collectList().map(deliveryDetails -> {
                deliveryDetails.forEach(requestApprovalDTO -> {
                    map.get(requestApprovalDTO.getDocumentId()).addRequestApproval(requestApprovalDTO);
                });
                return deliveryScheduleDTO;
            }).then(Mono.just(deliveryScheduleDTO));

        });
    }

    /**
     * Find the count of deliverySchedules by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of deliverySchedules
     */
    public Mono<Long> countByCriteria(DeliveryScheduleCriteria criteria) {
        LOG.debug("Request to get the count of all DeliverySchedules by Criteria");
        return deliveryScheduleRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of deliverySchedules available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return deliveryScheduleRepository.count();
    }

    /**
     * Get one deliverySchedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DeliveryScheduleDTO> findOne(UUID id) {
        LOG.debug("Request to get DeliverySchedule : {}", id);
        return deliveryScheduleRepository.findByIdAndDeletedAtIsNull(id).map(deliveryScheduleMapper::toDto).flatMap(deliveryScheduleDTO -> {
            return deliveryDetailRepository.findAllByDeliveryId(deliveryScheduleDTO.getId()).collectList().map(deliveryDetails -> {
                deliveryScheduleDTO.setCreateDeliveryDetails(deliveryDetails.stream().map(CreateDeliveryDetailDto::new).toList());
                return deliveryScheduleDTO;
            }).then(Mono.just(deliveryScheduleDTO));
        });
    }

    /**
     * Delete the deliverySchedule by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete DeliverySchedule : {}", id);
        return deliveryScheduleRepository.findById(id)
            .flatMap(deliverySchedule -> {
                if (deliverySchedule.getStatus() == DeliverySchedule.Status.APPROVED) {
                    return Mono.error(new BadRequestAlertException("DeliverySchedule status is not allowed to be deleted", "DeliverySchedule", "statusNotAllowed"));
                }
                return deliverySchedule.deleteAsync().then(deliveryScheduleRepository.save(deliverySchedule.setIsPersisted())).then();
            });
    }

}
