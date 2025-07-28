import { storageEndpoints } from 'app/constants/endpoints';
import { IStorage, IStorageParams } from 'app/shared/model/storage.model';
import axios from 'axios';

const getStorages = async (filter?: IStorageParams) => {
  try {
    const url = storageEndpoints.getStorages;

    const response = await axios.get<IStorage[]>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getStorages,
};
