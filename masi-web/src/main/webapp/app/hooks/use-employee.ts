import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import employeeService from 'app/services/employee.service';
import {
  IConfirmLeave,
  IEmployee,
  IEmployeeChangeLogParams,
  IEmployeeParams,
  IEmployeeSequenceIdParams,
  IProfileAttachment,
} from 'app/shared/model/employee.model';
import { useState } from 'react';
import { IEmployeeProfiles } from './../shared/model/employee.model';
import { AxiosResponse } from 'axios';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { useAppSelector } from 'app/config/store';

const {
  EMPLOYEE,
  EMPLOYEE_PROFILES,
  EMPLOYEE_PROFILE,
  EMPLOYEE_SEQUENCE_ID,
  EMPLOYEE_TAX_CODE,
  EMPLOYEE_BANK_NUMBER,
  EMPLOYEE_CITIZEN_ID,
  EMPLOYEE_EXPORT,
  EMPLOYEE_EXPORT_CONTRACT,
  EMPLOYEE_CHANGE_LOGS,
  EMPLOYEE_CHANGE_LOG,
  EMPLOYEE_LIST_DEPARTMENTS,
  EMPLOYEE_XLSX_TEMPLATE,
  USER_DEFAULT
} = QUERY_KEY;

const { CREATE_EMPLOYEE_PROFILES, UPDATE_EMPLOYEE_PROFILES, ENABLE_EMPLOYEE_PROFILES, DISABLE_EMPLOYEE_PROFILES, PATCH_CONFIRM_LEAVE } =
  MUTATION_KEY;

// Lấy danh sách nhân viên
const useGetEmployeesQuery = (
  filter?: IEmployeeParams,
  select?: (data: AxiosResponse<PaginationResponse<IEmployee>, any>) => PaginationResponse<IEmployee>
) => {
  return useQuery({
    queryKey: [EMPLOYEE],
    queryFn: () => employeeService.getEmployees(filter),
    select: data => {
      if (select) return select(data);
      return data?.data;
    },
  });
};

const useGetEmployeeProfilesQuery = (filter?: IEmployeeParams) => {
  return useQuery({
    queryKey: [EMPLOYEE_PROFILES,filter],
    queryFn: () => employeeService.getEmployeeProfiles(filter),
    select: data => data?.data,
  });
};

const useGetEmployeeProfileByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_PROFILE, id],
    queryFn: () => employeeService.getEmployeeProfileById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useGetProfileDefault = () => {
  const account = useAppSelector(state => state.authentication.account);
  const id = account?.employeeId;
  return useQuery({
    queryKey: [USER_DEFAULT],
    queryFn: () => employeeService.getEmployeeProfileById(id),
    select: data => data?.data,
    enabled: !!id,
    staleTime: Infinity
  });
};

const usePostEmployeeProfileMutation = (toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_EMPLOYEE_PROFILES],
    mutationFn: employeeService.postEmployeeProfile,
    onSuccess() {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
    },
  });
};

const usePatchEmployeeProfileMutation = (id: string, toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_EMPLOYEE_PROFILES, id],
    mutationFn: (data: IEmployeeProfiles) => employeeService.patchEmployeeProfile(id, data),
    onSuccess() {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILE, id],
      });
    },
  });
};

const usePatchEnableTimeKeepingDevice = (id: string, toggle?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_EMPLOYEE_PROFILES, id],
    mutationFn: () => employeeService.patchEnableTimeKeepingDevice(id),
    onSuccess() {
      toggle && toggle();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
    },
  });
};

const useGetEmployeeSequenceIdQuery = (filter: IEmployeeSequenceIdParams, enabled: boolean = true) => {
  return useQuery({
    queryKey: [EMPLOYEE_SEQUENCE_ID, filter?.gender],
    queryFn: () => employeeService.getEmployeeSequenceId(filter),
    select: data => data?.data,
    enabled: !!filter?.gender && enabled,
  });
};

const useGetEmployeeByTaxCodeQuery = (taxCode: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_TAX_CODE, taxCode],
    queryFn: () => employeeService.getEmployeeByTaxCode(taxCode),
    enabled: !!taxCode,
  });
};

const useGetEmployeeByCitizenIdQuery = (citizenId: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_CITIZEN_ID, citizenId],
    queryFn: () => employeeService.getEmployeeByCitizenId(citizenId),
    enabled: !!citizenId,
  });
};

const useGetEmployeeByBankNumberQuery = (bankNumber: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_BANK_NUMBER, bankNumber],
    queryFn: () => employeeService.getEmployeeByBankNumber(bankNumber),
    enabled: !!bankNumber,
  });
};

const usePatchEnableEmployeeProfileMutation = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [ENABLE_EMPLOYEE_PROFILES, id],
    mutationFn: () => employeeService.patchEnableEmployee(id),
    onSuccess() {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILE, id],
      });
    },
  });
};

const usePatchDisableEmployeeProfileMutation = (id: string, toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DISABLE_EMPLOYEE_PROFILES, id],
    mutationFn: () => employeeService.patchDisableEmployee(id),
    onSuccess() {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILE, id],
      });
    },
  });
};

const usePatchProfileAttachments = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: IProfileAttachment[]) => employeeService.patchProfileAttachments(data),
    onSuccess() {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILE],
      });
    },
  });
};

const usePatchConfirmLeaveMutation = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [PATCH_CONFIRM_LEAVE],
    mutationFn: (data: IConfirmLeave) => employeeService.patchConfirmLeave(data),
    onSuccess() {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILE],
      });
    },
  });
};

const useGetEmployeeExportLazyQuery = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EMPLOYEE_EXPORT],
    queryFn: () => employeeService.getEmployeeExport(),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EMPLOYEE_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetEmployeeExportXlsxLazyQuery = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EMPLOYEE_EXPORT],
    queryFn: () => employeeService.getEmployeeExportXlsx(),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EMPLOYEE_EXPORT],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const useGetEmployeeExportContractLazyQuery = (id: string) => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EMPLOYEE_EXPORT_CONTRACT, id],
    queryFn: () => employeeService.getEmployeeExportContract(id),
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EMPLOYEE_EXPORT_CONTRACT, id],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
};

const usePostEmployeeImportMutation = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (fileId: string) => employeeService.postEmployeeImport(fileId),
    onSuccess() {
      toggle && toggle();
      toggleSuccess && toggleSuccess();
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
    },
  });
};

const useGetXlsxTemplate = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [EMPLOYEE_XLSX_TEMPLATE],
    queryFn: () => employeeService.getXlsxTemplate(),
    enabled,
  });

  const trigger = () => setEnabled(true);

  if (query.isSuccess) {
    queryClient.resetQueries({
      queryKey: [EMPLOYEE_XLSX_TEMPLATE],
    });
  }

  if ((query.isSuccess || query.isError) && enabled) setEnabled(false);

  return { trigger, ...query };
};

const useGetEmployeeChangeLog = (filter?: IEmployeeChangeLogParams) => {
  return useQuery({
    queryKey: [EMPLOYEE_CHANGE_LOGS, filter?.page, filter?.size, filter?.employeeId],
    queryFn: () => employeeService.getEmployeeChangeLog(filter),
    select: data => data?.data,
    enabled: !!filter?.employeeId,
  });
};

const useGetEmployeeChangeLogById = (id: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_CHANGE_LOG, id],
    queryFn: () => employeeService.getEmployeeChangeLogById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useGetEmployeeListDepartments = () => {
  return useQuery({
    queryKey: [EMPLOYEE_LIST_DEPARTMENTS],
    queryFn: employeeService.getListDepartments,
    select: data => data?.data,
  });
};

const useGetListProfileByIds = <T = any>(ids: string[], format?: (emps: IEmployeeProfiles[]) => T) => {
  return useQuery({
    queryKey: [EMPLOYEE_PROFILES, ids],
    queryFn: () => employeeService.getListProfileByIds(ids),
    select: data => {
      if (format) return format(data?.data);
      return data;
    },
    enabled: !!ids.length,
  });
}



export default {
  useGetEmployeesQuery,
  useGetEmployeeProfilesQuery,
  useGetEmployeeProfileByIdQuery,
  usePostEmployeeProfileMutation,
  usePatchEmployeeProfileMutation,
  useGetEmployeeSequenceIdQuery,
  useGetEmployeeByTaxCodeQuery,
  useGetEmployeeByCitizenIdQuery,
  useGetEmployeeByBankNumberQuery,
  usePatchEnableEmployeeProfileMutation,
  usePatchDisableEmployeeProfileMutation,
  usePatchProfileAttachments,
  usePatchConfirmLeaveMutation,
  useGetEmployeeExportLazyQuery,
  useGetEmployeeExportXlsxLazyQuery,
  useGetEmployeeExportContractLazyQuery,
  usePostEmployeeImportMutation,
  useGetEmployeeChangeLog,
  useGetEmployeeChangeLogById,
  useGetEmployeeListDepartments,
  useGetXlsxTemplate,
  useGetListProfileByIds,
  usePatchEnableTimeKeepingDevice,
  useGetProfileDefault
};
