import {
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import notificationService from 'app/services/notification.service';
import { INotificationParams } from 'app/shared/model/notification.model';

const { NOTIFICATIONS, COUNT_UNREAD_NOTIFICATIONS } = QUERY_KEY;
const { READ_NOTIFICATION } = MUTATION_KEY;

const useMyNotificationsQuery = (
  filter?: INotificationParams,
  enabled?: boolean,
) => {
  return useQuery({
    queryKey: [NOTIFICATIONS, filter?.page, filter?.size, enabled],
    queryFn: () => notificationService.getMyNotifications(filter),
    select: data => data?.data,
    enabled,
  });
};

const useInfiniteMyNotificationsQuery = (filter?: INotificationParams) => {
  return useInfiniteQuery({
    queryKey: [NOTIFICATIONS],
    queryFn: ({ pageParam }) => {
      return notificationService.getMyNotifications({
        page: pageParam?.skip,
        size: pageParam?.limit,
      });
    },
    initialPageParam: {
      skip: DEFAULT_PAGE,
      limit: DEFAULT_PAGE_SIZE,
    },
    getNextPageParam: lastPage => {
      if (!lastPage) {
        return undefined;
      }

      if (filter?.page >= lastPage.data?.totalRecord) {
        return undefined;
      }

      return {
        skip: filter?.page + 1,
        limit: filter?.size,
      };
    },
    select: data => {
      return {
        data: data?.pages?.flatMap(page => page?.data?.data) ?? [],
        totalRecord: data?.pages?.[0]?.data?.totalRecord ?? 0,
      };
    },
  });
};

const useGetCountUnreadNotifications = () => {
  return useQuery({
    queryKey: [COUNT_UNREAD_NOTIFICATIONS],
    queryFn: notificationService.getCountUnreadNotifications,
    select: data => data?.data?.count,
  });
};

const useReadNotificationMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [READ_NOTIFICATION],
    mutationFn: (id: string) => notificationService.readNotification(id),
    onSuccess: () => {
      // queryClient.invalidateQueries({
      //   queryKey: [NOTIFICATIONS],
      // });
      queryClient.invalidateQueries({
        queryKey: [COUNT_UNREAD_NOTIFICATIONS],
      });
    },
  });
};

export default {
  useMyNotificationsQuery,
  useInfiniteMyNotificationsQuery,
  useGetCountUnreadNotifications,
  useReadNotificationMutation,
};
