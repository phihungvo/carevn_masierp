package com.masi.employee.service;

import com.masi.employee.domain.EmployeeIdSequence;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.repository.EmployeeIdSequenceRepository;
import com.masi.employee.service.dto.EmployeeIdSequenceDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.EmployeeIdSequence}.
 */
@Service
@Transactional
public class EmployeeIdSequenceService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeIdSequenceService.class);

    private final EmployeeIdSequenceRepository employeeIdSequenceRepository;

    public EmployeeIdSequenceService(EmployeeIdSequenceRepository employeeIdSequenceRepository) {
        this.employeeIdSequenceRepository = employeeIdSequenceRepository;
    }


    @Transactional(readOnly = true)
    public Flux<EmployeeIdSequenceDTO> findAll(Gender gender, String workspaceId) {
        log.debug("Request to get all EmployeeIdSequences");
        if (gender == null)
            return employeeIdSequenceRepository.findAllByWorkspaceId(workspaceId).map(EmployeeIdSequence::toDTO);
        return employeeIdSequenceRepository.findFirstByGenderAndWorkspaceId(gender, workspaceId).flux().map(EmployeeIdSequence::toDTO);
    }

    public Mono<String> getNextAndIncreaseOld(Gender gender, String workspaceId) {
        return employeeIdSequenceRepository.getAndIncrease(gender, workspaceId).map(sequence -> String.format("%s%04d", gender.name().charAt(0), sequence));
    }

    public Mono<String> getNextAndIncrease(Gender gender, String workspaceId) {
        return employeeIdSequenceRepository.getFirstByGenderAndWorkspace(gender, workspaceId).flatMap(employeeIdSequence -> {
            employeeIdSequence.setCurrentSequence(employeeIdSequence.getCurrentSequence() + 1);
            var javaFormat = employeeIdSequence.getJavaFormat();
            var nextCode = String.format(StringUtils.isBlank(javaFormat) ? "%s%04d" : javaFormat, employeeIdSequence.getCurrentSequence());
            return employeeIdSequenceRepository.save(employeeIdSequence.setIsPersisted()).thenReturn(nextCode);
        });
    }

    public Mono<List<EmployeeIdSequence>> findAll() {
        return employeeIdSequenceRepository.findAll().collectList();
    }

    public Mono<Void> saveAll(List<EmployeeIdSequence> employeeIdSequences) {
        return employeeIdSequenceRepository.saveAll(employeeIdSequences).then();
    }
}
