import { useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';

import leaveTrackingService from 'app/services/leave-tracking.service';
import { QUERY_KEY } from 'app/constants/query-key';

const { LEAVE_TRACKING } = QUERY_KEY;

const useGetLeaveTracking = (year: string, workspaceType: string, onSuccess?: () => void) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [LEAVE_TRACKING, year, workspaceType],
    queryFn: () => leaveTrackingService.getLeaveTracking(year, workspaceType),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [LEAVE_TRACKING],
    });
  }

  if (query.isSuccess && enabled) {
    onSuccess && onSuccess();
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

export default {
  useGetLeaveTracking,
};
