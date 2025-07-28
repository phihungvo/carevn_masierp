import { WORKSPACE_TYPE } from './enumerations/workspace.model';
import { PaginationParams } from './pagination.model';

export interface IWorkspace {
  id?: string;
  name?: string;
  description?: string;
  isActive?: boolean;
  workspaceType?: WORKSPACE_TYPE;
  canDelete?: boolean;
  normalizedName?: string;
}

export interface IWorkspaceParams extends PaginationParams {
  searchString?: string;
}
