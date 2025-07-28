import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import customerService from 'app/services/customer.service';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { AxiosResponse } from 'axios';
import { useState } from 'react';

const {
  CUSTOMERS,
  CUSTOMER,
  ENABLED_CUSTOMERS,
  DISABLED_CUSTOMERS,
  CUSTOMER_DISABLED_EXPORT,
  CUSTOMER_ENABLED_EXPORT,
  NEXT_CUSTOMER_CODE,
  CUSTOMER_BIRTHDAY,
} = QUERY_KEY;
const { CREATE_CUSTOMER, UPDATE_CUSTOMER, DISABLE_CUSTOMER, ACTIVATE_CUSTOMER, TRANSFER_CUSTOMER, DELETE_CUSTOMER } = MUTATION_KEY;

const useGetEnabledCustomers = (
  filter?: ICustomerParams,
  select?: (data: AxiosResponse<PaginationResponse<ICustomer>, any>) => any
) => {
  return useQuery({
    queryKey: [
      ENABLED_CUSTOMERS,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.customerStatus,
      filter?.contractFrom,
      filter?.contractTo,
      filter?.listEmployeeOwner,
      filter?.birthdayFrom,
      filter?.birthdayTo,
      filter?.sort,
      filter?.customerId
    ],
    queryFn: () => customerService.getEnabledCustomers(filter),
    select: data => select ? select(data) : data?.data
  });
};

const useGetDisabledCustomers = (filter: ICustomerParams) => {
  return useQuery({
    queryKey: [
      DISABLED_CUSTOMERS,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.customerStatus,
      filter?.contractFrom,
      filter?.contractTo,
      filter?.listEmployeeOwner,
    ],
    queryFn: () => customerService.getDisabledCustomers(filter),
    select: data => data.data,
  });
};

const useGetNextCustomerCode = () => {
  return useQuery({
    queryKey: [NEXT_CUSTOMER_CODE],
    queryFn: () => customerService.getNextCustomerCode(),
    select: data => data.data,
  });
};

const usePostCustomer = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_CUSTOMER],
    mutationFn: customerService.postCustomer,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchCustomer = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_CUSTOMER, id],
    mutationFn: (data: ICustomer) => customerService.patchCustomer(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useGetCustomerById = (id: string, select?: (data: AxiosResponse<ICustomer, any>) => any) => {
  return useQuery({
    queryKey: [CUSTOMERS, id],
    queryFn: () => customerService.getCustomerById(id),
    enabled: !!id,
    select: res => select ? select(res) : res.data,
  });
};

const useDisableCustomerMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_CUSTOMER],
    mutationFn: (id: string) => customerService.disableCustomer(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
    },
  });
};

const useActivateCustomerMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [ACTIVATE_CUSTOMER],
    mutationFn: (id: string) => customerService.activateCustomer(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
    },
  });
};

const useTransferCustomerMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [TRANSFER_CUSTOMER],
    mutationFn: ({ id, newOwnerId }: { id: string; newOwnerId: string }) => customerService.transferCustomer(id, newOwnerId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [CUSTOMERS] });
    },
  });
};

const useDeleteCustomerMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_CUSTOMER],
    mutationFn: (id: string) => customerService.deleteCustomer(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ENABLED_CUSTOMERS] });
      queryClient.invalidateQueries({ queryKey: [DISABLED_CUSTOMERS] });
    },
  });
};

const useGetCustomerByCode = (code: string) => {
  return useQuery({
    queryKey: [CUSTOMER, code],
    queryFn: () => customerService.getCustomerByCode(code),
    enabled: !!code,
  });
};

const useGetEnabledCustomersExportLazyQuery = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [CUSTOMER_ENABLED_EXPORT],
    queryFn: () => customerService.getCustomerEnabledExport(),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [CUSTOMER_ENABLED_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetDisabledCustomersExportLazyQuery = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [CUSTOMER_DISABLED_EXPORT],
    queryFn: () => customerService.getCustomerDisabledExport(),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [CUSTOMER_DISABLED_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetCustomerByTaxCode = (taxCode?: string) => {
  return useQuery({
    queryKey: [CUSTOMER, taxCode],
    queryFn: () => customerService.getCustomerByTaxCode(taxCode),
    enabled: !!taxCode,
  });
};

const useGetCustomerBirthday = (fromDate?: string, toDate?: string) => {
  return useQuery({
    queryKey: [CUSTOMER_BIRTHDAY, fromDate, toDate],
    queryFn: () => customerService.getCustomerBirthday(fromDate, toDate),
    select: data => data.data,
  });
};

export default {
  useGetEnabledCustomers,
  useGetDisabledCustomers,
  usePostCustomer,
  usePatchCustomer,
  useGetCustomerById,
  useDisableCustomerMutation,
  useActivateCustomerMutation,
  useTransferCustomerMutation,
  useDeleteCustomerMutation,
  useGetCustomerByCode,
  useGetEnabledCustomersExportLazyQuery,
  useGetDisabledCustomersExportLazyQuery,
  useGetNextCustomerCode,
  useGetCustomerByTaxCode,
  useGetCustomerBirthday,
};
