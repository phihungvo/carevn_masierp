import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import { Typography } from 'app/components/typography/typography';
import { useInventoriesStorageItems } from 'app/hooks/use-inventories';
import useItems from 'app/hooks/use-items';
import useUom from 'app/hooks/use-uom';
import useWorkspace from 'app/hooks/use-workspace';
import { ItemType } from 'app/shared/model/enumerations/item-category.model';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useEffect, useState } from 'react';
import { useFieldArray, useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';
import { ItemsMaterialTable } from './ItemsMaterialTable';
import { ItemTable } from './ItemsTable';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import { convertCurrency } from 'app/shared/util/format';

const { useGetItemsQuery } = useItems;
const { useGetUoms } = useUom;
const { useGetWorkspacesQuery } = useWorkspace;
const { useGetOrderByIdQuery } = useOrders;

export const InventoriesExportItemsTable = () => {
  const [searchParams] = useSearchParams();
  const warehouseExportType = searchParams.get('warehouse');

  const { mutate: getInventoriesItems } = useInventoriesStorageItems();

  const [itemType, setItemType] = useState<ItemType>(ItemType.ITEM);

  const { control, watch, formState, setValue } =
    useFormContext<InventoriesExportSchema>();
  const orderIdWatch = watch('orderId');

  const { append, remove } = useFieldArray<InventoriesExportSchema>({
    control: control,
    name: 'inventoriesItemDetails',
  });

  const { append: appendMaterial, remove: removeMaterial } =
    useFieldArray<InventoriesExportSchema>({
      control: control,
      name: 'inventoriesMaterialDetails',
    });

  const { data: uoms } = useGetUoms();
  const { data: workspaces } = useGetWorkspacesQuery();
  const { data: items } = useGetItemsQuery({
    'itemType.contains': ItemType.ITEM,
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: itemsMaterial } = useGetItemsQuery({
    'itemType.contains': ItemType.MATERIAL,
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: orderDetail } = useGetOrderByIdQuery(orderIdWatch);

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  const inventoriesItemDetailsW = watch('inventoriesItemDetails');
  const inventoriesMaterialDetailsW = watch('inventoriesMaterialDetails');

  const getInventoryItems = () => {
    const listItemIds = inventoriesItemDetailsW
      ?.map(x => x.itemId)
      ?.filter(x => x);
    if (listItemIds?.length)
      getInventoriesItems({
        documentIds: [...listItemIds],
        warehouseId: watch('incomingWarehouseId'),
      });
  };

  useEffect(() => {
    getInventoryItems();
  }, [inventoriesItemDetailsW]);

  useEffect(() => {
    if (orderDetail) {
      if (orderDetail?.contract?.contractMaterialDTOS?.length) {
        const itemsDetail = orderDetail?.contract?.contractMaterialDTOS.map(
          x => {
            const itemSelected = items?.data?.find(i => i.id === x.itemId);
            return {
              id: '',
              code: '',
              itemId: x.itemId,
              quantity: `${x.quantity}`,
              price: `${x.price}`,
              note: '',
              uomId: `${itemSelected?.uomId}`,
              totalPrice: convertCurrency(
                Number(x.price ?? 0) * Number(x.quantity ?? 0),
              ),
              vatId: '',
              vatRate: '',
              vatAmount: '',
            };
          },
        );
        setValue('inventoriesItemDetails', itemsDetail);
      }
    }
  }, [orderDetail]);

  return (
    <Flex direction="column" gap={16}>
      <Flex align="center" gap={16}>
        <Typography level={5} style={{ marginBottom: 0 }}>
          Danh sách hàng hoá
        </Typography>
        <ButtonAdd
          onClick={() => {
            if (itemType === ItemType.ITEM) append({ quantity: '0' });
            else appendMaterial({ quantity: '0' });
          }}
          text="Thêm"
          disabled={disabled}
        />
        {itemType === ItemType.ITEM && (
          <ButtonV2
            variant="fill"
            color="blue"
            disabled={disabled}
            onClick={() => getInventoryItems()}
          >
            Lấy tồn
          </ButtonV2>
        )}
      </Flex>
      {warehouseExportType ===
        (InventoriesWarehouse.WAREHOUSE_DEPRECIATION_EXPORT as string) && (
        <Flex align="center" gap={16}>
          <ButtonV2
            variant={itemType === ItemType.ITEM ? 'primary' : 'outline'}
            onClick={() => setItemType(ItemType.ITEM)}
            disabled={
              itemType === ItemType.MATERIAL &&
              inventoriesMaterialDetailsW?.length > 0
            }
          >
            Hàng hóa
          </ButtonV2>
          <ButtonV2
            variant={itemType !== ItemType.ITEM ? 'primary' : 'outline'}
            onClick={() => setItemType(ItemType.MATERIAL)}
            disabled={
              itemType === ItemType.ITEM && inventoriesItemDetailsW?.length > 0
            }
          >
            TS - CCDC
          </ButtonV2>
        </Flex>
      )}

      {itemType === ItemType.ITEM && (
        <ItemTable uoms={uoms?.data} items={items?.data} remove={remove} />
      )}

      {itemType === ItemType.MATERIAL && (
        <ItemsMaterialTable
          workspaces={workspaces?.data}
          items={itemsMaterial?.data}
          remove={removeMaterial}
        />
      )}

      {(formState.errors?.inventoriesItemDetails?.message ||
        formState.errors?.inventoriesItemDetails?.root) && (
        <FormError
          message={
            formState.errors?.inventoriesItemDetails?.message ||
            formState.errors?.inventoriesItemDetails?.root?.message
          }
        />
      )}
    </Flex>
  );
};
