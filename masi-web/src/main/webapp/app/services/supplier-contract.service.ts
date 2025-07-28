import { supplierContractEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  ISupplierContract,
  ISupplierContractFilterParams,
} from 'app/shared/model/supplier-contract.model';
import axios from 'axios';

export const getSupplierContracts = async (
  filter: ISupplierContractFilterParams,
) => {
  try {
    const url = supplierContractEndpoints.getSupplierContracts;

    const response = await axios.get<PaginationResponse<ISupplierContract>>(
      url,
      { params: { ...filter } },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const getSupplierContractsXlsx = async (
  filter: ISupplierContractFilterParams,
) => {
  try {
    const url = supplierContractEndpoints.exportSupplierContract;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
      params: { page: filter?.page, size: filter?.size },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const postSupplierContracts = async (
  data: Partial<ISupplierContract>,
) => {
  const url = supplierContractEndpoints.postSupplierContract;

  return await axios.post<ISupplierContract>(url, data);
};

export const patchSupplierContracts = async (
  id: string,
  data: Partial<ISupplierContract>,
) => {
  const url = supplierContractEndpoints.patchSupplierContract(id);

  return await axios.patch<ISupplierContract>(url, data);
};

export const getSupplierContractsById = async (id: string) => {
  try {
    const url = supplierContractEndpoints.getSupplierContractById(id);
    const response = await axios.get<ISupplierContract>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const deleteSupplierContracts = async (id: string) => {
  const url = supplierContractEndpoints.deleteSupplierContract(id);

  return await axios.patch(url);
};

export const reviewSupplierContracts = async (id: string) => {
  const url = supplierContractEndpoints.reqReviewSupplierContract(id);

  return await axios.patch(url);
};

export const confirmSupplierContracts = async (
  id: string,
  data: { assignSign: string; approvedSignName: string },
) => {
  const url = supplierContractEndpoints.approveSupplierContract(id);

  return await axios.patch(url, {
    approvedSign: data?.assignSign,
    approvedSignName: data?.approvedSignName,
    isApproved: true,
  });
};

export const rejectSupplierContracts = async (id: string, note: string) => {
  const url = supplierContractEndpoints.rejectSupplierContract(id);

  return await axios.patch(url, {
    rejectNote: note,
    isApproved: false,
  });
};

export const changeStatusSupplierContracts = async (id: string, status: string) => {
  const url = supplierContractEndpoints.changeStatusSupplierContract(id);
  return await axios.patch(url, {
    status,
  });
};


export const getSupplierContractsGeneratedCode = async () => {
  try {
    const url = supplierContractEndpoints.getSupplierContractNextCode();
    const response = await axios.get<{
      code: string;
      nextAndIncrement: string;
    }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export const reviewLiquidationSupplierContracts = async (id: string) => {
  const url =
    supplierContractEndpoints.reqReviewLiquidationSupplierContract(id);

  return await axios.patch(url);
};

export const confirmLiquidationSupplierContracts = async (
  id: string,
  data: { assignSign: string; approvedSignName: string },
) => {
  const url = supplierContractEndpoints.approveLiquidationSupplierContract(id);

  return await axios.patch(url, {
    approvedSign: data?.assignSign,
    approvedSignName: data?.approvedSignName,
    isApproved: true,
  });
};

export const rejectLiquidationSupplierContracts = async (
  id: string,
  note: string,
) => {
  const url = supplierContractEndpoints.rejectLiquidationSupplierContract(id);

  return await axios.patch(url, {
    rejectNote: note,
    isApproved: false,
  });
};
