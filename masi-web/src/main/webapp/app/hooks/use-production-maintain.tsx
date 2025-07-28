import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { productionMaintainService } from 'app/services/production-maintain.service';
import { IApiError } from 'app/shared/model/error.model';
import {
  IProductionMaintain,
  IProductionMaintainParams,
} from 'app/shared/model/production-maintain.model';
import { AxiosError } from 'axios';

const { PRODUCTION_MAINTAINS } = QUERY_KEY;
const {
  CREATE_PRODUCTION_MAINTAIN,
  UPDATE_PRODUCTION_MAINTAIN,
  DELETE_PRODUCTION_MAINTAIN,
} = MUTATION_KEY;

const useGetProductionMaintains = (filter?: IProductionMaintainParams) => {
  return useQuery({
    queryKey: [
      PRODUCTION_MAINTAINS,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.manufactureStartDate,
      filter?.manufactureEndDate,
      filter?.expiredStartDate,
      filter?.expiredEndDate,
      filter?.statuses,
      filter?.isProductRoutingSpecified,
    ],
    queryFn: () => productionMaintainService.getProductionMaintains(filter),
    select: data => data.data,
  });
};

const useGetProductionMaintainById = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_MAINTAINS, id],
    queryFn: () => productionMaintainService.getProductionMaintainById(id),
    enabled: !!id,
  });
};

const usePostProductionMaintain = (
  toggle: () => void,
  toggleSuccess: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_MAINTAIN],
    mutationFn: productionMaintainService.postProductionMaintain,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_MAINTAINS] });
      toggle();
      toggleSuccess();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const usePatchProductionMaintain = (
  id: string,
  toggle: () => void,
  toggleSuccess: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_MAINTAIN, id],
    mutationFn: (data: IProductionMaintain) =>
      productionMaintainService.patchProductionMaintain(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_MAINTAINS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useDeleteProductionMaintain = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_MAINTAIN],
    mutationFn: (id: string) =>
      productionMaintainService.deleteProductionMaintain(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_MAINTAINS] });
    },
  });
};

export default {
  useGetProductionMaintains,
  useGetProductionMaintainById,
  usePostProductionMaintain,
  usePatchProductionMaintain,
  useDeleteProductionMaintain,
};
