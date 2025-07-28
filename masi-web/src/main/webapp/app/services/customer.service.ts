import { customerEndpoints } from 'app/constants/endpoints';
import { ICustomer, ICustomerParams } from 'app/shared/model/customer.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getEnabledCustomers = async (filter?: ICustomerParams) => {
  try {
    const url = customerEndpoints.getEnabledCustomers;
    const response = await axios.get<PaginationResponse<ICustomer>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.contractFrom && { contractFrom: filter.contractFrom }),
        ...(filter?.contractTo && { contractTo: filter.contractTo }),
        ...(filter?.customerStatus && { customerStatus: filter.customerStatus }),
        ...(filter?.listEmployeeOwner && { listEmployeeOwner: filter.listEmployeeOwner }),
        ...(filter?.birthdayFrom && { birthdayFrom: filter.birthdayFrom }),
        ...(filter?.birthdayTo && { birthdayTo: filter.birthdayTo }),
        ...(filter?.sort && { sort: filter.sort }),
        ...(filter?.customerId && { customerId: filter.customerId }),
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

const getDisabledCustomers = async (filter?: ICustomerParams) => {
  try {
    const url = customerEndpoints.getDisabledCustomers;
    const response = await axios.get<PaginationResponse<ICustomer>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.contractFrom && { contractFrom: filter.contractFrom }),
        ...(filter?.contractTo && { contractTo: filter.contractTo }),
        ...(filter?.customerStatus && { customerStatus: filter.customerStatus }),
        ...(filter?.listEmployeeOwner && { listEmployeeOwner: filter.listEmployeeOwner }),
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

const getNextCustomerCode = async () => {
  try {
    const url = customerEndpoints.getNextCustomerCode;
    const response = await axios.get<{ nextCustomerCode: string }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postCustomer = async (data: ICustomer) => {
  const url = customerEndpoints.postCustomer;

  return await axios.post(url, data);
};

const patchCustomer = async (data: ICustomer, id: string) => {
  const url = customerEndpoints.patchCustomer(id);

  return await axios.patch(url, data);
};

const getCustomerById = async (id: string) => {
  try {
    const url = customerEndpoints.getCustomerById(id);

    const response = await axios.get<ICustomer>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const disableCustomer = async (id: string) => {
  const url = customerEndpoints.deleteCustomerDisable(id);

  return await axios.delete(url);
};

const activateCustomer = async (id: string) => {
  const url = customerEndpoints.patchCustomerEnable(id);

  return await axios.patch(url);
};

const transferCustomer = async (id: string, newOwnerId: string) => {
  const url = customerEndpoints.patchCustomerTransfer(id, newOwnerId);

  return await axios.patch(url);
};

const deleteCustomer = async (id: string) => {
  const url = customerEndpoints.deleteCustomer(id);

  return await axios.delete(url);
};

const getCustomerByCode = async (code: string) => {
  try {
    const url = customerEndpoints.getCustomerByCode(code);

    const response = await axios.get<ICustomer>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCustomerEnabledExport = async () => {
  try {
    const url = customerEndpoints.exportCustomersEnabled;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCustomerDisabledExport = async () => {
  try {
    const url = customerEndpoints.exportCustomersDisabled;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCustomerByTaxCode = async (taxCode: string) => {
  try {
    const url = customerEndpoints.getCustomerByTaxCode(taxCode);

    const response = await axios.get<ICustomer>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCustomerBirthday = async (fromDate: string, toDate: string) => {
  try {
    const url = customerEndpoints.getCustomerBirthday;
    const response = await axios.get<PaginationResponse<ICustomer>>(url, {
      params: {
        fromDate,
        toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getEnabledCustomers,
  getDisabledCustomers,
  postCustomer,
  patchCustomer,
  getCustomerById,
  disableCustomer,
  transferCustomer,
  activateCustomer,
  deleteCustomer,
  getCustomerByCode,
  getCustomerEnabledExport,
  getCustomerDisabledExport,
  getNextCustomerCode,
  getCustomerByTaxCode,
  getCustomerBirthday,
};
