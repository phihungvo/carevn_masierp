package com.masi.sale.service;

import static com.masi.sale.domain.enumeration.CustomerStatus.DISABLED;
import static com.masi.sale.domain.enumeration.CustomerStatus.ENABLED;

import com.carevn.masi.utils.FileManager;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.sale.domain.Customer;
import com.masi.sale.domain.CustomerCodeFormats;
import com.masi.sale.repository.CustomerRepository;
import com.masi.sale.service.dto.CustomerDTO;
import com.masi.sale.service.dto.CustomerRO;
import com.masi.sale.service.event.NotificationAddedEvent;
import com.masi.sale.service.mapper.CustomerMapper;
import com.masi.sale.service.web.client.EmployeeClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final StreamBridge streamBridge;

    private final EmployeeClient employeeClient;
    private final FileManager exportFileManager;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, StreamBridge streamBridge, EmployeeClient employeeClient) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.streamBridge = streamBridge;
        this.employeeClient = employeeClient;
        try {
            exportFileManager = new FileManager("export");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //@Scheduled(cron = "0 0 3 25 * ?", zone = "Asia/Ho_Chi_Minh")
    public Mono<Void> sendBirthday() {
        log.info("Send birthday to customer");
        int currentMonth = LocalDate.now().getMonthValue();
        LocalDate startOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        LocalDate endOfNextMonth = startOfNextMonth.plusMonths(1).minusDays(1);
        Map<String, Object> data = new HashMap<>();
        data.put("fromDate", startOfNextMonth);
        data.put("toDate", endOfNextMonth);
        var thang = (currentMonth + 1);
        var eventBuilder = NotificationAddedEvent.builder()
            .id(UUID.randomUUID())
            .content("Danh sách khách hàng sinh nhật tháng " + thang)
            .title("Thông báo sinh nhật")
            .createdAt(ZonedDateTime.now())
            .entityId(null)
            .entityType("Customer")
            .entityName("Customer")
            .createdBy("system")
            .createdAt(ZonedDateTime.now())
            .recipients(null)
            .data(data)
            .action("BirthdayNotification")
            .sentBy("system");
        return employeeClient.getEmployees().map(e -> {
            return e.getId().toString();
        }).collectList().flatMap(list -> {
            if (list.isEmpty()) {
                return Mono.empty();
            }
            eventBuilder.recipients(list);

            log.info("Send birthday notification to employee: {}", list);

            streamBridge.send("notification-added", eventBuilder.build(), MediaType.APPLICATION_JSON);
            return Mono.just(list);
        }).then();

    }

    /**
     * Save a customer.
     *
     * @param customerDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomerDTO> save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        customerDTO.setContractSigned(LocalDate.now());
        customerDTO.setIsDeleted(false);
        customerDTO.setCustomerStatus(ENABLED);
        customerDTO.setLastUpdated(ZonedDateTime.now());
        customerDTO.setCreatedDate(ZonedDateTime.now());
        return customerRepository.save(customerMapper.toEntity(customerDTO)).map(customerMapper::toDto);
    }

    /**
     * Partially update a customer.
     *
     * @param customerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CustomerDTO> partialUpdate(CustomerDTO customerDTO) {
        log.debug("Request to partially update Customer : {}", customerDTO);

        return customerRepository
            .findById(customerDTO.getId())
            .map(existingCustomer -> {
                customerDTO.applyUpdate(existingCustomer);
                existingCustomer.setIsDeleted(false);
                existingCustomer.setLastUpdated(ZonedDateTime.now());
                existingCustomer.setIsPersisted();
                return existingCustomer;
            })
            .flatMap(customerRepository::save)
            .map(Customer::toDto);
    }

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customerRepository.findAllBy(pageable).map(customerMapper::toDto);
    }

    /**
     * Returns the number of customers available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return customerRepository.count();
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CustomerDTO> findOne(UUID id) {
        log.debug("Request to get Customer : {}", id);
        return
            SecurityUtils.getCompanyId().flatMap(
                company -> customerRepository.findByIdAndIsDeleted(id, company)
            ).map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<CustomerDTO> findByCustomerId(String id) {
        log.debug("Request to get Customer by code : {}", id);
        return
            SecurityUtils.getCompanyId().flatMap(
                company -> customerRepository.findByIsDeletedAndCustomerCodeAndCompany(id, company)
            ).map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<CustomerDTO> findByTaxCode(String taxCode) {
        log.debug("Request to get Customer by tax code: {}", taxCode);
        return
            SecurityUtils.getCompanyId().flatMap(
                company -> customerRepository.findFirstByTaxCodeAndIsDeletedIsFalseAndCompany(taxCode, company)
            ).map(customerMapper::toDto);
    }


    /**
     * Delete the customer by id.
     *
     * @param pageable the id of the entity.
     * @return a Mono to signal the deletion
     */

    public Flux<CustomerDTO> findAllByQuery(Pageable pageable, CustomerRO ro) {
        log.debug("Request to get all Customer by query: {}", ro);
        return customerRepository.findAllByFilter(ro, pageable).map(Customer::toDto).collectList().flatMap(arr -> {
                Map<UUID, LinkedList<CustomerDTO>> map = new HashMap<>();
                arr.forEach(customerDTO -> {
                    if (map.containsKey(customerDTO.getCustomerOwner())) {
                        map.get(customerDTO.getCustomerOwner()).add(customerDTO);
                    } else {
                        LinkedList<CustomerDTO> list = new LinkedList<>();
                        list.add(customerDTO);
                        map.put(customerDTO.getCustomerOwner(), list);
                    }
                });
                return employeeClient.getEmployeesByListIds(new ArrayList<>(map.keySet())).map(employeeDTO -> {
                    map.get(employeeDTO.getId()).forEach(customerDTO -> {
                        customerDTO.setCustomerOwnerDTO(employeeDTO);
                    });
                    return employeeDTO;
                }).then(Mono.just(arr));
            }
        ).flatMapMany(Flux::fromIterable);
    }

    public Mono<Long> countAllByQuery(CustomerRO ro) {
        log.debug("Request to count all Customer by query: {}", ro);
        return customerRepository.countAllByFilter(ro);
    }

    ///////////////////////////////////////////////////

    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Customer : {}", id);

        return customerRepository
            .findById(id)
            .flatMap(customer -> {
                if (customer.getCustomerStatus() == DISABLED) {
                    customer.setIsDeleted(true);
                    customer.setLastUpdated(ZonedDateTime.now());
                    customer.setIsPersisted();
                    return customerRepository.save(customer);
                }
                return Mono.error(new IllegalStateException("Customer is enabled and cannot be deleted."));
            })
            .then();
    }

    public Mono<Void> disabled(UUID id) {
        log.debug("Request to disabled Customer : {}", id);
        return customerRepository
            .findById(id)
            .flatMap(customer -> {
                customer.setCustomerStatus(DISABLED);
                customer.setLastUpdated(ZonedDateTime.now());
                customer.setIsPersisted();
                return customerRepository.save(customer);
            })
            .then();
    }

    public Mono<Void> activated(UUID id) {
        log.debug("Request to activated Customer : {}", id);
        return customerRepository
            .findById(id)
            .flatMap(customer -> {
                customer.setCustomerStatus(ENABLED);
                customer.setLastUpdated(ZonedDateTime.now());
                customer.setIsPersisted();
                return customerRepository.save(customer);
            })
            .then();
    }

    public Mono<Void> change(UUID id, UUID setCustomerOwnerId) {
        log.debug("Request to change Customer : {}", id);

        return customerRepository.findById(id)
                .flatMap(customer -> {
                    customer.setCustomerOwner(setCustomerOwnerId);
                    customer.setLastUpdated(ZonedDateTime.now());
                    customer.setIsPersisted();
                    return customerRepository.save(customer);
                })
                .then()
                .doOnSuccess(v -> sendNotification(setCustomerOwnerId))
                .then();
    }

    private Mono<Void> sendNotification(UUID customerOwnerId) {
        log.info("Send change customer owner notification to employee: {}", customerOwnerId);

        Map<String, Object> data = new HashMap<>();
        data.put("customerId", customerOwnerId);

        NotificationAddedEvent event = NotificationAddedEvent.builder()
                .id(UUID.randomUUID())
                .content("Bạn đã được chuyển quyền quản lý khách hàng")
                .title("Thông báo chuyển quyền quản lý khách hàng")
                .createdAt(ZonedDateTime.now())
                .entityId(null)
                .entityType("Customer")
                .entityName("Customer")
                .createdBy("system")
                .recipients(List.of(customerOwnerId.toString()))
                .data(data)
                .action("ChangeCustomerOwner")
                .sentBy("system")
                .build();

        return Mono.fromRunnable(() -> streamBridge.send("notification-added", event, MediaType.APPLICATION_JSON))
                .doOnError(e -> log.error("Error sending notification: {}", e.getMessage())).then();
    }


    @Transactional(readOnly = true)
    public Mono<String> export(CustomerRO ro, Pageable pageable) {

        return customerRepository
            .findAllByFilter(ro, null)
            .map(Customer::toDto)
            .collectList().flatMap(arr -> {
                    Map<UUID, LinkedList<CustomerDTO>> map = new HashMap<>();
                    arr.forEach(customerDTO -> {
                        if (map.containsKey(customerDTO.getCustomerOwner())) {
                            map.get(customerDTO.getCustomerOwner()).add(customerDTO);
                        } else {
                            LinkedList<CustomerDTO> list = new LinkedList<>();
                            list.add(customerDTO);
                            map.put(customerDTO.getCustomerOwner(), list);
                        }
                    });
                    return employeeClient.getEmployeesByListIds(new ArrayList<>(map.keySet())).map(employeeDTO -> {
                        map.get(employeeDTO.getId()).forEach(customerDTO -> {
                            customerDTO.setCustomerOwnerDTO(employeeDTO);
                        });
                        return employeeDTO;
                    }).then(Mono.just(arr));
                }
            )
            .flatMap(this::toXlsx);

    }

    private Mono<String> toXlsx(List<CustomerDTO> customerDTOs) {
        var headers = new String[]{
            "Mã khách hàng",
            "Tên công ty",
            "Địa chỉ",
            "Mã số thuế",
            "Tên người liên hệ",
            "Ngày sinh",
            "Email",
            "Số điện thoại",
            "Chức vụ",
            "Người phụ trách",
            "Ngày tạo",
            "Hiệu lực từ",
            "Hiệu lực đến",
            "Ghi chú",
            "Trạng thái"

        };

        var byteOutputStream = new ByteArrayOutputStream();
        try (
            Workbook workbook = new XSSFWorkbook()) {
            var mainSheet = workbook.createSheet("Danh sách khách hàng");
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

            for (CustomerDTO dto : customerDTOs) {
                var row = mainSheet.createRow(rowNum++);
                var createDefaultCell = new Function<Integer, Cell>() {
                    @Override
                    public Cell apply(Integer integer) {
                        var cell = row.createCell(integer);
                        cell.setCellStyle(defaultStyle);
                        return cell;
                    }
                };
                createDefaultCell.apply(0).setCellValue(dto.getCustomerCode());
                createDefaultCell.apply(1).setCellValue(dto.getCompanyName());
                createDefaultCell.apply(2).setCellValue(dto.getAddress());
                createDefaultCell.apply(3).setCellValue(dto.getTaxCode());
                createDefaultCell.apply(4).setCellValue(dto.getLastName() + " " + dto.getFirstName());
                createDefaultCell.apply(5).setCellValue(dateToString(dto.getBirthday()));
                createDefaultCell.apply(6).setCellValue(dto.getEmail());
                createDefaultCell.apply(7).setCellValue(dto.getPhoneNumber());
                createDefaultCell.apply(8).setCellValue(dto.getPosition());
                createDefaultCell.apply(9).setCellValue(dto.getCustomerOwnerDTO() != null ? dto.getCustomerOwnerDTO().getFullName() : "");
                createDefaultCell.apply(10).setCellValue(dateToString(dto.getContractFrom()));
                createDefaultCell.apply(11).setCellValue(dateToString(dto.getContractTo()));
                createDefaultCell.apply(12).setCellValue(dateToString(dto.getContractSigned()));
                createDefaultCell.apply(13).setCellValue(dto.getNote());
                createDefaultCell.apply(14).setCellValue(dto.getCustomerStatus().toVietnamese());
                row.setHeight((short) (row.getHeight() * 1.4));
            }
            for (int i = 0; i < headers.length; i++) {
                mainSheet.autoSizeColumn(i);
            }
            workbook.write(byteOutputStream);
            workbook.close();
            return exportFileManager.saveFile("employee_profiles.xlsx", byteOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String dateToString(LocalDate date) {
        return date == null ? "" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public Mono<String> getNextCustomerCode(String company) {
        return customerRepository
            .findTemPlateByCompany(company)
            .collectList()
            .flatMap(customerCodes -> {
                if (customerCodes.isEmpty()) {
                    return Mono.empty();
                }
                CustomerCodeFormats customerCode = customerCodes.get(0);
                String nextCustomerCode = String.format(customerCode.getFormatCustomerCode(), customerCode.getCurrentValue());
                // Update current value logic here, if needed
                return Mono.just(nextCustomerCode);
            });
    }

    public Flux<CustomerDTO> findAllBirthday(LocalDate fromDate, LocalDate toDate, UUID id_employee) {
        return customerRepository.findAllByBirthday(fromDate, toDate, id_employee).map(Customer::toDto);
    }

    public Mono<Long> countAllBirthday(LocalDate fromDate, LocalDate toDate, UUID userId) {
        log.debug("Request to count all Customer by query: {}", userId);
        return customerRepository.countAllByFilter(fromDate, toDate, userId);
    }
}
