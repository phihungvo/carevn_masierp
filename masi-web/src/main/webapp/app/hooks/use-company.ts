import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import companiesService from 'app/services/companies.service';
import {
  ICompanyParams,
  IPatchCompanyDto,
  IPostCompanyDto,
} from 'app/shared/model/company.model';
import { IApiError } from 'app/shared/model/error.model';
import { AxiosError } from 'axios';

const {
  COMPANIES,
  CREATE_COMPANIES,
  UPDATE_COMPANIES,
  ACTIVE_COMPANIES,
  INACTIVE_COMPANIES,
} = QUERY_KEY;

const useGetCompanies = (filter?: ICompanyParams) => {
  return useQuery({
    queryKey: [COMPANIES, filter],
    queryFn: () => companiesService.getCompanies(filter),
    select: data => data.data,
  });
};

const useGetCompanyByIdQuery = (id: string) => {
  return useQuery({
    queryKey: [COMPANIES, id],
    queryFn: () => companiesService.getCompanyById(id),
    select: data => data.data,
    enabled: !!id,
  });
};

const usePostCompanyMutation = (toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_COMPANIES],
    mutationFn: (data: IPostCompanyDto) => companiesService.postCompany(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [COMPANIES] });
      toggleSuccess();
    },
  });
};

const useUpdateCompanyMutation = (id: string, toggleSuccess: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_COMPANIES, id],
    mutationFn: (data: IPatchCompanyDto) =>
      companiesService.patchCompany(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [COMPANIES] });
      toggleSuccess();
    },
  });
};

const useActiveCompanyMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [ACTIVE_COMPANIES],
    mutationFn: (id: string) => companiesService.activeCompany(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [COMPANIES] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useInactiveCompanyMutation = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [INACTIVE_COMPANIES],
    mutationFn: (id: string) => companiesService.inactiveCompany(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [COMPANIES] });
      onOk && onOk();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

export default {
  useGetCompanies,
  useGetCompanyByIdQuery,
  usePostCompanyMutation,
  useUpdateCompanyMutation,
  useActiveCompanyMutation,
  useInactiveCompanyMutation,
};
