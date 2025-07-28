import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import receiveMaterialChecklistsService from 'app/services/receive-material-checklists.service';
import { IReceiveMaterialCheckList } from 'app/shared/model/production-process.model';

const { CREATE_MATERIAL_RECEIPT_MONITORING, UPDATE_MATERIAL_RECEIPT_MONITORING, DELETE_MATERIAL_RECEIPT_MONITORING } = MUTATION_KEY;
const { PRODUCTION_PROCESS, RECEIVE_MATERIAL_CHECKLIST } = QUERY_KEY;

// Tạo mới biểu mẫu giám sát và tiếp nhận nguyên liệu
const usePostReceiveMaterialChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_MATERIAL_RECEIPT_MONITORING],
    mutationFn: receiveMaterialChecklistsService.postReceiveMaterialChecklist,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Cập nhật biểu mẫu giám sát và tiếp nhận nguyên liệu
const usePatchReceiveMaterialChecklist = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_MATERIAL_RECEIPT_MONITORING],
    mutationFn: (data: IReceiveMaterialCheckList) => receiveMaterialChecklistsService.patchReceiveMaterialChecklist(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Xoá biểu mẫu giám sát và tiếp nhận nguyên liệu
const useDeleteReceiveMaterialChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_MATERIAL_RECEIPT_MONITORING],
    mutationFn: (id: string) => receiveMaterialChecklistsService.deleteReceiveMaterialChecklist(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Lấy chi tiết biểu mẫu giám sát và tiếp nhận nguyên liệu
const useGetReceiveMaterialChecklistById = (id: string) => {
  return useQuery({
    queryKey: [RECEIVE_MATERIAL_CHECKLIST, id],
    queryFn: () => receiveMaterialChecklistsService.getReceiveMaterialChecklistById(id),
    enabled: !!id,
  });
};

export default {
  usePostReceiveMaterialChecklist,
  usePatchReceiveMaterialChecklist,
  useDeleteReceiveMaterialChecklist,
  useGetReceiveMaterialChecklistById,
};
