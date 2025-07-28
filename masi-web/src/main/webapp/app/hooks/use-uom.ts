import { useMutation, useQuery, useQueryClient, UseQueryResult } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import uomService from 'app/services/uom.service';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IUom, IUomGroup, IUomGroupParams, IUomParams } from 'app/shared/model/uom.model';

const { UOMS, UOM, UOM_GROUPS, UOM_GROUP } = QUERY_KEY;
const { CREATE_UOM, UPDATE_UOM, DELETE_UOM, CREATE_UOM_GROUP, UPDATE_UOM_GROUP, DELETE_UOM_GROUP } = MUTATION_KEY;

const useGetUoms = <T = any>(
  filter?: IUomParams,
  convertedFn?: (data: PaginationResponse<IUom>) => T
) => {
  return useQuery({
    queryKey: [UOMS, filter?.page, filter?.size],
    queryFn: () => uomService.getUoms(filter),
    select: data => {
      if (convertedFn) {
        return convertedFn(data?.data);
      }
      return data?.data
    },
  });
};

const useGetUomById = (id: string) => {
  return useQuery({
    queryKey: [UOM, id],
    queryFn: () => uomService.getUomById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useCreateUom = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UOM],
    mutationFn: uomService.createUom,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOMS],
      });
      onOk && onOk();
    },
  });
};

const useUpdateUom = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_UOM],
    mutationFn: (data: IUom) => uomService.updateUom(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOMS],
      });
      onOk && onOk();
    },
  });
};

const useDeleteUom = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_UOM],
    mutationFn: () => uomService.deleteUom(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOMS],
      });
      onOk && onOk();
    },
  });
};

const useGetUomGroups = (filter?: IUomGroupParams) => {
  return useQuery({
    queryKey: [UOM_GROUPS, filter?.page, filter?.size],
    queryFn: () => uomService.getUomGroups(filter),
    select: data => data?.data,
  });
};

const useGetUomGroupById = (id: string) => {
  return useQuery({
    queryKey: [UOM_GROUP, id],
    queryFn: () => uomService.getUomGroupById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useCreateUomGroup = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_UOM_GROUP],
    mutationFn: uomService.createUomGroup,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOM_GROUPS],
      });
      onOk && onOk();
    },
  });
};

const useUpdateUomGroup = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_UOM_GROUP],
    mutationFn: (data: IUomGroup) => uomService.updateUomGroup(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOM_GROUPS],
      });
      onOk && onOk();
    },
  });
};

const useDeleteUomGroup = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_UOM_GROUP],
    mutationFn: () => uomService.deleteUomGroup(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [UOM_GROUPS],
      });
      onOk && onOk();
    },
  });
};

export default {
  useGetUoms,
  useGetUomById,
  useCreateUom,
  useUpdateUom,
  useDeleteUom,
  useGetUomGroups,
  useGetUomGroupById,
  useCreateUomGroup,
  useUpdateUomGroup,
  useDeleteUomGroup,
};
