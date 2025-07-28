import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import timeKeepingExplanationService from 'app/services/time-keeping-explanation.service';
import {
  IPatchTimeKeepingExplanationDto,
  ITimeKeepingExplanationParams,
  ITimeKeepingExplanationReviewDto,
} from 'app/shared/model/time-keeping-explanation.model';

const { TIME_KEEPING_EXPLANATION, TIME_KEEPING_EXPLANATION_RECORD, TIME_KEEPING_VIOLATION } = QUERY_KEY;
const {
  UPDATE_TIME_KEEPING_EXPLANATION,
  CREATE_TIME_KEEPING_EXPLANATION,
  DELETE_TIME_KEEPING_EXPLANATION,
  CANCEL_TIME_KEEPING_EXPLANATION,
  REVIEW_TIME_KEEPING_EXPLANATION,
} = MUTATION_KEY;

const useGetTimeKeepingExplanationsQuery = (filter: ITimeKeepingExplanationParams) => {
  return useQuery({
    queryKey: [
      TIME_KEEPING_EXPLANATION,
      filter?.page,
      filter?.size,
      filter?.statuses,
      filter?.types,
      filter?.searchString,
      filter?.employeeIds,
      filter?.workspaceIds,
      filter?.startFrom,
      filter?.startTo,
    ],
    queryFn: () => timeKeepingExplanationService.getTimeKeepingExplanations(filter),
    select: data => data?.data,
  });
};

const usePostTimeKeepingExplanationMutation = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_TIME_KEEPING_EXPLANATION],
    mutationFn: timeKeepingExplanationService.postTimeKeepingExplanation,
    onSuccess: () => {
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_EXPLANATION],
      });
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_VIOLATION],
      });
    },
    onSettled: () => {
      toggle && toggle();
    },
  });
};

const usePatchTimeKeepingExplanationMutation = (id: string, toggle?: () => void, toggleError?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_TIME_KEEPING_EXPLANATION, id],
    mutationFn: (data: IPatchTimeKeepingExplanationDto) => timeKeepingExplanationService.patchTimeKeepingExplanation(data, id),
    onSuccess: () => {
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_EXPLANATION],
      });
    },
    onError: () => {
      toggleError && toggleError();
    },
    onSettled: () => {
      toggle && toggle();
    },
  });
};

const useGetTimeKeepingExplanationByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING_EXPLANATION_RECORD, id],
    queryFn: () => timeKeepingExplanationService.getTimeKeepingExplanationById(id),
    enabled: !!id,
  });
};

const useDeleteTimeKeepingExplanationMutation = (toggle?: () => void, toggleError?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_TIME_KEEPING_EXPLANATION],
    mutationFn: (body: { id: string }) => timeKeepingExplanationService.deleteTimeKeepingExplanation(body.id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_EXPLANATION],
      });
    },
    onError: () => {
      toggleError();
    },
  });
};

const usePatchTimeKeepingExplanationReviewMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_TIME_KEEPING_EXPLANATION],
    mutationFn: ({ data, id }: { data: ITimeKeepingExplanationReviewDto; id: string }) =>
      timeKeepingExplanationService.patchTimeKeepingExplanationReview(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_EXPLANATION],
      });
    },
  });
};

const usePatchTimeKeepingExplanationCancel = (toggle?: () => void, toggleError?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_TIME_KEEPING_EXPLANATION],
    mutationFn: (id: string) => timeKeepingExplanationService.patchTimeKeepingExplanationCancel(id),
    onSuccess: () => {
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_EXPLANATION],
      });
    },
    onSettled: () => {
      toggle && toggle();
    },
    onError: () => {
      toggleError && toggleError();
    },
  });
};

export default {
  useGetTimeKeepingExplanationsQuery,
  usePostTimeKeepingExplanationMutation,
  usePatchTimeKeepingExplanationMutation,
  useGetTimeKeepingExplanationByIdQuery,
  useDeleteTimeKeepingExplanationMutation,
  usePatchTimeKeepingExplanationReviewMutation,
  usePatchTimeKeepingExplanationCancel,
};
