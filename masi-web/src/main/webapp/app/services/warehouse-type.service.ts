import { warehouseTypeEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IWarehouseType, IWarehouseTypeParams } from 'app/shared/model/warehouse-type.model';
import axios from 'axios';

const getWarehouseTypes = async (filter?: IWarehouseTypeParams) => {
  const url = warehouseTypeEndpoints.getWarehouseTypes;

  try {
    const response = await axios.get<PaginationResponse<IWarehouseType>>(url, {
      params: filter,
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createWarehouseType = async (data: IWarehouseType) => {
  const url = warehouseTypeEndpoints.postWarehouseType;

  return axios.post<IWarehouseType>(url, data);
};

const updateWarehouseType = async (id: string, data: IWarehouseType) => {
  const url = warehouseTypeEndpoints.patchWarehouseTypeById(id);

  return axios.patch<IWarehouseType>(url, data);
};

const getWarehouseTypeById = async (id: string) => {
  try {
    const url = warehouseTypeEndpoints.getWarehouseTypeById(id);

    const response = await axios.get<IWarehouseType>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteWarehouseType = async (id: string) => {
  const url = warehouseTypeEndpoints.deleteWarehouseType(id);

  return axios.delete(url);
};

export default { getWarehouseTypes, createWarehouseType, updateWarehouseType, getWarehouseTypeById, deleteWarehouseType };
