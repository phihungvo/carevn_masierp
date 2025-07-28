package com.masi.employee.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.AnnualLeave;
import com.masi.employee.domain.DayOff;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.enumeration.WorkPlace;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.AnnualLeaveRepository;
import com.masi.employee.repository.DayOffRepository;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.repository.WorkspaceRepository;
import com.masi.employee.service.dto.AnnualLeaveDTO;
import com.masi.employee.service.dto.DayOffDTO;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.dto.reponse.AnnualLeaveDTOReponse;
import com.masi.employee.service.dto.reponse.SeniorityDtoReponse;
import com.masi.employee.service.dto.request.DayOffRequest;
import com.masi.employee.service.dto.request.UpdateEmployeeProfile;
import com.masi.employee.service.mapper.AnnualLeaveMapper;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;
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
 * Service Implementation for managing {@link AnnualLeave}.
 */
@Service
@Transactional
public class AnnualLeaveService {

    private static final Logger log = LoggerFactory.getLogger(AnnualLeaveService.class);

    private final AnnualLeaveRepository annualLeaveRepository;
    private final DayOffRepository dayOffRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final WorkspaceRepository workspaceRepository;
    private final static Integer AnnulLeaveMax = 17;

    public AnnualLeaveService(AnnualLeaveRepository annualLeaveRepository, DayOffRepository dayOffRepository, EmployeeProfileRepository employeeProfileRepository, WorkspaceRepository workspaceRepository) {
        this.annualLeaveRepository = annualLeaveRepository;
        this.dayOffRepository = dayOffRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.workspaceRepository = workspaceRepository;
    }


    /**
     * Partially update a annualLeave.
     *
     * @param annualLeaveOFFICE the entity to update partially.
     * @return the persisted entity.
     */

    public Mono<AnnualLeaveDTO> partialUpdate(AnnualLeaveDTO annualLeaveFACTORY, AnnualLeaveDTO annualLeaveOFFICE) {
        // NÀO ADD VÔ auth THÌ ĐỔI NHA
/*        retuyUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN")
                .flatMap(hasAuthority -> {
                    if (!hasAuthority) {
                        return Mono.error(new BadRequestAlertException("Failed to partialUpdate: Not Authority HCNS ", "contract", "approved"));
                    }
                    return Mono.empty();
                }rn Securit)*/
        return SecurityUtils.getCompanyId()
            .flatMap(companyId ->
                annualLeaveRepository.findByWorkPlaceAndCompany(WorkPlace.FACTORY, String.valueOf(companyId))
                    .flatMap(existingFactoryLeave -> {
                        existingFactoryLeave.setLeaveAfterProbation(annualLeaveFACTORY.getLeaveAfterProbation());
                        existingFactoryLeave.setLeavePerYear(annualLeaveFACTORY.getLeavePerYear());
                        existingFactoryLeave.setCarryForwardMonth(annualLeaveFACTORY.getCarryForwardMonth());
                        existingFactoryLeave.setLastUpdated(ZonedDateTime.now());
                        existingFactoryLeave.setIsPersisted();
                        return annualLeaveRepository.save(existingFactoryLeave);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        AnnualLeave newFactoryLeave = new AnnualLeave();
                        newFactoryLeave.setId(UUID.randomUUID());
                        newFactoryLeave.setWorkPlace(WorkPlace.FACTORY);
                        newFactoryLeave.setLeaveAfterProbation(annualLeaveFACTORY.getLeaveAfterProbation());
                        newFactoryLeave.setLeavePerYear(annualLeaveFACTORY.getLeavePerYear());
                        newFactoryLeave.setCarryForwardMonth(annualLeaveFACTORY.getCarryForwardMonth());
                        newFactoryLeave.setCreatedDate(ZonedDateTime.now());
                        newFactoryLeave.setLastUpdated(ZonedDateTime.now());
                        return annualLeaveRepository.save(newFactoryLeave);
                    }))
                    .then(annualLeaveRepository.findByWorkPlaceAndCompany(WorkPlace.OFFICE, String.valueOf(companyId))
                        .flatMap(existingOfficeLeave -> {
                            existingOfficeLeave.setLeaveAfterProbation(annualLeaveOFFICE.getLeaveAfterProbation());
                            existingOfficeLeave.setLeavePerYear(annualLeaveOFFICE.getLeavePerYear());
                            existingOfficeLeave.setCarryForwardMonth(annualLeaveOFFICE.getCarryForwardMonth());
                            existingOfficeLeave.setLastUpdated(ZonedDateTime.now());
                            existingOfficeLeave.setIsPersisted();
                            return annualLeaveRepository.save(existingOfficeLeave);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            AnnualLeave newOfficeLeave = new AnnualLeave();
                            newOfficeLeave.setId(UUID.randomUUID());
                            newOfficeLeave.setWorkPlace(WorkPlace.OFFICE);
                            newOfficeLeave.setLeaveAfterProbation(annualLeaveOFFICE.getLeaveAfterProbation());
                            newOfficeLeave.setLeavePerYear(annualLeaveOFFICE.getLeavePerYear());
                            newOfficeLeave.setCarryForwardMonth(annualLeaveOFFICE.getCarryForwardMonth());
                            newOfficeLeave.setCreatedDate(ZonedDateTime.now());
                            newOfficeLeave.setLastUpdated(ZonedDateTime.now());
                            return annualLeaveRepository.save(newOfficeLeave);
                        }))
                    )
                    .then(Mono.just(annualLeaveFACTORY))
                    .then(Mono.just(annualLeaveOFFICE)));
    }


    public Mono<AnnualLeaveDTOReponse> findAll() {
        return SecurityUtils.getCompanyId()
            .flatMap(companyId -> {
                Mono<AnnualLeaveDTO> factoryLeave = annualLeaveRepository.findByWorkPlaceAndCompany(WorkPlace.FACTORY, String.valueOf(companyId))
                    .map(AnnualLeave::toDto)
                    .defaultIfEmpty(new AnnualLeaveDTO());

                Mono<AnnualLeaveDTO> officeLeave = annualLeaveRepository.findByWorkPlaceAndCompany(WorkPlace.OFFICE, String.valueOf(companyId))
                    .map(AnnualLeave::toDto)
                    .defaultIfEmpty(new AnnualLeaveDTO());

                return Mono.zip(factoryLeave, officeLeave)
                    .map(tuple -> {
                        AnnualLeaveDTOReponse response = new AnnualLeaveDTOReponse();
                        response.setAnnualLeave_FACTORY(tuple.getT1());
                        response.setAnnualLeave_OFFICE(tuple.getT2());
                        return response;
                    });
            });
    }

    protected Mono<DayOff> findByEmployeeId(UUID idEmployee) {
        return dayOffRepository.findOneByEmployeeIdAndAndIsActive(idEmployee, true);
    }

    protected Flux<DayOff> findByEmployeeIds(List<UUID> idEmployee) {
        return dayOffRepository.findAllByEmployeeIdInAndIsActive(idEmployee, true);
    }

    public Mono<DayOffDTO> findOneById(UUID idEmployee) {
        log.debug("Request to get idEmployee : {}", idEmployee);
        return findByEmployeeId(idEmployee)
            .flatMap(employee -> {
                return Mono.just(employee.toDTO());
            })
            .switchIfEmpty(findDayOff(idEmployee))
            .doOnError(e -> log.error("Error finding DayOff for employee ID: {}", idEmployee, e));
    }

    public Flux<DayOffDTO> findOrCreateByEmployeeIds(List<UUID> employeeIds) {
        if (employeeIds.isEmpty()) {
            return Flux.empty();
        }
        log.debug("Request to get or create DayOff for employee IDs : {}", employeeIds);

        return findByEmployeeIds(employeeIds)
            .collectMap(DayOff::getEmployeeId, DayOff::toDTONotCal)
            .flatMapMany(existingDayOffs -> {
                List<UUID> missingEmployeeIds = employeeIds.stream()
                    .filter(id -> !existingDayOffs.containsKey(id))
                    .collect(Collectors.toList());

                return Flux.fromIterable(missingEmployeeIds)
                    .flatMap(this::findDayOffS)
                    .map(dayOffDTO -> {
                        existingDayOffs.put(dayOffDTO.getEmployeeId(), dayOffDTO);
                        return dayOffDTO;
                    })
                    .concatWith(Flux.fromIterable(existingDayOffs.values()));
            })
            .doOnError(e -> log.error("Error finding or creating DayOff for employee IDs: {}", employeeIds, e));
    }


    public Mono<Void> setNumberOffDayOff(UUID idEmployee, float newNumber) {
        return dayOffRepository.setNumberOfDayOff(idEmployee, newNumber);
    }

    public Mono<Void> setUseDayOff(UUID idEmployee, float newNumber) {
        return dayOffRepository.setUseDayOff(idEmployee, newNumber);
    }

    private Mono<DayOffDTO> findDayOff(UUID employeeId) {
        return findDayOff(employeeId, null, false);
    }

    private Mono<DayOffDTO> findDayOffS(UUID employeeId) {
        return findDayOff(employeeId, null, true);
    }

    @Transactional
    protected Mono<DayOffDTO> findDayOff(UUID employeeId, DayOff dayOff, Boolean checkIds) {
        log.info("Creating new DayOff for employee ID: {}", employeeId);
        return employeeProfileRepository.findById(employeeId)
            .flatMap(employeeProfile -> {
                log.info("Employee Profile: {}", employeeProfile.toString());
                return workspaceRepository.findByIdAndIsActiveTrue(employeeProfile.getWorkspaceId())
                    .flatMap(workspace -> {
                        log.info("Workspace: {}", workspace);
                        return annualLeaveRepository.findByWorkPlaceAndCompany(workspace.changeWorkspaceType(), employeeProfile.getCompany())
                            .flatMap(annualLeave -> {
                                DayOff newDayOff = new DayOff();
                                newDayOff.setId(UUID.randomUUID());
                                newDayOff.setEmployeeId(employeeId);
                                newDayOff.setAnnualLeave(annualLeave.getId());
                                newDayOff.setCreatedDate(ZonedDateTime.now());
                                newDayOff.setLastUpdated(ZonedDateTime.now());
                                newDayOff.setIsActive(true);

                                // Tính số ngày nghỉ phép
                                newDayOff = calculateDaysOff(
                                    newDayOff,
                                    annualLeave,
                                    employeeProfile,
                                    workspace.getWorkspaceType()
                                );

                                // Cập nhật năm và lưu vào repository
                                newDayOff.setAnnualLeave(annualLeave.getId());

                                if (newDayOff.getNumberDaysOff() == 0) {
                                    if (checkIds)
                                        return Mono.just(newDayOff.toDTO());
                                    return Mono.just(newDayOff.toDTO());
                                }
                                if (dayOff != null) {
                                    newDayOff.setId(dayOff.getId());
                                    newDayOff.setNowDaysOff(dayOff.getNowDaysOff());
                                    newDayOff.setUseDaysOff(dayOff.getUseDaysOff());
                                    newDayOff.setIsPersisted();
                                }

                                // Lưu đối tượng mới và chuyển đổi thành DTO
//                                            return Mono.just(newDayOff.toDTO());
                                if (checkIds)
                                    return dayOffRepository.save(newDayOff).map(DayOff::toDTONotCal);

                                return dayOffRepository.save(newDayOff).map(DayOff::toDTO);
                            });
                    });
            })
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Employee not found", "employee", employeeId.toString())));
    }


    private DayOff calculateDaysOff(DayOff newDayOff, AnnualLeave annualLeave, EmployeeProfile employeeProfile, WorkspaceType workType) {
        int month = annualLeave.getCarryForwardMonth(); // Lâý cái tháng reset
        if (month == 12)
            month = 11; // todo:fix this stupid bug

        Integer leaveAfterProbation = annualLeave.getLeaveAfterProbation(); // số phép sau thử việc
        Integer leavePerYear = annualLeave.getLeavePerYear(); // số phép reset mỗi năm

        LocalDate currentDate = LocalDate.of(LocalDate.now().getYear(), month + 1, 1);

        // cái này để xem coi phép năm đang của năm nào
        if (LocalDate.now().getMonthValue() < (annualLeave.getCarryForwardMonth() + 1)) {
            currentDate = LocalDate.of(LocalDate.now().getYear() - 1, month + 1, 1);
        }

        // Chỗ set mấy cái mặc định
        newDayOff.setUseDaysOff(0f);
        newDayOff.setNowDaysOff((float) month);
        newDayOff.setYear(currentDate.getYear());

        // bắt đầu tính
        LocalDate probationEndDate = employeeProfile.getStartWorkDate();
//        if (employeeProfile.getProbationDateFrom() == null && employeeProfile.getProbationDateTo() == null) {
////            if (employeeProfile.getContractDate().isBefore(employeeProfile.getStartWorkDate()))
////                probationEndDate = employeeProfile.getContractDate();
////            else
//                probationEndDate = employeeProfile.getStartWorkDate();
//
//        } else {
//            if (employeeProfile.getProbationDateTo() == null) {
//                probationEndDate = employeeProfile.getProbationDateFrom().plusMonths(2);
//                // Nếu ngày kết thúc thử việc sau ngày hiện tại, nghĩa là vẫn đang trong thời gian thử việc
//                if (probationEndDate.isAfter(LocalDate.now())) {
//                    newDayOff.setNumberDaysOff(0f);
//                    return newDayOff;
//                }
//                if (probationEndDate.equals(LocalDate.now())) {
//                    newDayOff.setNumberDaysOff(0f);
//                    return newDayOff;
//
//                }
//            } else {
//                probationEndDate = employeeProfile.getProbationDateTo();
//                // Nếu ngày kết thúc thử việc sau ngày hiện tại, nghĩa là vẫn đang trong thời gian thử việc
//                if (probationEndDate.isAfter(LocalDate.now())) {
//                    newDayOff.setNumberDaysOff(0f);
//                    return newDayOff;
//
//                }
//                if (probationEndDate.equals(LocalDate.now())) {
//                    newDayOff.setNumberDaysOff(0f);
//                    return newDayOff;
//
//                }
//            }
//        }

//        System.out.println("ProbationEndDate: " + probationEndDate);
        log.info("employeeProfile: {}", employeeProfile.getId());
        log.info("employeeProfile: {}", employeeProfile.getEmployeeCode());
        log.info("employeeProfile: {}", employeeProfile.getFullName());
        log.info("ProbationEndDate: {}", probationEndDate);
        // Nếu hoàn thành thử việc trong năm nay
        if (probationEndDate.isAfter(currentDate) && probationEndDate.isBefore(LocalDate.now())) {
            // Xử lý phép dựa trên ngày vào làm
            if (workType.equals(WorkspaceType.OFFICE)) {
                int startDay = probationEndDate.getDayOfMonth();
                int monthAdjustment = startDay <= 10 ? 1 : 0;

                Period period = Period.between(currentDate, probationEndDate);
                long monthsAfterProbation = period.getYears() * 12L + period.getMonths() + 1 - monthAdjustment;

                if ((period.getYears() * 12L + period.getMonths()) == 0) {
                    if (monthAdjustment == 0)
                        monthsAfterProbation = 1;
                    else
                        monthsAfterProbation = 0;
                }


//                long monthsAfterProbation = probationEndDate.getMonthValue() - monthAdjustment - month;

                System.out.println("\n\n\n\n\n" + monthsAfterProbation);

                newDayOff.setNumberDaysOff((float) (leaveAfterProbation * 12));
                newDayOff.setUseDaysOff((float) monthsAfterProbation * leaveAfterProbation);
                return newDayOff;

//                return (long) leaveAfterProbation * monthsAfterProbation;
            } else if (workType.equals(WorkspaceType.FACTORY)) {
                int startDay = probationEndDate.getDayOfMonth();
                float monthsAfterProbation;
                if (startDay == 1) {
                    // Nếu ngày bắt đầu làm là ngày 1 đầu tháng, khi ký hợp đồng sẽ tính có 1 ngày phép.
                    monthsAfterProbation = probationEndDate.getMonthValue() - 1 - month;
                } else if (startDay <= 16) {
                    // Nếu ngày vào làm là từ ngày 02 đến ngày 16, sẽ tính 0.5 phép.
                    monthsAfterProbation = (float) (probationEndDate.getMonthValue() - 0.5 - month);
//                    return (long) (leaveAfterProbation * monthsAfterProbation) / 2;
                } else {
                    // Nếu ngày vào làm từ ngày 17 trở đi, sẽ tính có 1 phép từ tháng tiếp theo.
                    monthsAfterProbation = probationEndDate.getMonthValue() - month;
                }
                newDayOff.setNumberDaysOff((float) (leaveAfterProbation * 12));
                newDayOff.setUseDaysOff(monthsAfterProbation * leaveAfterProbation);
                return newDayOff;

            }
        }

        if (workType.equals(WorkspaceType.FACTORY)) {
            newDayOff.setNumberDaysOff((float) (leaveAfterProbation * 12));
            return newDayOff;

        }

        // Tính số tháng đã làm việc
        long monthsWorked = ChronoUnit.MONTHS.between(probationEndDate, currentDate);

        // Tính số thâm niên (lấy phần nguyên khi chia cho 12)
//        long seniority = monthsWorked / 12; -- tạm kệ nó
//        log.info("Seniority: {} {}", monthsWorked, monthsWorked / 12);
        long seniority = employeeProfile.getOfficialWorkTypeDurationYear();

        // Tính tổng số ngày nghỉ phép dựa trên số thâm niên
        long totalLeaveDays = leavePerYear + Math.min(seniority, 17 - leavePerYear);

        // Đảm bảo số ngày phép không vượt quá 17
        newDayOff.setNumberDaysOff((float) Math.min(17, totalLeaveDays));

        return newDayOff;
    }

    public Mono<SeniorityDtoReponse> cacularotrSeniority(UUID workspaceId, LocalDate date) {
        return SecurityUtils.getCompanyId()
            .flatMap(companyId -> workspaceRepository.findByIdAndIsActiveTrue(workspaceId)
                .flatMap(workspace -> {
                    log.info("Workspace: {}", workspace);
                    return annualLeaveRepository.findByWorkPlaceAndCompany(workspace.changeWorkspaceType(), companyId)
                        .flatMap(annualLeave -> {
                            log.info("Calculated Seniority: {}", "");
                            LocalDate now = LocalDate.now();
                            int year = now.getYear();

                            LocalDate config = LocalDate.of(now.getYear(), annualLeave.getCarryForwardMonth() + 1, 1);
                            if (now.isBefore(config))
                                year = year - 1;

                            config = LocalDate.of(year, annualLeave.getCarryForwardMonth() + 1, 1);

                            // Tính số tháng đã làm việc
                            long monthsWorked = ChronoUnit.MONTHS.between(date, config);

                            // Tính số thâm niên (lấy phần nguyên khi chia cho 12)
                            long seniority = monthsWorked / 12;
                            SeniorityDtoReponse sen = new SeniorityDtoReponse(seniority);
                            return Mono.just(sen);
                        });
                })
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Workspace not found", "workspace", workspaceId.toString())))
            );
    }

    public Mono<Void> updateEmployeeProfile(UpdateEmployeeProfile updateEmployeeProfile) {
        if (updateEmployeeProfile.getContractDateNew() == null &&
            updateEmployeeProfile.getProbationDateToNew() == null &&
            updateEmployeeProfile.getProbationDateFromNew() == null) {
            return Mono.empty();
        }

        return dayOffRepository.findOneByEmployeeIdAndAndIsActive(updateEmployeeProfile.getEmployeeId(), true)
            .flatMap(dayOff -> findDayOff(updateEmployeeProfile.getEmployeeId(), dayOff, false))
            .then();
    }


    @Transactional
    public Mono<Void> resetDayOffEmployee(Integer monthReset) {
        return annualLeaveRepository.findAllByCarryForwardMonth(monthReset)
            .map(annualLeave ->
                dayOffRepository.resetDayOffEmployee(annualLeave.getId())
            )
            .then();
    }


}
