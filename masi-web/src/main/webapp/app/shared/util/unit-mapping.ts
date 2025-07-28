import { UNIT } from '../model/enumerations/unit.model';

export const unitTextMapping = (text: UNIT): string => {
  switch (text) {
    case UNIT.VND:
      return 'VND';
    case UNIT.USD:
      return 'USD';
    case UNIT.OTHER:
      return 'Khác';
    default:
      return '';
  }
};
