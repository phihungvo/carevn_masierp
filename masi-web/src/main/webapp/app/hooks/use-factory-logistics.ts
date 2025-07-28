import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import factoriesService from 'app/services/factory-logistics.service';
import { IApiError } from 'app/shared/model/error.model';
import { IFactoryLogistics, IFactoryLogisticsParams } from 'app/shared/model/factory-logistics.model';
import { AxiosError } from 'axios';

const { FACTORIES } = QUERY_KEY;
const { CREATE_FACTORIES, UPDATE_FACTORIES, DELETE_FACTORIES, DISABLE_FACTORIES, ENABLE_FACTORIES } = MUTATION_KEY;

const useGetFactoriesQuery = (filter?: IFactoryLogisticsParams) => {
  return useQuery({
    queryKey: [FACTORIES, filter],
    queryFn: () => factoriesService.getFactories(filter),
    select: data => data.data,
  });
};

const useGetFactoryByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [FACTORIES, id],
    queryFn: () => factoriesService.getFactoryById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useCreateFactory = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_FACTORIES],
    mutationFn: factoriesService.postFactory,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [FACTORIES],
      });
      onOk && onOk();
    },
  });
};

const useUpdateFactoryMutation = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_FACTORIES, id],
    mutationFn: (data: IFactoryLogistics) => factoriesService.patchFactory(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [FACTORIES] });
      toggle();
    },
  });
};

const useDisableFactoryMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_FACTORIES],
    mutationFn: (id: string) => factoriesService.disableFactory(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [FACTORIES] });
    },
  });
};

const useEnableFactoryMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_FACTORIES],
    mutationFn: (id: string) => factoriesService.enableFactory(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [FACTORIES] });
    },
  });
};

const useDeleteFactoryMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_FACTORIES],
    mutationFn: (id: string) => factoriesService.deleteFactory(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [FACTORIES] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

export default {
  useDeleteFactoryMutation,
  useGetFactoriesQuery,
  useCreateFactory,
  useGetFactoryByIdQuery,
  useUpdateFactoryMutation,
  useDisableFactoryMutation,
  useEnableFactoryMutation,
};
