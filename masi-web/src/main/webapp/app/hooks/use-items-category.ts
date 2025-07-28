import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import itemCategoriesService from 'app/services/itemCategorys.service';
import { IItemCategoryParams } from 'app/shared/model/item-category.model';

const { ITEM_CATEGORY } = QUERY_KEY;
// const {} = MUTATION_KEY;

const useGetItemsCategoryQuery = (filter?: IItemCategoryParams) => {
  return useQuery({
    queryKey: [ITEM_CATEGORY, filter],
    queryFn: () => itemCategoriesService.getItemCategories(filter),
    select: data => data.data,
  });
};

// const useGetWorkspaceByIdQuery = (id: string) => {
//   return useQuery({
//     queryKey: [WORKSPACES, id],
//     queryFn: () => workspaceService.getWorkspaceById(id),
//     enabled: !!id,
//   });
// };

// const usePostWorkspaceMutation = (toggle: () => void, toggleSuccess: () => void) => {
//   const queryClient = useQueryClient();

//   return useMutation({
//     mutationKey: [CREATE_WORKSPACE],
//     mutationFn: workspaceService.postWorkspaces,
//     onSuccess: () => {
//       queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
//       toggle();
//       toggleSuccess();
//     },
//   });
// };

// const useUpdateWorkspaceMutation = (id: string, toggle: () => void, toggleSuccess: () => void) => {
//   const queryClient = useQueryClient();

//   return useMutation({
//     mutationKey: [UPDATE_WORKSPACE, id],
//     mutationFn: (data: IWorkspace) => workspaceService.patchWorkspaces(data, id),
//     onSuccess: () => {
//       queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
//       toggle();
//       toggleSuccess();
//     },
//   });
// };

// const useDeleteWorkspaceMutation = (onOk?: () => void) => {
//   const queryClient = useQueryClient();

//   return useMutation({
//     mutationKey: [DELETE_WORKSPACE],
//     mutationFn: (id: string) => workspaceService.deleteWorkspace(id),
//     onSuccess: () => {
//       queryClient.invalidateQueries({ queryKey: [WORKSPACES] });
//       onOk && onOk();
//     },
//     onError: (error: AxiosError<IApiError>) => error,
//   });
// };

export default {
  useGetItemsCategoryQuery,
  // useGetWorkspaceByIdQuery,
  // usePostWorkspaceMutation,
  // useUpdateWorkspaceMutation,
  // useDeleteWorkspaceMutation,
};
