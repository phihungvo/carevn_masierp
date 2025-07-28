import { productionStandardEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IProductionStandardParams } from 'app/shared/model/production-command.model';
import {
  IDeleteProductionStandardResponse,
  IPostProductionStandardDto,
  IPostProductionStandardResponse,
  IProductionStandard,
  IPatchProductionStandardDto,
} from 'app/shared/model/production-standard.model';
import axios from 'axios';

// Tạo mới Định Mức Sản Xuất
const postProductionStandard = async (data: IPostProductionStandardDto) => {
  const url = productionStandardEndpoints.postProductionStandard;

  return await axios.post<IPostProductionStandardResponse>(url, data);
};

// Lấy Định Mức Sản Xuất
const getProductionStandardById = async (id: string) => {
  try {
    const url = productionStandardEndpoints.getProductionStandardById(id);
    const response = await axios.get<IProductionStandard>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Danh Sách Định Mức Sản Xuất
const getProductionStandards = async (filter: IProductionStandardParams) => {
  try {
    const url = productionStandardEndpoints.getProductionStandards;
    const response = await axios.get<PaginationResponse<IProductionStandard>>(
      url,
      {
        params: {
          page: filter?.page,
          size: filter?.size,
          ...filter,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Cập Nhật Định Mức Sản Xuất
const patchProductionStandard = async (
  data: IPatchProductionStandardDto & { id: string },
) => {
  const url = productionStandardEndpoints.getProductionStandardById(data.id);

  return await axios.patch<IPatchProductionStandardDto>(url, data);
};

// Xóa Định Mức Sản Xuất
const deleteProductionStandard = async (id: string) => {
  const url = productionStandardEndpoints.getProductionStandardById(id);

  return await axios.delete<IDeleteProductionStandardResponse>(url);
};

// Lấy Danh Sách Định Mức Sản Xuất và lệnh sản xuất
const getProductionStandardsWithCommands = async (
  filter: IProductionStandardParams,
) => {
  try {
    const url = productionStandardEndpoints.getProductionStandardWithCommands;
    const response = await axios.get<PaginationResponse<IProductionStandard>>(
      url,
      {
        params: {
          page: filter?.page,
          size: filter?.size,
          name: filter?.name,
          ...(filter?.startDate && { startDate: filter.startDate }),
          ...(filter?.startDate && { endDate: filter.endDate }),
          ...(filter?.searchString && { searchString: filter.searchString }),
          ...(filter?.statuses?.length && { statuses: filter.statuses }),
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

// Hủy Định Mức Sản Xuất
const disposeProductionStandard = async (id: string) =>
  await axios.delete<IDeleteProductionStandardResponse>(
    productionStandardEndpoints.disposeProductionStandard(id),
  );

export default {
  postProductionStandard,
  getProductionStandardById,
  getProductionStandards,
  patchProductionStandard,
  deleteProductionStandard,
  getProductionStandardsWithCommands,
  disposeProductionStandard,
};
