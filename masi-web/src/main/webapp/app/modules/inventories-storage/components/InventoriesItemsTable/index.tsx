import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2 from 'app/components/table-v2/Table';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useItems from 'app/hooks/use-items';
import useUom from 'app/hooks/use-uom';
import { ItemType } from 'app/shared/model/enumerations/item-category.model';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { convertCurrency } from 'app/shared/util/format';
import { InventoriesSchema } from 'app/validation/inventories.validation';
import { useFieldArray, useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';

const { useGetItemsQuery } = useItems;
const { useGetUoms } = useUom;

export const InventoriesItemsTable = () => {
  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const { control, watch, formState, setValue } =
    useFormContext<InventoriesSchema>();

  const inventoriesDetails = watch('inventoriesDetails');

  const { append, remove } = useFieldArray<InventoriesSchema>({
    control: control,
    name: 'inventoriesDetails',
  });

  const { data: uoms } = useGetUoms({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: items } = useGetItemsQuery({
    'itemType.contains': searchParams.get('itemType') ?? ItemType.ITEM,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  const calcTotalAmount = (index: number) => {
    const amount =
      watch(`inventoriesDetails.${index}.quantity`) &&
      watch(`inventoriesDetails.${index}.price`)
        ? Number(watch(`inventoriesDetails.${index}.quantity`)) *
          Number(watch(`inventoriesDetails.${index}.price`))
        : 0;

    return convertCurrency(amount, false);
  };

  const columns = [
    {
      header: {
        render: `Mã ${
          warehouseImportType ===
          (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
            ? 'sản phẩm'
            : 'hàng hóa'
        }`,
      },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesDetails.${index}.itemId`}
            control={control}
            name={`inventoriesDetails.${index}.itemId`}
            placeholder="Chọn"
            options={items?.data?.map(i => ({
              label: `${i?.code} - ${i?.name}`,
              value: i?.id,
            }))}
            disabled={disabled}
            onChanges={e => {
              const itemSelected = (items?.data ?? [])?.find(x => x.id === e);
              if (itemSelected)
                setValue(
                  `inventoriesDetails.${index}.uomId`,
                  `${itemSelected?.uomId ?? 0}`,
                );
            }}
            isClearable={false}
          />
        ),
      },
    },
    {
      header: { render: 'Số lượng' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
            key={`inventoriesDetails.${index}.quantity`}
            control={control}
            name={`inventoriesDetails.${index}.quantity`}
            placeholder="Điền"
            style={{ width: '80px' }}
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: 'Đơn vị' },
      body: {
        render: ({ data, index }) => (
          <FormSelect
            key={`inventoriesDetails.${index}.uomId`}
            control={control}
            name={`inventoriesDetails.${index}.uomId`}
            placeholder="Chọn"
            options={(uoms?.data ?? [])?.map(u => ({ label: u?.name, value: u?.id }))}
            disabled
          />
        ),
      },
    },
    {
      header: { render: 'Đơn giá' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
            key={`inventoriesDetails.${index}.price`}
            control={control}
            name={`inventoriesDetails.${index}.price`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '150px' }}
          />
        ),
      },
    },
    {
      header: { render: 'Thành tiền (VND)' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesDetails.${index}.totalPrice`}
            control={control}
            name={`inventoriesDetails.${index}.totalPrice`}
            disabled
            value={calcTotalAmount(index)}
          />
        ),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesDetails.${index}.note`}
            control={control}
            name={`inventoriesDetails.${index}.note`}
            placeholder="Điền"
            disabled={disabled}
          />
        ),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ index }) => (
          <ButtonDelete onClick={() => remove(index)} disabled={disabled} />
        ),
      },
    },
  ];

  return (
    <Flex direction="column" gap={16}>
      <Flex align="center" gap={16}>
        <Typography level={5} style={{ marginBottom: 0 }}>
          Danh sách hàng hoá
        </Typography>
        <ButtonAdd
          onClick={() => append({ quantity: '0' })}
          text="Thêm"
          disabled={disabled}
        />
      </Flex>
      <TableV2
        table_id="incoming_invoice"
        columns={columns}
        data={inventoriesDetails || []}
        custom_body_row={() => (
          <tr>
            <td>Tổng</td>
            <td>
              {inventoriesDetails?.reduce(
                (acc, item) => (acc += Number(item?.quantity)),
                0,
              )}
            </td>
            <td colSpan={2} />
            <td>
              {convertCurrency(
                inventoriesDetails?.reduce(
                  (acc, item) =>
                    (acc +=
                      Number(item?.price || 0) * Number(item?.quantity || 0)),
                  0,
                ),
              )}
            </td>
            <td colSpan={2} />
          </tr>
        )}
      />
      {(formState.errors?.inventoriesDetails?.message ||
        formState.errors?.inventoriesDetails?.root) && (
        <FormError
          message={
            formState.errors?.inventoriesDetails?.message ||
            formState.errors?.inventoriesDetails?.root?.message
          }
        />
      )}
    </Flex>
  );
};
