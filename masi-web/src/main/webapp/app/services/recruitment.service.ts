import { recruitmentEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IInterviewResult,
  IInterviewSchedule,
  IInterviewScheduleParams,
  IRecruitment,
  IRecruitmentApprovalConsent, IRecruitmentHistory,
  IRecruitmentParams,
  IRecruitmentReject,
  IRecruitmentRenew,
} from 'app/shared/model/recruitment.model';
import axios from 'axios';

const getRecruitmentRequests = async (filter?: IRecruitmentParams) => {
  try {
    const url = recruitmentEndpoints.getRecruitmentRequests;
    const response = await axios.get<PaginationResponse<IRecruitment>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.status?.length && { status: filter.status }),
        ...(filter?.position && { position: filter.position }),
        ...(filter?.interviewResult && { interviewResult: filter.interviewResult }),
        ...(filter?.recruitmentProcess && { recruitmentProcess: filter.recruitmentProcess }),
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

const getRecruitmentHistoryRequests = async (id: string) => {
  try {
    const url = recruitmentEndpoints.getRecruitmentHistoryRequests(id);
    return await axios.get<PaginationResponse<IRecruitmentHistory>>(url);
  } catch (error) {
    console.error(error);
  }
}

const getInterviewSchedule = async (id: string) => {
  try {
    const url = recruitmentEndpoints.getInterviewSchedule(id);
    return await axios.get<IInterviewSchedule>(url);
  } catch (error) {
    console.error(error);
  }
}

const getRecruitmentRequestById = async (id: string) => {
  try {
    const url = recruitmentEndpoints.getRecruitmentRequestById(id);
    const response = await axios.get<IRecruitment>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postRecruitmentRequest = async (data: IRecruitment) => {
  const url = recruitmentEndpoints.postRecruitmentRequest;
  return await axios.post<IRecruitment>(url, data);
};

const patchRecruitmentRequest = async (data: IRecruitment, id: string) => {
  const url = recruitmentEndpoints.patchRecruitmentRequest(id);
  return await axios.patch<IRecruitment>(url, data);
};

const patchInterviewSchedule = async (data: IInterviewSchedule, id: string) => {
  const url = recruitmentEndpoints.patchInterviewSchedule(id);
  return await axios.patch<IInterviewSchedule>(url, data);
};

const patchRecruitmentRequestAdjourn = async (data: IRecruitmentRenew, id: string) => {
  const url = recruitmentEndpoints.patchRecruitmentRequestAdjourn(id);
  return await axios.patch<IRecruitment>(url, data);
};


const deleteRecruitmentRequest = async (id: string) => {
  const url = recruitmentEndpoints.deleteRecruitmentRequest(id);
  return await axios.delete(url);
};

const approveRecruitmentRequest = async (data: IRecruitmentApprovalConsent, id: string) => {
  const url = recruitmentEndpoints.approveRecruitmentRequest(id);
  return await axios.patch<IRecruitmentApprovalConsent>(url, data);
};

const rejectRecruitmentRequest = async (data: IRecruitmentReject, id: string) => {
  const url = recruitmentEndpoints.rejectRecruitmentRequest(id);
  return await axios.patch<IRecruitment>(url, data);
};

const postInterviewSchedule = async (data: IInterviewSchedule, id: string) => {
  const url = recruitmentEndpoints.postRecruitmentInterview(id);
  return await axios.post<IInterviewSchedule>(url, data);
};

const patchInterviewResult = async (data: IInterviewResult, id: string) => {
  const url = recruitmentEndpoints.patchRecruitmentInterviewResult(id);
  return await axios.patch<IInterviewSchedule>(url, data);
};

const getRecruitmentCandidates = async (filter?: IInterviewScheduleParams) => {
  try {
    const url = recruitmentEndpoints.getRecruitmentCandidates;
    const response = await axios.get<PaginationResponse<IInterviewSchedule>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.interviewResult && { interviewResult: filter.interviewResult }),
        ...(filter?.process && { process: filter.process }),
        recruitmentId: filter?.recruitmentId
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

const getRecruitmentCandidateById = async (id: string) => {
  try {
    const url = recruitmentEndpoints.getRecruitmentCandidateById(id);
    const response = await axios.get<IInterviewSchedule>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getRecruitmentRequests,
  postRecruitmentRequest,
  getRecruitmentRequestById,
  patchRecruitmentRequest,
  deleteRecruitmentRequest,
  approveRecruitmentRequest,
  rejectRecruitmentRequest,
  postInterviewSchedule,
  patchInterviewResult,
  getRecruitmentCandidates,
  getRecruitmentCandidateById,
  patchRecruitmentRequestAdjourn,
  getRecruitmentHistoryRequests,
  getInterviewSchedule,
  patchInterviewSchedule
};
