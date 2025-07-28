export enum SuppliesRequestStatus {
  NEW = 'NEW',
  WAITING_APPROVE = 'WAITING_APPROVE',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export const suppliesStatus = {
  NEW: 'Mới',
  WAITING_APPROVE: 'Đợi duyệt',
  APPROVED: 'Đã duyệt',
  REJECTED: 'Từ chối',
  IN_PROGRESS: 'Đang tiến hành',
  COMPLETED: 'Hoàn thành',
  CANCELLED: 'Hủy',
};

export const suppliesStatusArr = [
  {
    label: suppliesStatus.NEW,
    value: SuppliesRequestStatus.NEW,
  },
  {
    label: suppliesStatus.WAITING_APPROVE,
    value: SuppliesRequestStatus.WAITING_APPROVE,
  },
  {
    label: suppliesStatus.APPROVED,
    value: SuppliesRequestStatus.APPROVED,
  },
  {
    label: suppliesStatus.REJECTED,
    value: SuppliesRequestStatus.REJECTED,
  },
  {
    label: suppliesStatus.IN_PROGRESS,
    value: SuppliesRequestStatus.IN_PROGRESS,
  },
  {
    label: suppliesStatus.COMPLETED,
    value: SuppliesRequestStatus.COMPLETED,
  },
  {
    label: suppliesStatus.CANCELLED,
    value: SuppliesRequestStatus.CANCELLED,
  },
]

export const suppliesStatusObj = {
  [SuppliesRequestStatus.NEW]: {
    label: suppliesStatus.NEW,
    value: SuppliesRequestStatus.NEW,
  },
  [SuppliesRequestStatus.WAITING_APPROVE]: {
    label: suppliesStatus.WAITING_APPROVE,
    value: SuppliesRequestStatus.WAITING_APPROVE,
  },
  [SuppliesRequestStatus.APPROVED]: {
    label: suppliesStatus.APPROVED,
    value: SuppliesRequestStatus.APPROVED,
  },
  [SuppliesRequestStatus.REJECTED]: {
    label: suppliesStatus.REJECTED,
    value: SuppliesRequestStatus.REJECTED,
  },
  [SuppliesRequestStatus.IN_PROGRESS]: {
    label: suppliesStatus.IN_PROGRESS,
    value: SuppliesRequestStatus.IN_PROGRESS,
  },
  [SuppliesRequestStatus.COMPLETED]: {
    label: suppliesStatus.COMPLETED,
    value: SuppliesRequestStatus.COMPLETED,
  },
  [SuppliesRequestStatus.CANCELLED]: {
    label: suppliesStatus.CANCELLED,
    value: SuppliesRequestStatus.CANCELLED,
  }
}
