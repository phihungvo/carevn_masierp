import { useState } from 'react';

import orderService from 'app/services/order.service';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { IOrder, IOrderParams, OrderReview } from 'app/shared/model/order.model';
import { IProductionOrdersParams } from 'app/shared/model/production-command.model';
import { AxiosResponse } from 'axios';
import { PaginationResponse } from 'app/shared/model/pagination.model';

const { ORDERS, ORDER, ORDER_REVIEW, ORDER_EXPORT, ORDER_WITH_MANUFACTURE } = QUERY_KEY;
const { CREATE_ORDER, UPDATE_ORDER, DELETE_ORDER, CREATE_ORDER_REVIEW, UPDATE_ORDER_REVIEW } = MUTATION_KEY;

const useOrdersQuery = (
  filter?: IOrderParams,
  select?: (data: AxiosResponse<PaginationResponse<IOrder>, any>) => any
) => {
  return useQuery({
    queryKey: [ORDERS, filter],
    queryFn: () => orderService.getOrders(filter),
    select: data => select ? select(data) : data?.data,
    retry: 5,
  });
};

const usePostOrderMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_ORDER],
    mutationFn: orderService.createOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchOrderMutation = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_ORDER, id],
    mutationFn: (data: IOrder) => orderService.updateOrder(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
      queryClient.invalidateQueries({ queryKey: [ORDER] });
      toggle();
      toggleSuccess();
    },
  });
};

const useGetOrderByIdQuery = (id: string, select?: (data: AxiosResponse<IOrder, any>) => IOrder) => {
  return useQuery({
    queryKey: [ORDER, id],
    queryFn: () => orderService.getOrderById(id),
    select: data => select ? select(data) : data?.data,
    enabled: !!id,
  });
};

const useDeleteOrderMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_ORDER],
    mutationFn: (id: string) => orderService.deleteOrder(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
    },
  });
};

const useCancelOrderMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_ORDER],
    mutationFn: (id: string) => orderService.cancelOrder(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
      queryClient.invalidateQueries({ queryKey: [ORDER] });
    },
  });
};

const usePostOrderReviewMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_ORDER_REVIEW],
    mutationFn: orderService.postOrderReview,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
      queryClient.invalidateQueries({ queryKey: [ORDER] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchOrderReviewMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_ORDER_REVIEW],
    mutationFn: (data: OrderReview) => orderService.patchOrderReview(data, data?.id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ORDERS] });
      queryClient.invalidateQueries({ queryKey: [ORDER] });
      toggle();
      toggleSuccess();
    },
  });
};

const useGetOrderReviewByOrderIdQuery = (orderId: string) => {
  return useQuery({
    queryKey: [ORDER_REVIEW, orderId],
    queryFn: () => orderService.getOrderReviewByOrderId(orderId),
    select: data => data?.data,
    enabled: !!orderId,
  });
};

const useGetOrderExport = (id: string, download: boolean, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [ORDER_EXPORT, id],
    queryFn: () => orderService.getOrderExport(id, download),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [ORDER_EXPORT],
    });
  }

  if (query.isSuccess && enabled) {
    toggleSuccess && toggleSuccess();
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useGetOrderWithManufacture = (filter?: IProductionOrdersParams) => {
  return useQuery({
    queryKey: [ORDER_WITH_MANUFACTURE, filter?.page, filter?.size, filter?.searchString, filter?.statuses, filter?.startDate, filter?.endDate],
    queryFn: () => orderService.getOrdersWithManufactureOrders(filter),
    select: data => data?.data,
  });
};

export default {
  useOrdersQuery,
  useGetOrderExport,
  useGetOrderByIdQuery,
  usePostOrderMutation,
  usePatchOrderMutation,
  useDeleteOrderMutation,
  useCancelOrderMutation,
  usePostOrderReviewMutation,
  usePatchOrderReviewMutation,
  useGetOrderReviewByOrderIdQuery,
  useGetOrderWithManufacture,
};
