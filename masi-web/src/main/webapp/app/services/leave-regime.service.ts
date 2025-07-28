import { leaveRegimeEndpoints } from 'app/constants/endpoints';
import { ILeaveRegime, ILeaveRegimeParams, ILeaveRegimeReview, IProcessLeaveRegimeRequest } from 'app/shared/model/leave-regime.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getLeaveRegimes = async (filter?: ILeaveRegimeParams) => {
  try {
    const url = leaveRegimeEndpoints.getLeaveRegimes;

    const response = await axios.get<PaginationResponse<ILeaveRegime>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        ...(filter?.leaveType && { leaveType: filter.leaveType }),
        ...(filter?.startDate && { startDate: filter.startDate }),
        ...(filter?.endDate && { endDate: filter.endDate }),
        ...(filter?.workspaceId && { workspaceId: filter.workspaceId }),
        ...(filter?.status && { status: filter.status }),
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

const getLeaveRegimeById = async (id: string) => {
  try {
    const url = leaveRegimeEndpoints.getLeaveRegimeById(id);

    const response = await axios.get<ILeaveRegime>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postLeaveRegime = async (data: ILeaveRegime) => {
  const url = leaveRegimeEndpoints.postLeaveRegime;

  return await axios.post(url, data);
};

const patchLeaveRegime = async (data: ILeaveRegime, id: string) => {
  const url = leaveRegimeEndpoints.patchLeaveRegime(id);

  return await axios.patch(url, data);
};

const deleteLeaveRegime = async (id: string) => {
  const url = leaveRegimeEndpoints.deleteLeaveRegime(id);

  return await axios.delete(url);
};

const cancelLeaveRegime = async (id: string) => {
  const url = leaveRegimeEndpoints.cancelLeaveRegime(id);

  return await axios.patch(url);
};

const processLeaveRegime = async (id: string, data: IProcessLeaveRegimeRequest) => {
  const url = leaveRegimeEndpoints.processLeaveRegime(id);

  return await axios.patch(url, data);
};

const reviewLeaveRegime = async (id: string, data: ILeaveRegimeReview) => {
  const url = leaveRegimeEndpoints.reviewLeaveRegime(id);

  return await axios.patch(url, data);
};

export default {
  getLeaveRegimes,
  getLeaveRegimeById,
  postLeaveRegime,
  patchLeaveRegime,
  deleteLeaveRegime,
  cancelLeaveRegime,
  processLeaveRegime,
  reviewLeaveRegime,
};
