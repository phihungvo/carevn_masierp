import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import {
  IQuotationCreate,
  IQuotationCustomerProcess,
  IQuotationInternalApprove,
  IQuotationInternalReject,
  IQuotationParams,
} from 'app/shared/model/quotation.model';
import quotationService from 'app/services/quotation.service';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';

const { QUOTATIONS, QUOTATION, QUOTATIONS_EXPORT } = QUERY_KEY;
const {
  CREATE_QUOTATION,
  UPDATE_QUOTATION,
  DELETE_QUOTATION,
  INTERNAL_SEND_QUOTATION,
  CANCEL_QUOTATION,
  CUSTOMER_SEND_QUOTATION,
  INTERNAL_APPROVE_QUOTATION,
  INTERNAL_REJECT_QUOTATION,
  CUSTOMER_PROCESS_QUOTATION,
} = MUTATION_KEY;

const useGetQuotations = (filter?: IQuotationParams) => {
  return useQuery({
    queryKey: [QUOTATIONS, filter?.page, filter?.size, filter?.search, filter?.companyId, filter?.status],
    queryFn: () => quotationService.getQuotations(filter),
    select: data => data.data,
  });
};

const usePostQuotation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_QUOTATION],
    mutationFn: (data: IQuotationCreate) => quotationService.postQuotation(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
      onOk && onOk();
    },
  });
};

const usePatchQuotation = (id: string, onOk?: () => void, onError?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_QUOTATION, id],
    mutationFn: (data: IQuotationCreate) => quotationService.patchQuotation(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATION, id],
      });
      onOk && onOk();
    },
    onError: () => {
      onError && onError();
    },
  });
};

const useGetQuotationById = (id: string) => {
  return useQuery({
    queryKey: [QUOTATION, id],
    queryFn: () => quotationService.getQuotationById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useDeleteQuotation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_QUOTATION],
    mutationFn: (id: string) => quotationService.deleteQuotation(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
    },
  });
};

const usePatchQuotationInternalSend = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [INTERNAL_SEND_QUOTATION],
    mutationFn: (id: string) => quotationService.internalSendQuotation(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
    },
  });
};

const usePatchQuotationCancel = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_QUOTATION],
    mutationFn: (id: string) => quotationService.cancelQuotation(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
    },
  });
};

const usePatchQuotationCustomerSend = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CUSTOMER_SEND_QUOTATION],
    mutationFn: (id: string) => quotationService.customerSend(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
    },
  });
};

const usePatchQuotationInternalApprove = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [INTERNAL_APPROVE_QUOTATION, id],
    mutationFn: (data: IQuotationInternalApprove) => quotationService.internalApprove(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchQuotationInternalReject = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [INTERNAL_REJECT_QUOTATION, id],
    mutationFn: (data: IQuotationInternalReject) => quotationService.internalReject(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchQuotationCustomerProcess = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CUSTOMER_PROCESS_QUOTATION, id],
    mutationFn: (data: IQuotationCustomerProcess) => quotationService.customerProcess(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATIONS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useGetQuotationExport = (id: string, download: boolean, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [QUOTATIONS_EXPORT, id],
    queryFn: () => quotationService.getQuotationExport(id, download),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [QUOTATIONS_EXPORT],
    });
  }

  if (query.isSuccess && enabled) {
    toggleSuccess && toggleSuccess();
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

export default {
  useGetQuotations,
  usePostQuotation,
  usePatchQuotation,
  useGetQuotationById,
  useDeleteQuotation,
  usePatchQuotationInternalSend,
  usePatchQuotationCancel,
  usePatchQuotationCustomerSend,
  usePatchQuotationInternalApprove,
  usePatchQuotationInternalReject,
  usePatchQuotationCustomerProcess,
  useGetQuotationExport,
};
