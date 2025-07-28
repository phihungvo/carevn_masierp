import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import uniformService from 'app/services/uniform.service';
import { IApiError } from 'app/shared/model/error.model';
import {
  IUniform,
  IUniformOrderParams,
  IUniformOrderProcess,
  IUniformOrderUpdate,
  IUniformParams,
  IUniformReleaseParams,
  IUniformStockParams,
} from 'app/shared/model/uniform.model';
import { AxiosError } from 'axios';
import { useState } from 'react';

const { UNIFORMS, UNIFORM, UNIFORMS_EXPORT, UNIFORM_ORDER, UNIFORM_ORDERS, UNIFORM_RELEASES, UNIFORM_RELEASE, UNIFORM_STOCKS, UNIFORM_RETURN_REMAINING } =
  QUERY_KEY;
const {
  CREATE_UNIFORM,
  UPDATE_UNIFORM,
  DELETE_UNIFORM,
  CREATE_UNIFORM_ORDER,
  UPDATE_UNIFORM_ORDER,
  DELETE_UNIFORM_ORDER,
  CANCEL_UNIFORM_ORDER,
  PROCESS_UNIFORM_ORDER,
  CREATE_UNIFORM_RELEASE,
  PATCH_UNIFORM_STOCK,
  CREATE_UNIFORM_RETURN,
  EXPORT_UNIFORM_STOCK_IN,
  EXPORT_UNIFORM_STOCK_OUT,
  CREATE_UNIFORM_ORDER_STOCK,
  UPDATE_UNIFORM_STATUS,
} = MUTATION_KEY;

const useUniforms = (filter?: IUniformParams) => {
  return useQuery({
    queryKey: [UNIFORMS, filter?.page, filter?.size],
    queryFn: () => uniformService.getUniforms(filter),
    select: data => data?.data,
  });
};

const useUniformById = (id: string) => {
  return useQuery({
    queryKey: [UNIFORM, id],
    queryFn: () => uniformService.getUniformById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useCreateUniform = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UNIFORM],
    mutationFn: uniformService.createUniform,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORMS],
      });
      onOk && onOk();
    },
  });
};

const useUpdateUniform = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_UNIFORM, id],
    mutationFn: (data: IUniform) => uniformService.updateUniform(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORMS],
      });
      onOk && onOk();
    },
  });
};

const useDeleteUniform = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_UNIFORM],
    mutationFn: (id: string) => uniformService.deleteUniform(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORMS],
      });
      onOk && onOk();
    },
  });
};

const useUpdateStatusUniform = (id: string, data: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_UNIFORM_STATUS, id, data],
    mutationFn: () => uniformService.patchUniformStatus(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORMS],
      });
      onOk && onOk();
    },
  });
}

const useUniformOrders = (filter?: IUniformOrderParams) => {
  return useQuery({
    queryKey: [UNIFORM_ORDERS, filter?.page, filter?.size, filter?.name, filter?.startDate, filter?.endDate, filter?.status],
    queryFn: () => uniformService.getUniformOrders(filter),
    select: data => data?.data,
  });
};

const usePostUniformOrder = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UNIFORM_ORDER],
    mutationFn: uniformService.postUniformOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      onOk && onOk();
    },
  });
};

const usePatchUniformOrder = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_UNIFORM_ORDER, id],
    mutationFn: (data: IUniformOrderUpdate) => uniformService.patchUniformOrder(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      onOk && onOk();
    },
  });
};

const useUniformOrderById = (id: string) => {
  return useQuery({
    queryKey: [UNIFORM_ORDER, id],
    queryFn: () => uniformService.getUniformOrderById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useCancelUniformOrder = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_UNIFORM_ORDER],
    mutationFn: (id: string) => uniformService.cancelUniformOrder(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      onOk && onOk();
    },
  });
};

const useDeleteUniformOrder = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_UNIFORM_ORDER],
    mutationFn: (id: string) => uniformService.deleteUniformOrder(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      onOk && onOk();
    },
  });
};

const useProcessUniformOrder = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [PROCESS_UNIFORM_ORDER, id],
    mutationFn: (data: IUniformOrderProcess) => uniformService.processUniformService(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      onOk && onOk();
    },
  });
};

const useUniformReleases = (filter?: IUniformReleaseParams) => {
  return useQuery({
    queryKey: [
      UNIFORM_RELEASES,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.type,
      filter?.uniformId,
      filter?.startDate,
      filter?.endDate,
      filter?.employeeIds,
    ],
    queryFn: () => uniformService.getUniformReleases(filter),
    select: data => data?.data,
  });
};

const usePostUniformRelease = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UNIFORM_RELEASE],
    mutationFn: uniformService.postUniformRelease,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_RELEASES],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_STOCKS],
      });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useUniformReleaseById = (id: string) => {
  return useQuery({
    queryKey: [UNIFORM_RELEASE, id],
    queryFn: () => uniformService.getUniformReleaseById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useUniformStock = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [PATCH_UNIFORM_STOCK, id],
    mutationFn: () => uniformService.uniformStock(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_RELEASES],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_STOCKS],
      });
      onOk && onOk();
    },
  });
};

const useUniformStocks = (filter?: IUniformStockParams) => {
  return useQuery({
    queryKey: [UNIFORM_STOCKS, filter?.page, filter?.size],
    queryFn: () => uniformService.uniformStocks(filter),
    select: data => data?.data,
  });
};

const usePostUniformReturn = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UNIFORM_RETURN],
    mutationFn: uniformService.postUniformReturn,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_RELEASES],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_RELEASE],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_STOCKS],
      });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useUniformReturnRemaining = (employeeId: string, uniformId: string) => {
  return useQuery({
    queryKey: [UNIFORM_RETURN_REMAINING, employeeId, uniformId],
    queryFn: () => uniformService.getUniformReturnsRemaining(employeeId, uniformId),
    select: data => data?.data,
    enabled: !!employeeId && !!uniformId,
  });
};

const useExportUniformStockIn = (startDate?: string, endDate?: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EXPORT_UNIFORM_STOCK_IN, startDate, endDate],
    queryFn: () => uniformService.exportUniformStockIn(startDate, endDate),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EXPORT_UNIFORM_STOCK_IN],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useExportUniformOrderStockIn = (startDate?: string, endDate?: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EXPORT_UNIFORM_STOCK_IN, startDate, endDate],
    queryFn: () => uniformService.exportUniformOrderStockIn(startDate, endDate),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EXPORT_UNIFORM_STOCK_IN],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useExportUniformStockOut = (startDate?: string, endDate?: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EXPORT_UNIFORM_STOCK_OUT, startDate, endDate],
    queryFn: () => uniformService.exportUniformStockOut(startDate, endDate),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EXPORT_UNIFORM_STOCK_OUT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const usePostUniformOrderStock = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UNIFORM_ORDER_STOCK],
    mutationFn: uniformService.postUniformOrderStock,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_ORDERS],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_RELEASES],
      });
      queryClient.invalidateQueries({
        queryKey: [UNIFORM_STOCKS],
      });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};
const useGetUniformsExportLazyQuery = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [UNIFORMS_EXPORT],
    queryFn: () => uniformService.getUniformExport(),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [UNIFORMS_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }
  return { trigger, ...query };
};


export default {
  useUniforms,
  useUniformById,
  useCreateUniform,
  useUpdateUniform,
  useDeleteUniform,
  useUniformOrders,
  usePostUniformOrder,
  usePatchUniformOrder,
  useUniformOrderById,
  useCancelUniformOrder,
  useDeleteUniformOrder,
  useProcessUniformOrder,
  useUniformReleases,
  usePostUniformRelease,
  useUniformReleaseById,
  useUniformStock,
  useUniformStocks,
  usePostUniformReturn,
  useUniformReturnRemaining,
  useExportUniformStockIn,
  useExportUniformOrderStockIn,
  useExportUniformStockOut,
  usePostUniformOrderStock,
  useUpdateStatusUniform,
  useGetUniformsExportLazyQuery,
};
