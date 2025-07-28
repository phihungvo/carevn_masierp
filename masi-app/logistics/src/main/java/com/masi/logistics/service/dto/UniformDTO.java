package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID uomId;

    private UomDTO uomDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    private Double basePrice;

    private UUID uomGroupId;




    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniformDTO)) {
            return false;
        }

        UniformDTO uniformDTO = (UniformDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniformDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniformDTO{" +
                "id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", createAt='" + getCreateAt() + "'" +
                ", createBy='" + getCreateBy() + "'" +
                ", updateAt='" + getUpdateAt() + "'" +
                ", updateBy='" + getUpdateBy() + "'" +
                ", deleteAt='" + getDeleteAt() + "'" +
                ", deleteBy='" + getDeleteBy() + "'" +
                ", company='" + getCompany() + "'" +
                ", department='" + getDepartment() + "'" +
                "}";
    }
}
