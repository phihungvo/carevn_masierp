import { Color } from 'app/shared/model/enumerations/color.model';

const reportUniformsExportsTextMapping = (text: string): string => {
  switch (text) {
    case 'STOCKED':
      return 'Nhập kho';
    case 'SALE':
      return 'Xuất bán';
    case 'SENIORITY':
      return 'Xuất thâm niên';
    case 'SUPPORT':
      return 'Xuất ứng';
    case 'OTHER':
      return 'Xuất cấp khác';
    case 'STOCK':
      return 'Tồn kho';
    case 'RELEASE':
      return 'Xuất kho';
    default:
      return '';
  }
};

const reportUniformsExportsTypeColorMapping = (text: string): Color => {
  switch (text) {
    case 'SALE':
    case 'STOCK':
      return Color.SUCCESS;
    case 'SENIORITY':
      return undefined;
    case 'SUPPORT':
      return Color.WARNING;
    case 'OTHER':
      return Color.PRIMARY;
    case 'RELEASE':
      return Color.ERROR;
    default:
      return undefined;
  }
};

export default {
  reportUniformsExportsTextMapping,
  reportUniformsExportsTypeColorMapping,
};
