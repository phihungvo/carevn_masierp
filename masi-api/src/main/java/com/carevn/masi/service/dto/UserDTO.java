package com.carevn.masi.service.dto;

import com.carevn.masi.domain.User;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String userName;
    private String firstName;
    private String lastName;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json companyJson;

    private Boolean isActivated;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.companyJson = user.getCompanyJson();
        this.isActivated = user.isActivated();

    }


}
