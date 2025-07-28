export enum PRODUCTION_PROCESS_STATUS {
  NEW = 'NEW',
  COMPLETED = 'COMPLETED',
  RUNNING = 'RUNNING',
  STOP = 'STOP',
}

export enum CHECKLIST_TYPE {
  MIXING_REPORT_CHECKLIST = 'MIXING_REPORT_CHECKLIST',
  ADDITIVE_MATERIAL_CHECKLIST = 'ADDITIVE_MATERIAL_CHECKLIST',
  MACHINE_OPERATION_CHECKLIST = 'MACHINE_OPERATION_CHECKLIST',
  METAL_DETECTION_CHECKLIST = 'METAL_DETECTION_CHECKLIST',
  STEAMING_PROCESS_CHECKLIST = 'STEAMING_PROCESS_CHECKLIST',
  RECEIVE_MATERIAL_CHECKLIST = 'RECEIVE_MATERIAL_CHECKLIST',
}

export interface IListOptionMaterialType {
  label: string;
  value: string;
}

interface ITranslationMap {
  [key: string]: {
    en: string;
    vi: string;
  };
}

export enum EMaterialType {
  FISH_BODY = 'FISH_BODY',
  FISH_HEAD = 'FISH_HEAD',
  OTHER = 'OTHER',
}

const translationMap: ITranslationMap = {
  FISH_BODY: {
    en: 'Fish Body',
    vi: 'Thân cá',
  },
  FISH_HEAD: {
    en: 'Fish Head',
    vi: 'Đầu cá',
  },
  OTHER: {
    en: 'Other',
    vi: 'Khác',
  },
};

export function mapMaterialTypes(language: 'en' | 'vi'): IListOptionMaterialType[] {
  return Object.keys(EMaterialType).map(key => ({
    label: translationMap[key][language],
    value: EMaterialType[key as keyof typeof EMaterialType],
  }));
}

export const materialType = [EMaterialType.FISH_BODY, EMaterialType.FISH_HEAD, EMaterialType.OTHER] as const;
