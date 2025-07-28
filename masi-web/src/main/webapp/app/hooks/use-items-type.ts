import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import itemTypesService from 'app/services/itemTypes.service';
import { IItemTypeParams } from 'app/shared/model/item-type.model';

const { ITEM_TYPE } = QUERY_KEY;

const useGetItemsTypeQuery = (filter?: IItemTypeParams) => {
  return useQuery({
    queryKey: [ITEM_TYPE, filter],
    queryFn: () => itemTypesService.getItemTypes(filter),
    select: data => data.data,
  });
};

export default {
  useGetItemsTypeQuery,
};
