import BadgeV2 from 'app/components/badge/badge-v2';
import {
  CALL_CENTER_GROUP,
  CALL_CENTER_SOURCE,
  CALL_CENTER_STATUS,
  CALL_CENTER_TYPE,
  CALL_CENTER_TYPE_PAGE,
} from 'app/shared/model/enumerations/call-center';

const complainType = {
  [CALL_CENTER_TYPE.COMPLAINT]: 'Khiếu nại',
  [CALL_CENTER_TYPE.REQUEST]: 'Yêu cầu hổ trợ',
  [CALL_CENTER_TYPE.OTHER]: 'Khác',
};

const complainGroup = {
  [CALL_CENTER_GROUP.EMPLOYEE]: 'Nhân viên',
  [CALL_CENTER_GROUP.CUSTOMER]: 'Khách hàng',
  [CALL_CENTER_GROUP.SUPPLIER]: 'NCC',
};

const complainStatus = {
  [CALL_CENTER_STATUS.NEW]: 'Tiếp nhận',
  [CALL_CENTER_STATUS.PROCESSING]: 'Xử lý',
  [CALL_CENTER_STATUS.CLOSED]: 'Đóng case',
};

const complainSource = {
  [CALL_CENTER_SOURCE.EMAIL]: 'Mail',
  [CALL_CENTER_SOURCE.CALL]: 'SĐT',
  [CALL_CENTER_SOURCE.OTHER]: 'Khác',
};

export const complainMappingType = (text: CALL_CENTER_TYPE): string => {
  return complainType[text] || '';
};

export const complainMappingGroup = (text: CALL_CENTER_GROUP): string => {
  return complainGroup[text] || '';
};

export const complainMappingSource = (text: CALL_CENTER_SOURCE): string => {
  return complainSource[text] || '';
};

export const complainMappingStatusText = (text: CALL_CENTER_STATUS): string => {
  return complainStatus[text] || '';
};

export const complainMappingStatusOptions = [
  { value: CALL_CENTER_STATUS.NEW, label: 'Tiếp nhận' },
  { value: CALL_CENTER_STATUS.PROCESSING, label: 'Xử lý' },
  { value: CALL_CENTER_STATUS.CLOSED, label: 'Đóng case' },
];

export const complainMappingSourceOptions = [
  { value: CALL_CENTER_SOURCE.EMAIL, label: 'Email' },
  { value: CALL_CENTER_SOURCE.CALL, label: 'SĐT' },
  { value: CALL_CENTER_SOURCE.OTHER, label: 'Khác' },
];

export const complainMappingGroupOptions = [
  { value: CALL_CENTER_GROUP.EMPLOYEE, label: 'Nhân viên' },
  { value: CALL_CENTER_GROUP.CUSTOMER, label: 'Khách hàng' },
  { value: CALL_CENTER_GROUP.SUPPLIER, label: 'NCC' },
];

export const complainMappingTypeOptions = [
  { value: CALL_CENTER_TYPE.COMPLAINT, label: 'Khiếu nại' },
  { value: CALL_CENTER_TYPE.REQUEST, label: 'Yêu cầu hổ trợ' },
  { value: CALL_CENTER_TYPE.OTHER, label: 'Khác' },
];

export const complainMappingTypePageOptions = [
  { value: CALL_CENTER_TYPE_PAGE.CALL_CENTER, label: 'Call Center' },
  { value: CALL_CENTER_TYPE_PAGE.COMPLAINT, label: 'Complain' },
];

export const complainStatusBadge = (key: string) => {
  const label = complainMappingStatusText(key as CALL_CENTER_STATUS);
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
