package com.masi.employee.service;


import com.masi.employee.domain.enumeration.InterviewProcess;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import com.masi.employee.repository.InterviewScheduleRepository;
import com.masi.employee.repository.RecruitmentRequestRepository;
import com.masi.employee.service.dto.InterviewScheduleDTO;
import com.masi.employee.service.dto.InterviewScheduleQuery;
import com.masi.employee.service.dto.request.ResultInterviewRequest;
import com.masi.employee.service.mapper.InterviewScheduleMapper;
import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InterviewScheduleService {
    private static final Logger log = LoggerFactory.getLogger(RecruitmentRequestService.class);
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final InterviewScheduleMapper interviewScheduleMapper;
    private final RecruitmentRequestRepository recruitmentRequestRepository;
    private final FileClient fileClient;

    public InterviewScheduleService(InterviewScheduleRepository interviewScheduleRepository, InterviewScheduleMapper interviewScheduleMapper, RecruitmentRequestRepository recruitmentRequestRepository, FileClient fileClient) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.interviewScheduleMapper = interviewScheduleMapper;
        this.recruitmentRequestRepository = recruitmentRequestRepository;
        this.fileClient = fileClient;
    }

    public Mono<InterviewScheduleDTO> saveInterview(InterviewScheduleDTO interviewScheduleDTO) {
        log.debug("Request to save InterviewScheduleDTO : {}", interviewScheduleDTO);
        return recruitmentRequestRepository
            .findById(interviewScheduleDTO.getRecruitmentRequestId())
            .flatMap((existingRecruitmentRequest) -> {
                var validStatus = List.of(RecruitmentStatus.APPROVED, RecruitmentStatus.WAITING_INTERVIEW);
                if (!validStatus.contains(existingRecruitmentRequest.getStatus())) {
                    return Mono.error(
                        new BadRequestAlertException("Failed to update RecruitmentRequest: Invalid status",
                            "recruitmentRequest", "invalidStatus"));
                }
                interviewScheduleDTO.setCreatedDate(ZonedDateTime.now());
                interviewScheduleDTO.setLastUpdated(ZonedDateTime.now());
                interviewScheduleDTO.setProcess(InterviewProcess.WAITING_INTERVIEW);
                interviewScheduleDTO.setIsDeleted(false);
                existingRecruitmentRequest.setLastUpdated(ZonedDateTime.now());
                existingRecruitmentRequest.setStatus(RecruitmentStatus.WAITING_INTERVIEW);
                return recruitmentRequestRepository.save(existingRecruitmentRequest)
                    .then(interviewScheduleRepository
                        .save(interviewScheduleMapper.toEntity(interviewScheduleDTO)))
                    .map(interviewScheduleMapper::toDto);
            }).switchIfEmpty(
                Mono.error(new BadRequestAlertException("Failed to update interviewScheduleDTO: Invalid status",
                    "interviewScheduleRequest", "invalidStatus")));
    }


    public Mono<Integer> updateResultInterview(UUID id, ResultInterviewRequest refusalOfReview) {
        return interviewScheduleRepository.findById(id)
            .flatMap(interviewSchedule -> {
                if (interviewSchedule.getProcess().equals(InterviewProcess.WAITING_INTERVIEW)) {
                    interviewSchedule.setProcess(InterviewProcess.INTERVIEWED);
                    interviewSchedule.setInterviewResult(refusalOfReview.getInterviewResult());
                    interviewSchedule.setRate(refusalOfReview.getRate());
                    interviewSchedule.setLastUpdated(ZonedDateTime.now());
                    interviewSchedule.setIsPersisted();
                    return interviewScheduleRepository.save(interviewSchedule)
                        .flatMap(savedInterviewSchedule -> autoUpdateRecruitmentRequest(savedInterviewSchedule.getRecruitmentRequestId())
                            .then(Mono.just(1)));
                }
                return Mono.error(new BadRequestAlertException(
                    "Cannot Update Review with status: " + interviewSchedule.getProcess(), "", ""));
            });
    }

    private Mono<Void> autoUpdateRecruitmentRequest(UUID id) {
        return interviewScheduleRepository.countByRecruitmentRequestIdAndIsDeletedIsFalseAndInterviewResult(id, InterviewResult.PASS)
            .zipWith(recruitmentRequestRepository.findById(id))
            .flatMap(tuple -> {
                var count = tuple.getT1();
                var recruitmentRequest = tuple.getT2();
                if (count >= recruitmentRequest.getQuantity()) {
                    recruitmentRequest.setStatus(RecruitmentStatus.COMPLETED);
                    recruitmentRequest.setLastUpdated(ZonedDateTime.now());
                    return recruitmentRequestRepository.save(recruitmentRequest).thenReturn(1);
                }
                return Mono.just(0);
            }).then();
    }

    private Flux<Integer> updateRecruimentRequest(UUID id) {
        return interviewScheduleRepository.findByIdAndProcess(id, InterviewProcess.WAITING_INTERVIEW)
            .flatMap(interviewSchedule -> Mono.just(1))
            .switchIfEmpty(
                recruitmentRequestRepository.findById(id)
                    .flatMap(recruitmentRequest -> {
                        recruitmentRequest.setLastUpdated(ZonedDateTime.now());
                        recruitmentRequest.setStatus(RecruitmentStatus.COMPLETED);
                        return recruitmentRequestRepository.save(recruitmentRequest).thenReturn(1);
                    })
                    .switchIfEmpty(Mono.just(0))
            );
    }


    public Flux<InterviewScheduleDTO> findAllByQuery(InterviewScheduleQuery query, Pageable pageable) {
        return interviewScheduleRepository.findByQuery(query, pageable).map(interviewScheduleMapper::toDto);
    }

    public Mono<Integer> countByRecruitmentPass(UUID query) {
        return interviewScheduleRepository.countByRecruitmentRequestIdAndIsDeletedIsFalse(query);
    }

    public Mono<Long> countByQuery(InterviewScheduleQuery query) {
        return interviewScheduleRepository.countByQuery(query);
    }

    public Mono<InterviewScheduleDTO> findById(UUID id) {
        return interviewScheduleRepository.findById(id).flatMap(e -> {
            var dto = interviewScheduleMapper.toDto(e);
            var fileId = StringUtils.isBlank(dto.getCvFile()) ? UUID.randomUUID() : UUID.fromString(dto.getCvFile());
            return fileClient.getFileAttachment(fileId).map(file -> {
                dto.setCvFileAttachment(file);
                return dto;
            }).switchIfEmpty(Mono.just(dto));
        });
    }

    public Mono<InterviewScheduleDTO> partialUpdate(InterviewScheduleDTO interviewScheduleDTO) {
        return interviewScheduleRepository.existsById(interviewScheduleDTO.getId())
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", "interviewSchedule", "idnotfound"));
                }
                return interviewScheduleRepository.findById(interviewScheduleDTO.getId())
                    .flatMap(interviewSchedule -> {
                        interviewScheduleMapper.partialUpdate(interviewSchedule, interviewScheduleDTO);
                        if (StringUtils.isBlank(interviewScheduleDTO.getCvFile())) {
                            interviewSchedule.setCvFile(null);
                        }
                        interviewSchedule.setIsPersisted();
                        interviewSchedule.setLastUpdated(ZonedDateTime.now());
                        return interviewScheduleRepository.save(interviewSchedule).map(interviewScheduleMapper::toDto);
                    });
            });
    }

    public Mono<Void> delete(UUID id) {
        return interviewScheduleRepository.findById(id)
            .flatMap(interviewSchedule -> {
                interviewSchedule.setIsDeleted(true);
                interviewSchedule.setLastUpdated(ZonedDateTime.now());
                return interviewScheduleRepository.save(interviewSchedule).then();
            });
    }
}
