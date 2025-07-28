import { productionPackagesEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IProductionPackage,
  IProductionPackageParams,
} from 'app/shared/model/production-package.model';
import axios from 'axios';

const getProductionPackages = async (filter: IProductionPackageParams) => {
  try {
    const url = productionPackagesEndpoints.getPackages;
    const response = await axios.get<PaginationResponse<IProductionPackage>>(
      url,
      {
        params: {
          ...filter,
          page: filter?.page,
          size: filter?.size,
          search: filter?.search,
          ...(filter?.manufactureOrderIds && {
            manufactureOrderIds: filter?.manufactureOrderIds,
          }),
          ...(filter?.moIds && { moIds: filter?.moIds }),
          ...(filter?.statuses && { statuses: filter?.statuses }),
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

const getProductionPackageById = async (id: string) => {
  try {
    const url = productionPackagesEndpoints.getPackageById(id);
    const response = await axios.get<IProductionPackage>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postProductionPackage = async (data: IProductionPackage) => {
  const url = productionPackagesEndpoints.postPackage;

  return await axios.post<IProductionPackage>(url, data);
};

const patchProductionPackage = async (data: IProductionPackage, id: string) => {
  const url = productionPackagesEndpoints.getPackageById(id);

  return await axios.patch<IProductionPackage>(url, data);
};

const deleteProductionPackage = async (id: string) => {
  const url = productionPackagesEndpoints.deletePackage(id);

  return await axios.delete(url);
};

export const productionPackageService = {
  getProductionPackages,
  getProductionPackageById,
  postProductionPackage,
  patchProductionPackage,
  deleteProductionPackage,
};
