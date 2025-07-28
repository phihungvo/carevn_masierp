import { groupEndpoints } from 'app/constants/endpoints';
import { GROUP_ACTION } from 'app/shared/model/enumerations/group.enum';
import { IGroup, IGroupMutation, IGroupParams, IGroupUser } from 'app/shared/model/group.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getGroups = async (filter: IGroupParams) => {
  try {
    const url = groupEndpoints.getGroups;

    const response = await axios.get<PaginationResponse<IGroup>>(url, { params: filter });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getGroupById = async (id: string) => {
  try {
    const url = groupEndpoints.getGroupById(id);

    const response = await axios.get<IGroup>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postGroup = async (group: IGroupMutation) => {
  const url = groupEndpoints.postGroup;

  return await axios.post<IGroupMutation>(url, group);
};

const patchGroup = async (id: string, group: IGroupMutation) => {
  const url = groupEndpoints.patchGroup(id);

  return await axios.put<IGroup>(url, group);
};

const deleteGroup = async (id: string) => {
  const url = groupEndpoints.deleteGroup(id);

  return await axios.delete(url);
};

const postGroupUsers = async (data: IGroupUser, action: GROUP_ACTION = GROUP_ACTION.REPLACE) => {
  const url = groupEndpoints.postGroupUser(action);

  return await axios.post(url, data);
};

export default {
  getGroups,
  getGroupById,
  postGroup,
  patchGroup,
  deleteGroup,
  postGroupUsers,
};
