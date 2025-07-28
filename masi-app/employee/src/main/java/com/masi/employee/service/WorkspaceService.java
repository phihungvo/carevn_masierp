package com.masi.employee.service;

import com.masi.employee.domain.Workspace;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.EmployeeRepository;
import com.masi.employee.repository.WorkspaceRepository;
import com.masi.employee.service.dto.WorkspaceDTO;
import com.masi.employee.service.dto.WorkspaceRO;
import com.masi.employee.service.mapper.WorkspaceMapper;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.Workspace}.
 */
@Service
@Transactional
public class WorkspaceService {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceService.class);

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final EmployeeRepository employeeRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceMapper workspaceMapper, EmployeeRepository employeeRepository) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMapper = workspaceMapper;
        this.employeeRepository = employeeRepository;
    }

    public Mono<WorkspaceDTO> findOneIdByNameAndCompany(String name, String company) {
        return workspaceRepository.findFirstByNameAndCompany(name, company).map(workspaceMapper::toDto)
            .switchIfEmpty(
                Mono.defer(() -> {
                    var newWorkspace = new Workspace();
                    newWorkspace.setName(name);
                    newWorkspace.setId(UUID.randomUUID());
                    newWorkspace.setCompany(company);
                    newWorkspace.setCreatedAt(ZonedDateTime.now());
                    newWorkspace.setLastUpdated(ZonedDateTime.now());
                    newWorkspace.setIsActive(true);
                    newWorkspace.setWorkspaceType(WorkspaceType.OFFICE);
                    return workspaceRepository.save(newWorkspace).map(workspaceMapper::toDto);
                }));

    }

/*    @Scheduled(fixedRate = 600000)
    public Mono<Void> reCalculateWorkspaceSlug() {
        return workspaceRepository.findAll().flatMap(workspace -> {
            if (StringUtils.isBlank(workspace.getNormalizedName())) {
                workspace.setNormalizedName(com.carevn.masi.utils.StringUtils.normalizeName(workspace.getName()));
                return workspaceRepository.save(workspace.setIsPersisted()).then();
            }
            return Mono.empty();
        }).then();
   }*/

    /**
     * Save a workspace.
     *
     * @param workspace the entity to save.
     * @return the persisted entity.
     */
    private Mono<Workspace> save(Workspace workspace) {
        log.debug("Save Workspace : {}", workspace);
        workspace.setId(UUID.randomUUID());
        workspace.setCreatedAt(ZonedDateTime.now());
        workspace.setLastUpdated(ZonedDateTime.now());
        workspace.setIsActive(true);
        return workspaceRepository.save(workspace);
    }

    /**
     * Update a workspace.
     *
     * @param workspace the entity to save.
     * @return the persisted entity.
     */
    private Mono<Workspace> update(Workspace workspace) {
        log.debug("Update Workspace : {}", workspace);
        workspace.setLastUpdated(ZonedDateTime.now());
        workspace.setIsPersisted();
        return workspaceRepository.save(workspace);
    }

    private Mono<Workspace> reActivate(Workspace workspace) {
        log.debug("Re-activate Workspace : {}", workspace);
        workspace.setIsActive(true);
        return update(workspace);
    }

    public Mono<WorkspaceDTO> create(WorkspaceDTO workspaceDTO) {
        log.debug("Request to create Workspace : {}", workspaceDTO);
        return workspaceRepository.existsByNameAndCompanyAndIsActiveTrue(workspaceDTO.getName(), workspaceDTO.getCompany()).flatMap(exists -> {
            if (Boolean.TRUE.equals(exists)) {
                return Mono.error(new BadRequestAlertException("Workspace already exists", "workspace", "workspaceexists"));
            } else {
                var entity = workspaceMapper.toEntity(workspaceDTO);
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setLastUpdated(ZonedDateTime.now());
                return save(entity).map(workspaceMapper::toDto);
            }
        });
    }

    /**
     * Partially update a workspace.
     *
     * @param workspaceDto the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkspaceDTO> partialUpdate(WorkspaceDTO workspaceDto) {
        log.debug("Request to partially update Workspace : {}", workspaceDto);
        return workspaceRepository.existsByIdAndIsActiveTrue(workspaceDto.getId())
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", "workspace", "idnotfound"));
                }
                return workspaceRepository.findById(workspaceDto.getId())
                    .flatMap(workspace -> update(workspace.partialUpdate(workspaceDto).setIsPersisted()).map(workspaceMapper::toDto));
            });
    }

    /**
     * Get all the workspaces.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkspaceDTO> getAllByQueryAndPaginate(Pageable pageable, WorkspaceRO ro) {
        log.debug("Request to get all Workspaces");
        return workspaceRepository.findAllActiveBy(pageable, ro).map(workspaceMapper::toDto);
    }

    /**
     * Returns the number of workspaces available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAllActive(WorkspaceRO ro) {
        return workspaceRepository.countAllActiveBy(ro);
    }

    /**
     * Get one workspace by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkspaceDTO> findOne(UUID id) {
        log.debug("Request to get Workspace : {}", id);
        return workspaceRepository.findByIdAndIsActiveTrue(id).map(workspaceMapper::toDto);
    }

    /**
     * Delete the workspace by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> deactivate(UUID id) {
        log.debug("Request to deactivate Workspace : {}", id);
        return workspaceRepository.findById(id).zipWith(employeeRepository.countAllByWorkspaceIdAndIsActiveIsTrue(id))
            .flatMap(tuple -> {
                if (tuple.getT2() > 0) {
                    return Mono.error(new BadRequestAlertException("Workspace has employees", "workspace", "workspacehasemployees"));
                }
                return workspaceRepository.findById(id)
                    .flatMap(workspace -> {
                        workspace.setIsActive(false);
                        workspace.setLastUpdated(ZonedDateTime.now());
                        return workspaceRepository.save(workspace).then();
                    });
            });
    }
}
