import { QueryObserverOptions, useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { DATE_FORMAT } from 'app/constants/common';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import suppliesRequestService from 'app/services/supplies-request.service';
import { ISuppliesRequest, ISuppliesRequestApprove, ISuppliesRequestParams } from 'app/shared/model/supplies-request.model';
import dayjs from 'dayjs';
import { useState } from 'react';

const { SUPPLIES_REQUESTS, SUPPLIES_REQUESTS_TYPE, SUPPLIES_REQUEST } =
  QUERY_KEY;
const {
  CREATE_SUPPLIES_REQUEST,
  UPDATE_SUPPLIES_REQUEST,
  DELETE_SUPPLIES_REQUEST,
  REVIEW_SUPPLIES_REQUEST,
  APPROVE_SUPPLIES_REQUEST,
  REJECT_SUPPLIES_REQUEST,
  PATMENT_REQUEST_EXPORT_EXCEL
} = MUTATION_KEY;

const useGetSuppliesRequestsType = (
  filter?: ISuppliesRequestParams,
  options?: Partial<QueryObserverOptions>
) => {
  return useQuery({
    queryKey: [SUPPLIES_REQUESTS_TYPE, filter?.page, filter?.size],
    queryFn: suppliesRequestService.getSuppliesRequestTypes(filter),
    select: data => data?.['data'],
    ...options,
  });
};

const useGetSuppliesRequests = (filter?: ISuppliesRequestParams) => {
  return useQuery({
    queryKey: [SUPPLIES_REQUESTS, filter],
    queryFn: () => suppliesRequestService.getSuppliesRequest(filter),
    select: data => data?.data,
  });
};

const useGetSuppliesRequestById = (id: string) => {
  return useQuery({
    queryKey: [SUPPLIES_REQUEST, id],
    queryFn: () => suppliesRequestService.getSuppliesRequestById(id),
    select: res => res?.data,
    enabled: !!id,
  });
};

const useCreateSuppliesRequest = (
  toggle?: () => void,
  onError?: (err: Error) => void
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: suppliesRequestService.createSuppliesRequest,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      toggle && toggle();
    },
    onError
  });
};

const useUpdateSuppliesRequest = (id: string, onOk?: () => void, onError?: (err: Error) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_SUPPLIES_REQUEST],
    mutationFn: (data: ISuppliesRequest) => suppliesRequestService.updateSuppliesRequest(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUEST, id],
      });
      onOk && onOk();
    },
    onError
  });
};

const useDeleteSuppliesRequest = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_SUPPLIES_REQUEST],
    mutationFn: () => suppliesRequestService.deleteSuppliesRequest(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      onOk && onOk();
    },
  });
};

const useReviewSuppliesRequest = (id: string, onOk?: () => void, onError?: (err: Error) => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [REVIEW_SUPPLIES_REQUEST, id],
    mutationFn: (data?: any) => suppliesRequestService.reviewSuppliesRequest(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUEST, id],
      });
      onOk && onOk();
    },
    onError
  });
}

const useApproveSignSuppliesRequest = (id: string, onOk?: () => void, onError?: (err: Error) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [APPROVE_SUPPLIES_REQUEST, id],
    mutationFn: (data: ISuppliesRequestApprove) => suppliesRequestService.approveSignSuppliesRequest(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUEST, id],
      });
      onOk && onOk();
    },
    onError
  });
}

const useRejectSignSuppliesRequest = (id: string, onOk?: () => void, onError?: (error: Error) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REJECT_SUPPLIES_REQUEST, id],
    mutationFn: (data: ISuppliesRequestApprove) => suppliesRequestService.rejectSignSuppliesRequest(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUEST, id],
      });
      onOk && onOk();
    },
    onError
  });
}

const useCancelSuppliesRequest = (id: string, onOk?: () => void, onError?: (error: Error) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: suppliesRequestService.cancelSuppliesRequest(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUESTS],
      });
      queryClient.invalidateQueries({
        queryKey: [SUPPLIES_REQUEST, id],
      });
      onOk && onOk();
    },
    onError
  });
}

const useNextCodeSuppliesRequest = () => {
  let date = dayjs().format('MMYY');
  return useQuery({
    queryKey: ['nextCodeSuppliesRequest'],
    queryFn: suppliesRequestService.nextCodeSuppliesRequest,
    select: (res) => 'ĐXMH' + date + '/' + res?.data?.currentSequence
  });
}

const useExportSuppliesRequest = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [PATMENT_REQUEST_EXPORT_EXCEL],
    queryFn: suppliesRequestService.exportExcelSuppliesRequest,
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({ queryKey: [PATMENT_REQUEST_EXPORT_EXCEL] });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
}


export default {
  useGetSuppliesRequests,
  useGetSuppliesRequestById,
  useCreateSuppliesRequest,
  useUpdateSuppliesRequest,
  useDeleteSuppliesRequest,
  useReviewSuppliesRequest,
  useApproveSignSuppliesRequest,
  useGetSuppliesRequestsType,
  useRejectSignSuppliesRequest,
  useCancelSuppliesRequest,
  useExportSuppliesRequest,
  useNextCodeSuppliesRequest
};
