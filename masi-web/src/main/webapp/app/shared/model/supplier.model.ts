import { IContract } from './contract.model';
import { IEmployeeProfiles } from './employee.model';
import { PaginationParams } from './pagination.model';
import { ISuppliesDetails } from './supplier-detail.model';
import { ISupplierGroup } from './supplier-group.model';

interface IContactForm {
  id?: string;
  type?: string;
  contactType?: { id: string; name: string };
  contactInfo?: string;
  position?: string;
  phone?: string;
  email?: string;
  birthDate?: Date;
}

export interface ISupplier {
  id?: string;
  code: string;
  name: string;
  address: string;
  addressService?: string;
  taxCode: string;
  supplierTypeId?: string;
  supplierType?: any;
  shortName?: string;
  contact?: string;
  phone: string;
  email: string;
  fax: string;
  note: string;
  isActive?: boolean;
  paymentTermNumber?: number;
  paymentTerm?: string;
  paymentTermText?: string;
  birthday?: string;
  supplierGroupId?: string;
  supplierGroup?: ISupplierGroup;
  suppliesDetails?: ISuppliesDetails[];
  supplierContracts?: IContract[];
  contacts?: IContactForm[];
  debtEmployees?: { id?: string; code?: string; name?: string }[];
  managerId?: string;
  manager?: IEmployeeProfiles;
  fullName?: string;
  position?: string;
  attribute?: any;
  type?: string;
  attachment?: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  bankInfo?: string;
}

export interface ISupplierParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
  search?: string;
  status?: boolean | undefined;
  companyId?: string;
}
