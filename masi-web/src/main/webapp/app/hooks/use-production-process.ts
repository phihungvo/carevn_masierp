import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import productionProcessService from 'app/services/production-process.service';
import { PaginationParams } from 'app/shared/model/pagination.model';
import { IPatchProductionProcess, IProductionProcessDetailParams } from 'app/shared/model/production-process.model';

const { PRODUCTION_PROCESSES, PRODUCTION_PROCESS, PRODUCTION_COMMAND_WO, PRODUCTION_COMMAND_BY_ID } = QUERY_KEY;
const {
  CREATE_PRODUCTION_PROCESS,
  UPDATE_PRODUCTION_PROCESS,
  START_PRODUCTION_PROCESS,
  STOP_PRODUCTION_PROCESS,
  COMPLETE_PRODUCTION_PROCESS,
  DELETE_PRODUCTION_PROCESS,
} = MUTATION_KEY;

// Lấy danh sách quy trình sản xuất
const useGetProductionProcessesQuery = (pagination?: PaginationParams) => {
  return useQuery({
    queryKey: [PRODUCTION_PROCESSES, pagination?.page, pagination?.size],
    queryFn: () => productionProcessService.getProductionProcesses(pagination),
    select: data => data?.data,
  });
};

// Tạo mới công đoạn sản xuất
const usePostProductionProcess = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_PROCESS],
    mutationFn: productionProcessService.postProductionProcess,
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_BY_ID] });
    },
  });
};

// Cập nhật công đoạn sản xuất
const usePatchProductionProcess = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_PROCESS, id],
    mutationFn: (data: IPatchProductionProcess) => productionProcessService.patchProductionProcess(data, id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
    },
  });
};

// Lấy chi tiết công đoạn sản xuất
const useGetProductionProcessById = (id: string, filter?: IProductionProcessDetailParams) => {
  return useQuery({
    queryKey: [PRODUCTION_PROCESS, id, filter?.startDate, filter?.endDate, filter?.page, filter?.size, filter?.name],
    queryFn: () => productionProcessService.getProductionProcessById(id, filter),
    select: data => data?.data,
    enabled: !!id,
  });
};

// Xoá công đoạn sản xuất
const useDeleteProductionProcess = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_PROCESS],
    mutationFn: (id: string) => productionProcessService.deleteProductionProcess(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
    },
  });
};

// Bắt đầu công đoạn sản xuất
const usePatchProductionProcessStart = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [START_PRODUCTION_PROCESS],
    mutationFn: (id: string) => productionProcessService.patchProductionProcessStart(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
    },
  });
};

// Dừng công đoạn sản xuất
const usePatchProductionProcessStop = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [STOP_PRODUCTION_PROCESS],
    mutationFn: (id: string) => productionProcessService.patchProductionProcessStop(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
    },
  });
};

// Hoàn thành công đoạn sản xuất
const usePatchProductionProcessComplete = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [COMPLETE_PRODUCTION_PROCESS],
    mutationFn: (id: string) => productionProcessService.patchProductionProcessComplete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_COMMAND_WO] });
    },
  });
};

export default {
  useGetProductionProcessesQuery,
  usePostProductionProcess,
  usePatchProductionProcess,
  useGetProductionProcessById,
  useDeleteProductionProcess,
  usePatchProductionProcessStart,
  usePatchProductionProcessStop,
  usePatchProductionProcessComplete,
};
