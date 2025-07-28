package com.masi.utility.service.mapper;

import com.masi.utility.domain.FileAttachment;
import com.masi.utility.service.dto.FileAttachmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileAttachment} and its DTO {@link FileAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileAttachmentMapper extends EntityMapper<FileAttachmentDTO, FileAttachment> {}
