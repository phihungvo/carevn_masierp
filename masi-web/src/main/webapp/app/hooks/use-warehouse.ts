import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import warehouseService from 'app/services/warehouse.service';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IWarehouse, IWarehouseParams } from 'app/shared/model/warehouse.model';
import { AxiosResponse } from 'axios';

const { WAREHOUSES, WAREHOUSE, WAREHOUSE_ROUTING } = QUERY_KEY;
const { CREATE_WAREHOUSE, UPDATE_WAREHOUSE, DELETE_WAREHOUSE } = MUTATION_KEY;

const useGetWarehouses = (
  filter?: IWarehouseParams,
  select?: (data: AxiosResponse<PaginationResponse<IWarehouse>, any>) => PaginationResponse<IWarehouse>
) => {
  return useQuery({
    queryKey: [WAREHOUSES, filter],
    queryFn: () => warehouseService.getWarehouses(filter),
    select: res => select ? select(res) : res?.data,
  });
};

const useGetWarehouseById = (id: string) => {
  return useQuery({
    queryKey: [WAREHOUSE, id],
    queryFn: () => warehouseService.getWarehouseById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useGetWarehouseRouting = (filter?: IWarehouseParams, productMaintainId?: string) => {
  return useQuery({
    queryKey: [WAREHOUSE_ROUTING, filter?.orderId],
    queryFn: () => warehouseService.getWarehouseRouting(filter),
    select: data => data,
    enabled: !!productMaintainId,
  });
};

const useCreateWarehouse = (onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_WAREHOUSE],
    mutationFn: warehouseService.createWarehouse,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSES],
      });
      onOk && onOk();
    },
  });
};

const useUpdateWarehouse = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_WAREHOUSE],
    mutationFn: (data: IWarehouse) => warehouseService.updateWarehouse(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSES],
      });
      onOk && onOk();
    },
  });
};

const useDeleteWarehouse = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_WAREHOUSE, id],
    mutationFn: () => warehouseService.deleteWarehouse(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [WAREHOUSES],
      });
      onOk && onOk();
    },
  });
};

export default { useGetWarehouses, useGetWarehouseById, useCreateWarehouse, useUpdateWarehouse, useDeleteWarehouse, useGetWarehouseRouting };
