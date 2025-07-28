import { IAuthority } from './authority.model';
import { IEmployeeProfiles } from './employee.model';
import { PaginationParams } from './pagination.model';

export interface IGroup {
  id?: string;
  name: string;
  description?: string;
  normalizedName?: string;
  users?: IEmployeeProfiles[];
  authorities?: IAuthority[];
}

export interface IGroupMutation {
  name: string;
  description?: string;
  users?: string[];
  authorities?: string[];
}

export interface IGroupUser {
  groupId: string;
  userIds: string[];
}

export interface IGroupParams extends PaginationParams {
  search?: string;
}
