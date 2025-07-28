package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

//public enum ProfileAttachmentType {
//    CMND,
//    HK,
//    SYLL,
//    DON_XV,
//    GKSK,
//    GCK,
//}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileAttachmentBase64DTO implements Serializable {
    public static class Base64File {
        public String fileName;
    }

    @NotNull(message = "EmployeeProfileId is required")
    private UUID employeeProfileId;
    private String cmndFileName;
    private String hkFileName;
    private String syllFileName;
    private String donXVFileName;
    private String gkskFileName;
    private String gckFileName;
    private Collection<Base64File> others;

    public Collection<ProfileAttachmentDTO> toListAttachmentDTO() {
        Collection<ProfileAttachmentDTO> listAttachment = new ArrayList<>();
        if (cmndFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.CMND)
                    .path(cmndFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (hkFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.HK)
                    .path(hkFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (syllFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.SYLL)
                    .path(syllFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (donXVFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.DON_XV)
                    .path(donXVFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (gkskFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.GKSK)
                    .path(gkskFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (gckFileName != null) {
            listAttachment.add(
                ProfileAttachmentDTO.builder()
                    .employeeProfileId(employeeProfileId)
                    .type(ProfileAttachmentType.GCK)
                    .path(gckFileName)
                    .id(UUID.randomUUID())
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .isDeleted(false)
                    .build()
            );
        }
        if (others != null) {
            for (Base64File other : others) {
                listAttachment.add(
                    ProfileAttachmentDTO.builder()
                        .employeeProfileId(employeeProfileId)
                        .type(ProfileAttachmentType.OTHER)
                        .path(other.fileName)
                        .id(UUID.randomUUID())
                        .createdAt(ZonedDateTime.now())
                        .updatedAt(ZonedDateTime.now())
                        .isDeleted(false)
                        .build()
                );
            }
        }
        return listAttachment;
    }


}
