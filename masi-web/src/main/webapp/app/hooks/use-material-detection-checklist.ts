import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import metalDetectionChecklistService from 'app/services/material-detection-checklist.service';
import { IMetalDetectionChecklist } from 'app/shared/model/production-process.model';

const { CREATE_MAGNET_MESH_TEST, UPDATE_MAGNET_MESH_TEST, DELETE_MAGNET_MESH_TEST } = MUTATION_KEY;
const { PRODUCTION_PROCESS, METAL_DETECTION_CHECKLIST } = QUERY_KEY;

// Tạo biểu mẫu kiểm tra nam châm và lưới
const usePostMetalDetectionChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_MAGNET_MESH_TEST],
    mutationFn: metalDetectionChecklistService.postMetalDetectionChecklist,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Cập nhật biểu mẫu kiểm tra nam châm và lưới
const usePatchMetalDetectionChecklist = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_MAGNET_MESH_TEST],
    mutationFn: (data: IMetalDetectionChecklist) => metalDetectionChecklistService.patchMetalDetectionChecklist(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Xoá biểu mẫu kiểm tra nam châm và lưới
const useDeleteMetalDetectionChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_MAGNET_MESH_TEST],
    mutationFn: (id: string) => metalDetectionChecklistService.deleteMetalDetectionChecklist(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Lấy chi tiết biểu mẫu kiểm tra nam châm và lưới
const useGetMetalDetectionChecklistById = (id: string) => {
  return useQuery({
    queryKey: [METAL_DETECTION_CHECKLIST, id],
    queryFn: () => metalDetectionChecklistService.getMetalDetectionChecklistById(id),
    enabled: !!id,
  });
};

export default {
  usePostMetalDetectionChecklist,
  usePatchMetalDetectionChecklist,
  useDeleteMetalDetectionChecklist,
  useGetMetalDetectionChecklistById,
};
