import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import inventoryService from 'app/services/inventory.service';
import { IInventoryMaterialParams } from 'app/shared/model/inventory.model';

const { INVENTORY_MATERIALS } = QUERY_KEY;

const useInventoryMaterials = (filter?: IInventoryMaterialParams) => {
  return useQuery({
    queryKey: [INVENTORY_MATERIALS, filter],
    queryFn: () => inventoryService.getInventoryMaterials(filter),
    select: data => data.data,
    // enabled: !!filter?.warehouseIds?.length,
  });
};

export default {
  useInventoryMaterials,
};
