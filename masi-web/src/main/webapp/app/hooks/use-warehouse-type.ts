import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import warehouseTypeService from 'app/services/warehouse-type.service';
import { IWarehouseType } from 'app/shared/model/warehouse-type.model';
import { IWarehouseParams } from 'app/shared/model/warehouse.model';

const { WAREHOUSE_TYPES, WAREHOUSE_TYPE } = QUERY_KEY;
const { CREATE_WAREHOUSE_TYPE, UPDATE_WAREHOUSE_TYPE, DELETE_WAREHOUSE_TYPE } = MUTATION_KEY;

const useGetWarehouseTypes = (filter?: IWarehouseParams) => {
  return useQuery({
    queryKey: [WAREHOUSE_TYPES, filter],
    queryFn: () => warehouseTypeService.getWarehouseTypes(filter),
    select: data => data?.data,
  });
};

const useGetWarehouseTypeById = (id: string) => {
  return useQuery({
    queryKey: [WAREHOUSE_TYPE, id],
    queryFn: () => warehouseTypeService.getWarehouseTypeById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useCreateWarehouseType = (onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_WAREHOUSE_TYPE],
    mutationFn: warehouseTypeService.createWarehouseType,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSE_TYPES],
      });
      onOk && onOk();
    },
  });
};

const useUpdateWarehouseType = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_WAREHOUSE_TYPE],
    mutationFn: (data: IWarehouseType) => warehouseTypeService.updateWarehouseType(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSE_TYPES],
      });
      onOk && onOk();
    },
  });
};

const useDeleteWarehouseType = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_WAREHOUSE_TYPE, id],
    mutationFn: () => warehouseTypeService.deleteWarehouseType(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSE_TYPES],
      });
      onOk && onOk();
    },
  });
};

export default {
  useGetWarehouseTypes,
  useGetWarehouseTypeById,
  useCreateWarehouseType,
  useUpdateWarehouseType,
  useDeleteWarehouseType,
};
