import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import additiveMaterialChecklistService from 'app/services/additive-material-checklist.service';
import { IAdditiveMaterialChecklist } from 'app/shared/model/production-process.model';

const { CREATE_SELECTING_ADDING_ADDITIVES, UPDATE_SELECTING_ADDING_ADDITIVES, DELETE_SELECTING_ADDING_ADDITIVES } = MUTATION_KEY;
const { PRODUCTION_PROCESS, ADDITIVE_MATERIAL_CHECKLIST } = QUERY_KEY;

// Tạo biểu mẫu giám sát nguyên liệu phụ gia
const usePostAdditiveMaterialChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_SELECTING_ADDING_ADDITIVES],
    mutationFn: additiveMaterialChecklistService.postAdditiveMaterialChecklist,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

const usePatchAdditiveMaterialChecklist = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_SELECTING_ADDING_ADDITIVES],
    mutationFn: (data: IAdditiveMaterialChecklist) => additiveMaterialChecklistService.patchAdditiveMaterialChecklist(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

const useDeleteAdditiveMaterialChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_SELECTING_ADDING_ADDITIVES],
    mutationFn: (id: string) => additiveMaterialChecklistService.deleteAdditiveMaterialChecklist(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

const useGetAdditiveMaterialChecklistById = (id: string) => {
  return useQuery({
    queryKey: [ADDITIVE_MATERIAL_CHECKLIST, id],
    queryFn: () => additiveMaterialChecklistService.getAdditiveMaterialChecklistById(id),
    enabled: !!id,
  });
};

export default {
  usePostAdditiveMaterialChecklist,
  usePatchAdditiveMaterialChecklist,
  useDeleteAdditiveMaterialChecklist,
  useGetAdditiveMaterialChecklistById,
};
