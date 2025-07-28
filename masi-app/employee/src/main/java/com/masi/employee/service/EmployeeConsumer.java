package com.masi.employee.service;

import com.masi.employee.domain.enumeration.EmployeeAccountStatus;
import com.masi.employee.repository.EmployeeProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class EmployeeConsumer {
    private EmployeeProfileService employeeProfileService;
    private EmployeeProfileRepository employeeProfileRepository;

    @KafkaListener(id = "masi-employee", topics = "userStatusChange-out-0", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, Map> data) {
        log.info("Received data: {}", data.value());
        try {
            String id = (String) data.value().getOrDefault("id", Strings.EMPTY);
            Boolean active = (Boolean) data.value().getOrDefault("active", true);
            Boolean deleted = (Boolean) data.value().getOrDefault("deleted", false);
            this.employeeProfileRepository.findById(UUID.fromString(id)).flatMap(employee -> {
                if (deleted) {
                    employee.setAccountStatus(EmployeeAccountStatus.NOT_HAVING_ACCOUNT.name());
                } else if (active) {
                    employee.setAccountStatus(EmployeeAccountStatus.ENABLED.name());
                } else {
                    employee.setAccountStatus(EmployeeAccountStatus.DISABLED.name());
                }
                return this.employeeProfileRepository.save(employee);
            }).subscribe();
        } catch (Exception e) {
            log.error("Error while updating employee status: {}", e.getMessage());
        }

    }

}
