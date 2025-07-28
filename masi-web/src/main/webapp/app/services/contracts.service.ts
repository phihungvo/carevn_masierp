import { contractsEndpoints } from 'app/constants/endpoints';
import {
  ContractReviewCreate,
  IApproveContract,
  IContract,
  IContractLiquidConsent,
  IContractLiquidRefuse,
  IContractMaterial,
  IContractParams,
  IContractProduct,
} from 'app/shared/model/contract.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getContracts = async (filter?: IContractParams) => {
  try {
    const url = contractsEndpoints.getContracts;
    const response = await axios.get<PaginationResponse<IContract>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        isExpired: filter?.isExpired,
        withTotal: filter?.withTotal,
        withFull: filter?.withFull,
        withSum: filter?.withSum,
        ...(filter?.contractType && { contractType: filter.contractType }),
        ...(filter?.contractStatusList && { contractStatusList: filter.contractStatusList }),
        ...(filter?.proteinPercent && { proteinPercent: filter.proteinPercent }),
        ...(filter?.contractValidFrom && { contractValidFrom: filter.contractValidFrom }),
        ...(filter?.contractValidTo && { contractValidTo: filter.contractValidTo }),
        ...(filter?.companyName && { companyName: filter.companyName }),
        ...(filter?.employeeOwner && { employeeOwner: filter.employeeOwner }),
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

const postContract = async (contract: IContract) => {
  const url = contractsEndpoints.postContract;

  return await axios.post<IContract>(url, contract);
};

const patchContract = async (id: string, contract: IContract) => {
  const url = contractsEndpoints.patchContract(id);

  return await axios.patch<IContract>(url, contract);
};

const patchContractRequestLiquidationReview = async (id: string, data: ContractReviewCreate) => {
  const url = contractsEndpoints.patchContractRequestLiquidationReview(id);

  return await axios.patch<IContract>(url, data);
};

const patchContractReview = async (data: IApproveContract) => {
  const url = contractsEndpoints.patchContractReview;

  return await axios.patch<IContract>(url, data);
};

const patchContractMaskFinished = async (id: string) => {
  const url = contractsEndpoints.patchContractMaskFinished(id);

  return await axios.patch<IContract>(url);
};

const patchContractProposeApprove = async (id: string, data: ContractReviewCreate) => {
  const url = contractsEndpoints.patchContractProposeApprove(id);

  return await axios.patch<IContract>(url, data);
};

const patchContractNormalReview = async (id: string, data: ContractReviewCreate) => {
  const url = contractsEndpoints.patchContractNormalReview(id);

  return await axios.patch<IContract>(url, data);
};

const deleteContract = async (id: string) => {
  const url = contractsEndpoints.deleteContract(id);

  return await axios.delete(url);
};

const getContractById = async (id: string) => {
  try {
    const url = contractsEndpoints.getContractById(id);
    const response = await axios.get<IContract>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const recoverContract = async (id: string) => {
  const url = contractsEndpoints.recoverContract(id);

  return await axios.patch(url);
};

const approvedReviewContract = async (id: string) => {
  const url = contractsEndpoints.approvedReviewContract(id);

  return await axios.patch(url);
};

const approveContract = async (id: string, data: IApproveContract) => {
  const url = contractsEndpoints.approveContract(id);

  return await axios.patch(url, data);
};

const reviewContractLiquid = async (id: string) => {
  const url = contractsEndpoints.reviewContractLiquid(id);

  return await axios.patch(url);
};

const consentContractLiquid = async (id: string, data: IContractLiquidConsent) => {
  const url = contractsEndpoints.consentContractLiquid(id);

  return await axios.patch(url, data);
};

const refuseContractLiquid = async (id: string, data: IContractLiquidRefuse) => {
  const url = contractsEndpoints.refuseContractLiquid(id);

  return await axios.patch(url, data);
};

const getContractsDeleted = async (filter?: IContractParams) => {
  try {
    const url = contractsEndpoints.getContractsDeleted;
    const response = await axios.get<PaginationResponse<IContract>>(url, {
      params: {
        ...filter,
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.contractType && { contractType: filter.contractType }),
        ...(filter?.contractStatusList && { contractStatusList: filter.contractStatusList }),
        ...(filter?.proteinPercent && { proteinPercent: filter.proteinPercent }),
        ...(filter?.contractValidFrom && { contractValidFrom: filter.contractValidFrom }),
        ...(filter?.contractValidTo && { contractValidTo: filter.contractValidTo }),
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

const getContractMaterials = async () => {
  try {
    const url = contractsEndpoints.getMaterials;
    const response = await axios.get<PaginationResponse<IContractMaterial>>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getContractProducts = async () => {
  try {
    const url = contractsEndpoints.getProducts;
    const response = await axios.get<PaginationResponse<IContractProduct>>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getFilesContract = async () => {
  try {
    const url = contractsEndpoints.getFilesContract;
    const response = await axios.get<string[]>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getContracts,
  postContract,
  patchContract,
  deleteContract,
  getContractById,
  recoverContract,
  approvedReviewContract,
  approveContract,
  reviewContractLiquid,
  consentContractLiquid,
  refuseContractLiquid,
  getContractsDeleted,
  getContractMaterials,
  getContractProducts,
  getFilesContract,
  patchContractMaskFinished,
  patchContractRequestLiquidationReview,
  patchContractReview,
  patchContractProposeApprove,
  patchContractNormalReview
};
