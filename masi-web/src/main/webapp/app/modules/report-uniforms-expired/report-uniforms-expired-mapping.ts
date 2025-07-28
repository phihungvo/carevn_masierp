import { Color } from 'app/shared/model/enumerations/color.model';
import {EUniformStatus} from "app/shared/model/enumerations/report-uniforms-expired";
import {UNIFORM_RELEASE_TYPE} from "app/shared/model/enumerations/uniform.model";

const reportUniformsExpiringTypeMapping = (type: string): string => {
  switch (type) {
    case 'SUPPORT':
      return 'Xuất ứng';
    case 'RETURN':
      return 'Hoàn ứng';
    default:
      return '';
  }
};

const reportUniformsExpiringTypeColorMapping = (type: string): Color => {
  switch (type) {
    case 'SUPPORT':
      return Color.PRIMARY;
    case 'RETURN':
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

const formatUniformExpirationStatusMapping = (type: EUniformStatus): string => {
  switch (type) {
    case EUniformStatus.ALLOCATED:
      return 'Đã cấp'
    case EUniformStatus.UNALLOCATED:
      return 'Chưa cấp';
    default:
      return undefined;
  }
};

const reportUniformsExpiringHistoryTypeMapping = (type: UNIFORM_RELEASE_TYPE): string => {
  switch (type) {
    case UNIFORM_RELEASE_TYPE.SUPPORT:
      return 'Xuất ứng';
    case UNIFORM_RELEASE_TYPE.SALE:
      return 'Xuất bán';
    case UNIFORM_RELEASE_TYPE.OTHER:
      return 'Xuất cấp khác';
    case UNIFORM_RELEASE_TYPE.SENIORITY:
      return 'Xuất thâm niên';
    default:
      return '';
  }
};

const reportUniformsExpiringHistoryTypeColorMapping = (type: UNIFORM_RELEASE_TYPE): Color => {
  switch (type) {
    case UNIFORM_RELEASE_TYPE.SUPPORT:
      return Color.PRIMARY;
    case UNIFORM_RELEASE_TYPE.SALE:
      return Color.WARNING;
    case UNIFORM_RELEASE_TYPE.SENIORITY:
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

export default {
  reportUniformsExpiringTypeMapping,
  reportUniformsExpiringTypeColorMapping,
  formatUniformExpirationStatusMapping,
  reportUniformsExpiringHistoryTypeMapping,
  reportUniformsExpiringHistoryTypeColorMapping
};





