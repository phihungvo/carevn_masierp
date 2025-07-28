import { factoryLogisticsEndpoints } from 'app/constants/endpoints';
import { IFactoryLogistics, IFactoryLogisticsParams } from 'app/shared/model/factory-logistics.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getFactories = async (filter: IFactoryLogisticsParams) => {
  try {
    const url = factoryLogisticsEndpoints.getFactories;
    const response = await axios.get<PaginationResponse<IFactoryLogistics>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        ...filter,
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

const postFactory = async (data: IFactoryLogistics) => {
  const url = factoryLogisticsEndpoints.postFactory;

  return await axios.post<IFactoryLogistics>(url, data);
};

const patchFactory = async (data: IFactoryLogistics, id: string) => {
  const url = factoryLogisticsEndpoints.patchFactory(id);

  return await axios.patch<IFactoryLogistics>(url, data);
};

const getFactoryById = async (id: string) => {
  try {
    const url = factoryLogisticsEndpoints.getFactoryById(id);
    const response = await axios.get<IFactoryLogistics>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteFactory = async (id: string) => {
  const url = factoryLogisticsEndpoints.deleteFactory(id);

  return await axios.delete(url);
};

const disableFactory = async (id: string) => {
  const url = factoryLogisticsEndpoints.disableFactory(id);

  return await axios.patch(url);
};

const enableFactory = async (id: string) => {
  const url = factoryLogisticsEndpoints.enableFactory(id);

  return await axios.patch(url);
};

export default {
  getFactories,
  postFactory,
  getFactoryById,
  patchFactory,
  deleteFactory,
  disableFactory,
  enableFactory,
};
