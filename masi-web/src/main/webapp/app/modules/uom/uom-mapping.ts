import { COMPANY } from 'app/shared/model/enumerations/company.model';

const companyTextMapping = (company: COMPANY): string => {
  switch (company) {
    case COMPANY.KIM_LONG:
      return 'Kim Long';
    case COMPANY.MASI:
      return 'Masi';
    case COMPANY.MMS:
      return 'MMS';
    default:
      return '';
  }
};

export default {
  companyTextMapping,
};
