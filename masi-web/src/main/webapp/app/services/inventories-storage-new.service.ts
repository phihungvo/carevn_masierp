import { inventoriesStorageNewEndpoints } from 'app/constants/endpoints';
import {
  IInventoriesStorage,
  IInventoriesStorageParams,
} from 'app/shared/model/inventories-storage.model';
import {
  PaginationParams,
  PaginationResponse,
} from 'app/shared/model/pagination.model';
import axios from 'axios';

export const getInventoriesStorage = async (
  filter?: IInventoriesStorageParams,
) => {
  try {
    const url = inventoriesStorageNewEndpoints.getInventoriesStorage;
    const response = await axios.get<PaginationResponse<IInventoriesStorage>>(
      url,
      {
        params: filter,
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const getDepreciationInventoriesStorage = async (
  filter?: IInventoriesStorageParams,
) => {
  try {
    const url =
      inventoriesStorageNewEndpoints.getDepreciationInventoriesStorage;
    const response = await axios.get<PaginationResponse<IInventoriesStorage>>(
      url,
      {
        params: filter,
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const getFishMealInventoriesStorage = async (
  filter?: IInventoriesStorageParams,
) => {
  try {
    const url = inventoriesStorageNewEndpoints.getFishMealInventoriesStorage;
    const response = await axios.get<PaginationResponse<IInventoriesStorage>>(
      url,
      { params: filter },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

const exportInventoriesStorageXlsx = async (
  params: IInventoriesStorageParams,
) => {
  try {
    const url = inventoriesStorageNewEndpoints.exportInventoriesStorageXlsx;
    const response = await axios.get(url, {
      params,
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

const updateInventoriesStorage = async (
  id: string,
  data: IInventoriesStorage,
) => {
  const url = inventoriesStorageNewEndpoints.updateInventoriesStorage(id);
  return await axios.patch<IInventoriesStorage>(url, data);
};

const getInventoriesStorageById = async (id: string) => {
  try {
    const url = inventoriesStorageNewEndpoints.getInventoriesStorageById(id);
    const response = await axios.get<IInventoriesStorage>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getInventoriesStorage,
  getInventoriesStorageById,
  updateInventoriesStorage,
  getDepreciationInventoriesStorage,
  getFishMealInventoriesStorage,
  exportInventoriesStorageXlsx,
};
