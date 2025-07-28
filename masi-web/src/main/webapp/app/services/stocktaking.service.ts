import { stocktakingEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IStocktaking,
  IStocktakingFilterParams,
} from 'app/shared/model/stocktaking.model';
import axios from 'axios';

export const getStocktaking = async (filter: IStocktakingFilterParams) => {
  try {
    const url = stocktakingEndpoints.getStocktaking;

    const response = await axios.get<PaginationResponse<IStocktaking>>(url, {
      params: { ...filter },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const getStocktakingXlsx = async (filter: IStocktakingFilterParams) => {
  try {
    const url = stocktakingEndpoints.exportStocktaking;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: { page: filter?.page, size: filter?.size },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const postStocktaking = async (data: Partial<IStocktaking>) => {
  const url = stocktakingEndpoints.postStocktaking;

  return await axios.post<IStocktaking>(url, data);
};

export const patchStocktaking = async (
  id: string,
  data: Partial<IStocktaking>,
) => {
  const url = stocktakingEndpoints.patchStocktaking(id);

  return await axios.patch<IStocktaking>(url, data);
};

export const getStocktakingById = async (id: string) => {
  try {
    const url = stocktakingEndpoints.getStocktakingById(id);
    const response = await axios.get<IStocktaking>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const deleteStocktaking = async (id: string) => {
  const url = stocktakingEndpoints.deleteStocktaking(id);

  return await axios.patch(url);
};

export const reviewStocktaking = async (id: string) => {
  const url = stocktakingEndpoints.reqReviewStocktaking(id);

  return await axios.patch(url);
};

export const confirmStocktaking = async (
  id: string,
  data: { assignSign: string; approvedSignName: string },
) => {
  const url = stocktakingEndpoints.reviewStocktaking(id);

  return await axios.patch(url, {
    approvedSign: data?.assignSign,
    approvedSignName: data?.approvedSignName,
    isApproved: true,
  });
};

export const rejectStocktaking = async (id: string, note: string) => {
  const url = stocktakingEndpoints.reviewStocktaking(id);

  return await axios.patch(url, {
    rejectNote: note,
    isApproved: false,
  });
};

export const getStocktakingGeneratedCode = async () => {
  try {
    const url = stocktakingEndpoints.getStocktakingNextCode();
    const response = await axios.get<{
      code: string;
      nextAndIncrement: string;
    }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};
