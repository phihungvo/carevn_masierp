import { factoryEndpoints } from 'app/constants/endpoints';
import { IFactory, IFactoryParams } from 'app/shared/model/factory.model';
import axios from 'axios';

const getFactories = async (filter?: IFactoryParams) => {
  try {
    const url = factoryEndpoints.getFactories;
    const response = await axios.get<IFactory[]>(url, {
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
  getFactories,
};
