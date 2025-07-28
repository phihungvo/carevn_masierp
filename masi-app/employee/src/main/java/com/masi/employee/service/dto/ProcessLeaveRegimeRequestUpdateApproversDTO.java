package com.masi.employee.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.carevn.masi.dto.UserJWTDetail;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;

/**
 * A DTO for the {@link com.masi.employee.domain.ProcessLeaveRegimeRequest}
 * entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessLeaveRegimeRequestUpdateApproversDTO implements Serializable {

    private Collection<UUID> approverIds;

    public ArrayList<ProcessLeaveRegimeRequestDTO> toListDTO(UUID id, UserJWTDetail user) {
        var listDTO = new ArrayList<ProcessLeaveRegimeRequestDTO>();
        for (var approverId : approverIds) {
            var dto = new ProcessLeaveRegimeRequestDTO();
            listDTO.add(dto.init(id, id, approverId));
        }
        return listDTO;
    }
}
