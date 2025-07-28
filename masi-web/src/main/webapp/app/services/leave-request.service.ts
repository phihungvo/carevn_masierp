import axios from 'axios';

import {
  ILeaveRequest,
  ILeaveRequestParams,
  IPatchLeaveRequestDto,
  IPatchLeaveRequestResponse,
  IPatchLeaveRequestReviewDto,
  IPostLeaveRequestDto,
  IPostLeaveRequestResponse,
  IPutLeaveRequestDto,
  IPutLeaveRequestResponse,
} from 'app/shared/model/leave-request.model';
import { leaveRequestEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';

// Lấy danh sách đơn nghỉ phép
const getLeaveRequests = async (filter?: ILeaveRequestParams) => {
  try {
    const url = leaveRequestEndpoints.getLeaveRequests;
    const response = await axios.get<PaginationResponse<ILeaveRequest>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        type: filter?.type,
        status: filter?.status,
        workspaceType: filter?.workspaceType,
        sort: filter?.sort,
        workspaceIds: filter?.workspaceIds,
        employeeIds: filter?.employeeIds,
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

// Tạo Đơn Nghỉ Phép
const postLeaveRequest = async (data: IPostLeaveRequestDto) => {
  const url = leaveRequestEndpoints.postLeaveRequest;

  return await axios.post<IPostLeaveRequestResponse>(url, data);
};

// Lấy Chi Tiết Đơn Nghỉ Phép
const getLeaveRequestById = async (id: string) => {
  try {
    const url = leaveRequestEndpoints.getLeaveRequestById(id);
    const response = await axios.get<ILeaveRequest>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy tổng số ngày nghỉ.
const getLeaveRequestCountDayEndOff = async (filter?: ILeaveRequestParams) => {
  try {
    const url = leaveRequestEndpoints.getLeaveRequestCountDayEndOff;
    const response = await axios.get<ILeaveRequest>(url, {
      params: {
        ...(filter?.fromDate && { fromDate: filter?.fromDate }),
        ...(filter?.totalDay && { totalDay: filter?.totalDay }),
        ...(filter?.type && { type: filter?.typeLeave }),
        ...(filter?.employeeId && { employeeId: filter?.employeeId }),
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Cập Nhật Đơn Nghỉ Phép
const putLeaveRequest = async (data: IPutLeaveRequestDto & { id: string }) => {
  const url = leaveRequestEndpoints.putLeaveRequest(data.id);

  return await axios.put<IPutLeaveRequestResponse>(url, data);
};

// Cập Nhật Đơn Nghỉ Phép
const patchLeaveRequest = async (data: IPatchLeaveRequestDto & { id: string }) => {
  const url = leaveRequestEndpoints.patchLeaveRequest(data.id);

  return await axios.patch<IPatchLeaveRequestResponse>(url, data);
};

// Xét Duyệt Đơn Nghỉ Phép
const patchLeaveRequestReview = async (
  leaving_request_id: string,
  status: IPatchLeaveRequestReviewDto['status'],
  reason: IPatchLeaveRequestReviewDto['reason'],
) => {
  const url = leaveRequestEndpoints.patchLeaveRequestReview(leaving_request_id);

  return await axios.patch<IPatchLeaveRequestResponse>(url, { status, reason });
};

// Huỷ đơn nghỉ phép
const patchLeaveRequestCancel = async (id: string) => {
  const url = leaveRequestEndpoints.patchLeaveRequestCancel(id);

  return await axios.patch<IPatchLeaveRequestResponse>(url);
};

// Xoá Đơn Nghỉ Phép
const deleteLeaveRequest = async (id: string) => {
  const url = leaveRequestEndpoints.deleteLeaveRequest(id);

  return await axios.delete(url);
};

const countDayOff = async (fromDate: string, toDate: string, employeeId: string, type: string) => {
  const url = leaveRequestEndpoints.countDayOff;
  try {
    const response = await axios.get(url, {
      params: {
        fromDate,
        toDate,
        employeeId,
        type,
      },
    });

    return response.data.count as number;
  }
  catch (error) {
    console.error(error);
  }
}

export default {
  getLeaveRequests,
  postLeaveRequest,
  getLeaveRequestById,
  putLeaveRequest,
  patchLeaveRequest,
  patchLeaveRequestReview,
  patchLeaveRequestCancel,
  deleteLeaveRequest,
  countDayOff,
  getLeaveRequestCountDayEndOff
};
