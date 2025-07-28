import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import groupService from 'app/services/group.service';
import { GROUP_ACTION } from 'app/shared/model/enumerations/group.enum';
import { IGroupMutation, IGroupParams, IGroupUser } from 'app/shared/model/group.model';

const { GROUPS, GROUP } = QUERY_KEY;
const { CREATE_GROUP, UPDATE_GROUP, DELETE_GROUP, ADD_GROUP_USERS } = MUTATION_KEY;

const useGroups = (filter?: IGroupParams) => {
  return useQuery({
    queryKey: [GROUPS, filter?.page, filter?.size, filter?.search],
    queryFn: () => groupService.getGroups(filter),
    select: data => data.data,
  });
};

const useGroupById = (id: string) => {
  return useQuery({
    queryKey: [GROUP, id],
    queryFn: () => groupService.getGroupById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostGroup = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_GROUP],
    mutationFn: groupService.postGroup,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [GROUPS] });
      onOk && onOk();
    },
  });
};

const usePatchGroup = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_GROUP],
    mutationFn: (data: IGroupMutation) => groupService.patchGroup(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [GROUPS] });
      onOk && onOk();
    },
  });
};

const useDeleteGroup = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_GROUP],
    mutationFn: () => groupService.deleteGroup(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [GROUPS] });
      onOk && onOk();
    },
  });
};

const usePostGroupUsers = (action?: GROUP_ACTION) => {
  return useMutation({
    mutationKey: [ADD_GROUP_USERS],
    mutationFn: (data: IGroupUser) => groupService.postGroupUsers(data, action),
  });
};

export default {
  useGroups,
  useGroupById,
  usePostGroup,
  usePatchGroup,
  useDeleteGroup,
  usePostGroupUsers,
};
