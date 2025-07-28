package com.masi.employee.service.mapper;

import com.masi.employee.domain.ConfirmLeave;
import com.masi.employee.domain.ConfirmLeaveAttachment;
import com.masi.employee.service.dto.ConfirmLeaveAttachmentDTO;
import com.masi.employee.service.dto.ConfirmLeaveDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfirmLeaveAttachment} and its DTO {@link ConfirmLeaveAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfirmLeaveAttachmentMapper extends EntityMapper<ConfirmLeaveAttachmentDTO, ConfirmLeaveAttachment> {
    @Mapping(target = "confirmLeave", source = "confirmLeave", qualifiedByName = "confirmLeaveId")
    ConfirmLeaveAttachmentDTO toDto(ConfirmLeaveAttachment s);

    @Named("confirmLeaveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConfirmLeaveDTO toDtoConfirmLeaveId(ConfirmLeave confirmLeave);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
