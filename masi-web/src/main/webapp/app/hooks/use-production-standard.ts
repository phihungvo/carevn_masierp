import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import productionStandardService from 'app/services/production-standard.service';
import { IApiError } from 'app/shared/model/error.model';
import { IProductionStandardParams } from 'app/shared/model/production-command.model';
import { AxiosError } from 'axios';

const { PRODUCTION_STANDARD, PRODUCTION_STANDARDS, PRODUCTION_STANDARDS_MO } =
  QUERY_KEY;
const {
  DELETE_PRODUCTION_STANDARD,
  CREATE_PRODUCTION_STANDARD,
  UPDATE_PRODUCTION_STANDARD,
  DISPOSE_PRODUCTION_STANDARD,
} = MUTATION_KEY;

// Tạo mới Định Mức Sản Xuất
const usePostProductionStandard = (toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_STANDARD],
    mutationFn: productionStandardService.postProductionStandard,
    onSuccess: () => {
      toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS] });
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

// Lấy Định Mức Sản Xuất
const useGetProductionStandardById = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_STANDARD, id],
    queryFn: () => productionStandardService.getProductionStandardById(id),
    enabled: !!id,
  });
};

// Lấy Danh Sách Định Mức Sản Xuất
const useGetProductionStandards = (
  filter?: IProductionStandardParams,
  enabled?: boolean,
) => {
  return useQuery({
    queryKey: [PRODUCTION_STANDARDS, filter],
    queryFn: () => productionStandardService.getProductionStandards(filter),
    select: data => data.data,
    enabled,
  });
};

// Cập Nhật Định Mức Sản Xuất
const usePatchProductionStandard = (toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_STANDARD],
    mutationFn: productionStandardService.patchProductionStandard,
    onSuccess: () => {
      toggle();
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS] });
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARD] });
    },
  });
};

// Xóa Định Mức Sản Xuất
const useDeleteProductionStandard = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_STANDARD],
    mutationFn: (body: { id: string }) =>
      productionStandardService.deleteProductionStandard(body.id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS] });
    },
  });
};

// Lấy Danh Sách Định Mức Sản Xuất và lệnh sản xuất
const useGetProductionStandardsWithCommands = (
  filter?: IProductionStandardParams,
) => {
  return useQuery({
    queryKey: [
      PRODUCTION_STANDARDS_MO,
      filter?.page,
      filter?.size,
      filter?.name,
      filter?.searchString,
      filter?.statuses,
      filter?.startDate,
      filter?.endDate,
    ],
    queryFn: () =>
      productionStandardService.getProductionStandardsWithCommands(filter),
    select: data => data.data,
  });
};

// Hủy Định Mức Sản Xuất
const useDisposeProductionStandard = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DISPOSE_PRODUCTION_STANDARD],
    mutationFn: (body: { id: string }) =>
      productionStandardService.disposeProductionStandard(body.id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_STANDARDS] });
    },
  });
};

export default {
  usePostProductionStandard,
  useGetProductionStandardById,
  useGetProductionStandards,
  usePatchProductionStandard,
  useDeleteProductionStandard,
  useGetProductionStandardsWithCommands,
  useDisposeProductionStandard,
};
