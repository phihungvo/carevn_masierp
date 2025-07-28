import { itemTypesEndpoints } from 'app/constants/endpoints';
import { IItemType, IItemTypeParams } from 'app/shared/model/item-type.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getItemTypes = async (filter: IItemTypeParams) => {
  try {
    const url = itemTypesEndpoints.getItemTypes;
    const response = await axios.get<PaginationResponse<IItemType>>(url, {
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

export default {
  getItemTypes,
};
