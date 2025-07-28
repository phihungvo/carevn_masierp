import {
  uniformEndpoints,
  uniformOrderEndpoints,
  uniformOrderStockEndpoints,
  uniformReleaseEndpoints,
  uniformReturnEndpoints,
  uniformStockEndpoints,
} from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IUniform,
  IUniformOrder,
  IUniformOrderCreate,
  IUniformOrderParams,
  IUniformOrderProcess,
  IUniformOrderStock,
  IUniformOrderUpdate,
  IUniformParams,
  IUniformRelease,
  IUniformReleaseParams,
  IUniformReturn,
  IUniformStock,
  IUniformStockParams,
} from 'app/shared/model/uniform.model';
import axios from 'axios';

const getUniforms = async (filter?: IUniformParams) => {
  const url = uniformEndpoints.getUniforms;

  try {
    const response = await axios.get<PaginationResponse<IUniform>>(url, filter && { params: filter });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformById = async (id: string) => {
  const url = uniformEndpoints.getUniformById(id);

  try {
    const response = await axios.get<IUniform>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createUniform = async (data: IUniform) => {
  const url = uniformEndpoints.postUniform;
  return await axios.post(url, data);
};

const updateUniform = async (id: string, data: IUniform) => {
  const url = uniformEndpoints.patchUniform(id);
  return await axios.patch(url, data);
};

const deleteUniform = async (id: string) => {
  const url = uniformEndpoints.deleteUniform(id);
  return await axios.delete(url);
};

const getUniformOrders = async (filter?: IUniformOrderParams) => {
  const url = uniformOrderEndpoints.getUniformOrders;

  try {
    const response = await axios.get<PaginationResponse<IUniformOrder>>(
      url,
      filter && {
        params: filter,
        paramsSerializer: {
          indexes: null,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postUniformOrder = async (data: IUniformOrderCreate) => {
  const url = uniformOrderEndpoints.postUniformOrder;
  return await axios.post(url, data);
};

const patchUniformOrder = async (id: string, data: IUniformOrderUpdate) => {
  const url = uniformOrderEndpoints.patchUniformOrder(id);
  return await axios.patch(url, data);
};

const getUniformOrderById = async (id: string) => {
  const url = uniformOrderEndpoints.getUniformOrderById(id);

  try {
    const response = await axios.get<IUniformOrder>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const cancelUniformOrder = async (id: string) => {
  const url = uniformOrderEndpoints.cancelUniformOrder(id);
  return await axios.patch(url);
};

const deleteUniformOrder = async (id: string) => {
  const url = uniformOrderEndpoints.deleteUniformOrder(id);
  return await axios.delete(url);
};

const processUniformService = async (id: string, data: IUniformOrderProcess) => {
  const url = uniformOrderEndpoints.processUniformOrder(id);
  return await axios.patch(url, data);
};

const getUniformReleases = async (filter?: IUniformReleaseParams) => {
  const url = uniformReleaseEndpoints.getUniformReleases;

  try {
    const response = await axios.get<PaginationResponse<IUniformRelease>>(
      url,
      filter && {
        params: {
          page: filter?.page,
          size: filter?.size,
          search: filter?.search,
          uniformId: filter?.uniformId,
          type: filter?.type,
          startDate: filter?.startDate,
          endDate: filter?.endDate,
          employeeIds: filter?.employeeIds,
        },
        paramsSerializer: {
          indexes: null,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postUniformRelease = async (data: IUniformRelease) => {
  const url = uniformReleaseEndpoints.postUniformRelease;
  return await axios.post(url, data);
};

const getUniformReleaseById = async (id: string) => {
  const url = uniformReleaseEndpoints.getUniformReleaseById(id);

  try {
    const response = await axios.get<IUniformRelease>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const uniformStock = async (id: string) => {
  const url = uniformOrderEndpoints.uniformStockIn(id);
  return await axios.patch(url);
};

const uniformStocks = async (filter?: IUniformStockParams) => {
  const url = uniformStockEndpoints.getUniformStocks;

  try {
    const response = await axios.get<PaginationResponse<IUniformStock>>(url, filter && { params: filter });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postUniformReturn = async (data: IUniformReturn) => {
  const url = uniformReturnEndpoints.postUniformReturn;
  return await axios.post(url, data);
};

const getUniformReturnsRemaining = async (employeeId: string, uniformId: string) => {
  const url = uniformReturnEndpoints.getUniformReturnsRemaining;

  try {
    const response = await axios.get<{ totalRemaining: number }>(url, {
      params: {
        employeeId,
        uniformId,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const exportUniformStockIn = async (startDate?: string, endDate?: string) => {
  try {
    const url = uniformOrderEndpoints.exportUniformStockIn;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: {
        startDate,
        endDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const exportUniformOrderStockIn = async (startDate?: string, endDate?: string) => {
  try {
    const url = uniformOrderStockEndpoints.exportUniformOrderStockIn;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: {
        startDate,
        endDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const exportUniformStockOut = async (startDate?: string, endDate?: string) => {
  try {
    const url = uniformReleaseEndpoints.exportUniformRelease;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: {
        startDate,
        endDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postUniformOrderStock = async (data: IUniformOrderStock) => {
  const url = uniformOrderEndpoints.uniformStockIn(data?.uniformOrderId);
  return await axios.patch(url, data);
};

const patchUniformStatus = async (id: string, status: string) => {
  const url = uniformEndpoints.patchUniformStatus(id, status);
  return await axios.patch(url);
}

const getUniformExport = async () => {
  try {
    const url = uniformEndpoints.exportUniform;
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
  getUniforms,
  getUniformById,
  createUniform,
  updateUniform,
  deleteUniform,
  getUniformOrders,
  postUniformOrder,
  patchUniformOrder,
  getUniformOrderById,
  cancelUniformOrder,
  deleteUniformOrder,
  processUniformService,
  getUniformReleases,
  postUniformRelease,
  getUniformReleaseById,
  uniformStock,
  uniformStocks,
  postUniformReturn,
  getUniformReturnsRemaining,
  exportUniformStockIn,
  exportUniformOrderStockIn,
  exportUniformStockOut,
  postUniformOrderStock,
  patchUniformStatus,
  getUniformExport,
};
