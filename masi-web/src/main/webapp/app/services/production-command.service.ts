import { productionCommandEndpoints } from 'app/constants/endpoints';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IPatchProductionCommandDto,
  IPostProductionCommandDto,
  IProductionCommand,
  IProductionCommandParams,
  IProductionCommandWithProcessParams,
} from 'app/shared/model/production-command.model';
import axios from 'axios';

// Lấy danh sách lệnh sản xuất
const getProductionCommands = async (filter: IProductionCommandParams) => {
  try {
    const url = productionCommandEndpoints.getProductionCommands;
    const response = await axios.get<PaginationResponse<IProductionCommand>>(
      url,
      {
        params: {
          ...filter,
          page: filter?.page,
          size: filter?.size,
          searchString: filter?.searchString,
          ...(filter?.fromDate && { fromDate: filter?.fromDate }),
          ...(filter?.toDate && { toDate: filter?.toDate }),
          ...(filter?.statuses?.length && { statuses: filter?.statuses }),
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

const getProductionCommandsMaintain = async (id: string) => {
  try {
    return await axios.get<IProductionCommand>(
      productionCommandEndpoints.getProductionCommandMaintain(id),
    );
  } catch (error) {
    console.error(error);
  }
};

// Tạo lệnh sản xuất
const postProductionCommand = async (data: IPostProductionCommandDto) => {
  const url = productionCommandEndpoints.postProductionCommand;

  return await axios.post<IProductionCommand>(url, data);
};

// Cập nhật lệnh sản xuất
const patchProductionCommand = async (
  data: IPatchProductionCommandDto,
  id: string,
) => {
  const url = productionCommandEndpoints.patchProductionCommand(id);

  return await axios.patch<IProductionCommand>(url, data);
};

// Lấy chi tiết lệnh sản xuất
const getProductionCommandById = async (id: string) => {
  try {
    const url = productionCommandEndpoints.getProductionCommandById(id);
    const response = await axios.get<IProductionCommand>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Xóa lệnh sản xuất
const deleteProductionCommand = async (id: string) => {
  const url = productionCommandEndpoints.deleteProductionCommand(id);

  return await axios.delete(url);
};

// Huỷ lệnh sản xuất
const cancelProductionCommand = async (id: string) => {
  const url = productionCommandEndpoints.patchProductionCommandCancel(id);

  return await axios.patch(url);
};

// Lấy danh sách lệnh sản xuất và các giai đoạn
const getProductionCommandsWithProcess = async (
  filter: IProductionCommandWithProcessParams,
) => {
  try {
    const url = productionCommandEndpoints.getProductionCommandsWithProcess;
    const response = await axios.get<PaginationResponse<IProductionCommand>>(
      url,
      {
        params: {
          page: filter?.page,
          size: filter?.size,
          searchString: filter?.searchString,
          ...(filter?.fromDate && { fromDate: filter?.fromDate }),
          ...(filter?.toDate && { toDate: filter?.toDate }),
          ...(filter?.statuses?.length && { statuses: filter?.statuses }),
          manufactureOrderType: filter?.manufactureOrderType,
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

// Lấy chi tiết lệnh sản xuất với các công đoạn
const getProductionCommandsWithProcessById = async (id: string) => {
  try {
    const response = await axios.get<IProductionCommand>(
      productionCommandEndpoints.getProductionCommandsWithProcessById(id),
    );
    return response;
  } catch (error) {
    console.error(error);
  }
};

// Bắt đồng công đoạn sản xuất
const patchProductionCommandStart = async (id: string) => {
  const url = productionCommandEndpoints.patchProductionCommandStart(id);

  return await axios.patch(url);
};

// Hoàn thành 1 công đoạn sản xuất
const completeStateProductionCommand = async (id: string, isSkip?: number) => {
  const url = productionCommandEndpoints.completeStateProductionCommand(
    id,
    isSkip,
  );

  return await axios.patch(url);
};

// Lấy danh sách lệnh sản xuất
const getProductionCommandsCountStandard = async (standardId: string) => {
  try {
    const url =
      productionCommandEndpoints.getProductionCommandsCountStandard(standardId);
    const response = await axios.get<number>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getProductionCommandsReportsExcel = async (
  type: PRODUCTION_COMMAND_TYPE,
) => {
  const url = productionCommandEndpoints.exportExcelProductionCommands();
  const response = await axios.get<string>(url, {
    params: { typePage: type },
    responseType: 'blob',
    headers: { Accept: 'application/octet-stream' },
  });

  return response;
};

export const productionCommandService = {
  getProductionCommands,
  postProductionCommand,
  patchProductionCommand,
  getProductionCommandById,
  deleteProductionCommand,
  cancelProductionCommand,
  getProductionCommandsWithProcess,
  getProductionCommandsWithProcessById,
  patchProductionCommandStart,
  getProductionCommandsMaintain,
  completeStateProductionCommand,
  getProductionCommandsCountStandard,
  getProductionCommandsReportsExcel,
};
