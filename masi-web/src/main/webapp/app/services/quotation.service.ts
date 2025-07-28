import axios from 'axios';

import {
  IQuotation,
  IQuotationCreate,
  IQuotationCustomerProcess,
  IQuotationInternalApprove,
  IQuotationInternalReject,
  IQuotationParams,
} from 'app/shared/model/quotation.model';
import { quotationsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';

const getQuotations = async (filter?: IQuotationParams) => {
  try {
    const url = quotationsEndpoints.getQuotations;
    const response = await axios.get<PaginationResponse<IQuotation>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.status?.length && { status: filter.status }),
        ...(filter?.companyId && { companyId: filter.companyId }),
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

const postQuotation = async (data: IQuotationCreate) => {
  const url = quotationsEndpoints.postQuotation;
  return await axios.post<IQuotation>(url, data);
};

const getQuotationById = async (id: string) => {
  try {
    const url = quotationsEndpoints.getQuotationById(id);
    const response = await axios.get<IQuotation>(url);
    return response;
  } catch (error) {
    console.error(error);
  }
};

const patchQuotation = async (id: string, data: IQuotationCreate) => {
  const url = quotationsEndpoints.patchQuotation(id);
  return await axios.patch<IQuotation>(url, data);
};

const deleteQuotation = async (id: string) => {
  const url = quotationsEndpoints.deleteQuotation(id);
  return await axios.delete(url);
};

const internalSendQuotation = async (id: string) => {
  const url = quotationsEndpoints.patchQuotationInternalSend(id);
  return await axios.patch(url, {
    approverId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
  });
};

const cancelQuotation = async (id: string) => {
  const url = quotationsEndpoints.patchQuotationCancel(id);
  return await axios.patch(url);
};

const customerSend = async (id: string) => {
  const url = quotationsEndpoints.patchQuotationSend(id);
  return await axios.patch(url);
};

const internalApprove = async (id: string, data: IQuotationInternalApprove) => {
  const url = quotationsEndpoints.patchInternalApproved(id);
  return await axios.patch(url, data);
};

const internalReject = async (id: string, data: IQuotationInternalReject) => {
  const url = quotationsEndpoints.patchInternalReject(id);
  return await axios.patch(url, data);
};

const customerProcess = async (id: string, data: IQuotationCustomerProcess) => {
  const url = quotationsEndpoints.patchCustomerApproved(id);
  return await axios.patch(url, data);
};

const getQuotationExport = async (id: string, download: boolean) => {
  try {
    const url = quotationsEndpoints.getQuotationExport(id, download);
    const response = await axios.get(url, {
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

export default {
  getQuotations,
  postQuotation,
  getQuotationById,
  patchQuotation,
  deleteQuotation,
  internalSendQuotation,
  cancelQuotation,
  customerSend,
  internalApprove,
  internalReject,
  customerProcess,
  getQuotationExport,
};
