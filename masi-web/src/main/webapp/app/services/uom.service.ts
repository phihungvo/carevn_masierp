import { uomEndpoints, uomGroupEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IUom, IUomGroup, IUomGroupParams, IUomParams } from 'app/shared/model/uom.model';
import axios from 'axios';

const getUoms = async (filter?: IUomParams) => {
  const url = uomEndpoints.getUoms;

  try {
    const response = await axios.get<PaginationResponse<IUom>>(url, {
      params: filter,
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createUom = async (uom: IUom) => {
  const url = uomEndpoints.postUom;

  return axios.post<IUom>(url, uom);
};

const updateUom = async (uom: IUom, id: string) => {
  const url = uomEndpoints.patchUom(id);

  return axios.patch<IUom>(url, uom);
};

const getUomById = async (id: string) => {
  try {
    const url = uomEndpoints.getUomById(id);

    const response = await axios.get<IUom>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteUom = async (id: string) => {
  const url = uomEndpoints.deleteUom(id);

  return axios.delete(url);
};

const getUomGroups = async (filter?: IUomGroupParams) => {
  const url = uomGroupEndpoints.getUomGroups;

  try {
    const response = await axios.get<PaginationResponse<IUomGroup>>(url, {
      params: filter,
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createUomGroup = async (uomGroup: IUomGroup) => {
  const url = uomGroupEndpoints.postUomGroup;

  return axios.post<IUomGroup>(url, uomGroup);
};

const updateUomGroup = async (uomGroup: IUomGroup, id: string) => {
  const url = uomGroupEndpoints.patchUomGroup(id);

  return axios.patch<IUomGroup>(url, uomGroup);
};

const getUomGroupById = async (id: string) => {
  try {
    const url = uomGroupEndpoints.getUomGroupById(id);

    const response = await axios.get<IUomGroup>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteUomGroup = async (id: string) => {
  const url = uomGroupEndpoints.deleteUomGroup(id);

  return axios.delete(url);
};

export default {
  getUoms,
  createUom,
  updateUom,
  getUomById,
  deleteUom,
  getUomGroups,
  createUomGroup,
  updateUomGroup,
  getUomGroupById,
  deleteUomGroup,
};
