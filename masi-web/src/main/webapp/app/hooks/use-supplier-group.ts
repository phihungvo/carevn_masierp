import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import supplierGroupService from 'app/services/supplier-group.service';
import { ISupplierGroupParams } from 'app/shared/model/supplier-group.model';

const { SUPPLIER_GROUPS, SUPPLIER_GROUP } = QUERY_KEY;
const { CREATE_SUPPLIER_GROUP, UPDATE_SUPPLIER_GROUP, DELETE_SUPPLIER_GROUP } = MUTATION_KEY;

const useGetSupplierGroups = (filter?: ISupplierGroupParams) => {
  return useQuery({
    queryKey: [SUPPLIER_GROUPS, filter?.page, filter?.size],
    queryFn: () => supplierGroupService.getSupplierGroups(filter),
    select: data => data?.data,
  });
};

const usePostSupplierGroup = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_SUPPLIER_GROUP],
    mutationFn: supplierGroupService.createSupplierGroup,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIER_GROUPS] });
      onOk && onOk();
    },
  });
};

const usePatchSupplierGroup = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_SUPPLIER_GROUP, id],
    mutationFn: (data: any) => supplierGroupService.updateSupplierGroup(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIER_GROUPS] });
      queryClient.invalidateQueries({ queryKey: [SUPPLIER_GROUP] });
      onOk && onOk();
    },
  });
};

const useGetSupplierGroupById = (id: string) => {
  return useQuery({
    queryKey: [SUPPLIER_GROUP, id],
    queryFn: () => supplierGroupService.getSupplierGroupById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useDeleteSupplierGroup = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_SUPPLIER_GROUP],
    mutationFn: supplierGroupService.deleteSupplierGroup,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [SUPPLIER_GROUPS] });
      onOk && onOk();
    },
  });
};

export default {
  useGetSupplierGroups,
  usePostSupplierGroup,
  usePatchSupplierGroup,
  useGetSupplierGroupById,
  useDeleteSupplierGroup,
};
