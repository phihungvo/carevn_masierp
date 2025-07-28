package com.masi.employee.service;

import com.carevn.masi.dto.EmployeeChangeEvent;
import com.carevn.masi.utils.DocxUtils;
import com.carevn.masi.utils.FileManager;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.*;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.dto.reponse.ListEmployeeDepartmentDTOReponse;
import com.masi.employee.service.mapper.EmployeeProfileMapper;
import com.masi.employee.service.web.client.AuthClient;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing
 * {@link EmployeeProfile}.
 */
@Service
@Transactional
public class EmployeeProfileService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeProfileService.class);

    private final EmployeeProfileRepository employeeProfileRepository;

    private final EmployeeProfileMapper employeeProfileMapper;
    private final ProfileAttachmentService profileAttachmentService;
    private final EmployeeIdSequenceService employeeIdSequenceService;
    private final FileManager contractFileManager;
    private final TimeKeepingService timeKeepingService;
    private final FileManager exportFileManager;
    private final EmployeeRepository employeeRepository;
    private final EmployeeChangeLogService employeeChangeLogService;
    private final WorkspaceService workspaceService;
    private ConfirmLeaveService confirmLeaveService;
    private final Validator validator;
    private DayOffRepository dayOffRepository;
    private final AnnualLeaveService annualLeaveService;
    private final StreamBridge streamBridge;
    private final AuthClient authClient;
    private static final Map<String, String> NAMETOCODEMAP = Map.of(
            "KIM_LONG", "1",
            "MMS", "2",
            "MASI", "3");

    private static final Map<String, String> IDTOCODEMAP = Map.of(
            "F", "1",
            "M", "2");

    @Autowired
    public void setConfirmLeaveService(@Lazy ConfirmLeaveService confirmLeaveService) {
        this.confirmLeaveService = confirmLeaveService;
    }

    public EmployeeProfileService(EmployeeProfileRepository employeeProfileRepository,
                                  EmployeeProfileMapper employeeProfileMapper, ProfileAttachmentService profileAttachmentService,
                                  EmployeeIdSequenceService employeeIdSequenceService, TimeKeepingService timeKeepingService, EmployeeRepository employeeRepository, EmployeeChangeLogService employeeChangeLogService, WorkspaceService workspaceService, AnnualLeaveService annualLeaveService, StreamBridge streamBridge, AuthClient authClient) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeProfileMapper = employeeProfileMapper;
        this.profileAttachmentService = profileAttachmentService;
        this.employeeIdSequenceService = employeeIdSequenceService;
        this.timeKeepingService = timeKeepingService;
        this.employeeRepository = employeeRepository;
        this.employeeChangeLogService = employeeChangeLogService;
        this.workspaceService = workspaceService;
        this.annualLeaveService = annualLeaveService;
        this.streamBridge = streamBridge;
        this.authClient = authClient;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        factory.close();

        try {
            contractFileManager = new FileManager("em_contracts");
            exportFileManager = new FileManager("em_export");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //    @Scheduled(cron = "0 41 20 * * ?")
    public Mono<Void> syncFullName() {
        return employeeProfileRepository.findAll().flatMap(employeeProfile -> {
            assert employeeProfile.getId() != null;
            return employeeRepository.findById(employeeProfile.getId()).flatMap(employee -> {
                employee.setFullName(employeeProfile.getFullName());
                employee.setFirstName(employeeProfile.getFirstName());
                employee.setLastName(employeeProfile.getLastName());
                employee.setIsActive(true);
                employee.setIsPersisted();
                return employeeRepository.save(employee);
            });
        }).then();
    }

    public Mono<Void> syncAccountStatus() {
        return employeeProfileRepository.findAll().collectList().flatMap(employeeProfile -> {
            Map<UUID, EmployeeProfile> employeeProfileMap = employeeProfile.stream().collect(Collectors.toMap(EmployeeProfile::getId, Function.identity()));
            log.info("Employee profile size: {}", employeeProfile.size());
            List<EmployeeProfile> updateList = new ArrayList<>();
            return authClient.checkHasAccount(new ArrayList<>(employeeProfileMap.keySet())).flatMap(list -> {
                log.info("Check has account: {}", list.size());
                for (var map : list) {
                    UUID id = UUID.fromString((String) map.get("id"));
                    var status = (String) map.get("status");
                    var employeeProfile1 = employeeProfileMap.get(id);
                    if (employeeProfile1 != null) {
                        if ("ACTIVE".equals(status)) {
                            employeeProfile1.setAccountStatus(EmployeeAccountStatus.ENABLED.name());
                        } else {
                            employeeProfile1.setAccountStatus(EmployeeAccountStatus.DISABLED.name());
                        }
                        employeeProfile1.setIsPersisted();
                        updateList.add(employeeProfile1.setIsPersisted());
                    }
                }
//                return employeeProfileRepository.saveAll(employeeProfile).then();
                return Mono.empty();
            }).then(employeeProfileRepository.saveAll(updateList).then());

        }).then();
    }

    private Mono<Employee> syncNewEmployee(EmployeeProfile entity) {
        EmployeeChangeEvent event = EmployeeChangeEvent.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .isNew(entity.isNew())
                .department(entity.getDepartment())
                .workspaceId(entity.getWorkspaceId())
                .phone(entity.getPhone())
                .birthday(entity.getBirthday())
                .gender(entity.getGender().name())
                .email(entity.getEmail())
                .company(entity.getCompany())
                .build();
        var e = new Employee();
        log.info("Sync new employee: {}", entity.getFullName());
        e.email(entity.getEmail());
        e.id(entity.getId());
        e.setWorkspaceId(entity.getWorkspaceId());
        e.setFirstName(entity.getFirstName());
        e.setLastName(entity.getLastName());
        e.setPhoneNumber(entity.getPhone());
        e.setFullName(entity.getFullName());
        e.setHireDate(entity.getStartWorkDate().atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")));
        e.setSalary(0L);
        e.setCommissionPct(0L);
        e.setIsActive(true);
        return employeeRepository.save(e).doOnError(ex -> log.error("Error while sync new employee", ex));
//            .doOnTerminate(() -> streamBridge.send(EmployeeChangeEvent.EVENT_NAME, event, MediaType.APPLICATION_JSON));
    }


    public Mono<Void> reEvaluateCode() {
        return employeeProfileRepository.findAll().collectList().zipWith(employeeIdSequenceService.findAll())
                .flatMap(tuple -> {
                    var employeeProfiles = tuple.getT1();
                    var sequences = tuple.getT2();
                    Map<String, Map<Gender, EmployeeIdSequence>> sequenceMap = sequences.stream()
                            .map(EmployeeIdSequence::reset)
                            .collect(Collectors.groupingBy(EmployeeIdSequence::getWorkspaceId,
                                    Collectors.toMap(EmployeeIdSequence::getGender, Function.identity())));
                    // re-evaluate code

                    List<EmployeeProfile> employeeProfilesToSave = new ArrayList<>();
                    for (var profile : employeeProfiles) {
                        var sequence = sequenceMap.getOrDefault(profile.getCompany(), new HashMap<>()).get(profile.getGender());
                        if (sequence == null) {
                            log.error("Sequence not found for workspace {}", profile.getCompany());
                            continue;
                        }
                        profile.setEmployeeCode(sequence.getAndIncrease());
                        profile.setPersisted(true);
                        employeeProfilesToSave.add(profile);
                    }
                    return employeeProfileRepository.saveAll(employeeProfilesToSave)
                            .doOnError(ex -> log.error("Error while re-evaluate code", ex))
                            .then(employeeIdSequenceService.saveAll(sequences.stream().map(EmployeeIdSequence::setIsPersisted).collect(Collectors.toList()))
                                    .doOnError(ex -> log.error("Error while re-evaluate code", ex))
                            ).then();
                }).then();
    }


    @Transactional
    public Mono<EmployeeProfileDTO> save(EmployeeProfileDTO employeeProfileDTO) {
        log.debug("Request to save EmployeeProfile : {}", employeeProfileDTO);
        var entity = employeeProfileDTO.toEntity();

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user ->
                        employeeIdSequenceService.getNextAndIncrease(entity.getGender(), user.getCompanyId())
                                .flatMap(employeeId -> {
                                    entity.setEmployeeCode(employeeId);
                                    return this.syncNewEmployee(entity)
                                            .then(Mono.just(entity));
                                })
                )
                .flatMap(employeeProfileRepository::save)
                .map(employeeProfileMapper::toDto)
                .flatMap(dto ->
                        this.updateOrCreateRoleUserId(dto)
                                .thenReturn(dto)
                );
    }

    private Mono<Void> updateOrCreateRoleUserId(EmployeeProfileDTO employeeProfileDTO) {
        return authClient.updateOrCreateRoleUserId(employeeProfileDTO.getId(), employeeProfileDTO.getPosition().toRole());
    }


    public Mono<EmployeeProfileDTO> update(EmployeeProfileDTO employeeProfileDTO) {
        log.debug("Request to update EmployeeProfile : {}", employeeProfileDTO);
        return employeeProfileRepository.save(employeeProfileMapper.toEntity(employeeProfileDTO).setIsPersisted())
                .map(employeeProfileMapper::toDto);
    }


    public Mono<EmployeeProfileDTO> partialUpdate(EmployeeProfileDTO employeeProfileDTO) {
        log.debug("Request to partially update EmployeeProfile : {}", employeeProfileDTO);

        return employeeProfileRepository
                .findById(employeeProfileDTO.getId())
                .flatMap(existingEmployeeProfile -> {
                    var oldEntity = existingEmployeeProfile.toBuilder().build();
                    employeeProfileDTO.applyUpdateTo(existingEmployeeProfile);

                    existingEmployeeProfile.setIsPersisted();
                    return employeeChangeLogService.makeChange(oldEntity, existingEmployeeProfile.toBuilder().build(), "system").
                            then(Mono.just(existingEmployeeProfile));
                })
                .flatMap(employeeProfileRepository::save)
                .flatMap(profile -> {
                    return employeeRepository.findById(employeeProfileDTO.getId())
                            .map(employee -> {
                                employee.email(profile.getEmail());
                                employee.setWorkspaceId(profile.getWorkspaceId());
                                employee.setFirstName(profile.getFirstName());
                                employee.setLastName(profile.getLastName());
                                employee.setPhoneNumber(profile.getPhone());
                                employee.setFullName(profile.getFullName());
                                employee.setHireDate(
                                        profile.getStartWorkDate().atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")));
                                employee.setIsActive(true);
                                return employee.setIsPersisted();
                            }).flatMap(employeeRepository::save).then(Mono.just(profile));
                })
                .map(employeeProfileMapper::toDto).doOnTerminate(() -> {
                    dayOffRepository.removeDayOffByEmployeeId(employeeProfileDTO.getId()).subscribe();
                    log.info("Reset day off for employee {}", employeeProfileDTO.getId());
                }).flatMap(dto ->
                        this.updateOrCreateRoleUserId(dto)
                                .thenReturn(dto)
                );
    }

    /**
     * Get all the employeeProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EmployeeProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeProfiles");
        return employeeProfileRepository.findAllBy(pageable).map(employeeProfileMapper::toDto);
    }

    /**
     * Returns the number of employeeProfiles available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return employeeProfileRepository.count();
    }

    /**
     * Get one employeeProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EmployeeProfileDTO> findOne(UUID id) {
        log.debug("Request to get EmployeeProfile : {}", id);

        // Lấy thông tin EmployeeProfileDTO và các dữ liệu liên quan
        Mono<EmployeeProfileDTO> employeeProfileMono = employeeProfileRepository.findByIdAndNotDelete(id)
                .map(EmployeeProfile::toDTO)
                .zipWith(profileAttachmentService.getByEmployeeProfileId(id)
                                .switchIfEmpty(Mono.just(Collections.emptyList())), // Đảm bảo luôn có danh sách trả về
                        (employeeProfileDTO, profileAttachmentDTO) -> {
                            employeeProfileDTO.setFiles(profileAttachmentDTO);
                            return employeeProfileDTO;
                        })
                .zipWith(confirmLeaveService.findOne(id)
                                .switchIfEmpty(Mono.just(new ConfirmLeaveDTO())),
                        (employeeProfileDTO, confirmLeaveDTO) -> {
                            employeeProfileDTO.setConfirmLeave(confirmLeaveDTO);
                            return employeeProfileDTO;
                        });

        // Sử dụng Mono.zip để kết hợp nhiều Mono
        return employeeProfileMono.flatMap(employeeProfileDTO ->
                Mono.zip(
                                timeKeepingService.countAllTotalWorkedHours(Collections.singletonList(id)).collectList(),
                                annualLeaveService.findOrCreateByEmployeeIds(Collections.singletonList(id)).collectList()
                        )
                        .flatMap(tuple -> {
                            Map<UUID, Float> workedHoursMap = tuple.getT1().stream()
                                    .flatMap(map -> map.entrySet().stream())
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                            Map<UUID, Float> dayOffMap = tuple.getT2().stream()
                                    .collect(Collectors.toMap(
                                            DayOffDTO::getEmployeeId,
                                            DayOffDTO::getNumberDaysOff,
                                            (existingValue, newValue) -> newValue
                                    ));

                            // Kết hợp dữ liệu số giờ làm việc và số ngày nghỉ
                            Float workedHours = workedHoursMap.getOrDefault(id, 0f);
                            Float dayOff = dayOffMap.getOrDefault(id, 12f);

                            // không vượt quá số ngày nghỉ được quy định
                            if (workedHours > dayOff)
                                workedHours = dayOff;

                            // Cập nhật thông tin vào DTO
                            employeeProfileDTO.setUseDaysOff(workedHours); // Số ngày đã phép nghỉ
                            employeeProfileDTO.setNumberDaysOff(dayOff); // số ngày được nghỉ

                            return Mono.just(employeeProfileDTO);
                        })
        );
    }


    /**
     * Delete the employeeProfile by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete EmployeeProfile : {}", id);
        return employeeProfileRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Mono<Long> countAllByQuery(EmployeeProfileQuery query) {
        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    if (query.getIsFilterCompany()){
                        query.setCompany(user.getCompanyId());
                    }
                    return employeeProfileRepository.countByQuery(query);
                });
    }

    public Mono<List<EmployeeProfileDTO>> findByInIds(Collection<UUID> ids) {
        return employeeProfileRepository.findByIdIn(ids).map(EmployeeProfile::toBriefDTO).collectList();
    }

    public Flux<EmployeeProfileDTO> findAllByQuery(EmployeeProfileQuery query, Pageable pageable) {
        return SecurityUtils.getUserJWTDetail()
                .flatMapMany(user -> {
                    if (query.getIsFilterCompany()){
                        query.setCompany(user.getCompanyId());
                    }
                    return employeeProfileRepository.findAllByQuery(query, pageable)
                            .map(EmployeeProfile::toDTO)
                            .collectList()
                            .flatMapMany(employeeProfiles -> {
                                List<UUID> listEmployeeIds = employeeProfiles.stream()
                                        .map(EmployeeProfileDTO::getId)
                                        .collect(Collectors.toList());
                                if (listEmployeeIds.isEmpty()) {
                                    return Flux.empty();
                                }
                                List<String> requiredToFull = Arrays.asList(ProfileAttachmentType.CMND.name(), ProfileAttachmentType.HK.name(), ProfileAttachmentType.GCK.name(), ProfileAttachmentType.GKSK.name(), ProfileAttachmentType.DON_XV.name(), ProfileAttachmentType.SYLL.name());

                                return Flux.zip(
                                        timeKeepingService.countAllTotalWorkedHours(listEmployeeIds).collectList(),
//                                        dayOffRepository.findAllByEmployeeIdInAndIsActive(listEmployeeIds,true).collectList()
                                        annualLeaveService.findOrCreateByEmployeeIds(listEmployeeIds)
                                                .collectList(),
                                        employeeProfileRepository.getProfileAttachmentInEmployeeIds(listEmployeeIds, requiredToFull)
                                                .collectList().defaultIfEmpty(Collections.emptyList())
                                ).flatMap(tuple -> {
                                    Map<UUID, Float> workedHoursMap = tuple.getT1().stream()
                                            .flatMap(map -> map.entrySet().stream())
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                    var profilesAttachment = tuple.getT3();

                                    Map<UUID, Float> dayOffMap = tuple.getT2().stream()
                                            .collect(Collectors.toMap(
                                                    DayOffDTO::getEmployeeId,
                                                    DayOffDTO::getNumberDaysOff,
                                                    (existingValue, newValue) -> newValue
                                            ));

                                    // Kết hợp dữ liệu số giờ làm việc và số ngày nghỉ
                                    employeeProfiles.forEach(profile -> {
                                        Float workedHours = workedHoursMap.get(profile.getId());
                                        Float dayOff = dayOffMap.get(profile.getId());
                                        List<ProfileAttachment> myProfileAttachment = profilesAttachment.stream()
                                                .filter(profileAttachment -> profile.getId().equals(profileAttachment.getEmployeeProfileId()) && profileAttachment.getIsDeleted().equals(false))
                                                .toList();
                                        var setProfileAttachment = myProfileAttachment.stream().map(ProfileAttachment::getType).map(ProfileAttachmentType::name).collect(Collectors.toSet());
                                        var isInRequired = new HashSet<>(setProfileAttachment).containsAll(requiredToFull);
                                        profile.setIsHasProfileAttachment(myProfileAttachment.size() >= requiredToFull.size() && isInRequired);
                                        // không vượt quá s ngày nghỉ dược quy định
                                        if (dayOff == 0)
                                            dayOff = 12f;

                                        if (workedHours > dayOff)
                                            workedHours = dayOff;

                                        profile.setUseDaysOff(workedHours); // Số đã phép đã nghỉ

                                        profile.setNumberDaysOff(dayOff); // số ngày được nghỉ

                                    });
                                    log.info("Profiles: {}", employeeProfiles);
                                    return Flux.fromIterable(employeeProfiles)
                                        .switchIfEmpty(Flux.empty())
                                        .collectList()
                                        .flatMapMany(listProfile -> {
                                            var listId = listProfile.stream().map(EmployeeProfileDTO::getId).toList();
                                            return authClient.getAllUserByListId(listId)
                                                .collectList()
                                                .defaultIfEmpty(Collections.emptyList())
                                                .flatMapMany(listUser -> {
                                                    log.info("List user: {}", listUser);
                                                    var mapUser = listUser.stream().filter(Objects::nonNull).collect(Collectors.toMap(AuthClient.UserDTO::id, x -> x));
                                                    listProfile.forEach(profile -> {
                                                        var isContain = mapUser.containsKey(profile.getId());
                                                        if (isContain){
                                                            var thisUser = mapUser.get(profile.getId());
                                                            profile.setUserName(thisUser.userName());
                                                            profile.setCompanyJson(thisUser.companyJson());
                                                            profile.setIsActivated(thisUser.isActivated());
                                                        }
                                                    });
                                                    return Flux.fromIterable(listProfile);
                                                });
                                    }).doOnError(e -> log.error("Error while get all user by list id", e));
                                });
                            });
                });
    }


//    @Transactional(readOnly = true)
//    public Flux<EmployeeProfileDTO> findAllByQuery(EmployeeProfileQuery query, Pageable pageable) {
//        return
//                SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
//                    query.setCompany(user.getCompanyId());
//                    return employeeProfileRepository.findAllByQuery(query, pageable).map(EmployeeProfile::toDTO);
//                });
//
//    }


    @Transactional(readOnly = true)
    public Mono<InputStreamResource> getImportTemplate() {
//        InputStreamResource rs;
//        Path path = Path.of("/uploaded-files/template1.xlsx");
//        if (Files.exists(path)) {
//            try {
//                rs = new InputStreamResource(Files.newInputStream(path));
//                return Mono.just(rs);
//            } catch (IOException ignored) {
//            }
//        }
        var workspaceMono = SecurityUtils.getUserJWTDetail().flatMap(user -> {
            var query = new WorkspaceRO();
            query.setCompany(user.getCompanyId());
            return workspaceService.getAllByQueryAndPaginate(null, query).collectList();
        });
        return workspaceMono.map(this::renderWorkspaceData);
    }

    private InputStreamResource renderWorkspaceData(List<WorkspaceDTO> workspaces) {
        try (Workbook workbook = new XSSFWorkbook(Objects.requireNonNull(getClass().getResourceAsStream("/templates/mau_insert.xlsx")))) {
            Sheet sheet = workbook.getSheetAt(1);
            int startRow = 3;
            for (int i = 0; i < workspaces.size(); i++) {
                var workspace = workspaces.get(i);
                Row row = sheet.createRow(startRow + i);

                row.createCell(0).setCellValue(workspace.getName());
                row.createCell(1).setCellValue(workspace.getId().toString());
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<Void> uploadImportFile(Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> {
//            luu vao
            // remove old template
            Path dest = Path.of("/uploaded-files/template1.xlsx");
            try {
                Files.delete(dest);
            } catch (IOException ignored) {
            }
            return filePart.transferTo(dest);
        });
    }

    @Transactional(readOnly = true)
    public Mono<EmployeeProfileDTO> findByCitizenId(String idCard) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return employeeProfileRepository.findByCitizenId(idCard, user.getCompanyId()).map(EmployeeProfile::toDTO);
        });
    }

    @Transactional(readOnly = true)
    public Mono<EmployeeProfileDTO> findByTaxCode(String taxId) {
        if (StringUtils.isBlank(taxId)) {
            return Mono.empty();
        }
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return employeeProfileRepository.findByTaxCode(taxId, user.getCompanyId()).map(EmployeeProfile::toDTO);
        });
    }

    @Transactional(readOnly = true)
    public Mono<EmployeeProfileDTO> findByBankCode(String bankCode) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return employeeProfileRepository.findByBankCode(bankCode, user.getCompanyId()).map(EmployeeProfile::toDTO);
        });
    }

    private String parseFullVietnamDate(LocalDate date) {
        if (date == null) {
            return "............";
        }
        return String.format("ngày %2d tháng %2d năm %4d", date.getDayOfMonth(),
                date.getMonthValue(), date.getYear());
    }

    private String getContractTemplate(EmployeeProfile employeeProfile) {
        switch (employeeProfile.getCompany()) {
            case "MMS":
                if (ContractType.TRIAL.equals(employeeProfile.getContractType())) {
                    return "MMS-TRIAL.docx";
                }
                return "MMS-HDLD.docx";
            case "KIM_LONG":
                if (ContractType.TRIAL.equals(employeeProfile.getContractType())) {
                    if (WorkspaceType.FACTORY.equals(employeeProfile.getWorkspace().getWorkspaceType())) {
                        return "KimLong-TRIAL-SX.docx";
                    }
                    return "KingLong-TRIAL-VP.docx";

                }
                if (WorkspaceType.FACTORY.equals(employeeProfile.getWorkspace().getWorkspaceType())) {
                    return "KimLong-HDLD-SX.docx";
                }
                return "KimLong-HDLD-VP.docx";
        }
        return "MMS-HDLD.docx";
    }

    public Mono<String> exportContract(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    String templateName = getContractTemplate(e);
                    String resourcePath = "/forms/hdld/" + templateName;
                    log.info("Use template: {}", resourcePath);
                    var templateStream = getClass().getResourceAsStream(resourcePath);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("employee_code", e.getEmployeeCode());
                    map.put("contract_number", e.getEmployeeCode());
                    map.put("full_name", e.getFullName());
                    map.put("birthday", this.parseToDateString(e.getBirthday()));
                    map.put("residence_address", e.getResidenceAddress());
                    map.put("temporary_address", e.getTemporaryAddress());
                    // citizen_id, citizen_issue_date, citizen_issue_place
                    // ,contract_date,contract_end_date}} ,role
                    map.put("citizen_id", e.getCitizenId());
                    map.put("citizen_issue_date",
                            this.parseToDateString(e.getCitizenIssueDate()));
                    map.put("citizen_issue_place", e.getCitizenIssuePlace());
                    map.put("contract_date", this.parseToDateString(e.getContractDate()));
                    map.put("contract_date_str", parseFullVietnamDate(e.getContractDate()));
                    map.put("contract_end_date",
                            this.parseToDateString(e.getContractEndDate()));
                    map.put("term", e.getContractTerm());
                    map.put("contract_end_date_str", parseFullVietnamDate(e.getContractEndDate()));
                    map.put("role", e.getRole());
                    map.put("position", e.getPosition().toVietnamese());
                    map.put("today_str", parseFullVietnamDate(LocalDate.now()));
                    return Mono.fromCallable(() -> DocxUtils.renderTemplate(templateStream, map, contractFileManager));
                }).doOnError(e -> log.error("Error while rendering contract ne", e));
    }

    // chạy mỗi tháng vào ngày 1 lúc 0h
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Ho_Chi_Minh")
    public void cleanUpContracts() {
        log.info("Cleaning up contracts");
        contractFileManager.deleteManyFiles(file -> {
            long currentTime = System.currentTimeMillis();
            long timestamp = file.getT2();
            return currentTime - timestamp > 30L * 24 * 60 * 60 * 1000;
        });
    }

    public Mono<EmployeeProfileDTO> disableProfile(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    e.setIsPersisted();
                    e.setIsActive(false);
                    e.setUpdatedAt(ZonedDateTime.now());
                    return employeeRepository.disableEmployeeById(e.getId()).then(Mono.just(e));
                }).flatMap(employeeProfileRepository::save).map(EmployeeProfile::toDTO);
    }

    public Mono<EmployeeProfileDTO> activeProfile(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    e.setIsPersisted();
                    e.setIsActive(true);
                    e.setUpdatedAt(ZonedDateTime.now());
                    return employeeRepository.enableEmployeeById(e.getId()).then(Mono.just(e));
                }).flatMap(employeeProfileRepository::save).map(EmployeeProfile::toDTO);
    }

    public static String generateCode(String company, String code) {
        String companyCode = NAMETOCODEMAP.getOrDefault(company, "0");
        String firstChar = String.valueOf(code.charAt(0)).toUpperCase();
        String charCode = IDTOCODEMAP.getOrDefault(firstChar, "0");
        String number = code.substring(1);
        if (number.length() > 4) {
            number = number.substring(number.length() - 4);
        } else {
            number = String.format("%04d", Integer.parseInt(number));
        }
        return companyCode + charCode + number;
    }

    public Mono<EmployeeProfileDTO> activeTimeKeepingDevice(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    e.setIsPersisted();
                    e.setIsActive(true);
                    String pin = generateCode(e.getCompany(), e.getEmployeeCode());
                    e.setPin(pin);
                    e.setUpdatedAt(ZonedDateTime.now());
                    return employeeProfileRepository.save(e);
                }).map(EmployeeProfile::toDTO);
    }

    public Mono<EmployeeProfileDTO> confirmLeave(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    e.setIsPersisted();
                    e.setStatus(EmployeeStatus.RESIGNED);
                    e.setUpdatedAt(ZonedDateTime.now());
                    return employeeRepository.disableEmployeeById(e.getId()).then(Mono.just(e));
                }).flatMap(employeeProfileRepository::save).map(EmployeeProfile::toDTO);
    }

    public Mono<EmployeeProfileDTO> confirmRetire(UUID id) {
        return employeeProfileRepository.findByIdAndNotDelete(id)
                .flatMap(e -> {
                    e.setIsPersisted();
                    e.setStatus(EmployeeStatus.WORKING);
                    e.setUpdatedAt(ZonedDateTime.now());
                    return employeeRepository.enableEmployeeById(e.getId()).then(Mono.just(e));
                }).flatMap(employeeProfileRepository::save).map(EmployeeProfile::toDTO);
    }


    private String parseToDateString(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter vietNameDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(vietNameDateFormatter);

    }

    private String getCellStringValue(Cell cell) {
        if (Objects.isNull(cell)) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private LocalDate getCellDateValue(Cell cell) {
        if (Objects.isNull(cell)) return null;
        if (Objects.isNull(cell.getDateCellValue())) return null;
        return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private EmployeeProfileXlsx parseFromRow(Row row) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NEVER)
                .appendLiteral('/')
                .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NEVER)
                .appendLiteral('/')
                .appendValue(ChronoField.YEAR, 4)
                .toFormatter();
        // F000005
        EmployeeProfileXlsx employeeProfile = new EmployeeProfileXlsx();
        var employeeCodeRegex = "^[FM]\\d+$";
        employeeProfile.setEmployeeCode(getCellStringValue(row.getCell(0)));
        // if math then set empty
        if (!employeeProfile.getEmployeeCode().matches(employeeCodeRegex)) {
            employeeProfile.setEmployeeCode("");
        }
        employeeProfile.setFullName(getCellStringValue(row.getCell(1)));
        employeeProfile.setGender(Gender.formVietnamese(getCellStringValue(row.getCell(2))));
        employeeProfile.setWorkspaceName(getCellStringValue(row.getCell(3)));
        employeeProfile.setCitizenId(getCellStringValue(row.getCell(4)));
        employeeProfile.setCitizenIssueDate(getCellDateValue(row.getCell(5)));
        employeeProfile.setCitizenIssuePlace(getCellStringValue(row.getCell(6)));
        employeeProfile.setResidenceAddress(getCellStringValue(row.getCell(7)));
        employeeProfile.setTemporaryAddress(getCellStringValue(row.getCell(8)));
        employeeProfile.setBirthday(getCellDateValue(row.getCell(9)));
        employeeProfile.setPhone(getCellStringValue(row.getCell(10)));
        employeeProfile.setTaxCode(getCellStringValue(row.getCell(11)));
        employeeProfile.setStartWorkDate(getCellDateValue(row.getCell(12)));
        employeeProfile.setRole(getCellStringValue(row.getCell(13)));
        employeeProfile.setPosition(Position.fromVietnamese(getCellStringValue(row.getCell(14))));
        employeeProfile.setBankCode(getCellStringValue(row.getCell(15)));
        employeeProfile.setBankNumber(getCellStringValue(row.getCell(16)));
        employeeProfile.setContractType(ContractType.fromVietnamese(getCellStringValue(row.getCell(17))));
        employeeProfile.setContractTerm(getCellStringValue(row.getCell(18)));
        employeeProfile.setContractNumber(getCellStringValue(row.getCell(19)));
        employeeProfile.setContractDate(getCellDateValue(row.getCell(20)));
        employeeProfile.setContractEndDate(getCellDateValue(row.getCell(21)));
        employeeProfile.setLevel(getCellStringValue(row.getCell(22)));
        employeeProfile.setParkingCard(getCellStringValue(row.getCell(23)));
        employeeProfile.setInsuranceCard(getCellStringValue(row.getCell(24)));
        employeeProfile.setReferrerCode(getCellStringValue(row.getCell(25)));
        employeeProfile
                .setReferrerDate(getCellDateValue(row.getCell(26)));
        employeeProfile.setEmail(getCellStringValue(row.getCell(27)));
        employeeProfile.setNote(getCellStringValue(row.getCell(28)));
        employeeProfile.setStatus(EmployeeStatus.fromVietnamese(getCellStringValue(row.getCell(29))));
        employeeProfile.setProbationDateFrom(getCellDateValue(row.getCell(30)));
        employeeProfile.setProbationDateTo(getCellDateValue(row.getCell(31)));
        employeeProfile.setOfficialWorkTypeDuration(
                StringUtils.isBlank(getCellStringValue(row.getCell(32))) ? null
                        : Float.parseFloat(getCellStringValue(row.getCell(32))));
        employeeProfile.setInsurancePaymentLevel(
                StringUtils.isBlank(getCellStringValue(row.getCell(33))) ? null
                        : Float.parseFloat(getCellStringValue(row.getCell(33))));

        var constraintViolations = validator.validate(employeeProfile);
        if (!constraintViolations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (var violation : constraintViolations) {
                log.error("Invalid employee: {} because {} : {}", employeeProfile.getFullName(), violation.getPropertyPath().toString(), violation.getMessage());
                sb.append(violation.getMessage()).append("<br/>");
            }
            employeeProfile.setError(sb.toString());
            employeeProfile.setIgnore(true);
        }
        return employeeProfile;
    }

    @Transactional
    protected Flux<EmployeeProfileXlsx> parseXLSXToList(byte[] file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(file))) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Iterable<Row> iterable = () -> rowIterator;
            var listEmXlsx = StreamSupport.stream(iterable.spliterator(), false).skip(1)
                    .filter(r -> !Strings.isBlank(r.getCell(1).getStringCellValue()))
                    .map(r -> {
                        try {
                            var rs = parseFromRow(r);
                            rs.setRowNumber(r.getRowNum());
                            return rs;
                        } catch (Exception e) {
                            var newE = new EmployeeProfileXlsx();
                            newE.setError(String.format("Lỗi khi đọc dòng %d: %s", r.getRowNum(), e.getMessage()));
                            newE.setRowNumber(r.getRowNum());
                            newE.setIgnore(true);
                            return newE;
                        }
                    }).toList();


            return SecurityUtils.getUserJWTDetail().flatMapMany(user -> Flux.fromIterable(listEmXlsx)
                    .flatMap(e -> {
                        if (e.isIgnore() && StringUtils.isBlank(e.getBankNumber()) && StringUtils.isBlank(e.getTaxCode()) && StringUtils.isBlank(e.getCitizenId())) {
                            e.setIgnore(true);
                            return Mono.just(e);
                        }
                        e.setCompany(user.getCompanyId());
                        log.info("Checking exist information for employee: {}", e.getFullName());
                        Mono<String> isCodeExistMono = employeeProfileRepository.isNewCodeExist(e.getEmployeeCode(), user.getCompanyId())
                                .map(exist -> {

                                    return exist ? String.format("Mã nhân viên <b>%s</b> đã tồn tại", e.getEmployeeCode()) : "";
                                })
                                .switchIfEmpty(Mono.just(""));
                        Mono<String> isTaxExistMono = employeeProfileRepository.isNewTaxExist(e.getTaxCode(), user.getCompanyId())
                                .map(exist -> {
                                    if (StringUtils.isBlank(e.getTaxCode())) return "";
                                    return exist ? String.format("Mã số thuế <b>%s</b> đã tồn tại", e.getTaxCode()) : "";
                                })
                                .switchIfEmpty(Mono.just(""));
                        Mono<String> isBankExistMono = employeeProfileRepository.isNewBankExist(e.getBankNumber(), user.getCompanyId())
                                .map(exist -> {
                                    return exist ? String.format("Số tài khoản <b>%s</b> đã tồn tại", e.getBankNumber()) : "";
                                })
                                .switchIfEmpty(Mono.just(""));
                        Mono<String> isCitizenExistMono = employeeProfileRepository.isNewCitizenExist(e.getCitizenId(), user.getCompanyId())
                                .map(exist -> {
                                    return exist ? String.format("Số CMND <b>%s</b> đã tồn tại", e.getCitizenId()) : "";
                                })
                                .switchIfEmpty(Mono.just(""));

//                    return employeeProfileRepository.isValidNewEmployee(e.getEmployeeCode(), e.getBankNumber(), e.getTaxCode(), e.getCitizenId(), user.getCompanyId())
//                        .hasElement().map(isUnValid -> {
//                            e.setCompany(user.getCompanyId());
//                            if (!isUnValid) {
//                                return e;
//                            }
//                            log.info("Invalid employee: {} because exist information {} {} {}", e.getFullName(), e.getEmployeeCode(), e.getBankNumber(), e.getTaxCode());
//                            e.setError("Nhân viên đã tồn tại 1 trong các thông tin (Mã nhân viên, Số tài khoản, Mã số thuế, Số CMND)");
//                            e.setIgnore(true);
//                            return e;
//                        });
                        return Mono.zip(isCodeExistMono, isTaxExistMono, isBankExistMono, isCitizenExistMono)
                                .map(t -> {
                                    if (t.getT1().isBlank() && t.getT2().isBlank() && t.getT3().isBlank() && t.getT4().isBlank()) {
                                        return e;
                                    }
                                    var listError = t.toList().stream().map(String::valueOf).filter(StringUtils::isNotBlank).toList();
                                    var joinError = String.join("<br/>", listError);
                                    log.info("Invalid employee: {} because exist information {} {} {}", e.getFullName(), e.getEmployeeCode(), e.getBankNumber(), e.getTaxCode());
                                    e.setError(e.getError() + joinError);
                                    e.setIgnore(true);
                                    return e;
                                });
                    }));

        }
    }

    public Flux<EmployeeProfileXlsx> importXLSX(byte[] file) {
        try {
            return parseXLSXToList(file).
                    flatMap(this::saveEmployeeProfile, 1)
                    .onErrorContinue((e, o) -> {
                        if (o instanceof EmployeeProfileXlsx employeeProfileXlsx) {
                            employeeProfileXlsx.setError(e.getMessage());
                            employeeProfileXlsx.setIgnore(true);
                        }
                    });

        } catch (IOException e) {
            return Flux.error(e);
        }
    }

    public Mono<EmployeeProfileXlsx> saveEmployeeProfile(EmployeeProfileXlsx employeeProfileXlsx) {
        // đã check code, tax, bank, citizen -> không cần check lại
        // lay workspace
        if (employeeProfileXlsx.isIgnore()) {
            return Mono.just(employeeProfileXlsx);
        }
        log.info("Saving employee: {}", employeeProfileXlsx.getFullName());
        Mono<EmployeeProfileXlsx> workspaceMono = workspaceService.findOneIdByNameAndCompany(employeeProfileXlsx.getWorkspaceName(), employeeProfileXlsx.getCompany())
                .flatMap(workspaceDTO -> {
                    employeeProfileXlsx.setWorkspaceId(workspaceDTO.getId());
                    return Mono.just(workspaceDTO);
                }).then(Mono.just(employeeProfileXlsx));
        Mono<EmployeeProfileXlsx> referrerMono = employeeProfileRepository.findFirstByEmployeeCodeAndCompany(employeeProfileXlsx.getReferrerCode(), employeeProfileXlsx.getCompany())
                .map(profile -> {
                    employeeProfileXlsx.setReferrerId(profile.getId());
                    return profile;
                }).then(Mono.just(employeeProfileXlsx));
        return workspaceMono.flatMap(w -> referrerMono)
                .then(Mono.defer(() -> {
                    var entity = employeeProfileXlsx.toEntity();
                    log.info("Start saving employee: {}", entity.getFullName());
                    return employeeIdSequenceService.getNextAndIncrease(entity.getGender(), entity.getCompany())
                            .flatMap(employeeId -> {
                                entity.setEmployeeCode(employeeId);
                                return employeeProfileRepository.save(entity);
                            }).flatMap(this::syncNewEmployee).then();
                })).then(Mono.just(employeeProfileXlsx));
    }

    public Mono<String> exportXLSX(EmployeeProfileQuery query, Pageable pageable) {
        return employeeProfileRepository.findAllByQuery(query, null).collectList()
                .flatMap(employeeProfiles -> {
                    try {
                        return exportXLSX(employeeProfiles);
                    } catch (IOException e) {
                        log.error("Error while exporting xlsx", e);
                        return Mono.error(e);
                    }
                });
    }

    // what the f am i writing?
    private Mono<String> exportXLSX(Collection<EmployeeProfile> employeeProfiles) throws IOException {
        var headers = new String[]{"Mã nhân viên", "Họ và tên", "Giới tính", "Tên phòng ban", "Số CMND",
                "Ngày cấp", "Nơi cấp", "Địa chỉ thường trú", "Địa chỉ tạm trú", "Ngày sinh", "Số điện thoại",
                "Mã số thuế", "Ngày vào làm", "Chức vụ", "Vị trí", "Mã ngân hàng", "Số tài khoản", "Loại hợp đồng",
                "Thời hạn hợp đồng", "Số hợp đồng", "Ngày ký hợp đồng", "Ngày hết hạn hợp đồng", "Trình độ", "Thẻ xe",
                "Thẻ bảo hiểm", "Người giới thiệu", "Ngày chi tiền giới thiệu", "Email", "Ghi chú", "Trạng thái",
                "Ngày thử việc từ", "Ngày thử việc đến", "Thời gian làm việc chính thức", "Mức đóng bảo hiểm"};

        var byteOutputStream = new ByteArrayOutputStream();
        try (
                Workbook workbook = new XSSFWorkbook()) {
            var mainSheet = workbook.createSheet("Danh sách nhân viên");
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 14);
            font.setFontName("Times New Roman");
            font.setBold(true);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(font);
            CellStyle defaultStyle = workbook.createCellStyle();
            defaultStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            defaultStyle.setAlignment(HorizontalAlignment.LEFT);
            Font defaultFont = workbook.createFont();
            defaultFont.setFontHeightInPoints((short) 13);
            defaultFont.setFontName("Times New Roman");
            defaultStyle.setFont(defaultFont);

            var headerRow = mainSheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                var cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            headerRow.setHeight((short) (headerRow.getHeight() * 1.4));
            int rowNum = 1;

            for (EmployeeProfile employeeProfile : employeeProfiles) {
                var row = mainSheet.createRow(rowNum++);
                var createDefaultCell = new Function<Integer, Cell>() {
                    @Override
                    public Cell apply(Integer integer) {
                        var cell = row.createCell(integer);
                        cell.setCellStyle(defaultStyle);
                        return cell;
                    }
                };
                createDefaultCell.apply(0).setCellValue(employeeProfile.getEmployeeCode());
                createDefaultCell.apply(1).setCellValue(employeeProfile.getFullName());
                createDefaultCell.apply(2).setCellValue(employeeProfile.getGender().toVietnamese());
                createDefaultCell.apply(3).setCellValue(
                        employeeProfile.getWorkspace() != null ? employeeProfile.getWorkspace().getName() : "");
                createDefaultCell.apply(4).setCellValue(employeeProfile.getCitizenId());
                createDefaultCell.apply(5).setCellValue(parseToDateString(employeeProfile.getCitizenIssueDate()));
                createDefaultCell.apply(6).setCellValue(employeeProfile.getCitizenIssuePlace());
                createDefaultCell.apply(7).setCellValue(employeeProfile.getResidenceAddress());
                createDefaultCell.apply(8).setCellValue(employeeProfile.getTemporaryAddress());
                createDefaultCell.apply(9).setCellValue(parseToDateString(employeeProfile.getBirthday()));
                createDefaultCell.apply(10).setCellValue(employeeProfile.getPhone());
                createDefaultCell.apply(11).setCellValue(employeeProfile.getTaxCode());
                createDefaultCell.apply(12).setCellValue(parseToDateString(employeeProfile.getStartWorkDate()));
                createDefaultCell.apply(13).setCellValue(employeeProfile.getRole());
                createDefaultCell.apply(14).setCellValue(employeeProfile.getPosition().toVietnamese());
                createDefaultCell.apply(15).setCellValue(employeeProfile.getBankCode());
                createDefaultCell.apply(16).setCellValue(employeeProfile.getBankNumber());
                createDefaultCell.apply(17).setCellValue(employeeProfile.getContractType().toVietnamese());
                createDefaultCell.apply(18).setCellValue(employeeProfile.getContractTerm());
                createDefaultCell.apply(19).setCellValue(employeeProfile.getContractNumber());
                createDefaultCell.apply(20).setCellValue(parseToDateString(employeeProfile.getContractDate()));
                createDefaultCell.apply(21).setCellValue(parseToDateString(employeeProfile.getContractEndDate()));
                createDefaultCell.apply(22).setCellValue(employeeProfile.getLevel());
                createDefaultCell.apply(23).setCellValue(employeeProfile.getParkingCard());
                createDefaultCell.apply(24).setCellValue(employeeProfile.getInsuranceCard());
                createDefaultCell.apply(25).setCellValue(
                        employeeProfile.getReferrer() != null ? employeeProfile.getReferrer().getFullName() : "");
                createDefaultCell.apply(26).setCellValue(parseToDateString(employeeProfile.getReferrerDate()).replaceAll("null", ""));
                createDefaultCell.apply(27).setCellValue(employeeProfile.getEmail());
                createDefaultCell.apply(28).setCellValue(employeeProfile.getNote());
                createDefaultCell.apply(29).setCellValue(employeeProfile.getStatus() != null ? employeeProfile.getStatus().toVietnamese() : "");
                createDefaultCell.apply(30).setCellValue(parseToDateString(employeeProfile.getProbationDateFrom()).replaceAll("null", ""));
                createDefaultCell.apply(31).setCellValue(parseToDateString(employeeProfile.getProbationDateTo()).replaceAll("null", ""));
                createDefaultCell.apply(32).setCellValue(String.valueOf(employeeProfile.getOfficialWorkTypeDuration()).replaceAll("null", ""));
                createDefaultCell.apply(33).setCellValue(String.valueOf(employeeProfile.getInsurancePaymentLevel()).replaceAll("null", ""));
                row.setHeight((short) (row.getHeight() * 1.4));
            }
            for (int i = 0; i < headers.length; i++) {
                mainSheet.autoSizeColumn(i);
            }
            workbook.write(byteOutputStream);
            workbook.close();
            return exportFileManager.saveFile("employee_profiles.xlsx", byteOutputStream.toByteArray());
        }
    }


    private final List<String> listDepartmentList = Arrays.asList("HCNS", "SALE", "WORKER", "LOGPUR");

    public Mono<ListEmployeeDepartmentDTOReponse> getListDepartment() {
        return SecurityUtils.getCompanyId().flatMap(company -> {
            ListEmployeeDepartmentDTOReponse listEmployeeDepartmentDTOReponse = new ListEmployeeDepartmentDTOReponse();

            return employeeProfileRepository.findByCompanyAndListDepartment(String.valueOf(company), listDepartmentList)
                    .collectMultimap(EmployeeProfile::getDepartment, Function.identity())
                    .map(departmentMap -> {
                        listEmployeeDepartmentDTOReponse.setEmployeesHCNS(mapToDTOs(departmentMap.get("HCNS")));
                        listEmployeeDepartmentDTOReponse.setEmployeesSALE(mapToDTOs(departmentMap.get("SALE")));
                        listEmployeeDepartmentDTOReponse.setEmployeesWORKER(mapToDTOs(departmentMap.get("WORKER")));
                        listEmployeeDepartmentDTOReponse.setEmployeesLOGPUR(mapToDTOs(departmentMap.get("LOGPUR")));
                        return listEmployeeDepartmentDTOReponse;
                    });
        });
    }

    private Collection<EmployeeProfileDTO> mapToDTOs(Collection<EmployeeProfile> employeeProfiles) {
        if (employeeProfiles == null) {
            return Collections.emptyList();
        }
        return employeeProfiles.stream()
                .map(employeeProfile -> new EmployeeProfileDTO(employeeProfile.getId(), employeeProfile.getFullName()))
                .collect(Collectors.toList());
    }

    @Autowired
    public void setDayOffRepository(DayOffRepository dayOffRepository) {
        this.dayOffRepository = dayOffRepository;
    }

    // export import excel template

    public Flux<EmployeeProfileDTO> findAllByListIdString(List<String> ids, String company) {
        return employeeProfileRepository.findAllByIdInAndCompany(ids, company)
                .map(EmployeeProfile::toDTO);
    }
}
