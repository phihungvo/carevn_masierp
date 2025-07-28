import axios from 'axios';


import {
  IPatchTimeKeepingDto,
  IPostTimeKeepingDto,
  IPostTimeKeepingRecordDto,
  IPutTimeKeepingRecordDto,
  ITimeSheet,
  ITimeSheetDetail,
  ITimeSheetGroupByEmployee,
} from 'app/shared/model/time-sheet.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { timeSheetEndpoints } from 'app/constants/endpoints';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { PaginationParams, PaginationResponse } from 'app/shared/model/pagination.model';

// Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Ngày
const getTimeKeepingByEmployeeWithDate = async (employee: string, date: string) => {
  const url = timeSheetEndpoints.getTimeKeepingByEmployeeWithDate(employee, date);

  try {
    const response = await axios.get<ITimeSheet>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Trong Giai Đoạn Ngày
const getTimeKeepingByEmployeeWithPeriod = async (employee: string, startDate: string, endDate: string) => {
  const url = timeSheetEndpoints.getTimeKeepingByEmployeeWithPeriod(employee, startDate, endDate);

  try {
    const response = await axios.get<PaginationResponse<ITimeSheet>>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Bản Ghi Chấm Công Theo Trong Giai Đoạn Ngày
const getTimeKeepingByRangeDatePeriod = async (
  startDate: string,
  endDate: string,
  pagination?: PaginationParams,
  type?: TIME_SHEET_TYPE,
  workSpaceTypes?: WORKSPACE_TYPE,
) => {
  try {
    const url = timeSheetEndpoints.getTimeKeepingByRangeDatePeriod(startDate, endDate);
    const response = await axios.get<PaginationResponse<ITimeSheet>>(
      url,
      pagination && {
        params: {
          page: pagination.page ?? DEFAULT_PAGE,
          size: pagination.size ?? DEFAULT_PAGE_SIZE,
          type,
          workSpaceTypes,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Bản Ghi Chấm Công Theo Trong Ngày
const getTimeKeepingByDate = async (startDate: string) => {
  try {
    const url = timeSheetEndpoints.getTimeKeepingByDate(startDate);
    const response = await axios.get<PaginationResponse<ITimeSheet>>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Cập Nhật Bản Ghi Chấm Công
const patchTimeKeeping = (body: { id: string; data: IPatchTimeKeepingDto }) => {
  const url = timeSheetEndpoints.patchTimeKeeping(body.id);

  return axios.patch<ITimeSheet>(url, body.data);
};

// Tạo Bản Ghi Chấm Công
const postTimeKeeping = async (data: IPostTimeKeepingDto) => {
  const url = timeSheetEndpoints.postTimeKeeping;

  return await axios.post<ITimeSheet>(url, data);
};

// Tạo Bản Ghi Chấm Công Chi Tiết
const postTimeKeepingRecord = async (data: IPostTimeKeepingRecordDto) => {
  const url = timeSheetEndpoints.postTimeKeepingRecord;

  return await axios.post<ITimeSheetDetail>(url, data);
};

// Lấy Bản Các Bảng Ghi Chấm Công Chi Tiết
const getTimeKeepingRecordByEmployeeWithDate = async (employee: string, date: string) => {
  const url = timeSheetEndpoints.getTimeKeepingRecordByEmployeeWithDate(employee, date);

  try {
    const response = await axios.get<ITimeSheetDetail[]>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Bản Ghi Chấm Công Chi Tiết
const getTimeKeepingRecordById = async (id: string) => {
  const url = timeSheetEndpoints.getTimeKeepingRecordById(id);

  try {
    // const response = await axios.get<ITimeSheetDetail>(url);
    const response = await axios.get<ITimeSheet>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Cập Nhật Bản Ghi Chấm Công Chi Tiết
const putTimeKeepingRecord = async (data: IPutTimeKeepingRecordDto, id: string) => {
  const url = timeSheetEndpoints.putTimeKeepingRecord(id);

  return await axios.put<ITimeSheetDetail>(url, data);
};

// Xóa Bản Ghi Chấm Công
const deleteTimeKeepingRecord = async (id: string) => {
  const url = timeSheetEndpoints.deleteTimeKeepingRecord(id);

  return await axios.delete(url);
};

// Xuất Chấm Công Theo Nhân Viên Và Ngày (CCHL)
const getTimeKeepingExportByDate = async (startDate: string, endDate: string) => {
  const url = timeSheetEndpoints.getTimeKeepingExportByDate(startDate, endDate);

  try {
    const response = await axios.get<string>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Xuất Chấm Công Theo Nhân Viên Và Ngày (BCC)
const getTimeKeepingRecordExportByDate = async (startDate: string, endDate: string) => {
  const url = timeSheetEndpoints.getTimeKeepingRecordExportByDate(startDate, endDate);

  try {
    const response = await axios.get<string>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy Danh Sách Bản Ghi Chấm Công Nhân Viên Và Trong Giai Đoạn Ngày
const getTimeKeepingGroupByEmployee = async (
  startDate: string,
  endDate: string,
  pagination?: PaginationParams,
  type?: TIME_SHEET_TYPE,
  workSpaceTypes?: WORKSPACE_TYPE,
) => {
  const url = timeSheetEndpoints.getTimeKeepingGroupByEmployee;

  try {
    const response = await axios.get<PaginationResponse<ITimeSheetGroupByEmployee>>(url, {
      params: {
        start_date: startDate,
        end_date: endDate,
        type,
        page: pagination?.page ?? DEFAULT_PAGE,
        size: pagination?.size ?? DEFAULT_PAGE_SIZE,
        workSpaceTypes,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getTimeKeepingOffTracking = async (employeeId: string, startDate: string, endDate: string) => {
  const url = timeSheetEndpoints.getOffTracking(employeeId);

  try {
    const response = await axios.get<ITimeSheet[]>(url, {
      params: {
        start_date: startDate,
        end_date: endDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const refreshTimeKeepingMachineData = async () => {
  const url = timeSheetEndpoints.refreshTimeKeepingMachineData;

  try {
    const response = await axios.post(url);

    return response;
  } catch (error) {
    console.error(error);
  }
}


export default {
  getTimeKeepingByEmployeeWithDate,
  getTimeKeepingByEmployeeWithPeriod,
  getTimeKeepingByRangeDatePeriod,
  getTimeKeepingByDate,
  patchTimeKeeping,
  postTimeKeeping,
  postTimeKeepingRecord,
  getTimeKeepingRecordByEmployeeWithDate,
  getTimeKeepingRecordById,
  putTimeKeepingRecord,
  deleteTimeKeepingRecord,
  getTimeKeepingExportByDate,
  getTimeKeepingRecordExportByDate,
  getTimeKeepingGroupByEmployee,
  getTimeKeepingOffTracking,
  refreshTimeKeepingMachineData,
};
