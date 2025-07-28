import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import { formatAuthoritiesList } from 'app/modules/authorities-group/utils/format-data';
import authoritiesService from 'app/services/authorities.service';
import { IAuthorityParams } from 'app/shared/model/authority.model';

const { AUTHORITIES } = QUERY_KEY;

export const useAuthoritiesConverted = (filter?: IAuthorityParams) => {
  return useQuery({
    queryKey: [AUTHORITIES, filter?.page, filter?.size, filter?.search],
    queryFn: () => authoritiesService.getAuthorities(filter),
    select: data => data?.data,
  });
};

export const useAuthorities = (filter?: IAuthorityParams) => {
  return useQuery({
    queryKey: [AUTHORITIES, filter?.page, filter?.size, filter?.search],
    queryFn: () => authoritiesService.getAuthorities(filter),
    select: data => data.data,
  });
};
