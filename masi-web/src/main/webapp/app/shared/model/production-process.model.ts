import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';
import { CHECKLIST_TYPE, EMaterialType, PRODUCTION_PROCESS_STATUS } from './enumerations/production-process.model';

export interface IProductionProcess {
  id: string;
  fromDate: string;
  toDate: string;
  status: PRODUCTION_PROCESS_STATUS;
  isActive: boolean;
  createdAt: string;
  lastUpdated: string;
  workItem: IProductionProcessItem;
  workItemId: string;
  manufactureOrder: IProductionCommand;
  moId: string;
  checklistType: CHECKLIST_TYPE;
  checklistOrder: number;
  checklist: any;
}

export interface IPostProductionProcessDto {
  fromDate: string;
  moId: string;
  checklistType: string;
}

export interface IPatchProductionProcess {
  fromDate: string;
  moId?: string;
}

export interface ICheckList { }

export interface IProductionProcessItem {
  id: string;
  isActive: boolean;
  createdAt: string;
  lastUpdated: string;
  checkListsCount: number;
  checkLists: any;
}

export interface IReceiveMaterialCheckList {
  id?: string;
  checkDate: string;
  zonedCheckDate?: string;
  checkTime: string;
  weightNumber: string;
  transportCondition: boolean;
  transportNote: string;
  checkStatus: boolean;
  statusNote: string;
  checkSmell: boolean;
  smellNote: string;
  checkImpurity: boolean;
  impurityNote: string;
  checkPoison: boolean;
  receiverId: string;
  note?: string;
  isActive?: boolean;
  createdAt?: string;
  lastUpdated?: string;
  workItemId: string;
  weight: string;
  materialType: EMaterialType;
  type?: CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST;
}

export interface ISteamingProcessChecklist {
  id?: string;
  checkDate: string;
  zonedCheckDate?: string;
  checkTime: string;
  weightNumber: string;
  steamerAtm: string;
  steamerTemp: string;
  steamerTime: string;
  tub1Atm: string;
  tub1Temp: string;
  tub1Time: string;
  tub2Atm: string;
  tub2Temp?: string;
  tub2Time: string;
  finProductNo: string;
  receiverId: string;
  workItemId: string;
  note?: string;
  isActive?: boolean;
  createdAt?: string;
  lastUpdatedAt?: string;
  type?: CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST;
}

export interface IAdditiveMaterialChecklist {
  id?: string;
  checkDate: string;
  zonedCheckDate?: string;
  checkTime: string;
  weightNumber: string;
  checkImpurity: boolean;
  impurityNote?: string;
  weightMaterial: number;
  weightMaterialUnit: string;
  bicabonatLotNumber: string;
  bicacbonatWeight: number;
  bicacbonatWeightUnit: string;
  receiverId: string;
  note?: string;
  isActive?: boolean;
  createdAt?: string;
  lastUpdated?: string;
  workItemId: string;
  type?: CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST;
}

export interface IMetalDetectionChecklist {
  id?: string;
  checkDate: string;
  zonedCheckDate?: string;
  finProductNo: string;
  magnetBegin: boolean;
  magnetBeginNote?: string;
  magnetEnd: boolean;
  magnetEndNote?: string;
  screen4Begin: boolean;
  screen4BeginNote?: string;
  screen4End: boolean;
  screen4EndNote?: string;
  screen3Begin: boolean;
  screen3BeginNote?: string;
  screen3End: boolean;
  screen3EndNote?: string;
  checkedBy: string;
  auditedBy: string;
  note?: string;
  isActive?: boolean;
  createdAt?: string;
  lastUpdated?: string;
  workItemId: string;
  type?: CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST;
}

export interface IMixingReportChecklist {
  id?: string;
  checkDate: string;
  zonedCheckDate?: string;
  finProduct1No: string;
  finProduct1Weight: number;
  finProduct1WeightUnit: string;
  finProduct2No: string;
  finProduct2Weight: number;
  finProduct2WeightUnit: string;
  bhtNo: string;
  bhtWeight: number;
  bhtWeightUnit: string;
  bhtWeightPrd: number;
  bhtWeightPrdUnit: string;
  weightPrdNo: string;
  checkImpurity: boolean;
  checkImpurityNote?: string;
  checkSmell: boolean;
  checkSmellNote?: string;
  checkColor: boolean;
  checkColorNote?: string;
  checkEmployeeId: string;
  note?: string;
  isActive?: boolean;
  createdAt?: string;
  lastUpdated?: string;
  moisture?: number;
  tvn?: number;
  ash?: number;
  protein?: number;
  workItemId: string;
  type?: CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST;
}

export interface IMachineOperationChecklist {
  id?: string;
  checkTime: string;
  incineratorAirDuct: boolean;
  incineratorAirDuctNote?: string;
  incinerator: boolean;
  incineratorCheckNote?: string;
  dryingOvenAirDuct: boolean;
  dryingOvenAirDuctNote?: string;
  dryingOvenMeter: boolean;
  dryingOvenMeterNote?: string;
  dryingOvenWall: boolean;
  dryingOvenWallNote?: string;
  dryingOvenValve: boolean;
  dryingOvenValveNote?: string;
  sieveScreen: boolean;
  sieveScreenNote?: string;
  crusher: boolean;
  crusherNote?: string;
  magnet: boolean;
  magnetNote?: string;
  mixer: boolean;
  mixerNote?: string;
  packagingMachine: boolean;
  packagingMachineNote?: string;
  isActive?: boolean;
  createdAt?: boolean;
  lastUpdated?: boolean;
  workItemId: string;
  type?: CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST;
}

export interface IProductionProcessDetailParams extends PaginationParams {
  startDate: string;
  endDate: string;
  name?: string;
}
