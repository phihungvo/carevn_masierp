import { productionMaintainsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IProductionMaintain,
  IProductionMaintainParams,
} from 'app/shared/model/production-maintain.model';
import axios from 'axios';

const getProductionMaintains = async (params: IProductionMaintainParams) => {
  try {
    const url = productionMaintainsEndpoints.getMaintains;
    const response = await axios.get<PaginationResponse<IProductionMaintain>>(
      url,
      {
        params: {
          ...params,
          page: params?.page,
          size: params?.size,
          search: params?.search,
          ...(params?.manufactureStartDate && {
            manufactureStartDate: params?.manufactureStartDate,
          }),
          ...(params?.manufactureEndDate && {
            manufactureEndDate: params?.manufactureEndDate,
          }),
          ...(params?.expiredStartDate && {
            expiredStartDate: params?.expiredStartDate,
          }),
          ...(params?.expiredEndDate && {
            expiredEndDate: params?.expiredEndDate,
          }),
          sort: 'createdAt,desc',
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getProductionMaintainById = async (id: string) => {
  try {
    const url = productionMaintainsEndpoints.getMaintainById(id);
    const response = await axios.get<IProductionMaintain>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postProductionMaintain = async (data: IProductionMaintain) => {
  const url = productionMaintainsEndpoints.postMaintain;

  return await axios.post<IProductionMaintain>(url, data);
};

const patchProductionMaintain = async (
  data: IProductionMaintain,
  id: string,
) => {
  const url = productionMaintainsEndpoints.getMaintainById(id);

  return await axios.patch<IProductionMaintain>(url, data);
};

const deleteProductionMaintain = async (id: string) => {
  const url = productionMaintainsEndpoints.deleteMaintain(id);

  return await axios.delete(url);
};

export const productionMaintainService = {
  getProductionMaintains,
  getProductionMaintainById,
  postProductionMaintain,
  patchProductionMaintain,
  deleteProductionMaintain,
};
