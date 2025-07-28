import { PaginationParams } from './pagination.model';

export interface IFactoryLogistics {
  id?: string;
  code: string;
  name: string;
  address: string;
  attribute: { location: { x: string; y: string } };
  employeeOwnerId: string;
  employeeOwner?: {
    id: string;
    code: string;
    fullName: string;
    employeeCode: string;
  };
  company?: string;
  note: string;
  isActive: boolean;
}

export interface IFactoryLogisticsParams extends PaginationParams {
  sort?: string[];
  'code.contains'?: string;
  'name.contains'?: string;
  'isActive.equals'?: boolean;
}
