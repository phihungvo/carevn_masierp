import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import timeSheetService from 'app/services/time-sheet.service';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { PaginationParams } from 'app/shared/model/pagination.model';
import { IPutTimeKeepingRecordDto } from 'app/shared/model/time-sheet.model';
import { useState } from 'react';
import { useModalsTimesheet } from './use-modals-timesheet';

const { TIME_KEEPING, TIME_KEEPING_RECORD, TIME_KEEPING_EXPORT, TIME_KEEPING_EXPORT_BULK, TIME_KEEPING_OFF_TRACKING } = QUERY_KEY;
const { UPDATE_TIME_KEEPING_RECORD, UPDATE_TIME_KEEPING } = MUTATION_KEY;

// Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Ngày
const useTimeKeepingByEmployeeWithDateQuery = (employee: string, date: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING, employee, date],
    queryFn: () => timeSheetService.getTimeKeepingByEmployeeWithDate(employee, date),
  });
};

// Lấy Bản Ghi Chấm Công Theo Nhân Viên Và Trong Giai Đoạn Ngày
const useTimeKeepingByEmployeeWithPeriodQuery = (employee: string, startDate: string, endDate: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING, employee, startDate, endDate],
    queryFn: () => timeSheetService.getTimeKeepingByEmployeeWithPeriod(employee, startDate, endDate),
  });
};

// Lấy Bản Ghi Chấm Công Theo Trong Giai Đoạn Ngày
const useTimeKeepingByRangeDatePeriodQuery = (
  startDate: string,
  endDate: string,
  pagination?: PaginationParams,
  type?: TIME_SHEET_TYPE,
  workSpaceTypes?: WORKSPACE_TYPE,
) => {
  return useQuery({
    queryKey: [TIME_KEEPING, startDate, endDate, pagination && pagination.page, pagination && pagination.size, type, workSpaceTypes],
    queryFn: () => timeSheetService.getTimeKeepingByRangeDatePeriod(startDate, endDate, pagination, type, workSpaceTypes),
    enabled: !!startDate && !!endDate,
    select: data => data.data,
  });
};

// Lấy Bản Ghi Chấm Công Theo Trong Ngày
const useTimeKeepingByDateQuery = (startDate: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING, startDate],
    queryFn: () => timeSheetService.getTimeKeepingByDate(startDate),
  });
};

// Cập Nhật Bản Ghi Chấm Công
const usePatchTimeKeepingMutation = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_TIME_KEEPING],
    mutationFn: timeSheetService.patchTimeKeeping,
    onSuccess: () => {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING] });
    },
  });
};

// Tạo Bản Ghi Chấm Công
const usePostTimeKeepingMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: timeSheetService.postTimeKeeping,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING] });
    },
  });
};

// Tạo Bản Ghi Chấm Công Chi Tiết
const usePostTimeKeepingRecordMutation = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: timeSheetService.postTimeKeepingRecord,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING] });
    },
  });
};

// Lấy Bản Các Bảng Ghi Chấm Công Chi Tiết
const useTimeKeepingRecordByEmployeeWithDateQuery = (employee: string, date: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING_RECORD, employee, date],
    queryFn: () => timeSheetService.getTimeKeepingRecordByEmployeeWithDate(employee, date),
  });
};

// Lấy Bản Ghi Chấm Công Chi Tiết
const useTimeKeepingRecordByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING_RECORD, id],
    queryFn: () => timeSheetService.getTimeKeepingRecordById(id),
    enabled: !!id,
  });
};

// Cập Nhật Bản Ghi Chấm Công Chi Tiết
const usePutTimeKeepingRecordMutation = (id: string) => {
  const queryClient = useQueryClient();

  const [{ toggleUpdateSuccess }] = useModalsTimesheet();

  return useMutation({
    mutationKey: [UPDATE_TIME_KEEPING_RECORD, id],
    mutationFn: (data: IPutTimeKeepingRecordDto) => timeSheetService.putTimeKeepingRecord(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING_RECORD] });
      toggleUpdateSuccess();
    },
  });
};

// Xóa Bản Ghi Chấm Công
const useDeleteTimeKeepingRecordMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: timeSheetService.deleteTimeKeepingRecord,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING_RECORD] });
    },
  });
};

// Xuất Chấm Công Theo Nhân Viên Và Ngày (BCC)
const useTimeKeepingExportByEmployeeWithDateLazyQuery = (startDate: string, endDate: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [TIME_KEEPING_EXPORT, startDate, endDate],
    queryFn: () => timeSheetService.getTimeKeepingRecordExportByDate(startDate, endDate),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [TIME_KEEPING_EXPORT, startDate, endDate],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

// Xuất Chấm Công Theo Nhân Viên Và Ngày (CCHL)
const useTimeKeepingExportByEmployeeWithDateMonthLazyQuery = (startDate: string, endDate: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [TIME_KEEPING_EXPORT_BULK, startDate, endDate],
    queryFn: () => timeSheetService.getTimeKeepingExportByDate(startDate, endDate),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [TIME_KEEPING_EXPORT_BULK, startDate, endDate],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

// Lấy Danh Sách Bản Ghi Chấm Công Nhân Viên Và Trong Giai Đoạn Ngày
const useTimeKeepingGroupByEmployee = (
  startDate: string,
  endDate: string,
  pagination: PaginationParams,
  type?: TIME_SHEET_TYPE,
  workSpaceTypes?: WORKSPACE_TYPE,
) => {
  return useQuery({
    queryKey: [TIME_KEEPING, startDate, endDate, pagination.page, pagination.size, type, workSpaceTypes],
    queryFn: () => timeSheetService.getTimeKeepingGroupByEmployee(startDate, endDate, pagination, type, workSpaceTypes),
    enabled: !!startDate && !!endDate,
    select: data => data.data,
  });
};

const useTimeKeepingOffTracking = (employeeId: string, startDate: string, endDate: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING_OFF_TRACKING, employeeId, startDate, endDate],
    queryFn: () => timeSheetService.getTimeKeepingOffTracking(employeeId, startDate, endDate),
    enabled: !!employeeId && !!startDate && !!endDate,
  });
};

const useRefreshTimeKeepingMachineData = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: timeSheetService.refreshTimeKeepingMachineData,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TIME_KEEPING] });
    },
  });
}

export default {
  useTimeKeepingByEmployeeWithDateQuery,
  useTimeKeepingByEmployeeWithPeriodQuery,
  useTimeKeepingByRangeDatePeriodQuery,
  useTimeKeepingByDateQuery,
  usePatchTimeKeepingMutation,
  usePostTimeKeepingMutation,
  usePostTimeKeepingRecordMutation,
  useTimeKeepingRecordByEmployeeWithDateQuery,
  useTimeKeepingRecordByIdQuery,
  usePutTimeKeepingRecordMutation,
  useDeleteTimeKeepingRecordMutation,
  useTimeKeepingExportByEmployeeWithDateLazyQuery,
  useTimeKeepingGroupByEmployee,
  useTimeKeepingExportByEmployeeWithDateMonthLazyQuery,
  useTimeKeepingOffTracking,
  useRefreshTimeKeepingMachineData,
};
