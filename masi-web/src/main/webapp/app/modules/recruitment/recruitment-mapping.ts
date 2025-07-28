import { Color } from 'app/shared/model/enumerations/color.model';
import {
  INTERVIEW_MODE,
  INTERVIEW_RESULT,
  RECRUITMENT_CONTRACT_TYPE,
  RECRUITMENT_POSITION,
  RECRUITMENT_PROCESS,
  RECRUITMENT_STATUS,
} from 'app/shared/model/enumerations/recruitment.model';

const recruitmentStatusColorMapping = (text: RECRUITMENT_STATUS): Color => {
  switch (text) {
    case RECRUITMENT_STATUS.APPROVED:
      return Color.PRIMARY;
    case RECRUITMENT_STATUS.REJECTED:
      return Color.ERROR;
    case RECRUITMENT_STATUS.WAITING_APPROVAL:
      return Color.WARNING;
    case RECRUITMENT_STATUS.WAITING_RENEW:
      return Color.WARNING;
    case RECRUITMENT_STATUS.COMPLETED:
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

const recruitmentStatusTextMapping = (text: RECRUITMENT_STATUS): string => {
  switch (text) {
    case RECRUITMENT_STATUS.APPROVED:
      return 'Đã duyệt';
    case RECRUITMENT_STATUS.REJECTED:
      return 'Từ chối';
    case RECRUITMENT_STATUS.WAITING_APPROVAL:
      return 'Đợi duyệt';
    case RECRUITMENT_STATUS.WAITING_INTERVIEW:
      return 'Chờ phỏng vấn';
    case RECRUITMENT_STATUS.WAITING_RENEW:
      return 'Đợi duyệt GH';
    case RECRUITMENT_STATUS.COMPLETED:
      return 'Hoàn thành';
    default:
      return '';
  }
};

const recruitmentProcessTextMapping = (text: RECRUITMENT_PROCESS): string => {
  switch (text) {
    case RECRUITMENT_PROCESS.INTERVIEWED:
      return 'Đã phỏng vấn';
    case RECRUITMENT_PROCESS.WAITING_INTERVIEW:
      return 'Đợi phỏng vấn';
    default:
      return '';
  }
};

const recruitmentProcessColorMapping = (text: RECRUITMENT_PROCESS): Color => {
  switch (text) {
    case RECRUITMENT_PROCESS.INTERVIEWED:
      return Color.SUCCESS;
    case RECRUITMENT_PROCESS.WAITING_INTERVIEW:
      return Color.PRIMARY;
    default:
      return undefined;
  }
};

const interviewResultTextMapping = (text: INTERVIEW_RESULT): string => {
  switch (text) {
    case INTERVIEW_RESULT.PASS:
      return 'Đạt';
    case INTERVIEW_RESULT.FAIL:
      return 'Không đạt';
    case INTERVIEW_RESULT.HOLD:
      return 'Bảo lưu';
    default:
      return '';
  }
};

const recruitmentPositionTextMapping = (text: RECRUITMENT_POSITION): string => {
  switch (text) {
    case RECRUITMENT_POSITION.EMPLOYEE:
      return 'Nhân viên (NV)';
    case RECRUITMENT_POSITION.TEAM_LEADER:
      return 'Trưởng bộ phận (TBP)';
    case RECRUITMENT_POSITION.SUPERVISOR:
      return 'Giám đốc bộ phận (GĐBP)';
    case RECRUITMENT_POSITION.DIRECTOR:
      return 'Giám đốc (GĐ)';
  }
};

const interviewModeTextMapping = (text: INTERVIEW_MODE): string => {
  switch (text) {
    case INTERVIEW_MODE.ONLINE:
      return 'Online';
    case INTERVIEW_MODE.OFFLINE:
      return 'Offline';
    default:
      return '';
  }
};

const interviewModeColorMapping = (text: INTERVIEW_MODE): Color => {
  switch (text) {
    case INTERVIEW_MODE.ONLINE:
      return Color.SUCCESS;
    case INTERVIEW_MODE.OFFLINE:
      return Color.ERROR;
    default:
      return undefined;
  }
};

const recruitmentContractTypeTextMapping = (text: RECRUITMENT_CONTRACT_TYPE): string => {
  switch (text) {
    case RECRUITMENT_CONTRACT_TYPE.TRIAL:
      return 'Thử việc';
    case RECRUITMENT_CONTRACT_TYPE.FIXED_TERM:
      return 'Xác định thời hạn';
    case RECRUITMENT_CONTRACT_TYPE.UNDEFINED_TERM:
      return 'Không xác định thời hạn';
    case RECRUITMENT_CONTRACT_TYPE.SEASONAL:
      return 'Thời vụ';
    default:
      return '';
  }
};

export default {
  recruitmentStatusColorMapping,
  recruitmentStatusTextMapping,
  recruitmentProcessTextMapping,
  recruitmentProcessColorMapping,
  interviewResultTextMapping,
  recruitmentPositionTextMapping,
  interviewModeTextMapping,
  recruitmentContractTypeTextMapping,
  interviewModeColorMapping,
};
