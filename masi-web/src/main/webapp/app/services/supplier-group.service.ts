import { supplierGroupsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ISupplierGroup, ISupplierGroupParams } from 'app/shared/model/supplier-group.model';
import axios from 'axios';

const getSupplierGroups = async (filter?: ISupplierGroupParams) => {
  try {
    const url = supplierGroupsEndpoints.getSupplierGroups;

    const response = await axios.get<PaginationResponse<ISupplierGroup>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
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

const createSupplierGroup = async (supplier: ISupplierGroup) => {
  const url = supplierGroupsEndpoints.postSupplierGroup;

  return axios.post<ISupplierGroup>(url, supplier);
};

const updateSupplierGroup = async (supplier: ISupplierGroup, id: string) => {
  const url = supplierGroupsEndpoints.patchSupplierGroup(id);

  return axios.patch<ISupplierGroup>(url, supplier);
};

const getSupplierGroupById = async (id: string) => {
  try {
    const url = supplierGroupsEndpoints.getSupplierGroupById(id);

    const response = await axios.get<ISupplierGroup>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteSupplierGroup = async (id: string) => {
  const url = supplierGroupsEndpoints.deleteSupplierGroup(id);

  return axios.delete(url);
};

export default {
  getSupplierGroups,
  createSupplierGroup,
  updateSupplierGroup,
  getSupplierGroupById,
  deleteSupplierGroup,
};
