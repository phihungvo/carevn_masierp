import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { employeeEndpoints } from 'app/constants/endpoints';
import {
  IConfirmLeave,
  IEmployee,
  IEmployeeChangeLog,
  IEmployeeChangeLogParams,
  IEmployeeParams,
  IEmployeeProfiles,
  IEmployeeSequenceId,
  IEmployeeSequenceIdParams,
  IListDepartment,
  IProfileAttachment,
} from 'app/shared/model/employee.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

// Lấy danh sách nhân viên
const getEmployees = async (filter: IEmployeeParams) => {
  try {
    const url = employeeEndpoints.getEmployees;
    const response = await axios.get<PaginationResponse<IEmployee>>(url, {
      params: {
        page: filter?.page ?? DEFAULT_PAGE,
        size: filter?.size ?? DEFAULT_PAGE_SIZE_NAX,
      }
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeProfiles = async (filter: IEmployeeParams) => {
  try {
    const url = employeeEndpoints.getEmployeeProfiles;
    const response = await axios.get<PaginationResponse<IEmployeeProfiles>>(url, {
      params: {
        page: filter?.page ?? DEFAULT_PAGE,
        size: filter?.size ?? DEFAULT_PAGE_SIZE_NAX,
        search: filter?.search,
        ...(filter?.profileStates?.length && { profileStates: filter.profileStates }),
        ...(filter?.workspaceIds?.length && { workspaceIds: filter.workspaceIds }),
        workspaceTypes: filter?.workspaceTypes,
        employeeStatuses: filter?.employeeStatuses,
        hasAccount: filter?.hasAccount,
        isFilterCompany: filter?.isFilterCompany,
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

const getEmployeeProfileById = async (id: string) => {
  try {
    const url = employeeEndpoints.getEmployeeById(id);
    const response = await axios.get<IEmployeeProfiles>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postEmployeeProfile = async (data: IEmployeeProfiles) => {
  const url = employeeEndpoints.postEmployee;
  return await axios.post<IEmployeeProfiles>(url, data);
};

const patchEmployeeProfile = async (id: string, data: IEmployeeProfiles) => {
  const url = employeeEndpoints.patchEmployee(id);
  return await axios.patch<IEmployeeProfiles>(url, data);
};

const patchEnableTimeKeepingDevice = async (id: string) => {
  const url = employeeEndpoints.patchEnableTimeKeepingDevice(id);
  return await axios.patch<IEmployeeProfiles>(url);
};

const getEmployeeSequenceId = async (filter: IEmployeeSequenceIdParams) => {
  try {
    const url = employeeEndpoints.getEmployeeSequenceId;
    const response = await axios.get<IEmployeeSequenceId[]>(url, {
      params: {
        gender: filter.gender,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeByTaxCode = async (taxId: string) => {
  try {
    const url = employeeEndpoints.getEmployeeByTaxCode(taxId);
    const response = await axios.get<IEmployeeProfiles>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeByCitizenId = async (idCard: string) => {
  try {
    const url = employeeEndpoints.getEmployeeByCitizenId(idCard);
    const response = await axios.get<IEmployeeProfiles>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeByBankNumber = async (bankCode: string) => {
  try {
    const url = employeeEndpoints.getEmployeeByBankNumber(bankCode);
    const response = await axios.get<IEmployeeProfiles>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const patchEnableEmployee = async (id: string) => {
  const url = employeeEndpoints.enableEmployee(id);
  return await axios.patch<IEmployeeProfiles>(url);
};

const patchDisableEmployee = async (id: string) => {
  const url = employeeEndpoints.disabledEmployee(id);
  return await axios.patch<IEmployeeProfiles>(url);
};

const patchProfileAttachments = async (data: IProfileAttachment[]) => {
  const url = employeeEndpoints.profileAttachments(data?.[0]?.employeeProfileId);
  return await axios.patch(url, data, {
    params: {
      "clear-old": true,
    },
  });
};

const patchConfirmLeave = async (data: IConfirmLeave) => {
  const url = employeeEndpoints.confirmLeave;
  return await axios.post<IConfirmLeave>(url, data);
};

const getEmployeeExport = async () => {
  try {
    const url = employeeEndpoints.exportEmployee;
    const response = await axios.get(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeExportXlsx = async () => {
  try {
    const url = employeeEndpoints.exportEmployeeXlsx;
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

const getEmployeeExportContract = async (id: string) => {
  try {
    const url = employeeEndpoints.exportEmployeeContract(id);
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

const postEmployeeImport = async (fileId: string) => {
  const url = employeeEndpoints.importEmployee(fileId);
  return await axios.post(url);
};

const getEmployeeChangeLog = async (filter?: IEmployeeChangeLogParams) => {
  const url = employeeEndpoints.getEmployeeChangeLog;

  try {
    const response = await axios.get<PaginationResponse<IEmployeeChangeLog>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        employeeId: filter?.employeeId,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeChangeLogById = async (id: string) => {
  const url = employeeEndpoints.getEmployeeChangeLogById(id);

  try {
    const response = await axios.get<IEmployeeChangeLog>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getXlsxTemplate = async () => {
  const url = employeeEndpoints.getXlsxTemplate;

  try {
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

const getListDepartments = async () => {
  const url = employeeEndpoints.getListDepartments;

  try {
    const response = await axios.get<IListDepartment>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};


const getListProfileByIds = async (ids: string[]) => {
  const url = employeeEndpoints.getListProfileByIds;
  try {
    const response = await axios.get<IEmployeeProfiles[]>(url, {
      params: {
        ids: ids,
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
}
const syncAccountStatus = async () => {
  const url = employeeEndpoints.syncAccountStatus;
  try {
    const response = await axios.patch(url);
    return response;
  } catch (error) {
    console.error(error);
  }
}


export default {
  getEmployees,
  getEmployeeProfiles,
  getEmployeeProfileById,
  postEmployeeProfile,
  patchEmployeeProfile,
  getEmployeeSequenceId,
  getEmployeeByTaxCode,
  getEmployeeByCitizenId,
  getEmployeeByBankNumber,
  patchEnableEmployee,
  patchDisableEmployee,
  patchProfileAttachments,
  patchConfirmLeave,
  getEmployeeExport,
  getEmployeeExportXlsx,
  getEmployeeExportContract,
  postEmployeeImport,
  getEmployeeChangeLog,
  getEmployeeChangeLogById,
  getListDepartments,
  getXlsxTemplate,
  getListProfileByIds,
  patchEnableTimeKeepingDevice,
  syncAccountStatus
};
