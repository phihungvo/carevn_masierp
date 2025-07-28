import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import factoryService from 'app/services/factory.service';
import { IFactoryParams } from 'app/shared/model/factory.model';

const { FACTORIES } = QUERY_KEY;

const useFactories = (filter?: IFactoryParams) => {
  return useQuery({
    queryKey: [FACTORIES, filter?.page, filter?.size],
    queryFn: () => factoryService.getFactories(filter),
  });
};

export default {
  useFactories,
};
