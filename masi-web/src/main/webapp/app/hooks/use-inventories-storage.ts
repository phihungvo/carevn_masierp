import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { IItemCategoryParams } from 'app/shared/model/item-category.model';
import inventoriesStorageService from 'app/services/inventories-storage-new.service';
import { useState } from 'react';
import {
  IInventoriesStorage,
  IInventoriesStorageParams,
} from 'app/shared/model/inventories-storage.model';

const { INVENTORIES_STORAGES } = QUERY_KEY;
const { UPDATE_INVENTORIES_STORAGES } = MUTATION_KEY;

const useGetInventoriesStorageQuery = (filter?: IInventoriesStorageParams) => {
  return useQuery({
    queryKey: [INVENTORIES_STORAGES, filter],
    queryFn: () => inventoriesStorageService.getInventoriesStorage(filter),
    select: data => data.data,
  });
};

const useGetDepreciationInventoriesStorageQuery = (
  filter?: IInventoriesStorageParams,
) => {
  return useQuery({
    queryKey: [INVENTORIES_STORAGES, filter],
    queryFn: () =>
      inventoriesStorageService.getDepreciationInventoriesStorage(filter),
    select: data => data.data,
  });
};

const useGetFishMealInventoriesStorageQuery = (
  filter?: IInventoriesStorageParams,
) => {
  return useQuery({
    queryKey: [INVENTORIES_STORAGES, filter],
    queryFn: () =>
      inventoriesStorageService.getFishMealInventoriesStorage(filter),
    select: data => data.data,
  });
};

export const useExportInventoriesStorageXlsxLazyQuery = (
  filter?: IInventoriesStorageParams,
) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [QUERY_KEY.INVENTORIES_STORAGES_EXPORT, filter],
    queryFn: () =>
      inventoriesStorageService.exportInventoriesStorageXlsx(filter),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [QUERY_KEY.SUPPLIER_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useUpdateInventoriesStorageMutation = (
  id: string,
  toggleSuccess: () => void,
) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_INVENTORIES_STORAGES, id],
    mutationFn: (data: IInventoriesStorage) =>
      inventoriesStorageService.updateInventoriesStorage(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [INVENTORIES_STORAGES] });
      toggleSuccess();
    },
  });
};

const useGetInventoriesStorageByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [INVENTORIES_STORAGES, id],
    queryFn: () => inventoriesStorageService.getInventoriesStorageById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

export default {
  useGetInventoriesStorageQuery,
  useGetDepreciationInventoriesStorageQuery,
  useGetFishMealInventoriesStorageQuery,
  useExportInventoriesStorageXlsxLazyQuery,
  useUpdateInventoriesStorageMutation,
  useGetInventoriesStorageByIdQuery,
};
