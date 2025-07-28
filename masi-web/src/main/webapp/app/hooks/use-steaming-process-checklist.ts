import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import steamingProcessChecklistService from 'app/services/steaming-process-checklist.service';
import { ISteamingProcessChecklist } from 'app/shared/model/production-process.model';

const { CREATE_MONITORING_STEAMING_DRYING, UPDATE_MONITORING_STEAMING_DRYING, DELETE_MONITORING_STEAMING_DRYING } = MUTATION_KEY;
const { PRODUCTION_PROCESS, STEAMING_PROCESS_CHECKLIST } = QUERY_KEY;

// Tạo biểu mẫu giám sát quy trình hấp sấy
const usePostSteamingProcessChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_MONITORING_STEAMING_DRYING],
    mutationFn: steamingProcessChecklistService.postSteamingProcessChecklist,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Cập nhật biểu mẫu giám sát quy trình hấp sấy
const usePatchSteamingProcessChecklist = (id: string, toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_MONITORING_STEAMING_DRYING],
    mutationFn: (data: ISteamingProcessChecklist) => steamingProcessChecklistService.patchSteamingProcessChecklist(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Xoá biểu mẫu giám sát quy trình hấp sấy
const useDeleteSteamingProcessChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_MONITORING_STEAMING_DRYING],
    mutationFn: (id: string) => steamingProcessChecklistService.deleteSteamingProcessChecklist(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Lấy chi tiết biểu mẫu giám sát quy trình hấp sấy
const useGetSteamingProcessChecklistById = (id: string) => {
  return useQuery({
    queryKey: [STEAMING_PROCESS_CHECKLIST, id],
    queryFn: () => steamingProcessChecklistService.getSteamingProcessChecklistById(id),
    enabled: !!id,
  });
};

export default {
  usePostSteamingProcessChecklist,
  usePatchSteamingProcessChecklist,
  useDeleteSteamingProcessChecklist,
  useGetSteamingProcessChecklistById,
};
