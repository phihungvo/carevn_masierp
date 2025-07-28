import reportsService from 'app/services/reports.service';
import {
  IEmployeeExpiringContractParams,
  IHrChangeReportParams,
  ILeaveRegimeReportParams,
  IRecruitmentReportParams,
  IUniformChangeReportParams,
  IUniformExpiringParams,
  IUniformImportParams,
  IUniformSupportParams,
} from 'app/shared/model/report.model';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import { useState } from 'react';

const {
  REPORTS_UNIFORM_EXPIRING,
  REPORTS_UNIFORM_IMPORT,
  REPORTS_UNIFORM_EXPORT,
  REPORTS_UNIFORM_INVENTORY,
  REPORTS_UNIFORM_SUPPORT,
  REPORTS_EMPLOYEE_EXPIRING_CONTRACT,
  REPORTS_LEAVE_REGIME,
  REPORTS_RECRUITMENT,
  REPORTS_LEAVE_REGIME_EXCEL,
  REPORTS_RECRUITMENT_EXCEL,
  REPORTS_EMPLOYEE_EXPIRING_CONTRACT_EXCEL,
  REPORTS_HR_CHANGE,
  REPORTS_UNIFORM_ALL,
  REPORTS_UNIFORM_CHANGE,
  REPORTS_UNIFORM_EXPORT_EXCEL
} = QUERY_KEY;

const useGetUniformExpiringReports = (filter: IUniformExpiringParams) => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_EXPIRING, filter?.page, filter?.size, filter?.type],
    queryFn: () => reportsService.getUniformExpiringReports(filter),
    select: data => data.data,
  });
};

const useGetUniformImportReport = (filter: IUniformImportParams, type: 'IMPORT' | 'EXPORT' | 'INVENTORY') => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_IMPORT, filter.page, filter.size, filter?.fromDate, filter?.toDate, type],
    queryFn: () => reportsService.getUniformImportReports(filter),
    select: data => data.data,
    enabled: type === 'IMPORT',
  });
};

const useGetUniformExportReport = (filter: IUniformImportParams, type: 'IMPORT' | 'EXPORT' | 'INVENTORY') => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_EXPORT, filter.page, filter.size, filter?.fromDate, filter?.toDate, filter?.type, type],
    queryFn: () => reportsService.getUniformExportReports(filter),
    select: data => data.data,
    enabled: type === 'EXPORT',
  });
};

const useGetUniformExportReportExcel = (fromDate: string, toDate: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [REPORTS_UNIFORM_EXPORT_EXCEL, fromDate, toDate],
    queryFn: () => reportsService.getUniformChangeReportsExcel(fromDate, toDate),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [REPORTS_UNIFORM_EXPORT_EXCEL, fromDate, toDate],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetUniformInventoryReport = (filter: IUniformImportParams, type: 'IMPORT' | 'EXPORT' | 'INVENTORY') => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_INVENTORY, filter.page, filter.size, type],
    queryFn: () => reportsService.getUniformInventoryReports(filter),
    select: data => data.data,
    enabled: type === 'INVENTORY',
  });
};

const useGetUniformSupportReport = (filter: IUniformSupportParams) => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_SUPPORT, filter.page, filter.size, filter?.fromDate, filter?.toDate],
    queryFn: () => reportsService.getUniformSupportReports(filter),
    select: data => data.data,
  });
};

const useGetEmployeeExpiringContractReports = (filter: IEmployeeExpiringContractParams) => {
  return useQuery({
    queryKey: [REPORTS_EMPLOYEE_EXPIRING_CONTRACT, filter.page, filter.size],
    queryFn: () => reportsService.getEmployeeExpiringContractReports(filter),
    select: data => data.data,
  });
};

const useEmployeeExpiringContractReportsExcel = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [REPORTS_EMPLOYEE_EXPIRING_CONTRACT_EXCEL],
    queryFn: () => reportsService.getEmployeeExpiringContractReportsExcel(),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [REPORTS_EMPLOYEE_EXPIRING_CONTRACT_EXCEL],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useGetLeaveRegimeReports = (filter: ILeaveRegimeReportParams) => {
  return useQuery({
    queryKey: [REPORTS_LEAVE_REGIME, filter?.fromDate, filter?.toDate],
    queryFn: () => reportsService.getLeaveRegimeReports(filter),
    select: data => data.data,
  });
};

const useGetLeaveRegimeReportsExcel = (fromDate?: string, toDate?: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [REPORTS_LEAVE_REGIME_EXCEL, fromDate, toDate],
    queryFn: () => reportsService.getLeaveRegimeReportsExcel(fromDate, toDate),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [REPORTS_LEAVE_REGIME_EXCEL],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useGetRecruitmentReports = (filter: IRecruitmentReportParams) => {
  return useQuery({
    queryKey: [REPORTS_RECRUITMENT, filter?.page, filter?.size, filter?.fromDate, filter?.toDate],
    queryFn: () => reportsService.getRecruitmentReports(filter),
    select: data => data.data,
  });
};

const useGetRecruitmentReportsExcel = (fromDate?: string, toDate?: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [REPORTS_RECRUITMENT_EXCEL, fromDate, toDate],
    queryFn: () => reportsService.getRecruitmentReportsExcel(fromDate, toDate),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [REPORTS_RECRUITMENT_EXCEL],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useGetHrChangeReports = (filter: IHrChangeReportParams) => {
  return useQuery({
    queryKey: [REPORTS_HR_CHANGE, filter?.page, filter?.size, filter?.fromDate, filter?.toDate],
    queryFn: () => reportsService.getHrChangeReports(filter),
    select: data => data.data,
  });
};

const useGetUniformAllReports = (filter: IUniformImportParams) => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_ALL, filter?.page, filter?.size, filter?.fromDate, filter?.toDate],
    queryFn: () => reportsService.getAllUniformReports(filter),
    select: data => data.data,
  });
};

const useGetUniformChangeReports = (id: string, filter: IUniformChangeReportParams) => {
  return useQuery({
    queryKey: [REPORTS_UNIFORM_CHANGE, id, filter?.page, filter?.size, filter?.fromDate, filter?.toDate, filter?.type],
    queryFn: () => reportsService.getUniformChangeReports(id, filter),
    select: data => data.data,
    enabled: !!id && !!filter?.type,
  });
};

export default {
  useGetUniformExpiringReports,
  useGetUniformImportReport,
  useGetUniformExportReport,
  useGetUniformInventoryReport,
  useGetUniformSupportReport,
  useGetEmployeeExpiringContractReports,
  useEmployeeExpiringContractReportsExcel,
  useGetLeaveRegimeReports,
  useGetLeaveRegimeReportsExcel,
  useGetRecruitmentReports,
  useGetRecruitmentReportsExcel,
  useGetHrChangeReports,
  useGetUniformAllReports,
  useGetUniformChangeReports,
  useGetUniformExportReportExcel
};
