import { productionRoutingsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IProductionRouting, IProductionRoutingParams } from 'app/shared/model/production-routing.model';
import axios from 'axios';

const getProductionRoutings = async (filter: IProductionRoutingParams) => {
  try {
    const url = productionRoutingsEndpoints.getRoutings;
    const response = await axios.get<PaginationResponse<IProductionRouting>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.factoryId && { factoryId: filter?.factoryId }),
        ...(filter?.storageId && { storageId: filter?.storageId }),
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getProductionRoutingById = async (id: string) => {
  try {
    const url = productionRoutingsEndpoints.getRoutingById(id);
    const response = await axios.get<IProductionRouting>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postProductionRouting = async (data: IProductionRouting) => {
  const url = productionRoutingsEndpoints.postRouting;

  return await axios.post<IProductionRouting>(url, data);
};

const patchProductionRouting = async (data: IProductionRouting, id: string) => {
  const url = productionRoutingsEndpoints.getRoutingById(id);

  return await axios.patch<IProductionRouting>(url, data);
};

const deleteProductionRouting = async (id: string) => {
  const url = productionRoutingsEndpoints.deleteRouting(id);

  return await axios.delete(url);
};

export const productionRoutingService = {
  getProductionRoutings,
  getProductionRoutingById,
  postProductionRouting,
  patchProductionRouting,
  deleteProductionRouting,
};
