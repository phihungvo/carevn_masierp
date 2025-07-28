import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import vatRatesService from 'app/services/vat-rates.service';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IVatRate, IVatRateParams } from 'app/shared/model/vat-rate.model';
import { AxiosResponse } from 'axios';

const { VAT_RATES } = QUERY_KEY;
const { CREATE_VAT_RATES, UPDATE_VAT_RATES, DELETE_VAT_RATES } = MUTATION_KEY;

const useGetVatRates = (
  filter?: IVatRateParams,
  select?: (data: AxiosResponse<PaginationResponse<IVatRate>, any>) => any
) => {
  return useQuery({
    queryKey: [VAT_RATES, filter?.page, filter?.size],
    queryFn: () => vatRatesService.getVatRates(filter),
    select: data => {
      if (select) return select(data);
      return data?.data;
    },
  });
};

const usePostVatRate = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_VAT_RATES],
    mutationFn: vatRatesService.createVatRate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [VAT_RATES] });
      onOk && onOk();
    },
  });
};

const usePatchVatRate = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_VAT_RATES, id],
    mutationFn: (data: any) => vatRatesService.updateVatRate(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [VAT_RATES] });
      onOk && onOk();
    },
  });
};

const useGetVatRateById = (id: string) => {
  return useQuery({
    queryKey: [VAT_RATES, id],
    queryFn: () => vatRatesService.getVatRateById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useDeleteVatRate = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_VAT_RATES],
    mutationFn: vatRatesService.deleteVatRate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [VAT_RATES] });
      onOk && onOk();
    },
  });
};

export default {
  useGetVatRates,
  usePostVatRate,
  usePatchVatRate,
  useGetVatRateById,
  useDeleteVatRate,
};
