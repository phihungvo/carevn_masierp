import { Color } from 'app/shared/model/enumerations/color.model';

const reportUniformsReimbursementTextMapping = (text: string): string => {
  switch (text) {
    case 'RELEASE':
      return 'Xuất ứng';
    case 'RETURN':
      return 'Hoàn ứng';
    default:
      return '';
  }
};

const reportUniformsReimbursementTypeColorMapping = (text: string): Color => {
  switch (text) {
    case 'RELEASE':
      return Color.PRIMARY;
    case 'RETURN':
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

export default {
  reportUniformsReimbursementTextMapping,
  reportUniformsReimbursementTypeColorMapping,
};
