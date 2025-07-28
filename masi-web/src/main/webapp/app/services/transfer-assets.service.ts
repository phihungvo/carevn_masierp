import { transferAssetsEndpoints } from 'app/constants/endpoints';
import { ITransferAssets, ITransferAssetsParams } from 'app/shared/model/transfer-assets.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getTransferAssets = async (params: ITransferAssetsParams) => {
  try {
    const url = transferAssetsEndpoints.getTransferAssets;
    const response = await axios.get<PaginationResponse<ITransferAssets>>(url, {
      params,
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postTransferAssets = async (data: ITransferAssets) => {
  const url = transferAssetsEndpoints.postTransferAssets;

  return await axios.post<ITransferAssets>(url, data);
};

const patchTransferAssets = async (data: ITransferAssets, id: string) => {
  const url = transferAssetsEndpoints.patchTransferAssets(id);

  return await axios.patch<ITransferAssets>(url, data);
};

const getTransferAssetsById = async (id: string) => {
  try {
    const url = transferAssetsEndpoints.getTransferAssetsById(id);
    const response = await axios.get<ITransferAssets>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteTransferAssets = async (id: string) => {
  const url = transferAssetsEndpoints.deleteTransferAssets(id);

  return await axios.delete(url);
};

const exportTransferAssets = async (params: ITransferAssetsParams) => {
  try {
    const url = transferAssetsEndpoints.exportTransferAssets;
    const response = await axios.get(url, {
      params,
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const nextCodeTransferAssets = () => {
  const url = transferAssetsEndpoints.nextCodeTransferAssets();
  return axios.get(url);
}


const disableTransferAssets = (id: string) => {
  const url = transferAssetsEndpoints.disableTransferAssets(id);
  return axios.patch(url);
}


const enableTransferAssets = (id: string) => {
  const url = transferAssetsEndpoints.enableTransferAssets(id);
  return axios.patch(url);
}



export default {
  getTransferAssets,
  postTransferAssets,
  getTransferAssetsById,
  patchTransferAssets,
  deleteTransferAssets,
  exportTransferAssets,
  nextCodeTransferAssets,
  disableTransferAssets,
  enableTransferAssets
};
