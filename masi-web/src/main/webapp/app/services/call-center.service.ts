import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { callCenterEndpoints } from 'app/constants/endpoints';
import {
  ICallCenter,
  ICallCenterFilterParams,
  IPatchCallCenterDto,
  IPostCallCenterDto,
} from 'app/shared/model/call-center.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getCallCenters = async (filter: ICallCenterFilterParams) => {
  try {
    const url = callCenterEndpoints.getCallCenters;
    const response = await axios.get<PaginationResponse<ICallCenter>>(url, {
      params: {
        page: filter?.page ?? DEFAULT_PAGE,
        size: filter?.size ?? DEFAULT_PAGE_SIZE_NAX,
        ...filter,
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

const postCallCenters = async (data: IPostCallCenterDto) => {
  const url = callCenterEndpoints.postCallCenter;

  return await axios.post<ICallCenter>(url, data);
};

const patchCallCenters = async (data: IPatchCallCenterDto, id: string) => {
  const url = callCenterEndpoints.patchCallCenter(id);

  return await axios.patch<ICallCenter>(url, data);
};

const getCallCenterById = async (id: string) => {
  try {
    const url = callCenterEndpoints.getCallCenterById(id);
    const response = await axios.get<ICallCenter>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteCallCenter = async (id: string) => {
  const url = callCenterEndpoints.deleteCallCenter(id);

  return await axios.delete(url);
};

const approveCallCenter = async (id: string) => {
  const url = callCenterEndpoints.approveCallCenter(id);

  return await axios.patch(url);
};

const completeCallCenter = async (id: string) => {
  const url = callCenterEndpoints.completeCallCenter(id);

  return await axios.patch(url);
};

const closeCallCenter = async (id: string) => {
  const url = callCenterEndpoints.closeCallCenter(id);

  return await axios.patch(url);
};

export default {
  getCallCenters,
  postCallCenters,
  patchCallCenters,
  getCallCenterById,
  deleteCallCenter,
  approveCallCenter,
  completeCallCenter,
  closeCallCenter,
};
