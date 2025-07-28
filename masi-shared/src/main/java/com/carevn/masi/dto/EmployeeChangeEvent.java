package com.carevn.masi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeChangeEvent  implements Serializable {
    public  static final String EVENT_NAME = "EmployeeChangeEvent";
    @Serial
    private static final long serialVersionUID = 7239901107437178821L;

    private boolean isNew;
    @Id
    private UUID id;
    private String fullName;

    private String gender;
    private UUID workspaceId;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String company;
    private String department;


    public String getFirstName() {
        var split = this.fullName.trim().replaceAll("\\s+", " ").split(" ");
        if (split.length == 0) {
            return "";
        }
        return split[split.length - 1];
    }

    public String getLastName() {
        var split = this.fullName.trim().replaceAll("\\s+", " ").split(" ");
        if (split.length == 0) {
            return "";
        }
        StringBuilder lastName = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            lastName.append(split[i]);
            if (i < split.length - 2) {
                lastName.append(" ");
            }
        }
        return lastName.toString();
    }
}
