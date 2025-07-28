import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import currencyService from 'app/services/currencies.service';
import { ICurrencyParams } from 'app/shared/model/currencies.model';

const { CURRENCIES, CURRENCY } = QUERY_KEY;
const { CREATE_CURRENCY, UPDATE_CURRENCY, DELETE_CURRENCY } = MUTATION_KEY;

const useGetCurrencies = (filter?: ICurrencyParams) => {
  return useQuery({
    queryKey: [CURRENCIES, filter],
    queryFn: () => currencyService.getCurrencies(filter),
    select: data => data?.data,
  });
};

const usePostCurrency = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_CURRENCY],
    mutationFn: currencyService.createCurrency,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CURRENCIES] });
      onOk && onOk();
    },
  });
};

const usePatchCurrency = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CURRENCY, id],
    mutationFn: (data: any) => currencyService.updateCurrency(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CURRENCIES] });
      onOk && onOk();
    },
  });
};

const useGetCurrencyById = (id: string) => {
  return useQuery({
    queryKey: [CURRENCY, id],
    queryFn: () => currencyService.getCurrencyById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useDeleteCurrency = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_CURRENCY],
    mutationFn: currencyService.deleteCurrency,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CURRENCIES] });
      onOk && onOk();
    },
  });
};

export default {
  useGetCurrencies,
  usePostCurrency,
  usePatchCurrency,
  useGetCurrencyById,
  useDeleteCurrency,
};
