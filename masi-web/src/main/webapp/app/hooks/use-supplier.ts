import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import supplierService from 'app/services/supplier.service';
import { ISupplierParams } from 'app/shared/model/supplier.model';
import { useState } from 'react';

const { SUPPLIERS, SUPPLIER, SUPPLIERS_TYPE } = QUERY_KEY;
const {
  CREATE_SUPPLIER,
  UPDATE_SUPPLIER,
  DELETE_SUPPLIER,
  DISABLE_SUPPLIER,
  ENABLE_SUPPLIER,
} = MUTATION_KEY;

const useGetSuppliers = (filter?: ISupplierParams) => {
  return useQuery({
    queryKey: [SUPPLIERS, filter],
    queryFn: () => supplierService.getSuppliers(filter),
    select: data => data?.data,
  });
};

const useGetSupplierTypes = (filter?: ISupplierParams) => {
  return useQuery({
    queryKey: [SUPPLIERS_TYPE, filter],
    queryFn: () => supplierService.getSupplierTypes(filter),
    select: data => data?.data,
  });
};

const usePostSupplier = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_SUPPLIER],
    mutationFn: supplierService.createSupplier,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIERS] });
      onOk && onOk();
    },
  });
};

const usePatchSupplier = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_SUPPLIER, id],
    mutationFn: (data: any) => supplierService.updateSupplier(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIERS] });
      queryClient.invalidateQueries({ queryKey: [SUPPLIER] });
      onOk && onOk();
    },
  });
};

const useGetSupplierById = (id: string) => {
  return useQuery({
    queryKey: [SUPPLIER, id],
    queryFn: () => supplierService.getSupplierById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useDeleteSupplier = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_SUPPLIER],
    mutationFn: supplierService.deleteSupplier,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIERS] });
      onOk && onOk();
    },
  });
};

const useDisableSupplier = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DISABLE_SUPPLIER],
    mutationFn: (id: string) => supplierService.disableSupplier(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIERS] });
    },
  });
};

const useEnableSupplier = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [ENABLE_SUPPLIER],
    mutationFn: (id: string) => supplierService.enableSupplier(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIERS] });
    },
  });
};

export const useExportSuppliersXlsxLazyQuery = (filter?: ISupplierParams) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [QUERY_KEY.SUPPLIER_EXPORT, filter],
    queryFn: () => supplierService.exportSupplier(filter),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [QUERY_KEY.SUPPLIER_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

export default {
  useGetSuppliers,
  useGetSupplierTypes,
  usePostSupplier,
  usePatchSupplier,
  useGetSupplierById,
  useDeleteSupplier,
  useDisableSupplier,
  useEnableSupplier,
  useExportSuppliersXlsxLazyQuery,
};
