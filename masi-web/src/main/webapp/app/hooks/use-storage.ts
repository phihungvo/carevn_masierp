import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import storageService from 'app/services/storage.service';

const { STORAGES } = QUERY_KEY;

const useStorages = (filter?: any) => {
  return useQuery({
    queryKey: [STORAGES, filter?.page, filter?.size],
    queryFn: () => storageService.getStorages(filter),
  });
};

export default {
  useStorages,
};
