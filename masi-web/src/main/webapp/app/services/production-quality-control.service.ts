import { qualityCheckSampleEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IProductionQualityParams,
  IQualityCheckSample,
} from 'app/shared/model/production-quality-control.model';
import axios from 'axios';

// Lấy danh sách biểu mẫu giám sát chất lượng sản phẩm
const getQualityCheckSamples = async (filter: IProductionQualityParams) => {
  try {
    const url = qualityCheckSampleEndpoints.getQualityCheckSamples;
    const response = await axios.get<PaginationResponse<IQualityCheckSample>>(
      url,
      {
        params: {
          page: filter?.page,
          size: filter?.size,
          status: filter.status,
          search: filter.search,
          ...filter,
        },
        paramsSerializer: {
          indexes: null,
        },
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Tạo biểu mẫu giám sát chất lượng sản phẩm
const postQualityCheckSample = async (data: IQualityCheckSample) => {
  const url = qualityCheckSampleEndpoints.postQualityCheckSample;

  return await axios.post<IQualityCheckSample>(url, data);
};

// Cập nhật biểu mẫu giám sát chất lượng sản phẩm
const patchQualityCheckSample = async (
  data: IQualityCheckSample,
  id: string,
) => {
  const url = qualityCheckSampleEndpoints.patchQualityCheckSample(id);

  return await axios.patch<IQualityCheckSample>(url, data);
};

// Lấy chi tiết biểu mẫu giám sát chất lượng sản phẩm
const getQualityCheckSampleById = async (id: string) => {
  try {
    const url = qualityCheckSampleEndpoints.getQualityCheckSampleById(id);
    const response = await axios.get<IQualityCheckSample>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Xoá biểu mẫu giám sát chất lượng sản phẩm
const deleteQualityCheckSample = async (id: string) => {
  const url = qualityCheckSampleEndpoints.deleteQualityCheckSample(id);

  return await axios.delete(url);
};

// Cập nhập trạng thái đã đạt của mẫu giám sát chất lượng sản phẩm
const patchQualityCheckSampleByIdReviewPass = async (id: string) => {
  const url =
    qualityCheckSampleEndpoints.patchQualityCheckSampleByIdReviewPass(id);

  return await axios.patch<IQualityCheckSample>(url);
};

// Cập nhập trạng thái hủy của mẫu giám sát chất lượng sản phẩm
const patchQualityCheckSampleByIdReviewFail = async (id: string) => {
  const url =
    qualityCheckSampleEndpoints.patchQualityCheckSampleByIdReviewFail(id);

  return await axios.patch<IQualityCheckSample>(url);
};

const rejectQualityCheckSample = async (
  data: { rejectNote: string },
  id: string,
) => {
  const url = qualityCheckSampleEndpoints.rejectQualityCheckSample(id);
  return await axios.patch(url, data);
};

export default {
  getQualityCheckSamples,
  postQualityCheckSample,
  patchQualityCheckSample,
  getQualityCheckSampleById,
  deleteQualityCheckSample,
  patchQualityCheckSampleByIdReviewPass,
  patchQualityCheckSampleByIdReviewFail,
  rejectQualityCheckSample,
};
