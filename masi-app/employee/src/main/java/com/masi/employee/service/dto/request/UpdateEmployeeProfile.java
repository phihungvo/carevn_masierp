package com.masi.employee.service.dto.request;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.helper.ObjectComparator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployeeProfile {
    private UUID employeeId;
    private LocalDate contractDateOld;
    private LocalDate contractDateNew;

    private LocalDate startWorkDateOld;
    private LocalDate startWorkDateNew;

    private LocalDate probationDateFromOld;
    private LocalDate probationDateFromNew;

    private LocalDate probationDateToOld;
    private LocalDate probationDateToNew;


    public void applyDiff(EmployeeProfile oldProfile, EmployeeProfile newProfile) {
        var listDiff = ObjectComparator.compareObjects(oldProfile, newProfile);
        var formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (listDiff.isEmpty()) {
            return;
        }
        listDiff.forEach(diff -> {
            switch (diff.getFieldName().toLowerCase(Locale.ROOT)) {
                case "contractdate":
                    this.contractDateOld = diff.getOldValue().equals("null") ? null : LocalDate.parse(diff.getOldValue(), formatter);
                    this.contractDateNew = diff.getNewValue().equals("null") ? null : LocalDate.parse(diff.getNewValue(), formatter);
                    break;
                case "startworkdate":
                    this.startWorkDateOld = diff.getOldValue().equals("null") ? null : LocalDate.parse(diff.getOldValue(), formatter);
                    this.startWorkDateNew = diff.getNewValue().equals("null") ? null : LocalDate.parse(diff.getNewValue(), formatter);
                    break;
                case "probationdatefrom":
                    this.probationDateFromOld = diff.getOldValue().equals("null") ? null : LocalDate.parse(diff.getOldValue(), formatter);
                    this.probationDateFromNew = diff.getNewValue().equals("null") ? null : LocalDate.parse(diff.getNewValue(), formatter);
                    break;
                case "probationdateto":
                    this.probationDateToOld = diff.getOldValue().equals("null") ? null : LocalDate.parse(diff.getOldValue(), formatter);
                    this.probationDateToNew = diff.getNewValue().equals("null") ? null : LocalDate.parse(diff.getNewValue(), formatter);
                    break;
            }
        });

    }
}
