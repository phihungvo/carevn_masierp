import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import workCenterService from 'app/services/work-center.service';
import { IApiError } from 'app/shared/model/error.model';
import { IPostWorkCenterDto, IWorkCenterParams } from 'app/shared/model/work-center.model';
import { AxiosError } from 'axios';

const { PRODUCTION_WORK_CENTERS } = QUERY_KEY;
const { CREATE_WORK_CENTER, UPDATE_WORK_CENTER, DELETE_WORK_CENTER } = MUTATION_KEY;

// Lấy danh sách cụm máy sản xuất
const useGetWorkCentersQuery = (filter: IWorkCenterParams) => {
  return useQuery({
    queryKey: [PRODUCTION_WORK_CENTERS, filter?.page, filter?.size, filter?.search, filter?.status],
    queryFn: () => workCenterService.getWorkCenters(filter),
    select: data => data.data,
  });
};

// Lấy chi tiết cụm máy sản xuất
const useGetWorkCenterByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_WORK_CENTERS, id],
    queryFn: () => workCenterService.getWorkCenterById(id),
    enabled: !!id,
  });
};

// Tạo cụm máy sản xuất
const usePostWorkCenterMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_WORK_CENTER],
    mutationFn: workCenterService.postWorkCenter,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_WORK_CENTERS] });
      toggle();
      toggleSuccess();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

// Cập nhật cụm máy sản xuất
const useUpdateWorkCenterMutation = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_WORK_CENTER, id],
    mutationFn: (data: IPostWorkCenterDto) => workCenterService.patchWorkCenter(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_WORK_CENTERS] });
      toggle();
      toggleSuccess();
    },
  });
};

// Xóa cụm máy sản xuất
const useDeleteWorkCenterMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_WORK_CENTER],
    mutationFn: (id: string) => workCenterService.deleteWorkCenter(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_WORK_CENTERS] });
    },
  });
};

export default {
  useGetWorkCentersQuery,
  useGetWorkCenterByIdQuery,
  usePostWorkCenterMutation,
  useUpdateWorkCenterMutation,
  useDeleteWorkCenterMutation,
};
