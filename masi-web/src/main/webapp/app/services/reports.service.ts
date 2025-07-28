import { reportsEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import {
  IEmployeeExpiringContract,
  IEmployeeExpiringContractParams,
  IHrChangeReport,
  IHrChangeReportParams,
  ILeaveRegimeReport,
  ILeaveRegimeReportParams,
  IRecruitmentReportParams,
  IReportRecruitment,
  IUniformChangeReport,
  IUniformChangeReportParams,
  IUniformExpiring,
  IUniformExpiringParams,
  IUniformImport,
  IUniformImportParams,
  IUniformSupport,
  IUniformSupportParams,
} from 'app/shared/model/report.model';
import axios from 'axios';

const getUniformExpiringReports = async (filter: IUniformExpiringParams) => {
  try {
    const url = reportsEndpoints.getUniformExpiringReports;
    const response = await axios.get<PaginationResponse<IUniformExpiring>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        type: filter?.type,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformImportReports = async (filter: IUniformImportParams) => {
  try {
    const url = reportsEndpoints.getUniformImportReports;
    const response = await axios.get<PaginationResponse<IUniformImport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformExportReports = async (filter: IUniformImportParams) => {
  try {
    const url = reportsEndpoints.getUniformExportReports;
    const response = await axios.get<PaginationResponse<IUniformImport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        type: filter?.type,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformInventoryReports = async (filter: IUniformImportParams) => {
  try {
    const url = reportsEndpoints.getUniformInventoryReports;
    const response = await axios.get<PaginationResponse<IUniformImport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getAllUniformReports = async (filter: IUniformSupportParams) => {
  try {
    const url = reportsEndpoints.getAllUniformReports;
    const response = await axios.get<PaginationResponse<IUniformSupport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformChangeReports = async (id: string, filter: IUniformChangeReportParams) => {
  try {
    const url = reportsEndpoints.getUniformChangeReports(id);
    const response = await axios.get<PaginationResponse<IUniformChangeReport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
        type: filter?.type,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getUniformChangeReportsExcel = async (fromDate: string, toDate: string) => {
  try {
    const url = reportsEndpoints.getUniformChangeReportsExcel;
    const response = await axios.get<string>(url, {
      params: {
        fromDate,
        toDate,
      },
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

const getUniformSupportReports = async (filter: IUniformSupportParams) => {
  try {
    const url = reportsEndpoints.getUniformSupportReports;
    const response = await axios.get<PaginationResponse<IUniformSupport>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeExpiringContractReports = async (filter: IEmployeeExpiringContractParams) => {
  try {
    const url = reportsEndpoints.getEmployeeExpiringContractReports;
    const response = await axios.get<PaginationResponse<IEmployeeExpiringContract>>(url, {
      params: {
        page: filter.page,
        size: filter.size,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeExpiringContractReportsExcel = async (fromDate?: string, toDate?: string) => {
  try {
    const url = reportsEndpoints.getEmployeeExpiringContractReportsExcel;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
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

const getLeaveRegimeReports = async (filter: ILeaveRegimeReportParams) => {
  try {
    const url = reportsEndpoints.getLeaveRegimeReports;
    const response = await axios.get<ILeaveRegimeReport[]>(url, {
      params: {
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getLeaveRegimeReportsExcel = async (fromDate?: string, toDate?: string) => {
  try {
    const url = reportsEndpoints.getLeaveRegimeReportsExcel;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
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

const getRecruitmentReports = async (filter: IRecruitmentReportParams) => {
  try {
    const url = reportsEndpoints.getRecruitmentReports;
    const response = await axios.get<PaginationResponse<IReportRecruitment>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getRecruitmentReportsExcel = async (fromDate?: string, toDate?: string) => {
  try {
    const url = reportsEndpoints.getRecruitmentReportsExcel;
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
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

const getHrChangeReports = async (filter: IHrChangeReportParams) => {
  try {
    const url = reportsEndpoints.getHumanResourceChangeReports;
    const response = await axios.get<PaginationResponse<IHrChangeReport>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        fromDate: filter?.fromDate,
        toDate: filter?.toDate,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getUniformExpiringReports,
  getUniformImportReports,
  getUniformExportReports,
  getUniformInventoryReports,
  getUniformSupportReports,
  getAllUniformReports,
  getEmployeeExpiringContractReports,
  getEmployeeExpiringContractReportsExcel,
  getLeaveRegimeReports,
  getLeaveRegimeReportsExcel,
  getRecruitmentReports,
  getRecruitmentReportsExcel,
  getHrChangeReports,
  getUniformChangeReports,
  getUniformChangeReportsExcel
};
