import {
  QueryObserverOptions,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import itemsService from 'app/services/items.service';
import supplierService from 'app/services/supplier.service';
import { IApiError } from 'app/shared/model/error.model';
import { IItem, IItemParams } from 'app/shared/model/item.model';
import { AxiosError } from 'axios';
import { useState } from 'react';

const { ITEMS, ITEMS_EXPORT } = QUERY_KEY;
const { CREATE_ITEM, UPDATE_ITEM, DELETE_ITEM, DISABLE_ITEM, ENABLE_ITEM } =
  MUTATION_KEY;

const useGetItemsQuery = (
  filter?: IItemParams,
  options?: Partial<QueryObserverOptions>,
) => {
  return useQuery({
    queryKey: [ITEMS, filter],
    queryFn: () => itemsService.getItems(filter),
    select: data => data?.['data'],
    placeholderData: old => old,
    ...options,
  });
};

const useGetItemByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [ITEMS, id],
    queryFn: () => itemsService.getItemById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useCreateItem = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_ITEM],
    mutationFn: itemsService.postItem,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [ITEMS],
      });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useUpdateItemMutation = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_ITEM, id],
    mutationFn: (data: IItem) => itemsService.patchItem(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ITEMS] });
      toggle();
    },
  });
};

const useDeleteItemMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_ITEM],
    mutationFn: (id: string) => itemsService.deleteItem(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ITEMS] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useDisableItemMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_ITEM],
    mutationFn: (id: string) => itemsService.disableItem(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ITEMS] });
    },
  });
};

const useEnableItemMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_ITEM],
    mutationFn: (id: string) => itemsService.enableItem(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ITEMS] });
    },
  });
};

export const useExportItemsXlsxLazyQuery = (filter?: IItemParams) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [QUERY_KEY.ITEMS_EXPORT, filter],
    queryFn: () => itemsService.exportItems(filter),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [QUERY_KEY.ITEMS_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetItemsPercentProteinQuery = (
  filter?: IItemParams,
  options?: Partial<QueryObserverOptions>,
) => {
  return useQuery({
    queryKey: [ITEMS, filter],
    queryFn: () => itemsService.getItemsPercentProtein(filter),
    select: data => data?.['data'],
    placeholderData: old => old,
    ...options,
  });
};

export default {
  useDeleteItemMutation,
  useGetItemsQuery,
  useCreateItem,
  useGetItemByIdQuery,
  useUpdateItemMutation,
  useDisableItemMutation,
  useEnableItemMutation,
  useExportItemsXlsxLazyQuery,
  useGetItemsPercentProteinQuery,
};
