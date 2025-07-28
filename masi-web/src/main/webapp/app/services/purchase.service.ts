import { purchaseRequestEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IPurchase, IPurchaseDelivery, IPurchaseParams, PurchaseReview, PurchaseReviewCreate } from 'app/shared/model/purchase.model';
import axios from 'axios';

const getPurchases = async (filter?: IPurchaseParams) => {
  try {
    const url = purchaseRequestEndpoints.getPurchaseRequests;
    const response = await axios.get<PaginationResponse<IPurchase>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        searchString: filter?.searchString,
        ...(filter?.statuses?.length && { statuses: filter.statuses }),
        ...(filter?.createdFrom && { createdFrom: filter.createdFrom }),
        ...(filter?.createdTo && { createdTo: filter.createdTo }),
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {}
};

const getPurchaseById = async (id: string) => {
  try {
    const url = purchaseRequestEndpoints.getPurchaseRequestById(id);
    const response = await axios.get<IPurchase>(url);

    return response;
  } catch (error) {}
};

const postPurchase = async (data: IPurchase) => {
  const url = purchaseRequestEndpoints.postPurchaseRequest;

  return await axios.post(url, data);
};

const patchPurchase = async (data: IPurchase, id: string) => {
  const url = purchaseRequestEndpoints.patchPurchaseRequest(id);

  return await axios.patch(url, data);
};

const deletePurchase = async (id: string) => {
  const url = purchaseRequestEndpoints.deletePurchaseRequest(id);

  return await axios.delete(url);
};

const patchPurchaseDelivery = async (data: IPurchaseDelivery, id: string) => {
  const url = purchaseRequestEndpoints.patchPurchaseRequestDelivery(id);

  return await axios.patch(url, data);
};

const postPurchaseReview = async (data: PurchaseReviewCreate) => {
  const url = purchaseRequestEndpoints.postPurchaseReview;

  return axios.post(url, data);
};

const patchPurchaseReview = async (data: PurchaseReview, id: string) => {
  const url = purchaseRequestEndpoints.patchPurchaseReview(id);

  return axios.patch(url, data);
};

const getPurchaseReviewByPurchaseRequestId = async (id: string) => {
  try {
    const url = purchaseRequestEndpoints.getPurchaseReviewByPurchaseRequestId(id);

    return axios.get<PurchaseReview[]>(url);
  } catch (error) {
    console.error(error);
  }
};

const getPurchaseRequestFile = async (id: string) => {
  try {
    const url = purchaseRequestEndpoints.getPurchaseRequestFileById(id);

    return axios.get<{ file: string; fileName: string }>(url);
  } catch (error) {
    console.error(error);
  }
};

export default {
  getPurchases,
  getPurchaseById,
  postPurchase,
  patchPurchase,
  deletePurchase,
  patchPurchaseDelivery,
  postPurchaseReview,
  patchPurchaseReview,
  getPurchaseReviewByPurchaseRequestId,
  getPurchaseRequestFile,
};
