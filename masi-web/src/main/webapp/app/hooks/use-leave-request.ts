import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import leaveRequestService from 'app/services/leave-request.service';
import { ILeaveRequestParams, IPatchLeaveRequestReviewDto } from 'app/shared/model/leave-request.model';

const { LEAVE_REQUEST, LEAVE_REQUEST_DETAIL } = QUERY_KEY;
const { CREATE_LEAVE_REQUEST, CANCEL_LEAVE_REQUEST, DELETE_LEAVE_REQUEST, REVIEW_LEAVE_REQUEST, LEAVE_REQUEST_COUNT_DAY_END_OFF } = MUTATION_KEY;

// Lấy danh sách đơn nghỉ phép
const useGetLeaveRequestsQuery = (filter: ILeaveRequestParams) => {
  return useQuery({
    queryKey: [
      LEAVE_REQUEST,
      filter?.page,
      filter?.size,
      filter?.status,
      filter?.type,
      filter?.workspaceType,
      filter?.sort,
      filter?.workspaceIds,
      filter?.employeeIds,
    ],
    queryFn: () => leaveRequestService.getLeaveRequests(filter),
    select: data => data.data,
  });
};

// Lấy tổng số ngày nghỉ.
const useGetLeaveRequestCountDayEndOff = () => {
  return useMutation({
    mutationKey: [LEAVE_REQUEST_COUNT_DAY_END_OFF],
    mutationFn: (filter: ILeaveRequestParams) => leaveRequestService.getLeaveRequestCountDayEndOff(filter)
  });
};

// Tạo Đơn Nghỉ Phép
const usePostLeaveRequestMutation = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_LEAVE_REQUEST],
    mutationFn: leaveRequestService.postLeaveRequest,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
      toggle();
      toggleSuccess();
    },
  });
};

// Lấy Chi Tiết Đơn Nghỉ Phép
const useGetLeaveRequestByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [LEAVE_REQUEST_DETAIL, id],
    queryFn: () => leaveRequestService.getLeaveRequestById(id),
    enabled: !!id,
  });
};

// Cập Nhật Đơn Nghỉ Phép
const usePutLeaveRequestMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: leaveRequestService.putLeaveRequest,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
    },
  });
};

// Cập Nhật Đơn Nghỉ Phép
const usePatchLeaveRequestMutation = (toggle: () => void, toggleError?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: leaveRequestService.patchLeaveRequest,
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
    },
    onError: () => {
      toggleError && toggleError();
    },
  });
};

// Xét Duyệt Đơn Nghỉ Phép - Từ chối đơn nghỉ phép
const usePatchLeaveRequestReviewMutation = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_LEAVE_REQUEST],
    mutationFn: (body: IPatchLeaveRequestReviewDto) => leaveRequestService.patchLeaveRequestReview(body.reviewId, body.status, body.reason),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST_DETAIL] });

    },
  });
};

// Huỷ Đơn Nghỉ Phép
const usePatchLeaveRequestCancelMutation = (toggle?: () => void, toggleError?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CANCEL_LEAVE_REQUEST],
    mutationFn: (id: string) => leaveRequestService.patchLeaveRequestCancel(id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
    },
    onError: () => {
      toggleError && toggleError();
    },
  });
};

// Xoá Đơn Nghỉ Phép
const useDeleteLeaveRequestMutation = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_LEAVE_REQUEST],
    mutationFn: (body: { id: string }) => leaveRequestService.deleteLeaveRequest(body.id),
    onMutate: () => {
      toggle && toggle();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LEAVE_REQUEST] });
    },
  });
};

const useCountDayOff = () => {
  return useMutation({
    mutationFn: ({ employeeId, fromDate, toDate, leaveRequestDayType }: {
      employeeId: string;
      fromDate: string;
      toDate: string;
      leaveRequestDayType: string;
    }) => leaveRequestService.countDayOff(fromDate, toDate, employeeId, leaveRequestDayType),
  });
};


export default {
  useGetLeaveRequestsQuery,
  usePostLeaveRequestMutation,
  useGetLeaveRequestByIdQuery,
  usePutLeaveRequestMutation,
  usePatchLeaveRequestMutation,
  useDeleteLeaveRequestMutation,
  usePatchLeaveRequestCancelMutation,
  usePatchLeaveRequestReviewMutation,
  useCountDayOff,
  useGetLeaveRequestCountDayEndOff
};
