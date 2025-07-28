import { suppliesRequestEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ISuppliesRequest, ISuppliesRequestParams, SuppliesDetail, SuppliesRequestTypes } from 'app/shared/model/supplies-request.model';
import axios from 'axios';

const getSuppliesRequestTypes = (params: ISuppliesRequestParams) => () => {
  return axios.get<PaginationResponse<SuppliesRequestTypes>>(
    suppliesRequestEndpoints.getSuppliesRequestTypes,
    {
      params
    })
}

const getSuppliesRequest = async (params: ISuppliesRequestParams) => {
  try {
    const url = suppliesRequestEndpoints.getSuppliesRequests;

    const response = await axios.get<PaginationResponse<ISuppliesRequest>>(url, {
      params,
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getSuppliesRequestById = async (id: string) => {
  try {
    const url = suppliesRequestEndpoints.getSuppliesRequestById(id);

    const response = await axios.get<SuppliesDetail>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createSuppliesRequest = (suppliesRequest: ISuppliesRequest) => {
  const url = suppliesRequestEndpoints.postSuppliesRequest;

  return axios.post<ISuppliesRequest>(url, suppliesRequest);
};

const updateSuppliesRequest = async (suppliesRequest: ISuppliesRequest, id: string) => {
  const url = suppliesRequestEndpoints.patchSuppliesRequest(id);

  return axios.patch<ISuppliesRequest>(url, suppliesRequest);
};

const deleteSuppliesRequest = async (id: string) => {
  const url = suppliesRequestEndpoints.deleteSuppliesRequest(id);

  return axios.delete(url);
};

const reviewSuppliesRequest = async (id: string, data: any) => {
  const url = suppliesRequestEndpoints.patchSuppliesRequestReview(id);

  return axios.patch(url, data);
}

const approveSignSuppliesRequest = async (id: string, data: any) => {
  const url = suppliesRequestEndpoints.patchSuppliesRequestApprovalSign(id);

  return axios.patch(url, data);
}

const rejectSignSuppliesRequest = async (id: string, data: any) => {
  const url = suppliesRequestEndpoints.patchSuppliesRequestRejectSign(id);

  return axios.patch(url, data);
}

const cancelSuppliesRequest = (id: string) => () => {
  const url = suppliesRequestEndpoints.patchSuppliesRequestCancel(id);

  return axios.patch(url);
}

const nextCodeSuppliesRequest = () => {
  const url = suppliesRequestEndpoints.nextCodeSuppliesRequest();

  return axios.get(url);
}

const exportExcelSuppliesRequest = () => {
  const url = suppliesRequestEndpoints.exportExcelSuppliesRequest();

  return axios.get(url, {
    params: {
      download: true,
    },
    responseType: 'blob',
    headers: {
      Accept: 'application/octet-stream',
    },
  });
}

export default {
  getSuppliesRequest,
  getSuppliesRequestById,
  createSuppliesRequest,
  updateSuppliesRequest,
  deleteSuppliesRequest,
  reviewSuppliesRequest,
  approveSignSuppliesRequest,
  getSuppliesRequestTypes,
  rejectSignSuppliesRequest,
  cancelSuppliesRequest,
  exportExcelSuppliesRequest,
  nextCodeSuppliesRequest
};
