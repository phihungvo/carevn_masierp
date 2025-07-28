import axios from 'axios';

import {
  ITimeKeepingMonthly,
  ITimeKeepingMonthlyApprove,
  ITimeKeepingMonthlyParams,
  ITimeKeepingMonthlyReviewDto,
} from 'app/shared/model/time-keeping-monthly.model';
import { timeKeepingMonthlyEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';

// Lấy danh sách chấm công theo tháng
const getTimeKeepingMonthly = async (filter?: ITimeKeepingMonthlyParams) => {
  const url = timeKeepingMonthlyEndpoints.getTimeKeepingMonthly;

  try {
    const response = await axios.get<PaginationResponse<ITimeKeepingMonthly>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        type: filter?.type,
        ...(filter.month && { month: filter.month }),
        workspaceType: filter?.workspaceType,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Chi tiết chấm công tháng
const getTimeKeepingMonthlyById = async (id: string) => {
  const url = timeKeepingMonthlyEndpoints.getTimeKeepingMonthlyById(id);

  try {
    const response = await axios.get<ITimeKeepingMonthly>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy danh sách chấm công theo tháng
const getTimeKeepingMonthlyExport = async (filter?: ITimeKeepingMonthlyParams) => {
  const url = timeKeepingMonthlyEndpoints.getTimeKeepingMonthlyExport;

  try {
    const response = await axios.get<string>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        type: filter?.type,
        ...(filter.month && { month: filter.month }),
        workspaceType: filter?.workspaceType,
      },
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Duyệt chấm công tháng
const postTimeKeepingMonthlyReview = async (data: ITimeKeepingMonthlyReviewDto) => {
  const url = timeKeepingMonthlyEndpoints.reviewTimeKeepingMonthlyApprove;

  return await axios.post(url, data);
};

const postApproveTimeKeepingMonthly = async (data: ITimeKeepingMonthlyApprove) => {
  const url = timeKeepingMonthlyEndpoints.approveTimesheetBulk;

  return await axios.post(url, data);
};

const postRejectTimeKeepingMonthly = async (data: ITimeKeepingMonthlyApprove) => {
  const url = timeKeepingMonthlyEndpoints.rejectTimesheetBulk;

  return await axios.post(url, data);
};

export default {
  getTimeKeepingMonthly,
  postTimeKeepingMonthlyReview,
  postApproveTimeKeepingMonthly,
  postRejectTimeKeepingMonthly,
  getTimeKeepingMonthlyExport,
  getTimeKeepingMonthlyById,
};
