import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { workspaceEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IWorkspace, IWorkspaceParams } from 'app/shared/model/workspace.model';
import axios from 'axios';

const getWorkspaces = async (filter: IWorkspaceParams) => {
  try {
    const url = workspaceEndpoints.getWorkspaces;
    const response = await axios.get<PaginationResponse<IWorkspace>>(url, {
      params: {
        page: filter?.page ?? DEFAULT_PAGE,
        size: filter?.size ?? DEFAULT_PAGE_SIZE_NAX,
        searchString: filter?.searchString,
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postWorkspaces = async (data: IWorkspace) => {
  const url = workspaceEndpoints.postWorkspace;

  return await axios.post<IWorkspace>(url, data);
};

const patchWorkspaces = async (data: IWorkspace, id: string) => {
  const url = workspaceEndpoints.patchWorkspace(id);

  return await axios.patch<IWorkspace>(url, data);
};

const getWorkspaceById = async (id: string) => {
  try {
    const url = workspaceEndpoints.getWorkspaceById(id);
    const response = await axios.get<IWorkspace>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteWorkspace = async (id: string) => {
  const url = workspaceEndpoints.deleteWorkspace(id);

  return await axios.delete(url);
};

export default {
  getWorkspaces,
  postWorkspaces,
  patchWorkspaces,
  getWorkspaceById,
  deleteWorkspace,
};
