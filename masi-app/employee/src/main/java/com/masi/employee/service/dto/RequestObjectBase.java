package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.employee.domain.enumeration.ReviewStatus;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@ParameterObject
public class RequestObjectBase {
    protected UUID id;
    protected List<String> statuses;
    protected String searchString;
    @JsonIgnore
    protected String company;

    public RequestObjectBase(UUID id, List<String> statuses, String searchString) {
        this.id = id;
        this.statuses = statuses;
        this.searchString = searchString;
    }

    //empty constructor
    public RequestObjectBase() {
    }

    //getters and setters

    //equals and hashcode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestObjectBase that = (RequestObjectBase) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(searchString, that.searchString)) return false;
        return Objects.equals(statuses, that.statuses);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (statuses != null ? statuses.hashCode() : 0);
        result = 31 * result + (searchString != null ? searchString.hashCode() : 0);
        return result;
    }

    //toString method
    @Override
    public String toString() {
        return "ExplanationReviewRO{" +
            "id=" + id +
            ", statuses=" + statuses +
            ", searchString='" + searchString + '\'' +
            '}';
    }
}
