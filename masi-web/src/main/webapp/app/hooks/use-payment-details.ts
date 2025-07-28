import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import paymentDetailService from 'app/services/payment-details.service'
import { MUTATION_KEY, QUERY_KEY } from "app/constants/query-key";
import { IPatchPaymemtDetailsDto, IPaymemtDetailsParams, IPostPaymemtDetailsDto } from "app/shared/model/payment-details.model";

const { PAYMENT_REQUEST, PAYMENT_REQUEST_DETAIL } = QUERY_KEY;

const {
    PATMENT_REQUEST_CREATE,
    PATMENT_REQUEST_UPDATE,
    PATMENT_REQUEST_DELETE
} = MUTATION_KEY;

const useGetPaymentDetails = (filter?: IPaymemtDetailsParams) => {
    return useQuery({
        queryKey: [
            PAYMENT_REQUEST,
            filter?.page,
            filter?.size,
            filter?.status,
            filter?.type,
            filter?.size,
            filter?.['paymentRequestId.equals']
        ],
        queryFn: () => paymentDetailService.getPaymentDetails(filter),
        select: data => data?.data,
    });
};

const useGetPaymentDetailById = (id: string) => {
    return useQuery({
        queryKey: [PAYMENT_REQUEST_DETAIL, id],
        queryFn: () => paymentDetailService.getPaymentDetailsById(id),
        select: data => data?.data,
        enabled: !!id,
    });
};

const usePostPaymentDetails = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationKey: [PATMENT_REQUEST_CREATE],
        mutationFn: (data: IPostPaymemtDetailsDto[]) => paymentDetailService.postPaymentDetails(data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST] });
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST_DETAIL] });
        },
    });
};

const usePatchPaymentDetails = (id: string) => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationKey: [PATMENT_REQUEST_UPDATE],
        mutationFn: (data: IPatchPaymemtDetailsDto[]) => paymentDetailService.patchPaymentDetails(data, id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST] });
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST_DETAIL] });
        },
    });
};

const useDeletePaymentDetails = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationKey: [PATMENT_REQUEST_DELETE],
        mutationFn: (id: string) => paymentDetailService.deletePaymentDetails(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST] });
            queryClient.invalidateQueries({ queryKey: [PAYMENT_REQUEST_DETAIL] });
        },
    });
};

export default {
    useGetPaymentDetails,
    useGetPaymentDetailById,
    usePostPaymentDetails,
    usePatchPaymentDetails,
    useDeletePaymentDetails
}