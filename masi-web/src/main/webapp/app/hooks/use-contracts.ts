import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import contractsService from 'app/services/contracts.service';
import { ContractReviewCreate, IApproveContract, IContract, IContractLiquidConsent, IContractLiquidRefuse, IContractParams } from 'app/shared/model/contract.model';

const { CONTRACTS, CONTRACT, CONTRACTS_DELETED, CONTRACT_PRODUCTS, CONTRACT_MATERIALS, CONTRACT_FILES } = QUERY_KEY;
const {
  CREATE_CONTRACT,
  UPDATE_CONTRACT,
  CONTRACT_PROPOSE_APPROVE,
  CONTRACT_NORMAL_REVIEW,
  CONTRACT_REQUEST_LIQUIDATION_REVIEW,
  CONTRACT_REVIEW,
  MASK_FINISHED_COMTRACT,
  DELETE_CONTRACT,
  RECOVER_CONTRACT,
  APPROVED_REVIEW_CONTRACT,
  APPROVE_CONTRACT,
  REVIEW_LIQUID_CONTRACT,
  CONSENT_LIQUID_CONTRACT,
  REFUSE_LIQUID_CONTRACT,
} = MUTATION_KEY;

const useGetContracts = (filter?: IContractParams) => {
  return useQuery({
    queryKey: [
      CONTRACTS,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.contractType,
      filter?.contractStatusList,
      filter?.proteinPercent,
      filter?.contractValidFrom,
      filter?.contractValidTo,
      filter?.isExpired,
      filter?.withTotal,
      filter?.companyName,
      filter?.employeeOwner,
      filter?.withFull,
      filter?.withSum
    ],
    queryFn: () => contractsService.getContracts(filter),
    select: data => data.data,
  });
};

const usePostContract = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_CONTRACT],
    mutationFn: contractsService.postContract,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchContract = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CONTRACT, id],
    mutationFn: (data: IContract) => contractsService.patchContract(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchContractProposeApprove = (id: string, toggle: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CONTRACT_PROPOSE_APPROVE, id],
    mutationFn: (data: ContractReviewCreate) => contractsService.patchContractProposeApprove(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();

    },
  });
};

const usePatchContractNormalReview = (id: string, toggle: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CONTRACT_NORMAL_REVIEW, id],
    mutationFn: (data: ContractReviewCreate) => contractsService.patchContractNormalReview(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchContractMaskFinished = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [MASK_FINISHED_COMTRACT, id],
    mutationFn: () => contractsService.patchContractMaskFinished(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchContractRequestLiquidationReview = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CONTRACT_REQUEST_LIQUIDATION_REVIEW, id],
    mutationFn: (data: ContractReviewCreate) => contractsService.patchContractRequestLiquidationReview(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const usePatchContractReview = (toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CONTRACT_REVIEW],
    mutationFn: (data: IApproveContract) => contractsService.patchContractReview(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACT] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useDeleteContract = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_CONTRACT],
    mutationFn: (id: string) => contractsService.deleteContract(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS_DELETED] });
    },
  });
};

const useGetContractById = (id: string) => {
  return useQuery({
    queryKey: [CONTRACT, id],
    queryFn: () => contractsService.getContractById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const useRecoverContract = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [RECOVER_CONTRACT, id],
    mutationFn: () => contractsService.recoverContract(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      queryClient.invalidateQueries({ queryKey: [CONTRACTS_DELETED] });
      toggle();
      toggleSuccess();
    },
  });
};

const useApprovedReviewContract = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [APPROVED_REVIEW_CONTRACT, id],
    mutationFn: () => contractsService.approvedReviewContract(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useApproveContract = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [APPROVE_CONTRACT, id],
    mutationFn: (data: IApproveContract) => contractsService.approveContract(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useContractReviewLiquid = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REVIEW_LIQUID_CONTRACT, id],
    mutationFn: () => contractsService.reviewContractLiquid(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useContractConsentLiquid = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CONSENT_LIQUID_CONTRACT, id],
    mutationFn: (data: IContractLiquidConsent) => contractsService.consentContractLiquid(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useContractRefuseLiquid = (id: string, toggle: () => void, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [REFUSE_LIQUID_CONTRACT, id],
    mutationFn: (data: IContractLiquidRefuse) => contractsService.refuseContractLiquid(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTRACTS] });
      toggle();
      toggleSuccess();
    },
  });
};

const useGetContractsDeleted = (filter?: IContractParams) => {
  return useQuery({
    queryKey: [CONTRACTS_DELETED, filter],
    queryFn: () => contractsService.getContractsDeleted(filter),
    select: data => data.data,
  });
};

const useGetContractProducts = () => {
  return useQuery({
    queryKey: [CONTRACT_PRODUCTS],
    queryFn: () => contractsService.getContractProducts(),
    select: data => data.data,
  });
};

const useGetContractMaterials = () => {
  return useQuery({
    queryKey: [CONTRACT_MATERIALS],
    queryFn: () => contractsService.getContractMaterials(),
    select: data => data.data,
  });
};

const useGetFilesContract = () => {
  return useQuery({
    queryKey: [CONTRACT_FILES],
    queryFn: () => contractsService.getFilesContract(),
    select: data => data.data,
  });
};

export default {
  useGetContracts,
  usePostContract,
  usePatchContract,
  useDeleteContract,
  useGetContractById,
  useRecoverContract,
  useApprovedReviewContract,
  useApproveContract,
  useContractReviewLiquid,
  useContractConsentLiquid,
  useContractRefuseLiquid,
  useGetContractsDeleted,
  useGetContractProducts,
  useGetFilesContract,
  useGetContractMaterials,
  usePatchContractMaskFinished,
  usePatchContractRequestLiquidationReview,
  usePatchContractReview,
  usePatchContractProposeApprove,
  usePatchContractNormalReview
};
