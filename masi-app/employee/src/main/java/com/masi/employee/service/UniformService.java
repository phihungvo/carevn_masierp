package com.masi.employee.service;

import com.masi.employee.domain.DocumentSequence;
import com.masi.employee.repository.DocumentSequenceRepository;
import com.masi.employee.repository.UniformRepository;
import com.masi.employee.service.dto.UniformDTO;
import com.masi.employee.service.dto.UniformQuery;
import com.masi.employee.domain.Uniform;
import com.masi.employee.service.mapper.UniformMapper;

import java.text.Normalizer;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Uniform}.
 */
@Service
@Transactional
public class UniformService {

    private String entity_name = "masiEmployeeUniform";

    private static final Logger log = LoggerFactory.getLogger(UniformService.class);

    private final UniformRepository uniformRepository;

    private final UniformMapper uniformMapper;

    private final DocumentSequenceRepository documentSequenceRepository;

    public UniformService(UniformRepository uniformRepository, UniformMapper uniformMapper, DocumentSequenceRepository documentSequenceRepository) {
        this.uniformRepository = uniformRepository;
        this.uniformMapper = uniformMapper;
        this.documentSequenceRepository = documentSequenceRepository;
    }

    /**
     * Save a uniform.
     *
     * @param name the entity to save.
     * @return the persisted entity.
     */
    public static String convertName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        // Bỏ dấu
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");

        // In hoa
        String upperCaseName = withoutAccents.toUpperCase(Locale.ROOT);

        // Thay thế khoảng trắng bằng dấu gạch dưới
        return upperCaseName.replaceAll("\\s+", "_");
    }


    public static void main(String[] args) {
        System.out.println(convertName("QUAN THUN CO MAU0005"));
    }

    private Mono<DocumentSequence> checkDocumentSequence(String company) {
        log.debug("Request to check and save entity_name : {}", entity_name);
        return documentSequenceRepository.findByEntityNameAndCompanyAndIsActiveIsTrue(entity_name, company)
            .switchIfEmpty(
                Mono.defer(() -> {
                    DocumentSequence documentSequence = new DocumentSequence();
                    documentSequence.setEntityName(entity_name);
                    documentSequence.setCompany(company);
                    documentSequence.setCurrentSequence(1);
                    documentSequence.setIsActive(true);
                    return documentSequenceRepository.save(documentSequence);
                })
            );
    }


    public Mono<UniformDTO> save(UniformDTO uniformDTO, String company) {
        log.debug("Request to save Uniform : {}", uniformDTO);

        return this.checkDocumentSequence(company)
            .flatMap(document -> {
                String numberCode = String.format("%04d", document.getCurrentSequence());
                String code = convertName(uniformDTO.getName()) + "_" + numberCode;
                uniformDTO.setCode(code);
                return uniformRepository.save(uniformMapper.toEntity(uniformDTO))
                    .flatMap(uniform -> {
                        return documentSequenceRepository
                            .UpdateByEntityNameAndCompanyAndIsActiveIsTrue(document.getEntityName(), company)
                            .thenReturn(uniform);
                    });
            })
            .map(uniformMapper::toDto);
    }


    /**
     * Partially update a uniform.
     *
     * @param uniformDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformDTO> partialUpdate(UniformDTO uniformDTO) {
        log.debug("Request to partially update Uniform : {}", uniformDTO);

        return uniformRepository
            .findById(uniformDTO.getId())
            .map(existingUniform -> {
                uniformDTO.applyUpdate(existingUniform);
                existingUniform.setIsPersisted();
                return existingUniform;
            })
            .flatMap(uniformRepository::save)
            .map(Uniform::toDto);
    }

    /**
     * Get all the uniforms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformDTO> findAllByCompanyAndIsDeletedIsFalse(Pageable pageable, UniformQuery query) {
        log.debug("Request to get all Uniforms");
        return uniformRepository.findAllByFilter(pageable, query).map(Uniform::toDto);
    }

    /**
     * Returns the number of uniforms available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll(UniformQuery query) {
        return uniformRepository.countByFilter(query);
    }

    /**
     * Get one uniform by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformDTO> findOne(UUID id) {
        log.debug("Request to get Uniform : {}", id);
        return uniformRepository.findById(id).map(Uniform::toDto);
    }

    /**
     * Delete the uniform by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id, UUID empId) {
        log.debug("Request to delete Uniform : {}", id);
        return uniformRepository.findById(id).flatMap(uniform -> {
            uniform.setDeleteAt(ZonedDateTime.now());

            if (String.valueOf(empId).equals("") || empId == null)
                uniform.setDeleteBy("System");
            else
                uniform.setDeleteBy(String.valueOf(empId));

            uniform.setIsPersisted();
            return uniformRepository.save(uniform).then();
        });
    }

    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Uniform : {}", id);
        return uniformRepository.deleteById(id);
    }

    public Mono<Void> setStatus(UUID id, String status) {
        log.debug("Request to set status Uniform : {}", id);
        return uniformRepository.findById(id).flatMap(uniform -> {
            uniform.setStatus(status);
            uniform.setUpdateAt(ZonedDateTime.now());
            uniform.setIsPersisted();
            return uniformRepository.save(uniform).then();
        });
    }
}
