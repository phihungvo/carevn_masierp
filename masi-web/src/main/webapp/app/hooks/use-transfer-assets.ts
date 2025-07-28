import {
  QueryObserverOptions,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { DATE_FORMAT } from 'app/constants/common';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import itemsService from 'app/services/items.service';
import transferAssetsService from 'app/services/transfer-assets.service';
import { IApiError } from 'app/shared/model/error.model';
import { ITransferAssets, ITransferAssetsParams } from 'app/shared/model/transfer-assets.model';
import { AxiosError } from 'axios';
import dayjs from 'dayjs';
import { useState } from 'react';

const { TRANSFER_ASSETS, TRANSFER_ASSETS_EXPORT, TRANSFER_ASSETS_NEXT_CODE } = QUERY_KEY;
const { CREATE_TRANSFER_ASSETS, UPDATE_TRANSFER_ASSETS, DELETE_TRANSFER_ASSETS, DISABLE_TRANSFER_ASSETS, ENABLE_TRANSFER_ASSETS } =
  MUTATION_KEY;

const useGetTransferAssetsQuery = (
  filter?: ITransferAssetsParams,
  options?: Partial<QueryObserverOptions>,
) => {
  return useQuery({
    queryKey: [TRANSFER_ASSETS, filter],
    queryFn: () => transferAssetsService.getTransferAssets(filter),
    select: data => data?.['data'],
    placeholderData: old => old,
    ...options,
  });
};

const useNextCodeTransferAssets = () => {
  const suffix = dayjs(new Date()).format(DATE_FORMAT.MONTH_YEAR);
  return useQuery({
    queryKey: [TRANSFER_ASSETS_NEXT_CODE],
    queryFn: () =>  transferAssetsService.nextCodeTransferAssets(),
    select: data => `MTS${suffix}/${data.data?.nextAndIncrement}` ,
  });
}

const useGetTransferAssetsByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [TRANSFER_ASSETS, id],
    queryFn: () => transferAssetsService.getTransferAssetsById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useCreateTransferAssets = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_TRANSFER_ASSETS],
    mutationFn: transferAssetsService.postTransferAssets,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TRANSFER_ASSETS],
      });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useUpdateTransferAssetsMutation = (id: string, toggle: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_TRANSFER_ASSETS, id],
    mutationFn: (data: ITransferAssets) => transferAssetsService.patchTransferAssets(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TRANSFER_ASSETS] });
      toggle();
    },
  });
};

const useDeleteTransferAssetsMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_TRANSFER_ASSETS],
    mutationFn: (id: string) => transferAssetsService.deleteTransferAssets(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TRANSFER_ASSETS] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};


export const useExportTransferAssetsXlsxLazyQuery = (filter?: ITransferAssetsParams) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [TRANSFER_ASSETS_EXPORT, filter],
    queryFn: () => transferAssetsService.exportTransferAssets(filter),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [TRANSFER_ASSETS_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useDisableTransferAssetsMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_TRANSFER_ASSETS],
    mutationFn: (id: string) => transferAssetsService.disableTransferAssets(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TRANSFER_ASSETS] });
    },
  });
};

const useEnableTransferAssetsMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [ENABLE_TRANSFER_ASSETS],
    mutationFn: (id: string) => transferAssetsService.enableTransferAssets(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [TRANSFER_ASSETS] });
    },
  });
};


export default {
  useDeleteTransferAssetsMutation,
  useGetTransferAssetsQuery,
  useNextCodeTransferAssets,
  useCreateTransferAssets,
  useGetTransferAssetsByIdQuery,
  useUpdateTransferAssetsMutation,
  useExportTransferAssetsXlsxLazyQuery,
  useDisableTransferAssetsMutation,
  useEnableTransferAssetsMutation,


};
