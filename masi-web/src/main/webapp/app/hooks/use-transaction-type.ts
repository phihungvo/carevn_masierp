import {
  QueryObserverOptions,
  useQuery,
} from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import transactionTypeService from 'app/services/transaction-type.service';
import { ITransactionTypeParams } from 'app/shared/model/transaction-type.model';

const { TRANSACTION_TYPE } = QUERY_KEY;

const useGetTransactionTypeQuery = (
  filter?: ITransactionTypeParams,
  options?: Partial<QueryObserverOptions>,
) => {
  return useQuery({
    queryKey: [TRANSACTION_TYPE, filter],
    queryFn: () => transactionTypeService.getTransactionType(filter),
    select: data => data?.['data'],
    placeholderData: old => old,
    ...options,
  });
};

const useGetTransferAssetsByIdQuery = (
  filter?: ITransactionTypeParams,
  options?: Partial<QueryObserverOptions>,
) => {
  return useQuery({
    queryKey: [TRANSACTION_TYPE, filter],
    queryFn: () => transactionTypeService.getTransactionType(filter),
    select: data => data?.['data'],
    placeholderData: old => old,
    ...options,
  });
};

export default {
  useGetTransactionTypeQuery,
  useGetTransferAssetsByIdQuery
};
