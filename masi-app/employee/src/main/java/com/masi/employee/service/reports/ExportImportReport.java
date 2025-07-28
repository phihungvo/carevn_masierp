package com.masi.employee.service.reports;

import java.util.UUID;

import jakarta.persistence.Column;
import lombok.Data;
@Data
public class ExportImportReport {
     @Column(name = "id")
        private UUID id;
        @Column(name = "name")
        private String name;
        @Column(name = "total")
        private Long total;
        @Column(name = "type")
        private String type;
    
}
