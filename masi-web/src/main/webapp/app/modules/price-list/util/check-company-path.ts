import { PATH } from 'app/constants/path';
import { COMPANY } from 'app/shared/model/enumerations/company.model';

export const checkCompanyCreatePath = (company: COMPANY): string => {
  switch (company) {
    case COMPANY.KIM_LONG:
      return PATH.PRICE_LIST_KIM_LONG;
    case COMPANY.MMS:
      return PATH.PRICE_LIST_MMS;
    default:
      return '';
  }
};

export const checkCompanyUpdatePath = (company: COMPANY, id: string): string => {
  switch (company) {
    case COMPANY.KIM_LONG:
      return PATH.PRICE_LIST_KIM_LONG_UPDATE.replace(':id', id);
    case COMPANY.MMS:
      return PATH.PRICE_LIST_MMS_UPDATE.replace(':id', id);
    default:
      return '';
  }
};

export const checkCompanyDetailCreatePath = (company: COMPANY, id: string): string => {
  switch (company) {
    case COMPANY.KIM_LONG:
      return PATH.PRICE_LIST_CREATE_KIM_LONG.replace(':id', id);
    case COMPANY.MMS:
      return PATH.PRICE_LIST_CREATE_MMS.replace(':id', id);
    default:
      return '';
  }
};

export const checkCompanyDetailPath = (company: COMPANY, id: string): string => {
  switch (company) {
    case COMPANY.KIM_LONG:
      return PATH.PRICE_LIST_DETAIL_KIM_LONG.replace(':id', id);
    case COMPANY.MMS:
      return PATH.PRICE_LIST_DETAIL_MMS.replace(':id', id);
    default:
      return '';
  }
};
