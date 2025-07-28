package com.carevn.masi.service.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
public class UserStatusChangeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 21312465846L;
    private UUID Id = UUID.randomUUID();
    private Boolean active = true;
    private Boolean deleted = false;
    //todo: add more fields
}
