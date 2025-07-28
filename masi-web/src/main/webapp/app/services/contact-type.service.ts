import { contactTypesEndpoints } from 'app/constants/endpoints';
import { IContactType, IContactTypeParams } from 'app/shared/model/contact-type.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getContactTypes = async (filter?: IContactTypeParams) => {
  try {
    const url = contactTypesEndpoints.getContactTypes;

    const response = await axios.get<PaginationResponse<IContactType>>(url, {
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

const createContactType = async (supplier: IContactType) => {
  const url = contactTypesEndpoints.postContactType;

  return axios.post<IContactType>(url, supplier);
};

const updateContactType = async (supplier: IContactType, id: string) => {
  const url = contactTypesEndpoints.patchContactType(id);

  return axios.patch<IContactType>(url, supplier);
};

const getContactTypeById = async (id: string) => {
  try {
    const url = contactTypesEndpoints.getContactTypeById(id);

    const response = await axios.get<IContactType>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteContactType = async (id: string) => {
  const url = contactTypesEndpoints.deleteContactType(id);

  return axios.delete(url);
};

export default {
  getContactTypes,
  createContactType,
  updateContactType,
  getContactTypeById,
  deleteContactType,
};
