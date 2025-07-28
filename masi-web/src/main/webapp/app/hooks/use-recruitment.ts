import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import recruitmentService from 'app/services/recruitment.service';
import { IFIle } from 'app/shared/model/file.model';
import {
  IInterviewResult,
  IInterviewSchedule,
  IInterviewScheduleParams,
  IRecruitment,
  IRecruitmentApprovalConsent,
  IRecruitmentParams,
  IRecruitmentReject,
  IRecruitmentRenew,
} from 'app/shared/model/recruitment.model';

const { RECRUITMENTS, RECRUITMENTS_CANDIDATES, RECRUITMENTS_CANDIDATE, RECRUITMENTS_HISTORY, INTERVIEW_SCHEDULE_DETAILS } = QUERY_KEY;
const {
  CREATE_RECRUITMENT,
  UPDATE_RECRUITMENT,
  UPDATE_INTERVIEW_SCHEDULE,
  DELETE_RECRUITMENT,
  APPROVE_RECRUITMENT,
  REJECT_RECRUITMENT,
  CREATE_INTERVIEW,
  UPDATE_INTERVIEW_RESULT,
  UPDATE_RECRUITMENT_ADJOURN,
} = MUTATION_KEY;

const useGetRecruitments = (filter?: IRecruitmentParams) => {
  return useQuery({
    queryKey: [
      RECRUITMENTS,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.status,
      filter?.position,
      filter?.interviewResult,
      filter?.recruitmentProcess,
    ],
    queryFn: () => recruitmentService.getRecruitmentRequests(filter),
    select: data => data.data,
  });
};

const useGetRecruitmentHistory = (id: string) => {
  return useQuery({
    queryKey: [RECRUITMENTS_HISTORY, id],
    queryFn: () => recruitmentService.getRecruitmentHistoryRequests(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useGetInterviewSchedule = (id: string) => {
  return useQuery({
    queryKey: [INTERVIEW_SCHEDULE_DETAILS, id],
    queryFn: () => recruitmentService.getInterviewSchedule(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useGetRecruitmentById = (id: string) => {
  return useQuery({
    queryKey: [RECRUITMENTS, id],
    queryFn: () => recruitmentService.getRecruitmentRequestById(id),
    enabled: !!id,
    select: data => data.data,
  });
};

const usePostRecruitment = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_RECRUITMENT],
    mutationFn: (data: IRecruitment) => recruitmentService.postRecruitmentRequest(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchRecruitment = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_RECRUITMENT, id],
    mutationFn: (data: IRecruitment) => recruitmentService.patchRecruitmentRequest(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchInterviewSchedule = (id: string, setFile: React.Dispatch<React.SetStateAction<IFIle>>, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_INTERVIEW_SCHEDULE, id],
    mutationFn: (data: IInterviewSchedule) => recruitmentService.patchInterviewSchedule(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS_CANDIDATES],
      });
      setFile(null)
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchRecruitmentAdjourn = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_RECRUITMENT_ADJOURN, id],
    mutationFn: (data: IRecruitmentRenew) => recruitmentService.patchRecruitmentRequestAdjourn(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useDeleteRecruitment = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_RECRUITMENT],
    mutationFn: (id: string) => recruitmentService.deleteRecruitmentRequest(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useApproveRecruitment = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [APPROVE_RECRUITMENT, id],
    mutationFn: (data: IRecruitmentApprovalConsent) => recruitmentService.approveRecruitmentRequest(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useRejectRecruitment = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REJECT_RECRUITMENT, id],
    mutationFn: (data: IRecruitmentReject) => recruitmentService.rejectRecruitmentRequest(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePostRecruitmentInterview = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_INTERVIEW, id],
    mutationFn: (data: IInterviewSchedule) => recruitmentService.postInterviewSchedule(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS_CANDIDATES],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const usePatchInterviewResult = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_INTERVIEW_RESULT, id],
    mutationFn: (data: IInterviewResult) => recruitmentService.patchInterviewResult(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS],
      });
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS_CANDIDATES],
      });
      queryClient.invalidateQueries({
        queryKey: [RECRUITMENTS_CANDIDATE, id],
      });
      toggle && toggle();
      toggleSuccess && toggleSuccess();
    },
  });
};

const useGetRecruitmentsCandidates = (filter?: IInterviewScheduleParams) => {
  return useQuery({
    queryKey: [
      RECRUITMENTS_CANDIDATES,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.interviewResult,
      filter?.process,
      filter?.recruitmentId,
    ],
    queryFn: () => recruitmentService.getRecruitmentCandidates(filter),
    select: data => data.data,
  });
};

const useGetRecruitmentsCandidateById = (id: string) => {
  return useQuery({
    queryKey: [RECRUITMENTS_CANDIDATE, id],
    queryFn: () => recruitmentService.getRecruitmentCandidateById(id),
    enabled: !!id,
    select: data => data.data,
  });
};

export default {
  useGetRecruitments,
  useGetRecruitmentById,
  usePostRecruitment,
  usePatchRecruitment,
  useDeleteRecruitment,
  useApproveRecruitment,
  useRejectRecruitment,
  usePostRecruitmentInterview,
  usePatchInterviewResult,
  useGetRecruitmentsCandidates,
  useGetRecruitmentsCandidateById,
  usePatchRecruitmentAdjourn,
  useGetRecruitmentHistory,
  useGetInterviewSchedule,
  usePatchInterviewSchedule
};
