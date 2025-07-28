import { PaginationParams } from './pagination.model';
import { IQuotation } from './quotation.model';
import { IRecruitment } from './recruitment.model';

export type NotificationAction = 'BirthdayNotification' | 'RecruitmentRequestUpdated' | 'INTERNAL_APPROVED_QUOTATION' | 'ChangeCustomerOwner';
export type INotificationEntity = IRecruitment | IQuotation;
export interface INotification {
  id?: number;
  title?: string;
  content?: string;
  entityName?: string;
  entityId?: string;
  entityType?: string;
  data?: INotificationData;
  category?: string;
  sentBy?: string;
}

export interface INotificationRecipient {
  id: string;
  notificationId: string;
  recipientId: string;
  read: boolean;
  readAt: string;
  notification: INotification;
}

export interface INotificationParams extends PaginationParams { }

export interface INotificationData {
  action: NotificationAction;
  fromDate: string;
  toDate: string;
  entity: INotificationEntity;
}

export interface IData {
  action?: NotificationAction;
  customerId: string;
  entity?: any;
  fromDate?: string;
  toDate?: string;
}