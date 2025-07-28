package com.masi.employee.service.mapper;

import com.masi.employee.domain.ProfileAttachment;
import com.masi.employee.service.dto.ProfileAttachmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileAttachment} and its DTO {@link ProfileAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileAttachmentMapper extends EntityMapper<ProfileAttachmentDTO, ProfileAttachment> {}
