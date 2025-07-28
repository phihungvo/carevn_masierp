import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import incomingInvoiceService from 'app/services/incoming-invoice.service';
import {
  IIncomingInvoice,
  IIncomingInvoiceParams,
} from 'app/shared/model/incoming-invoice.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { AxiosResponse } from 'axios';

function useIncomingInvoices(
  filter?: IIncomingInvoiceParams,
  select?: (data: AxiosResponse<PaginationResponse<IIncomingInvoice>, any>) => any
) {
  return useQuery({
    queryKey: [QUERY_KEY.INCOMING_INVOICES, filter],
    queryFn: () => incomingInvoiceService.getIncomingInvoices(filter),
    select: data => {
      if (select) return select(data);
      return data?.data;
    },
  });
}

function useNextIncomingInvoiceNumber() {
  return useQuery({
    queryKey: [QUERY_KEY.NEXT_INCOMING_INVOICE_CODE],
    queryFn: () => incomingInvoiceService.getNextIncomingInvoiceNumber(),
    select: data => data,
  });
}

function useIncomingInvoiceById(id: string) {
  return useQuery({
    queryKey: [QUERY_KEY.INCOMING_INVOICES, id],
    queryFn: () => incomingInvoiceService.getIncomingInvoiceById(id),
    select: data => data.data,
    enabled: !!id,
  });
}

function useCreateIncomingInvoice(onSuccess?: () => void) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: incomingInvoiceService.createIncomingInvoice,
    mutationKey: [MUTATION_KEY.CREATE_INCOMING_INVOICE],
    onSuccess() {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.INCOMING_INVOICES],
      });
      onSuccess && onSuccess();
    },
  });
}

function useUpdateIncomingInvoice(onSuccess?: () => void) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: IIncomingInvoice) =>
      incomingInvoiceService.patchIncomingInvoice(data),
    mutationKey: [MUTATION_KEY.UPDATE_INCOMING_INVOICE],
    onSuccess() {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.INCOMING_INVOICES],
      });
      onSuccess && onSuccess();
    },
  });
}

function useDeleteIncomingInvoice(onSuccess?: () => void) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) =>
      incomingInvoiceService.deleteIncomingInvoice(id),
    mutationKey: [MUTATION_KEY.DELETE_INCOMING_INVOICE],
    onSuccess() {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.INCOMING_INVOICES],
      });
      onSuccess && onSuccess();
    },
  });
}

export default {
  useIncomingInvoices,
  useIncomingInvoiceById,
  useCreateIncomingInvoice,
  useNextIncomingInvoiceNumber,
  useUpdateIncomingInvoice,
  useDeleteIncomingInvoice,
};
