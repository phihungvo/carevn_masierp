import { inventoryEndpoints } from 'app/constants/endpoints';
import { IInventoryMaterial, IInventoryMaterialParams } from 'app/shared/model/inventory.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getInventoryMaterials = async (filter?: IInventoryMaterialParams) => {
  try {
    const url = inventoryEndpoints.getInventoryMaterials;

    const response = await axios.get<PaginationResponse<IInventoryMaterial>>(url, {
      params: {
        ...filter,
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
  getInventoryMaterials,
};
