package com.masi.logistics.service.dto;

import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.RequestApproval;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class CreateReviewRequest {
    @Data
    public static class ReviewRequestDetail {

        private UUID employeeId;
        private int index = 0;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            ReviewRequestDetail that = (ReviewRequestDetail) o;

            return new EqualsBuilder().append(employeeId, that.employeeId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(employeeId).toHashCode();
        }

        public RequestApproval toEntity(UUID documentId, UserJWTDetail userJWTDetail) {
            RequestApproval requestApproval = new RequestApproval();
            requestApproval.setDocumentId(documentId);
            requestApproval.setCreatedBy(userJWTDetail.getUserId().toString());
            requestApproval.setCreatedDate(ZonedDateTime.now());
            requestApproval.setDepartment(userJWTDetail.getGroupId());
            requestApproval.setIndex(index);
            requestApproval.setCompany(userJWTDetail.getCompanyId());
            requestApproval.setEmployeeId(employeeId);
            requestApproval.setIsDeleted(false);

            return requestApproval;
        }
    }

    private UUID documentId;

    private Collection<UUID> employeeIds;


    public Mono<Set<RequestApproval>> toSetReviewAsync() {
        return SecurityUtils.getUserJWTDetail().map(userJWTDetail -> {
            return employeeIds.stream().map(employeeId -> {
                ReviewRequestDetail reviewRequestDetail = new ReviewRequestDetail();
                reviewRequestDetail.setEmployeeId(employeeId);
                return reviewRequestDetail.toEntity(documentId, userJWTDetail);
            }).collect(Collectors.toSet());
        });
    }
}
