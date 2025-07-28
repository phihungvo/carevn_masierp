import { useMutation, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import productionQualityDisposalService from 'app/services/production-quality-disposal.service';
import {
  IReviewSampleDisposal,
  ISampleDisposal,
} from 'app/shared/model/production-quality-control.model';

const { PRODUCTION_QUALITIES, PRODUCTION_QUALITY } = QUERY_KEY;
const {
  CREATE_QUALITY_DISPOSAL,
  UPDATED_QUALITY_DISPOSAL,
  REVIEW_QUALITY_DISPOSAL,
} = MUTATION_KEY;

// Tạo biểu mẫu huỷ mẫu kiểm thử
const usePostQualityDisposal = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_QUALITY_DISPOSAL],
    mutationFn: (data: ISampleDisposal) =>
      productionQualityDisposalService.postCancelTestingTemplate(data),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

const usePatchQualityDisposal = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATED_QUALITY_DISPOSAL],
    mutationFn: (data: ISampleDisposal) =>
      productionQualityDisposalService.patchCancelTestingTemplate(
        data,
        data?.id,
      ),
    onSuccess: () => {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
    },
  });
};

const usePatchQualityDisposalReviews = (
  toggle?: () => void,
  toggleSuccess?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_QUALITY_DISPOSAL],
    mutationFn: ({
      data,
      id,
    }: { data: IReviewSampleDisposal } & { id: string }) =>
      productionQualityDisposalService.patchQualityDisposalReviews(data, id),
    onSuccess: () => {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITIES],
      });
      queryClient.invalidateQueries({
        queryKey: [PRODUCTION_QUALITY],
      });
    },
  });
};

export default {
  usePostQualityDisposal,
  usePatchQualityDisposal,
  usePatchQualityDisposalReviews,
};
