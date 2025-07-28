import { itemsEndpoints } from 'app/constants/endpoints';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getItems = async (params: IItemParams) => {
  try {
    const url = itemsEndpoints.getItems;
    const response = await axios.get<PaginationResponse<IItem>>(url, {
      params,
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postItem = async (data: IItem) => {
  const url = itemsEndpoints.postItem;

  return await axios.post<IItem>(url, data);
};

const patchItem = async (data: IItem, id: string) => {
  const url = itemsEndpoints.patchItem(id);

  return await axios.patch<IItem>(url, data);
};

const getItemById = async (id: string) => {
  try {
    const url = itemsEndpoints.getItemById(id);
    const response = await axios.get<IItem>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteItem = async (id: string) => {
  const url = itemsEndpoints.deleteItem(id);

  return await axios.delete(url);
};

const disableItem = async (id: string) => {
  const url = itemsEndpoints.disableItem(id);

  return await axios.patch(url);
};

const enableItem = async (id: string) => {
  const url = itemsEndpoints.enableItem(id);

  return await axios.patch(url);
};

const exportItems = async (params: IItemParams) => {
  try {
    const url = itemsEndpoints.exportItems;
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

const getItemsPercentProtein = async (params: IItemParams) => {
  try {
    const url = itemsEndpoints.getItemsPercentProtein;
    const response = await axios.get<PaginationResponse<IItem>>(url, {
      params,
      paramsSerializer: { indexes: null },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getItems,
  postItem,
  getItemById,
  patchItem,
  deleteItem,
  disableItem,
  enableItem,
  exportItems,
  getItemsPercentProtein,
};
