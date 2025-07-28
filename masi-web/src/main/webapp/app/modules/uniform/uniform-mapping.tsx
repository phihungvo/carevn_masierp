import { UNIFORM_RELEASE_TYPE } from 'app/shared/model/enumerations/uniform.model';

const uniformReleaseTypeMapping = (text: UNIFORM_RELEASE_TYPE): string => {
  switch (text) {
    case UNIFORM_RELEASE_TYPE.SALE:
      return 'Xuất bán';
    case UNIFORM_RELEASE_TYPE.SENIORITY:
      return 'Xuất thâm niên';
    case UNIFORM_RELEASE_TYPE.SUPPORT:
      return 'Xuất ứng';
    case UNIFORM_RELEASE_TYPE.OTHER:
      return 'Xuất cấp khác';
    default:
      return '';
  }
};

export default {
  uniformReleaseTypeMapping,
};
