import { warehouseEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IWarehouse, IWarehouseParams } from 'app/shared/model/warehouse.model';
import axios from 'axios';

const getWarehouses = async (filter?: IWarehouseParams) => {
  const url = warehouseEndpoints.getWarehouses;

  try {
    const response = await axios.get<PaginationResponse<IWarehouse>>(url, {
      params: filter,
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};


const getWarehouseRouting = async (filter?: IWarehouseParams) => {
  try {
    return await axios.get<PaginationResponse<IWarehouse>>(warehouseEndpoints.getWarehouseRouting, {
      params: filter,
    });
  } catch (error) {
    console.error(error);
  }
};

const createWarehouse = async (warehouse: IWarehouse) => {
  const url = warehouseEndpoints.postWarehouse;

  return axios.post<IWarehouse>(url, warehouse);
};

const updateWarehouse = async (id: string, warehouse: IWarehouse) => {
  const url = warehouseEndpoints.patchWarehouse(id);

  return axios.patch<IWarehouse>(url, warehouse);
};

const getWarehouseById = async (id: string) => {
  try {
    const url = warehouseEndpoints.getWarehouseById(id);

    const response = await axios.get<IWarehouse>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteWarehouse = async (id: string) => {
  const url = warehouseEndpoints.deleteWarehouse(id);

  return axios.delete(url);
};

export default { getWarehouses, createWarehouse, updateWarehouse, getWarehouseById, deleteWarehouse, getWarehouseRouting };
