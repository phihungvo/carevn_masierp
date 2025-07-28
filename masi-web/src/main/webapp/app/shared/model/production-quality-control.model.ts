import { PRODUCTION_QUALITY_STATUS } from './enumerations/production-quality-control.model';
import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';

export interface IQualityCheckSample {
  id?: string;
  samplingDate: string;
  zonedSamplingDate?: string;
  sampleNo: string;
  productType: string;
  sampleWeight: number;
  customer: string;
  reason: string;
  sampleReleaseDate?: string;
  zonedSampleReleaseDate?: string;
  internalHum?: string;
  internalTvn?: string;
  internalAsh?: string;
  internalProtein?: string;
  externalHum?: string;
  externalTvn?: string;
  externalAsh?: string;
  externalProtein?: string;
  samplingEmployeeId: string;
  status?: PRODUCTION_QUALITY_STATUS;
  note?: string;
  createdBy?: string;
  createdAt?: string;
  lastUpdatedAt?: string;
  isActive?: boolean;
  disposalId?: string;
  disposal?: ISampleDisposal;
  manufactureOrder?: IProductionCommand;
  manufactureOrderId?: string;
  packageId?: string;

  proteinPercentageApply?: number;
  itemId?: string;

  reviewerId?: string;
  attributes?: { isDone?: boolean };
}

export interface IReviewSampleDisposal {
  id?: string;
  reviewerApproved: boolean;
  reviewerNote?: string;
  reviewerSignFile?: string;
  reviewer?: IReviewer;
}

interface IReviewer {
  id: string;
}

export interface ISampleDisposal {
  id?: string;
  qualitySampleCheckId: string;
  requestDate?: string;
  involveEmployee?: string;
  position?: string;
  disposalNote?: string;
  quantityStt?: number;
  quantitySampleName?: string;
  quantitySampleNo?: string;
  quantity?: number;
  quantitySaveDate?: string;
  quantityReleaseDate?: string;
  disposalMethod?: string;
  disposalResult?: string;
  reviewerId?: string;
  requesterId?: string;
  reviewer?: IReviewer;
  reviewerNote?: string;
}

export interface IProductionQualityParams extends PaginationParams {
  status: PRODUCTION_QUALITY_STATUS[];
  search?: string;
  isHasQC?: boolean;
  isExistItem?: boolean;
}
