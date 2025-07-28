import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import machineOperationChecklistService from 'app/services/machine-operation-checklist.service';
import { IMachineOperationChecklist } from 'app/shared/model/production-process.model';

const { CREATE_MACHINE_OPERATION_MONITORING, UPDATE_MACHINE_OPERATION_MONITORING, DELETE_MACHINE_OPERATION_MONITORING } = MUTATION_KEY;
const { PRODUCTION_PROCESS, MACHINE_OPERATION_CHECKLIST } = QUERY_KEY;

// Tạo biểu mẫu giám sát hoạt động máy
const usePostMachineOperationChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_MACHINE_OPERATION_MONITORING],
    mutationFn: machineOperationChecklistService.postMachineOperationChecklist,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Cập nhật biểu mẫu giám sát hoạt động máy
const usePatchMachineOperationChecklist = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_MACHINE_OPERATION_MONITORING],
    mutationFn: (data: IMachineOperationChecklist) => machineOperationChecklistService.patchMachineOperationChecklist(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Xoá biểu mẫu giám sát hoạt động máy
const useDeleteMachineOperationChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_MACHINE_OPERATION_MONITORING],
    mutationFn: (id: string) => machineOperationChecklistService.deleteMachineOperationChecklist(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Lấy chi tiết biểu mẫu giám sát hoạt động máy
const useGetMachineOperationChecklistById = (id: string) => {
  return useQuery({
    queryKey: [MACHINE_OPERATION_CHECKLIST, id],
    queryFn: () => machineOperationChecklistService.getMachineOperationChecklistById(id),
    enabled: !!id,
  });
};

export default {
  usePostMachineOperationChecklist,
  usePatchMachineOperationChecklist,
  useDeleteMachineOperationChecklist,
  useGetMachineOperationChecklistById,
};
