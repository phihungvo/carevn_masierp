import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { productionRoutingService } from 'app/services/production-routing.service';
import { IProductionRouting, IProductionRoutingParams } from 'app/shared/model/production-routing.model';

const { PRODUCTION_ROUTINGS } = QUERY_KEY;
const { CREATE_PRODUCTION_ROUTING, UPDATE_PRODUCTION_ROUTING, DELETE_PRODUCTION_ROUTING } = MUTATION_KEY;

// Lấy danh sách bảng định tuyến sản xuất
const useGetProductionRoutingsQuery = (filter: IProductionRoutingParams) => {
  return useQuery({
    queryKey: [PRODUCTION_ROUTINGS, filter?.page, filter?.size, filter?.search, filter?.factoryId, filter?.storageId],
    queryFn: () => productionRoutingService.getProductionRoutings(filter),
    select: data => data.data,
  });
};

// Lấy chi tiết bảng định tuyến sản xuất
const useGetProductionRoutingByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_ROUTINGS, id],
    queryFn: () => productionRoutingService.getProductionRoutingById(id),
    enabled: !!id,
  });
};

// Tạo bảng định tuyến sản xuất
const usePostProductionRoutingMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_ROUTING],
    mutationFn: productionRoutingService.postProductionRouting,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_ROUTINGS] });
      toggle();
      toggleSuccess();
    },
  });
};

// Cập nhật bảng định tuyến sản xuất
const useUpdateProductionRoutingMutation = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_ROUTING, id],
    mutationFn: (data: IProductionRouting) => productionRoutingService.patchProductionRouting(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_ROUTINGS] });
      toggle();
      toggleSuccess();
    },
  });
};

// Xóa bảng định tuyến sản xuất
const useDeleteProductionRoutingMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_ROUTING],
    mutationFn: productionRoutingService.deleteProductionRouting,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_ROUTINGS] });
    },
  });
};

export default {
  useGetProductionRoutingsQuery,
  useGetProductionRoutingByIdQuery,
  usePostProductionRoutingMutation,
  useUpdateProductionRoutingMutation,
  useDeleteProductionRoutingMutation,
};
