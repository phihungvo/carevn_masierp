import { timeKeepingExplanationEndpoints, timeKeepingExplanationReviewEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IPatchTimeKeepingExplanationDto,
  IPostTimeKeepingExplanationDto,
  ITimeKeepingExplanation,
  ITimeKeepingExplanationParams,
  ITimeKeepingExplanationReviewDto,
} from 'app/shared/model/time-keeping-explanation.model';
import axios from 'axios';

// Lấy danh sách giải trình chấm công
const getTimeKeepingExplanations = async (filter?: ITimeKeepingExplanationParams) => {
  const url = timeKeepingExplanationEndpoints.getTimeKeepingExplanations;

  try {
    const response = await axios.get<PaginationResponse<ITimeKeepingExplanation>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        searchString: filter?.searchString,
        ...(filter.statuses.length && { statuses: filter.statuses }),
        ...(filter.types.length && { types: filter.types }),
        ...(filter?.startFrom && { startFrom: filter.startFrom }),
        ...(filter?.startTo && { startTo: filter.startTo }),
        ...(filter?.employeeIds?.length && { employeeIds: filter.employeeIds }),
        ...(filter?.workspaceIds?.length && { workspaceIds: filter.workspaceIds }),
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

// Tạo giải trình chấm công
const postTimeKeepingExplanation = async (data: IPostTimeKeepingExplanationDto[]) => {
  const url = timeKeepingExplanationEndpoints.postTimeKeepingExplanation;

  try {
    const response = await axios.post<ITimeKeepingExplanation>(url, data);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Cập nhật giải trình chấm công
const patchTimeKeepingExplanation = async (data: IPatchTimeKeepingExplanationDto, id: string) => {
  const url = timeKeepingExplanationEndpoints.patchTimeKeepingExplanation(id);

  const response = await axios.patch<ITimeKeepingExplanation>(url, data);

  return response;
};

// Lấy chi tiết giải trình chấm công
const getTimeKeepingExplanationById = async (id: string) => {
  const url = timeKeepingExplanationEndpoints.getTimeKeepingExplanationById(id);

  try {
    const response = await axios.get<ITimeKeepingExplanation>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Xóa giải trình chấm công
const deleteTimeKeepingExplanation = async (id: string) => {
  const url = timeKeepingExplanationEndpoints.deleteTimeKeepingExplanation(id);

  const response = await axios.delete(url);

  return response;
};

// Từ chối / xét duyệt giải trình chấm công
const patchTimeKeepingExplanationReview = async (data: ITimeKeepingExplanationReviewDto, id: string) => {
  const url = timeKeepingExplanationReviewEndpoints.patchTimeKeepingExplanationReviews(id);

  const response = await axios.patch(url, data);

  return response;
};

// Huỷ giải trình chấm công
const patchTimeKeepingExplanationCancel = async (id: string) => {
  const url = timeKeepingExplanationEndpoints.patchTimeKeepingExplanationCancel(id);

  const response = await axios.patch(url);

  return response;
};

export default {
  getTimeKeepingExplanations,
  postTimeKeepingExplanation,
  patchTimeKeepingExplanation,
  getTimeKeepingExplanationById,
  deleteTimeKeepingExplanation,
  patchTimeKeepingExplanationReview,
  patchTimeKeepingExplanationCancel,
};
