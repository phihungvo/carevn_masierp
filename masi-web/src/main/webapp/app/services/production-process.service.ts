import { productionProcessEndpoints } from 'app/constants/endpoints';
import { PaginationParams, PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IPatchProductionProcess,
  IPostProductionProcessDto,
  IProductionProcess,
  IProductionProcessDetailParams,
} from 'app/shared/model/production-process.model';
import axios from 'axios';

// Lấy danh sách quy trình sản xuất
const getProductionProcesses = async (pagination: PaginationParams) => {
  try {
    const url = productionProcessEndpoints.getProductionProcesses;
    const response = await axios.get<PaginationResponse<IProductionProcess>>(
      url,
      pagination && {
        params: {
          page: pagination.page,
          size: pagination.size,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Tạo mới công đoạn sản xuất
const postProductionProcess = async (data: IPostProductionProcessDto) => {
  const url = productionProcessEndpoints.postProductionProcess;

  return await axios.post<IProductionProcess>(url, data);
};

// Cập nhật công đoạn sản xuất
const patchProductionProcess = async (data: IPatchProductionProcess, id: string) => {
  const url = productionProcessEndpoints.patchProductionProcess(id);

  return await axios.patch<IProductionProcess>(url, data);
};

// Lấy chi tiết công đoạn sản xuấL
const getProductionProcessById = async (id: string, filter: IProductionProcessDetailParams) => {
  try {
    const url = productionProcessEndpoints.getProductionProcessById(id);
    const response = await axios.get<IProductionProcess>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        name: filter?.name,
        ...(filter?.startDate && { startDate: filter?.startDate }),
        ...(filter?.endDate && { endDate: filter?.endDate }),
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Xoá công đoạn sản xuất
const deleteProductionProcess = async (id: string) => {
  const url = productionProcessEndpoints.deleteProductionProcess(id);

  return await axios.delete(url);
};

// Bắt đầu công đoạn sản xuất
const patchProductionProcessStart = async (id: string) => {
  const url = productionProcessEndpoints.patchProductionProcessStart(id);

  return await axios.patch(url);
};

// Dừng công đoạn sản xuất
const patchProductionProcessStop = async (id: string) => {
  const url = productionProcessEndpoints.patchProductionProcessStop(id);

  return await axios.patch(url);
};

// Hoàn thành công đoạn sản xuất
const patchProductionProcessComplete = async (id: string) => {
  const url = productionProcessEndpoints.patchProductionProcessComplete(id);

  return await axios.patch(url);
};

export default {
  getProductionProcesses,
  postProductionProcess,
  patchProductionProcess,
  getProductionProcessById,
  deleteProductionProcess,
  patchProductionProcessStart,
  patchProductionProcessStop,
  patchProductionProcessComplete,
};
