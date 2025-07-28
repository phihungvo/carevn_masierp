import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import callCenterService from 'app/services/call-center.service';
import {
  ICallCenterFilterParams,
  IPatchCallCenterDto,
  IPostCallCenterDto,
} from 'app/shared/model/call-center.model';
import { IApiError } from 'app/shared/model/error.model';
import { AxiosError } from 'axios';

const { CALL_CENTER } = QUERY_KEY;
const { CREATE_CALL_CENTER, UPDATE_CALL_CENTER, DELETE_CALL_CENTER } =
  MUTATION_KEY;

const useGetCallCentersQuery = (filter?: ICallCenterFilterParams) => {
  return useQuery({
    queryKey: [CALL_CENTER, filter],
    queryFn: () => callCenterService.getCallCenters(filter),
    select: data => data.data,
  });
};

const useGetCallCenterByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [CALL_CENTER, id],
    queryFn: () => callCenterService.getCallCenterById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostCallCenterMutation = (toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_CALL_CENTER],
    mutationFn: (data: IPostCallCenterDto) =>
      callCenterService.postCallCenters(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      toggleSuccess();
    },
  });
};

const useUpdateCallCenterMutation = (id: string, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CALL_CENTER, id],
    mutationFn: (data: IPatchCallCenterDto) =>
      callCenterService.patchCallCenters(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      toggleSuccess();
    },
  });
};

const useDeleteCallCenterMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_CALL_CENTER],
    mutationFn: (id: string) => callCenterService.deleteCallCenter(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useApproveCallCenterMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CALL_CENTER],
    mutationFn: (id: string) => callCenterService.approveCallCenter(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useCompleteCallCenterMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CALL_CENTER],
    mutationFn: (id: string) => callCenterService.completeCallCenter(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useCloseCallCenterMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CALL_CENTER],
    mutationFn: (id: string) => callCenterService.closeCallCenter(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CALL_CENTER] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

export default {
  useGetCallCentersQuery,
  useGetCallCenterByIdQuery,
  usePostCallCenterMutation,
  useUpdateCallCenterMutation,
  useDeleteCallCenterMutation,
  useApproveCallCenterMutation,
  useCompleteCallCenterMutation,
  useCloseCallCenterMutation,
};
