import { notificationEndpoints } from 'app/constants/endpoints';
import { INotificationParams, INotificationRecipient } from 'app/shared/model/notification.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getMyNotifications = async (filter?: INotificationParams) => {
  const url = notificationEndpoints.getMyNotifications;

  try {
    const response = await axios.get<PaginationResponse<INotificationRecipient>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCountUnreadNotifications = async () => {
  const url = notificationEndpoints.countUnreadNotifications;

  try {
    const response = await axios.get<{ count: number }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const readNotification = async (id: string) => {
  const url = notificationEndpoints.readNotification(id);

  return await axios.patch(url);
};

export default {
  getMyNotifications,
  getCountUnreadNotifications,
  readNotification,
};
