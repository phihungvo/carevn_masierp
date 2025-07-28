import {
  inventoriesStorageEndpoints,
  inventoriesType,
} from 'app/constants/endpoints';
import {
  IInventoriesStorage,
  IInventoriesStorageParams,
  IInventoriesType,
  IInventoriesTypeParams,
} from 'app/shared/model/inventories-storage.model';
import { IItem } from 'app/shared/model/item.model';
import {
  PaginationParams,
  PaginationResponse,
} from 'app/shared/model/pagination.model';
import axios from 'axios';

export const getInventories = async (filter?: IInventoriesStorageParams) => {
  try {
    const url = inventoriesStorageEndpoints.getInventoriesStorage;
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

export const getInventoriesXlsx = async (filter: IInventoriesStorageParams) => {
  try {
    const url = inventoriesStorageEndpoints.exportInventoriesStorage;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: {
        'warehouseGroupType.equals': filter?.['warehouseGroupType.equals'],
        page: filter?.page,
        size: filter?.size,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const getInventoriesTypes = async (filter?: IInventoriesTypeParams) => {
  try {
    const url = inventoriesType.getInventoriesType;
    const response = await axios.get<PaginationResponse<IInventoriesType>>(
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

export const postInventories = async (data: Partial<IInventoriesStorage>) => {
  const url = inventoriesStorageEndpoints.postInventoriesStorage;

  return await axios.post<IInventoriesStorage>(url, data);
};

export const patchInventories = async (
  id: string,
  data: Partial<IInventoriesStorage>,
) => {
  const url = inventoriesStorageEndpoints.patchInventoriesStorage(id);

  return await axios.patch<IInventoriesStorage>(url, data);
};

export const getInventoriesById = async (id: string) => {
  try {
    const url = inventoriesStorageEndpoints.getInventoriesStorageById(id);
    const response = await axios.get<IInventoriesStorage>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const deleteInventories = async (id: string) => {
  const url = inventoriesStorageEndpoints.deleteInventoriesStorage(id);

  return await axios.patch(url);
};

export const reviewInventories = async (id: string) => {
  const url = inventoriesStorageEndpoints.reqReviewInventoriesStorage(id);

  return await axios.patch(url);
};

export const confirmInventories = async (
  id: string,
  data: { assignSign: string; approvedSignName: string },
) => {
  const url = inventoriesStorageEndpoints.reviewInventoriesStorage(id);

  return await axios.patch(url, {
    approvedSign: data?.assignSign,
    approvedSignName: data?.approvedSignName,
    isApproved: true,
  });
};

export const rejectInventories = async (id: string, note: string) => {
  const url = inventoriesStorageEndpoints.reviewInventoriesStorage(id);

  return await axios.patch(url, {
    rejectNote: note,
    isApproved: false,
  });
};

export const getInventoriesGeneratedCode = async () => {
  try {
    const url = inventoriesStorageEndpoints.getInventoriesStorageNextCode();
    const response = await axios.get<{
      code: string;
      nextAndIncrement: string;
    }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const confirmImportInventories = async (id: string) => {
  const url = inventoriesStorageEndpoints.confirmImportInventoriesStorage(id);

  return await axios.patch(url);
};

export const getInventoriesStorageItems = async (
  data: Partial<{ documentIds: string[]; warehouseId?: string }>,
) => {
  const url = inventoriesStorageEndpoints.getInventoriesStorageItems;
  return await axios.post<{ itemId: string; totalQuantity: number }[]>(
    url,
    data,
  );
};

export const getInventoriesStorageItemsAllByWarehouse = async (
  id: string,
  filter: PaginationParams,
) => {
  try {
    const url =
      inventoriesStorageEndpoints.getInventoriesStorageItemsAllByWarehouse(id);
    const response = await axios.get(url, { params: filter });

    return response;
  } catch (error) {
    console.error(error);
  }
};
