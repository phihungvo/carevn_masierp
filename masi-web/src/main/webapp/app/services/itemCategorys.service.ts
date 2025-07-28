import { itemCategoriesEndpoints } from 'app/constants/endpoints';
import { IItemCategory, IItemCategoryParams } from 'app/shared/model/item-category.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getItemCategories = async (filter: IItemCategoryParams) => {
  try {
    const url = itemCategoriesEndpoints.getItemCategories;
    const response = await axios.get<PaginationResponse<IItemCategory>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        searchString: filter?.searchString,
        ['itemTypeCategory.equals']: filter?.itemTypeCategory,
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

// const postWorkspaces = async (data: IWorkspace) => {
//   const url = workspaceEndpoints.postWorkspace;

//   return await axios.post<IWorkspace>(url, data);
// };

// const patchWorkspaces = async (data: IWorkspace, id: string) => {
//   const url = workspaceEndpoints.patchWorkspace(id);

//   return await axios.patch<IWorkspace>(url, data);
// };

// const getWorkspaceById = async (id: string) => {
//   try {
//     const url = workspaceEndpoints.getWorkspaceById(id);
//     const response = await axios.get<IWorkspace>(url);

//     return response;
//   } catch (error) {
//     console.error(error);
//   }
// };

// const deleteWorkspace = async (id: string) => {
//   const url = workspaceEndpoints.deleteWorkspace(id);

//   return await axios.delete(url);
// };

export default {
  getItemCategories,
  // postWorkspaces,
  // patchWorkspaces,
  // getWorkspaceById,
  // deleteWorkspace,
};
