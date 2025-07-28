import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import mixingReportChecklistService from 'app/services/mixing-report-checklist.service';
import { IMixingReportChecklist } from 'app/shared/model/production-process.model';

const { CREATE_MIXING_REPORT, UPDATE_MIXING_REPORT, DELETE_MIXING_REPORT } = MUTATION_KEY;
const { PRODUCTION_PROCESS, MIXING_REPORT_CHECKLIST } = QUERY_KEY;

// Tạo biểu mẫu báo cáo trộn sản phẩm bột cá
const usePostMixingReportChecklist = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_MIXING_REPORT],
    mutationFn: mixingReportChecklistService.postReportMixingFishmealTemplate,
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Cập nhật biểu mẫu báo cáo trộn sản phẩm bột cá
const usePatchMixingReportChecklist = (id: string, toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_MIXING_REPORT],
    mutationFn: (data: IMixingReportChecklist) => mixingReportChecklistService.patchReportMixingFishmealTemplate(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Xoá biểu mẫu báo cáo trộn sản phẩm bột cá
const useDeleteMixingReportChecklist = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_MIXING_REPORT],
    mutationFn: (id: string) => mixingReportChecklistService.deleteReportMixingFishmealTemplate(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PROCESS] });
    },
  });
};

// Lấy chi tiết biểu mẫu báo cáo trộn sản phẩm bột cá theo id
const useGetMixingReportChecklistById = (id: string) => {
  return useQuery({
    queryKey: [MIXING_REPORT_CHECKLIST, id],
    queryFn: () => mixingReportChecklistService.getReportMixingFishmealTemplateById(id),
    enabled: !!id,
  });
};

export default {
  usePostMixingReportChecklist,
  usePatchMixingReportChecklist,
  useDeleteMixingReportChecklist,
  useGetMixingReportChecklistById,
};
