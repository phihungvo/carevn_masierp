import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { suppliersEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ISupplier, ISupplierParams } from 'app/shared/model/supplier.model';
import axios from 'axios';

const getSuppliers = async (filter?: ISupplierParams) => {
  try {
    const url = suppliersEndpoints.getSuppliers;

    const response = await axios.get<PaginationResponse<ISupplier>>(url, {
      params: {
        page: filter?.page ? filter?.page : DEFAULT_PAGE,
        size: filter?.size ? filter?.size : DEFAULT_PAGE_SIZE_NAX,
        search: filter?.search,
        status: filter?.status,
        companyId: filter?.companyId,
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

const getSupplierTypes = async (filter?: ISupplierParams) => {
  try {
    const url = suppliersEndpoints.getSupplierTypes;

    const response = await axios.get<PaginationResponse<ISupplier>>(url, {
      params: {
        page: filter?.page ? filter?.page : DEFAULT_PAGE,
        size: filter?.size ? filter?.size : DEFAULT_PAGE_SIZE_NAX,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
}
const createSupplier = async (supplier: ISupplier) => {
  const url = suppliersEndpoints.postSupplier;

  return axios.post<ISupplier>(url, supplier);
};

const updateSupplier = async (supplier: ISupplier, id: string) => {
  const url = suppliersEndpoints.patchSupplier(id);

  return axios.patch<ISupplier>(url, supplier);
};

const getSupplierById = async (id: string) => {
  try {
    const url = suppliersEndpoints.getSupplierById(id);

    const response = await axios.get<ISupplier>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteSupplier = async (id: string) => {
  const url = suppliersEndpoints.deleteSupplier(id);

  return axios.delete(url);
};

const disableSupplier = async (id: string) => {
  const url = suppliersEndpoints.disableSupplier(id);

  return await axios.patch(url);
};

const enableSupplier = async (id: string) => {
  const url = suppliersEndpoints.enableSupplier(id);

  return await axios.patch(url);
};

const exportSupplier = async (filter?: ISupplierParams) => {
  try {
    const url = suppliersEndpoints.exportSuppliers;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: {
        page: filter?.page ? filter?.page : DEFAULT_PAGE,
        size: filter?.size ? filter?.size : DEFAULT_PAGE_SIZE_NAX,
        search: filter?.search,
        status: filter?.status,
        companyId: filter?.companyId,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getSuppliers,
  getSupplierTypes,
  createSupplier,
  updateSupplier,
  getSupplierById,
  deleteSupplier,
  disableSupplier,
  enableSupplier,
  exportSupplier,
};
