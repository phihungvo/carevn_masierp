import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import timeKeepingMonthlyServiceService from 'app/services/time-keeping-monthly-service.service';
import { ITimeKeepingMonthlyParams, ITimeKeepingMonthlyReviewDto } from 'app/shared/model/time-keeping-monthly.model';
import { useState } from 'react';

const { TIME_KEEPING_MONTHLY, TIME_KEEPING_MONTHLY_EXPORT, TIME_KEEPING_MONTHLY_DETAIL } = QUERY_KEY;
const { REVIEW_TIME_KEEPING_MONTHLY, APPROVE_TIME_KEEPING_MONTHLY, REJECT_TIME_KEEPING_MONTHLY } = MUTATION_KEY;

const useGetTimeKeepingMonthlyQuery = (filter: ITimeKeepingMonthlyParams) => {
  return useQuery({
    queryKey: [TIME_KEEPING_MONTHLY, filter?.page, filter?.size, filter?.month, filter?.type, filter?.workspaceType],
    queryFn: () => timeKeepingMonthlyServiceService.getTimeKeepingMonthly(filter),
    select: data => data.data,
  });
};

const useGetTimeKeepingMonthlyDetailQuery = (id: string) => {
  return useQuery({
    queryKey: [TIME_KEEPING_MONTHLY_DETAIL, id],
    queryFn: () => timeKeepingMonthlyServiceService.getTimeKeepingMonthlyById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostTimeKeepingMonthlyReview = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_TIME_KEEPING_MONTHLY],
    mutationFn: (data: ITimeKeepingMonthlyReviewDto) => timeKeepingMonthlyServiceService.postTimeKeepingMonthlyReview(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_MONTHLY],
      });
      toggle && toggle();
    },
  });
};

const useGetTimeKeepingMonthlyExport = (filter: ITimeKeepingMonthlyParams) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [TIME_KEEPING_MONTHLY_EXPORT, filter?.month, filter?.type, filter?.workspaceType],
    queryFn: () => timeKeepingMonthlyServiceService.getTimeKeepingMonthlyExport(filter),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [TIME_KEEPING_MONTHLY_EXPORT, filter?.month, filter?.type, filter?.workspaceType],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const usePostTimeKeepingMonthlyApprove = (onSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [APPROVE_TIME_KEEPING_MONTHLY],
    mutationFn: timeKeepingMonthlyServiceService.postApproveTimeKeepingMonthly,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_MONTHLY],
      });
      onSuccess && onSuccess();
    },
  });
};

const usePostTimeKeepingMonthlyReject = (onSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REJECT_TIME_KEEPING_MONTHLY],
    mutationFn: timeKeepingMonthlyServiceService.postRejectTimeKeepingMonthly,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [TIME_KEEPING_MONTHLY],
      });
      onSuccess && onSuccess();
    },
  });
};

export default {
  useGetTimeKeepingMonthlyQuery,
  useGetTimeKeepingMonthlyDetailQuery,
  usePostTimeKeepingMonthlyReview,
  usePostTimeKeepingMonthlyApprove,
  usePostTimeKeepingMonthlyReject,
  useGetTimeKeepingMonthlyExport,
};
