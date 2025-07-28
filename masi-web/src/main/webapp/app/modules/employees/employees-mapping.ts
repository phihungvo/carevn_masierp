import { Color } from 'app/shared/model/enumerations/color.model';
import { EMPLOYEE_STATUS, PROFILE_STATES } from 'app/shared/model/enumerations/employee.model';
import { GENDER } from 'app/shared/model/enumerations/recruitment.model';
import contractsMapping from '../contracts/contracts-mapping';
import { CONTRACT_TYPE } from 'app/shared/model/enumerations/contract.model';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { HISTORY_KEYS, IListDepartment } from 'app/shared/model/employee.model';

const { contractTypeTextMapping } = contractsMapping;

const profileStatesMapping = (text: PROFILE_STATES): string => {
  switch (text) {
    case PROFILE_STATES.ACTIVE:
      return 'Hiệu lực';
    case PROFILE_STATES.INACTIVE:
      return 'Vô hiệu';
    default:
      return '';
  }
};

const activeTextMapping = (isActive: boolean): string => {
  if (isActive) return 'Hiệu lực';
  return 'Vô hiệu';
};

const activeColorMapping = (isActive: boolean): Color => {
  if (isActive) return Color.PRIMARY;
  return Color.ERROR;
};

const employeeProfileStatusMapping = (text: EMPLOYEE_STATUS): string => {
  switch (text) {
    case EMPLOYEE_STATUS.WORKING:
      return 'Đang làm';
    case EMPLOYEE_STATUS.RESIGNED:
      return 'Đã nghỉ';
    default:
      return '';
  }
};

const employeeProfileStatusColorMapping = (text: EMPLOYEE_STATUS): Color => {
  switch (text) {
    case EMPLOYEE_STATUS.WORKING:
      return Color.PRIMARY;
    default:
      return undefined;
  }
};

const genderTextMapping = (text: GENDER): string => {
  switch (text) {
    case GENDER.FEMALE:
      return 'Nữ';
    case GENDER.MALE:
      return 'Nam';
    case GENDER.NO_REFERENCE:
      return 'Không xác định';
  }
};

const historyFieldNameMapping = (fieldName: HISTORY_KEYS): string => {
  switch (fieldName) {
    case 'probationDateFrom':
      return 'Ngày bắt đầu thử việc';
    case 'probationDateTo':
      return 'Ngày kết thúc thử việc';
    case 'officialWorkTypeDuration':
      return 'Thâm niên';
    case 'insurancePaymentLevel':
      return 'Mức đóng bảo hiểm';
    case 'contractType':
      return 'Loại hợp đồng';
  }
};

const historyFieldValuesMapping = (fieldName: HISTORY_KEYS, value: string): string => {
  switch (fieldName) {
    case 'contractType':
      return value && value !== 'null' ? contractTypeTextMapping(value as CONTRACT_TYPE) : 'Trống';
    case 'probationDateFrom':
    case 'probationDateTo':
      return value && value !== 'null' ? dayjs(value).format(DATE_FORMAT.DATE) : 'Trống';
    case 'officialWorkTypeDuration':
      return value && value !== 'null' ? value : 'Trống';
    case 'insurancePaymentLevel':
      return value && value !== 'null' ? value : 'Trống';
    default:
      return value;
  }
};

const employeeDepartmentMapping = (department: keyof IListDepartment): string => {
  switch (department) {
    case 'employeesHCNS':
      return 'BP kế toán';
    case 'employeesSALE':
      return 'BP sale';
    case 'employeesWORKER':
      return 'BP sản xuất';
    case 'employeesLOGPUR':
      return 'BP LOG&PUR';
    default:
      return '';
  }
};

export default {
  profileStatesMapping,
  employeeProfileStatusMapping,
  employeeProfileStatusColorMapping,
  activeTextMapping,
  genderTextMapping,
  historyFieldNameMapping,
  historyFieldValuesMapping,
  employeeDepartmentMapping,
};
