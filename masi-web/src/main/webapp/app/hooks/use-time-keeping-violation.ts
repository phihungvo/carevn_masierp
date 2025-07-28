import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import timeKeepingViolationService from 'app/services/time-keeping-violation.service';
import { ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';

const { TIME_KEEPING_VIOLATION } = QUERY_KEY;

const useGetTimeKeepingViolationsQuery = (id: string, filter: ITimeKeepingViolationsParams) => {
  return useQuery({
    queryKey: [
      TIME_KEEPING_VIOLATION,
      filter?.page,
      filter?.size,
      filter?.fromDate,
      filter?.toDate,
      filter?.type,
      filter?.employeeIds,
      filter?.workspaceIds,
      filter?.explained
    ],
    queryFn: () => timeKeepingViolationService.getTimeKeepingViolations(filter),
    enabled: !id,
    select: data => data.data,
  });
};

const useGetTimeKeepingViolationsByExplanationQuery = (filter: ITimeKeepingViolationsParams) => {
  return useQuery({
    queryKey: [TIME_KEEPING_VIOLATION, ...Object.values(filter || {})],
    queryFn: () => timeKeepingViolationService.getTimeKeepingViolations(filter),
    enabled: !!filter?.explanationId,
    select: data => data.data,
  });
};

export default {
  useGetTimeKeepingViolationsQuery,
  useGetTimeKeepingViolationsByExplanationQuery,
};
