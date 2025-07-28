import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import leaveRegimeService from 'app/services/leave-regime.service';
import { IBodyFile } from 'app/shared/model/file.model';
import { ILeaveRegime, ILeaveRegimeParams, ILeaveRegimeReview, IProcessLeaveRegimeRequest } from 'app/shared/model/leave-regime.model';

const { LEAVE_REGIMES, LEAVE_REGIME, ANNUAL_LEAVES_EMPLOYEE } = QUERY_KEY;
const { CREATE_LEAVE_REGIME, UPDATE_LEAVE_REGIME, DELETE_LEAVE_REGIME, CANCEL_LEAVE_REGIME, PROCESS_LEAVE_REGIME, REVIEW_LEAVE_REGIME } =
  MUTATION_KEY;

const useGetLeaveRegimes = (filter: ILeaveRegimeParams) => {
  return useQuery({
    queryKey: [
      LEAVE_REGIMES,
      filter?.page,
      filter?.size,
      filter?.leaveType,
      filter?.startDate,
      filter?.endDate,
      filter?.workspaceId,
      filter?.status,
    ],
    queryFn: () => leaveRegimeService.getLeaveRegimes(filter),
    select: data => data.data,
  });
};

const useGetLeaveRegimeById = (id: string) => {
  return useQuery({
    queryKey: [LEAVE_REGIME, id],
    queryFn: () => leaveRegimeService.getLeaveRegimeById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostLeaveRegime = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_LEAVE_REGIME],
    mutationFn: leaveRegimeService.postLeaveRegime,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchLeaveRegime = (id: string, toggle: () => void, toggleSuccess: () => void, setFiles: (item: IBodyFile[]) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_LEAVE_REGIME, id],
    mutationFn: (data: ILeaveRegime) => leaveRegimeService.patchLeaveRegime(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIME] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      toggle();
      toggleSuccess();
      setFiles(null);
    },
  });
};

const useDeleteLeaveRegime = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_LEAVE_REGIME],
    mutationFn: (id: string) => leaveRegimeService.deleteLeaveRegime(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      toggle();
      toggleSuccess();
    },
  });
};

const useCancelLeaveRegime = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_LEAVE_REGIME],
    mutationFn: (id: string) => leaveRegimeService.cancelLeaveRegime(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      toggle();
      toggleSuccess();
    },
  });
};

const useProcessLeaveRegime = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [PROCESS_LEAVE_REGIME, id],
    mutationFn: (data: IProcessLeaveRegimeRequest) => leaveRegimeService.processLeaveRegime(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIME, id] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      onOk && onOk();
    },
  });
};

const useReviewLeaveRegime = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_LEAVE_REGIME, id],
    mutationFn: (data: ILeaveRegimeReview) => leaveRegimeService.reviewLeaveRegime(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REGIMES] });
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES_EMPLOYEE] });
      onOk && onOk();
    },
  });
};

export default {
  useGetLeaveRegimes,
  usePostLeaveRegime,
  usePatchLeaveRegime,
  useDeleteLeaveRegime,
  useGetLeaveRegimeById,
  useCancelLeaveRegime,
  useProcessLeaveRegime,
  useReviewLeaveRegime,
};
