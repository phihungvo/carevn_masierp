import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import quotationDetailsService from 'app/services/quotation-details.service';
import { IQuotationDetail } from 'app/shared/model/quotation.model';

const { QUOTATION_DETAILS, QUOTATION } = QUERY_KEY;
const { CREATE_QUOTATION_DETAILS, UPDATE_QUOTATION_DETAILS, DELETE_QUOTATION_DETAILS } = MUTATION_KEY;

const useGetQuotationDetailsById = (id: string) => {
  return useQuery({
    queryKey: [QUOTATION_DETAILS, id],
    queryFn: () => quotationDetailsService.getQuotationDetailsById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostQuotationDetails = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_QUOTATION_DETAILS],
    mutationFn: (data: IQuotationDetail) => quotationDetailsService.postQuotationDetails(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATION],
      });
      onOk && onOk();
    },
  });
};

const usePatchQuotationDetails = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_QUOTATION_DETAILS, id],
    mutationFn: (data: IQuotationDetail) => quotationDetailsService.patchQuotationDetails(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATION],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useDeleteQuotationDetails = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_QUOTATION_DETAILS],
    mutationFn: (id: string) => quotationDetailsService.deleteQuotationDetails(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUOTATION],
      });
    },
  });
};

export default {
  useGetQuotationDetailsById,
  usePostQuotationDetails,
  usePatchQuotationDetails,
  useDeleteQuotationDetails,
};
