import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import productionQualityControlService from 'app/services/production-quality-control.service';
import { IApiError } from 'app/shared/model/error.model';
import {
  IProductionQualityParams,
  IQualityCheckSample,
} from 'app/shared/model/production-quality-control.model';
import { AxiosError } from 'axios';

const {
  CREATE_QUALITY_SAMP_REVIEWS,
  UPDATE_QUALITY_SAMP_REVIEWS,
  UPDATE_QUALITY_SAMP_REVIEWS_PASS,
  DELETE_QUALITY_SAMP_REVIEWS,
  REJECT_QUALITY_SAMP_REVIEWS,
} = MUTATION_KEY;
const { PRODUCTION_QUALITY, PRODUCTION_QUALITIES } = QUERY_KEY;

// Lấy danh sách biểu mẫu giám sát chất lượng sản phẩm
const useGetQualityCheckSamples = (filter: IProductionQualityParams) => {
  return useQuery({
    queryKey: [PRODUCTION_QUALITIES, filter],
    queryFn: () =>
      productionQualityControlService.getQualityCheckSamples(filter),
    select: data => data.data,
  });
};

// Tạo biểu mẫu giám sát chất lượng sản phẩm
const usePostQualityCheckSample = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_QUALITY_SAMP_REVIEWS],
    mutationFn: (data: IQualityCheckSample) =>
      productionQualityControlService.postQualityCheckSample(data),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

// Cập nhật biểu mẫu giám sát chất lượng sản phẩm
const usePatchQualityCheckSample = (id: string, toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_QUALITY_SAMP_REVIEWS],
    mutationFn: (data: IQualityCheckSample) =>
      productionQualityControlService.patchQualityCheckSample(data, id),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

// Lấy chi tiết biểu mẫu giám sát chất lượng sản phẩm
const useGetQualityCheckSampleById = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_QUALITY, id],
    queryFn: () =>
      productionQualityControlService.getQualityCheckSampleById(id),
    enabled: !!id,
  });
};

// Xoá biểu mẫu giám sát chất lượng sản phẩm
const useDeleteQualityCheckSample = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_QUALITY_SAMP_REVIEWS],
    mutationFn: (id: string) =>
      productionQualityControlService.deleteQualityCheckSample(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

// Cập nhập trạng thái đã đạt của mẫu giám sát chất lượng sản phẩm
const usePatchQualityCheckSampleByIdReviewPass = (
  id: string,
  toggle?: () => void,
  toggleSuccess?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_QUALITY_SAMP_REVIEWS_PASS],
    mutationFn: () =>
      productionQualityControlService.patchQualityCheckSampleByIdReviewPass(id),
    onSuccess: () => {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

// Cập nhập trạng thái hủy của mẫu giám sát chất lượng sản phẩm
const usePatchQualityCheckSampleByIdReviewFail = (
  id: string,
  toggle?: () => void,
  toggleSuccess?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_QUALITY_SAMP_REVIEWS_PASS],
    mutationFn: () =>
      productionQualityControlService.patchQualityCheckSampleByIdReviewFail(id),
    onSuccess: () => {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

const useRejectQualityCheckSamples = (
  id: string,
  toggle?: () => void,
  toggleSuccess?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REJECT_QUALITY_SAMP_REVIEWS, id],
    mutationFn: (data: { rejectNote: string }) =>
      productionQualityControlService.rejectQualityCheckSample(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

export default {
  useGetQualityCheckSamples,
  usePostQualityCheckSample,
  usePatchQualityCheckSample,
  useGetQualityCheckSampleById,
  useDeleteQualityCheckSample,
  usePatchQualityCheckSampleByIdReviewPass,
  usePatchQualityCheckSampleByIdReviewFail,
  useRejectQualityCheckSamples,
};
