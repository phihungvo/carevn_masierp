import { documentariesEndpoints } from 'app/constants/endpoints';
import { IDocumentary, IDocumentaryParams, IDocumentaryReviewConsent, IDocumentaryReviewRefusal } from 'app/shared/model/documentary.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getDocumentaries = async (filter: IDocumentaryParams) => {
  try {
    const url = documentariesEndpoints.getDocumentaries;
    const response = await axios.get<PaginationResponse<IDocumentary>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        search: filter.search,
        documentDateFrom: filter.documentDateFrom,
        documentDateTo: filter.documentDateTo,
        documentDate: filter.documentDate,
        documentaryType: filter.documentaryType,
        documentaryGroup: filter.documentaryGroup,
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

const postDocumentary = async (data: IDocumentary) => {
  const url = documentariesEndpoints.postDocumentary;
  return await axios.post<IDocumentary>(url, data);
};

const patchDocumentary = async (id: string, data: IDocumentary) => {
  const url = documentariesEndpoints.patchDocumentary(id);
  return await axios.patch<IDocumentary>(url, data);
};

const getDocumentaryById = async (id: string) => {
  try {
    const url = documentariesEndpoints.getDocumentaryById(id);
    return await axios.get<IDocumentary>(url);
  } catch (error) {
    console.error(error);
  }
};

const deleteDocumentary = async (id: string) => {
  const url = documentariesEndpoints.deleteDocumentary(id);
  return await axios.delete(url);
};

const patchDocumentaryReview = async (id: string) => {
  const url = documentariesEndpoints.patchDocumentaryReview(id);
  return await axios.patch(url);
};

const patchDocumentaryReviewConsent = async (id: string, data: IDocumentaryReviewConsent) => {
  const url = documentariesEndpoints.patchDocumentaryConsent(id);
  return await axios.patch(url, data);
};

const patchDocumentaryRefusal = async (id: string, data: IDocumentaryReviewRefusal) => {
  const url = documentariesEndpoints.patchDocumentaryRefusal(id);
  return await axios.patch(url, data);
};

export default {
  getDocumentaries,
  postDocumentary,
  patchDocumentary,
  getDocumentaryById,
  deleteDocumentary,
  patchDocumentaryReview,
  patchDocumentaryReviewConsent,
  patchDocumentaryRefusal,
};
