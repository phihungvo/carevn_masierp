import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { productionCommandService } from 'app/services/production-command.service';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import {
  IPatchProductionCommandDto,
  IProductionCommandParams,
  IProductionCommandWithProcessParams,
} from 'app/shared/model/production-command.model';
import { useState } from 'react';

const {
  CREATE_PRODUCTION_COMMAND,
  UPDATE_PRODUCTION_COMMAND,
  DELETE_PRODUCTION_COMMAND,
  CANCEL_PRODUCTION_COMMAND,
  START_PRODUCTION_COMMAND,
  PRODUCTION_COMMANDS_MAINTAIN,
  COMPLETE_STATE_PRODUCTION_COMMAND,
} = MUTATION_KEY;
const {
  PRODUCTION_COMMANDS,
  PRODUCTION_COMMAND_WO,
  PRODUCTION_COMMAND_BY_ID,
  ORDER_WITH_MANUFACTURE,
  PRODUCTION_STANDARDS_MO,
  PRODUCTION_COMMAND_EXCEL,
} = QUERY_KEY;

// Lấy danh sách lệnh sản xuất
const useGetProductionCommandsQuery = (filter?: IProductionCommandParams) => {
  return useQuery({
    queryKey: [PRODUCTION_COMMANDS, filter],
    queryFn: () => productionCommandService.getProductionCommands(filter),
    select: data => data?.data,
  });
};

const useGetProductionCommandsMaintain = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_COMMANDS_MAINTAIN, id],
    queryFn: () => productionCommandService.getProductionCommandsMaintain(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

// Tạo lệnh sản xuất
const usePostProductionCommand = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_COMMAND],
    mutationFn: productionCommandService.postProductionCommand,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
      queryClient.invalidateQueries({ queryKey: [ORDER_WITH_MANUFACTURE] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS_MO] });
    },
  });
};

// Cập nhật lệnh sản xuất
const usePatchProductionCommand = (id: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_COMMAND],
    mutationFn: (data: IPatchProductionCommandDto) =>
      productionCommandService.patchProductionCommand(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
      queryClient.invalidateQueries({ queryKey: [ORDER_WITH_MANUFACTURE] });
    },
  });
};

// Lấy chi tiết lệnh sản xuất
const useGetProductionCommandById = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_COMMANDS, id],
    queryFn: () => productionCommandService.getProductionCommandById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

// Xóa lệnh sản xuất
const useDeleteProductionCommand = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_COMMAND],
    mutationFn: (id: string) =>
      productionCommandService.deleteProductionCommand(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
      queryClient.invalidateQueries({ queryKey: [ORDER_WITH_MANUFACTURE] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS_MO] });
    },
  });
};

// Huỷ lệnh sản xuất
const useCancelProductionCommand = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_PRODUCTION_COMMAND],
    mutationFn: (id: string) =>
      productionCommandService.cancelProductionCommand(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
      queryClient.invalidateQueries({ queryKey: [ORDER_WITH_MANUFACTURE] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS_MO] });
    },
  });
};

// Lấy danh sách lệnh sản xuất và các giai đoạn
const useGetProductionCommandsWithProcessQuery = (
  filter?: IProductionCommandWithProcessParams,
) => {
  return useQuery({
    queryKey: [
      PRODUCTION_COMMAND_WO,
      filter?.page,
      filter?.size,
      filter?.fromDate,
      filter?.toDate,
      filter?.statuses,
      filter?.searchString,
      filter?.manufactureOrderType,
    ],
    queryFn: () =>
      productionCommandService.getProductionCommandsWithProcess(filter),
    select: data => data?.data,
  });
};

// Lấy chi tiết lệnh sản xuất và các giai đoạn
const useGetProductionCommandsWithProcessById = (id: string) => {
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [PRODUCTION_COMMAND_BY_ID, id],
    queryFn: () =>
      productionCommandService.getProductionCommandsWithProcessById(id),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

// Bắt đầu công đoạn sản xuất
const usePatchProductionCommandStart = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [START_PRODUCTION_COMMAND],
    mutationFn: (id: string) =>
      productionCommandService.patchProductionCommandStart(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
      queryClient.invalidateQueries({ queryKey: [ORDER_WITH_MANUFACTURE] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS_MO] });
    },
  });
};

// Bắt đầu công đoạn sản xuất
const useCompleteStateProductionCommand = (id: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [START_PRODUCTION_COMMAND],
    mutationFn: (isSkip?: number) =>
      productionCommandService.completeStateProductionCommand(id, isSkip),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMANDS] });
    },
  });
};

const useGetProductionCommandCountStandardsQuery = (standardId: string) => {
  return useQuery({
    queryKey: [PRODUCTION_COMMANDS, standardId],
    queryFn: () =>
      productionCommandService.getProductionCommandsCountStandard(standardId),
    enabled: !!standardId,
    select: data => data?.data,
  });
};

const useGeProductionCommandExportReportExcel = (
  type: PRODUCTION_COMMAND_TYPE,
) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [PRODUCTION_COMMAND_EXCEL],
    queryFn: () =>
      productionCommandService.getProductionCommandsReportsExcel(type),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess)
    queryClient.resetQueries({ queryKey: [PRODUCTION_COMMAND_EXCEL] });

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

export default {
  useGetProductionCommandsQuery,
  usePostProductionCommand,
  usePatchProductionCommand,
  useGetProductionCommandById,
  useDeleteProductionCommand,
  useCancelProductionCommand,
  useGetProductionCommandsWithProcessQuery,
  useGetProductionCommandsWithProcessById,
  usePatchProductionCommandStart,
  useGetProductionCommandsMaintain,
  useCompleteStateProductionCommand,
  useGetProductionCommandCountStandardsQuery,
  useGeProductionCommandExportReportExcel,
};
