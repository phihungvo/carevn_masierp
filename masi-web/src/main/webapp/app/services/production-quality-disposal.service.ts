import { qualityDisposalEndpoints } from 'app/constants/endpoints';
import { IReviewSampleDisposal, ISampleDisposal } from 'app/shared/model/production-quality-control.model';
import axios from 'axios';

// Tạo biểu mẫu huỷ mẫu kiểm thử
const postCancelTestingTemplate = async (data: ISampleDisposal) => {
  const url = qualityDisposalEndpoints.postQualityDisposal;

  return await axios.post<ISampleDisposal>(url, data);
};

const patchCancelTestingTemplate = async (data: ISampleDisposal, id:string) => {
  const url = qualityDisposalEndpoints.patchQualityDisposal(id);

  return await axios.patch<ISampleDisposal>(url, data);
};

// Từ chối / Xác nhận huỷ mẫu kiểm thử
const patchQualityDisposalReviews = async (data: IReviewSampleDisposal, id: string) => {
  const url = qualityDisposalEndpoints.patchQualityDisposalReviews(id);

  return await axios.patch<IReviewSampleDisposal>(url, data);
};

export default {
  postCancelTestingTemplate,
  patchCancelTestingTemplate,
  patchQualityDisposalReviews,
};
