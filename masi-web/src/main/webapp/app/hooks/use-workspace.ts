import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import workspaceService from 'app/services/workspace.service';
import { IApiError } from 'app/shared/model/error.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IWorkspace, IWorkspaceParams } from 'app/shared/model/workspace.model';
import { AxiosError, AxiosResponse } from 'axios';

const { WORKSPACES } = QUERY_KEY;
const { CREATE_WORKSPACE, UPDATE_WORKSPACE, DELETE_WORKSPACE } = MUTATION_KEY;

const useGetWorkspacesQuery = (
  filter?: IWorkspaceParams,
  select?: (data: AxiosResponse<PaginationResponse<IWorkspace>, any>) => any
) => {
  return useQuery({
    queryKey: [WORKSPACES, filter?.page, filter?.size, filter?.searchString],
    queryFn: () => workspaceService.getWorkspaces(filter),
    select: data => select ? select(data) : data?.data,
  });
};

const useGetWorkspaceByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [WORKSPACES, id],
    queryFn: () => workspaceService.getWorkspaceById(id),
    enabled: !!id,
  });
};

const usePostWorkspaceMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_WORKSPACE],
    mutationFn: workspaceService.postWorkspaces,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
      toggle();
      toggleSuccess();
    },
  });
};

const useUpdateWorkspaceMutation = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_WORKSPACE, id],
    mutationFn: (data: IWorkspace) => workspaceService.patchWorkspaces(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
      toggle();
      toggleSuccess();
    },
  });
};

const useDeleteWorkspaceMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_WORKSPACE],
    mutationFn: (id: string) => workspaceService.deleteWorkspace(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

export default {
  useGetWorkspacesQuery,
  useGetWorkspaceByIdQuery,
  usePostWorkspaceMutation,
  useUpdateWorkspaceMutation,
  useDeleteWorkspaceMutation,
};
