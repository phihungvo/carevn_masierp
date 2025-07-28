import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import purchaseService from 'app/services/purchase.service';
import { IPurchaseDelivery, IPurchaseParams, PurchaseReview } from 'app/shared/model/purchase.model';
import { useState } from 'react';

const { PURCHASES, PURCHASE, PURCHASE_REVIEW, PURCHASE_REQUEST_FILE } = QUERY_KEY;
const { CREATE_PURCHASE, UPDATE_PURCHASE, DELETE_PURCHASE, UPDATE_PURCHASE_DELIVERY, CREATE_PURCHASE_REVIEW, UPDATE_PURCHASE_REVIEW } =
  MUTATION_KEY;

const useGetPurchases = (filter: IPurchaseParams) => {
  return useQuery({
    queryKey: [PURCHASES, filter?.page, filter?.size, filter?.searchString, filter?.statuses, filter?.createdFrom, filter?.createdTo],
    queryFn: () => purchaseService.getPurchases(filter),
    select: data => data.data,
  });
};

const useGetPurchaseById = (id: string) => {
  return useQuery({
    queryKey: [PURCHASES, id],
    queryFn: () => purchaseService.getPurchaseById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostPurchase = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_PURCHASE],
    mutationFn: purchaseService.postPurchase,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PURCHASES],
      });

      toggle();
      toggleSuccess();
    },
  });
};

const usePatchPurchase = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_PURCHASE, id],
    mutationFn: (data: any) => purchaseService.patchPurchase(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PURCHASES],
      });
      queryClient.invalidateQueries({
        queryKey: [PURCHASE],
      });
      toggle();
      toggleSuccess();
    },
  });
};

const useDeletePurchase = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_PURCHASE],
    mutationFn: (id: string) => purchaseService.deletePurchase(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PURCHASES],
      });
    },
  });
};

const usePatchPurchaseDelivery = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_PURCHASE_DELIVERY, id],
    mutationFn: (data: IPurchaseDelivery) => purchaseService.patchPurchaseDelivery(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [PURCHASES],
      });
      queryClient.invalidateQueries({
        queryKey: [PURCHASE],
      });
      toggle();
      toggleSuccess();
    },
  });
};

const usePostPurchaseReviewMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PURCHASE_REVIEW],
    mutationFn: purchaseService.postPurchaseReview,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PURCHASES] });
      queryClient.invalidateQueries({
        queryKey: [PURCHASE],
      });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchPurchaseReviewMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PURCHASE_REVIEW],
    mutationFn: (data: PurchaseReview) => purchaseService.patchPurchaseReview(data, data.id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PURCHASES] });
      queryClient.invalidateQueries({
        queryKey: [PURCHASE],
      });
      toggle();
      toggleSuccess();
    },
  });
};

const useGetPurchaseReviewByPurchaseRequestId = (id: string) => {
  return useQuery({
    queryKey: [PURCHASE_REVIEW, id],
    queryFn: () => purchaseService.getPurchaseReviewByPurchaseRequestId(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useGetPurchaseRequestFile = (id: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [PURCHASE_REQUEST_FILE, id],
    queryFn: () => purchaseService.getPurchaseRequestFile(id),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [PURCHASE_REQUEST_FILE, id],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

export default {
  useGetPurchases,
  useGetPurchaseById,
  usePostPurchase,
  usePatchPurchase,
  useDeletePurchase,
  usePatchPurchaseDelivery,
  usePostPurchaseReviewMutation,
  usePatchPurchaseReviewMutation,
  useGetPurchaseReviewByPurchaseRequestId,
  useGetPurchaseRequestFile,
};
