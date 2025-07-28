import { IEmployeeProfiles } from './employee.model';
import { DOCUMENTARY_GROUP, DOCUMENTARY_STATUS, DOCUMENTARY_TYPE } from './enumerations/documentary';
import { IBodyFile, IFIle } from './file.model';
import { PaginationParams } from './pagination.model';

export interface IDocumentary {
  id?: string;
  documentNumber: string;
  dateStart: string;
  group: DOCUMENTARY_GROUP;
  type: DOCUMENTARY_TYPE;
  content: string;
  signer: string;
  employeeProfileSigner?: IEmployeeProfiles;
  recipient: string;
  archiveLocation: string;
  senderOrReceiver?: string;
  employeeProfileSender?: IEmployeeProfiles;
  attachmentsContentFile?: string;
  attachmentsFile?: IFIle;
  approvalSignFile?: string;
  signFile?: IFIle;
  rejectNote?: string;
  idGroup?: string;
  idCompany?: string;
  status?: DOCUMENTARY_STATUS;
  department?: string;
  embedFiles?: IBodyFile[]
  attachments?: IBodyFile[]
}

export interface IDocumentaryParams extends PaginationParams {
  documentDateFrom?: string;
  documentDateTo?: string;
  documentDate?: string;
  documentaryType?: DOCUMENTARY_TYPE;
  documentaryGroup?: DOCUMENTARY_GROUP;
  search?: string;
}

export interface IDocumentaryReviewConsent {
  approvalSignFile: string;
}

export interface IDocumentaryReviewRefusal {
  rejectNote: string;
}
