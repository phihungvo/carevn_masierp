import { MapKeySelect } from 'app/shared/model/arr-obj.model';

export const statusData: MapKeySelect['data'] = {
  arr: [
    {
      label: 'Mất',
      value: 'LOST',
    },
    {
      label: 'Huỷ',
      value: 'CANCELED',
    },
    {
      label: 'Bể',
      value: 'BROKEN',
    },
    {
      label: 'Bán',
      value: 'SOLD',
    },
    {
      label: 'Góp vốn',
      value: 'CONTRIBUTED',
    },
    {
      label: 'Hết hạn',
      value: 'EXPIRED',
    },
    {
      label: 'Giảm chuyển kho',
      value: 'TRANSFERRED',
    },
  ],
  obj: {
    LOST: {
      label: 'Mất',
      value: 'LOST',
    },
    CANCELED: {
      label: 'Huỷ',
      value: 'CANCELED',
    },
    BROKEN: {
      label: 'Bể',
      value: 'BROKEN',
    },
    SOLD: {
      label: 'Bán',
      value: 'SOLD',
    },
    CONTRIBUTED: {
      label: 'Góp vốn',
      value: 'CONTRIBUTED',
    },
    EXPIRED: {
      label: 'Hết hạn',
      value: 'EXPIRED',
    },
    TRANSFERRED: {
      label: 'Giảm chuyển kho',
      value: 'TRANSFERRED',
    },
  },
};

export enum LiquidationStatus {
  NEW = 'NEW',
  WAITING_APPROVE = 'WAITING_APPROVED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export const liquidationStatus = {
  NEW: 'Mới',
  WAITING_APPROVE: 'Đợi duyệt',
  APPROVED: 'Đã duyệt',
  REJECTED: 'Từ chối',
  IN_PROGRESS: 'Đang tiến hành',
  COMPLETED: 'Hoàn thành',
  CANCELLED: 'Hủy',
};

export const liquidationStatusArr = [
  {
    label: liquidationStatus.NEW,
    value: LiquidationStatus.NEW,
  },
  {
    label: liquidationStatus.WAITING_APPROVE,
    value: LiquidationStatus.WAITING_APPROVE,
  },
  {
    label: liquidationStatus.APPROVED,
    value: LiquidationStatus.APPROVED,
  },
  {
    label: liquidationStatus.REJECTED,
    value: LiquidationStatus.REJECTED,
  },
  {
    label: liquidationStatus.IN_PROGRESS,
    value: LiquidationStatus.IN_PROGRESS,
  },
  {
    label: liquidationStatus.COMPLETED,
    value: LiquidationStatus.COMPLETED,
  },
  {
    label: liquidationStatus.CANCELLED,
    value: LiquidationStatus.CANCELLED,
  },
];

export const liquidationStatusObj = {
  [LiquidationStatus.NEW]: {
    label: liquidationStatus.NEW,
    value: LiquidationStatus.NEW,
  },
  [LiquidationStatus.WAITING_APPROVE]: {
    label: liquidationStatus.WAITING_APPROVE,
    value: LiquidationStatus.WAITING_APPROVE,
  },
  [LiquidationStatus.APPROVED]: {
    label: liquidationStatus.APPROVED,
    value: LiquidationStatus.APPROVED,
  },
  [LiquidationStatus.REJECTED]: {
    label: liquidationStatus.REJECTED,
    value: LiquidationStatus.REJECTED,
  },
  [LiquidationStatus.IN_PROGRESS]: {
    label: liquidationStatus.IN_PROGRESS,
    value: LiquidationStatus.IN_PROGRESS,
  },
  [LiquidationStatus.COMPLETED]: {
    label: liquidationStatus.COMPLETED,
    value: LiquidationStatus.COMPLETED,
  },
  [LiquidationStatus.CANCELLED]: {
    label: liquidationStatus.CANCELLED,
    value: LiquidationStatus.CANCELLED,
  },
};
