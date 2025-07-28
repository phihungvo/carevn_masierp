import BadgeV2 from 'app/components/badge/badge-v2';
import {
  CALL_CENTER_GROUP,
  CALL_CENTER_SOURCE,
  CALL_CENTER_STATUS,
  CALL_CENTER_TYPE,
  CALL_CENTER_TYPE_PAGE,
} from 'app/shared/model/enumerations/call-center';

const calLCenterType = {
  [CALL_CENTER_TYPE.COMPLAINT]: 'Khiếu nại',
  [CALL_CENTER_TYPE.REQUEST]: 'Yêu cầu hổ trợ',
  [CALL_CENTER_TYPE.OTHER]: 'Khác',
};

const callCenterGroup = {
  [CALL_CENTER_GROUP.EMPLOYEE]: 'Nhân viên',
  [CALL_CENTER_GROUP.CUSTOMER]: 'Khách hàng',
  [CALL_CENTER_GROUP.SUPPLIER]: 'Khác',
};

const callCenterStatus = {
  [CALL_CENTER_STATUS.NEW]: 'Tiếp nhận',
  [CALL_CENTER_STATUS.PROCESSING]: 'Xử lý',
  [CALL_CENTER_STATUS.CLOSED]: 'Đóng case',
};

export const callCenterMappingType = (text: CALL_CENTER_TYPE): string => {
  return calLCenterType[text] || '';
};

export const callCenterMappingGroup = (text: CALL_CENTER_GROUP): string => {
  return callCenterGroup[text] || '';
};

export const callCenterMappingStatusText = (
  text: CALL_CENTER_STATUS,
): string => {
  return callCenterStatus[text] || '';
};

export const callCenterMappingStatusOptions = [
  { value: CALL_CENTER_STATUS.NEW, label: 'Tiếp nhận' },
  { value: CALL_CENTER_STATUS.PROCESSING, label: 'Xử lý' },
  { value: CALL_CENTER_STATUS.CLOSED, label: 'Đóng case' },
];

export const callCenterMappingSourceOptions = [
  { value: CALL_CENTER_SOURCE.EMAIL, label: 'Email' },
  { value: CALL_CENTER_SOURCE.OTHER, label: 'Khác' },
];

export const calLCenterMappingGroupOptions = [
  { value: CALL_CENTER_GROUP.EMPLOYEE, label: 'Nhân viên' },
  { value: CALL_CENTER_GROUP.CUSTOMER, label: 'Khách hàng' },
  { value: CALL_CENTER_GROUP.SUPPLIER, label: 'Khác' },
];

export const callCenterMappingTypeOptions = [
  { value: CALL_CENTER_TYPE.COMPLAINT, label: 'Khiếu nại' },
  { value: CALL_CENTER_TYPE.REQUEST, label: 'Yêu cầu hổ trợ' },
  { value: CALL_CENTER_TYPE.OTHER, label: 'Khác' },
];

export const callCenterMappingTypePageOptions = [
  { value: CALL_CENTER_TYPE_PAGE.CALL_CENTER, label: 'Call Center' },
  { value: CALL_CENTER_TYPE_PAGE.COMPLAINT, label: 'Complain' },
];

export const callCenterStatusBadge = (key: string) => {
  const label = callCenterMappingStatusText(key as CALL_CENTER_STATUS);
  switch (key) {
    case 'NEW':
      return <BadgeV2 className="bv2 pr-waiting_approve">{label}</BadgeV2>;
    case 'PROCESSING':
      return <BadgeV2 className="bv2 pr-processing">{label}</BadgeV2>;
    case 'CLOSED':
      return <BadgeV2 className="bv2 pr-closed">{label}</BadgeV2>;
    case 'COMPLETED':
      return <BadgeV2 className="bv2 pr-approved">{label}</BadgeV2>;
    default:
      break;
  }
};
