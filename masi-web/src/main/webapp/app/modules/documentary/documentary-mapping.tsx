import { Color } from 'app/shared/model/enumerations/color.model';
import { DOCUMENTARY_GROUP, DOCUMENTARY_STATUS, DOCUMENTARY_TYPE } from 'app/shared/model/enumerations/documentary';

const documentaryGroupMapping = (text: DOCUMENTARY_GROUP): string => {
  switch (text) {
    case DOCUMENTARY_GROUP.INCOMING:
      return 'Công văn vào';
    case DOCUMENTARY_GROUP.OUTGOING:
      return 'Công văn đi';
    case DOCUMENTARY_GROUP.INTERNAL:
      return 'Nội bộ';
    default:
      return '';
  }
};

const documentaryTypeMapping = (text: DOCUMENTARY_TYPE): string => {
  switch (text) {
    case DOCUMENTARY_TYPE.ANNOUNCEMENT:
      return 'Thông báo';
    case DOCUMENTARY_TYPE.DOCUMENTARY:
      return 'Công văn';
    case DOCUMENTARY_TYPE.RESPONSE:
      return 'Phúc đáp';
    default:
      return '';
  }
};

const documentaryStatusTextMapping = (text: DOCUMENTARY_STATUS): string => {
  switch (text) {
    case DOCUMENTARY_STATUS.NEW:
      return 'Mới';
    case DOCUMENTARY_STATUS.PENDING_APPROVAL:
      return 'Đợi duyệt';
    case DOCUMENTARY_STATUS.APPROVED:
      return 'Đã duyệt';
    case DOCUMENTARY_STATUS.REQUEST_EDIT:
      return 'Yêu cầu chỉnh sửa';
    default:
      return '';
  }
};

const documentaryStatusColorMapping = (text: DOCUMENTARY_STATUS): Color => {
  switch (text) {
    case DOCUMENTARY_STATUS.NEW:
      return Color.PRIMARY;
    case DOCUMENTARY_STATUS.PENDING_APPROVAL:
      return Color.WARNING;
    case DOCUMENTARY_STATUS.APPROVED:
      return Color.SUCCESS;
    case DOCUMENTARY_STATUS.REQUEST_EDIT:
      return Color.ERROR;
    default:
      return undefined;
  }
};

export default {
  documentaryGroupMapping,
  documentaryTypeMapping,
  documentaryStatusTextMapping,
  documentaryStatusColorMapping,
};
