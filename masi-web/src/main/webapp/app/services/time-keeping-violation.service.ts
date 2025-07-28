import { timeKeepingViolationsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ITimeKeepingViolation, ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import axios from 'axios';

// Lấy danh sách vi phạm chám công
const getTimeKeepingViolations = async (filter?: ITimeKeepingViolationsParams) => {
  const url = timeKeepingViolationsEndpoints.getTimeKeepingViolations;

  try {
    const response = await axios.get<PaginationResponse<ITimeKeepingViolation>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        ...(filter.fromDate && { fromDate: filter.fromDate }),
        ...(filter.toDate && { toDate: filter.toDate }),
        ...(filter.type?.length && { type: filter.type }),
        ...(filter.employeeIds?.length && { employeeIds: filter.employeeIds }),
        ...(filter.workspaceIds?.length && { workspaceIds: filter.workspaceIds }),
        ...(filter.explanationId && { explanationId: filter.explanationId }),
        explained: filter.explained
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy danh sách vi phạm chấm công theo giải trình
const getTimeKeepingViolationsByExplanation = async (explanationId: string) => {
  const url = timeKeepingViolationsEndpoints.getTimeKeepingViolationsByExplanation(explanationId);

  try {
    const response = await axios.get<PaginationResponse<ITimeKeepingViolation>>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getTimeKeepingViolations,
  getTimeKeepingViolationsByExplanation,
};
