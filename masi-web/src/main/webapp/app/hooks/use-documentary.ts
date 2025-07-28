import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import documentaryService from 'app/services/documentary.service';
import { IDocumentary, IDocumentaryParams, IDocumentaryReviewConsent, IDocumentaryReviewRefusal } from 'app/shared/model/documentary.model';

const { DOCUMENTARIES, DOCUMENTARY } = QUERY_KEY;
const {
  CREATE_DOCUMENTARY,
  UPDATE_DOCUMENTARY,
  DELETE_DOCUMENTARY,
  CREATE_DOCUMENTARY_REVIEW,
  APPROVE_DOCUMENTARY_REVIEW,
  REFUSE_DOCUMENTARY_REVIEW,
} = MUTATION_KEY;

const useGetDocumentariesQuery = (filter: IDocumentaryParams) => {
  return useQuery({
    queryKey: [
      DOCUMENTARIES,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.documentDateFrom,
      filter?.documentDateTo,
      filter?.documentDate,
      filter?.documentaryType,
      filter?.documentaryGroup,
    ],
    queryFn: () => documentaryService.getDocumentaries(filter),
    select: data => data?.data,
  });
};

const useGetDocumentaryByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [DOCUMENTARY, id],
    queryFn: () => documentaryService.getDocumentaryById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const usePostDocumentaryMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_DOCUMENTARY],
    mutationFn: documentaryService.postDocumentary,
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

const usePatchDocumentaryMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [UPDATE_DOCUMENTARY, id],
    mutationFn: (data: IDocumentary) => documentaryService.patchDocumentary(id, data),
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

const useDeleteDocumentaryMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [DELETE_DOCUMENTARY],
    mutationFn: (id: string) => documentaryService.deleteDocumentary(id),
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

const usePatchDocumentaryReviewMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [CREATE_DOCUMENTARY_REVIEW, id],
    mutationFn: () => documentaryService.patchDocumentaryReview(id),
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

const usePatchDocumentaryReviewConsentMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [APPROVE_DOCUMENTARY_REVIEW, id],
    mutationFn: (data: IDocumentaryReviewConsent) => documentaryService.patchDocumentaryReviewConsent(id, data),
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

const usePatchDocumentaryReviewRefusalMutation = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [REFUSE_DOCUMENTARY_REVIEW, id],
    mutationFn: (data: IDocumentaryReviewRefusal) => documentaryService.patchDocumentaryRefusal(id, data),
    onSuccess: () => {
      onOk && onOk();
      queryClient.invalidateQueries({
        queryKey: [DOCUMENTARIES],
      });
    },
  });
};

export default {
  useGetDocumentariesQuery,
  usePostDocumentaryMutation,
  usePatchDocumentaryMutation,
  useGetDocumentaryByIdQuery,
  useDeleteDocumentaryMutation,
  usePatchDocumentaryReviewMutation,
  usePatchDocumentaryReviewConsentMutation,
  usePatchDocumentaryReviewRefusalMutation,
};
