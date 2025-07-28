import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2 from 'app/components/table-v2/Table';
import { useGetResultInventoriesStorageItems } from 'app/hooks/use-inventories';
import useVatRate from 'app/hooks/use-vat-rate';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { IItem } from 'app/shared/model/item.model';
import { IUom } from 'app/shared/model/uom.model';
import { convertCurrency } from 'app/shared/util/format';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';

const { useGetVatRates } = useVatRate;

interface ItemTableProps {
  items: IItem[];
  uoms: IUom[];
  remove: (index: number) => void;
}
export const ItemTable = ({ items, uoms, remove }: ItemTableProps) => {
  const { control, watch, setValue } =
    useFormContext<InventoriesExportSchema>();

  const inventoriesItemDetails = watch('inventoriesItemDetails');

  const { data: vatRates } = useGetVatRates();
  // Lấy dữ liệu từ cache

  const inventories = useGetResultInventoriesStorageItems();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const calcVatAmount = (index: number, vatId: string, price, quantity) => {
    const vatSelected = vatRates?.data?.find(x => x.id === vatId);
    if (vatSelected) {
      const vatR = vatSelected?.value ?? 0;
      setValue(`inventoriesItemDetails.${index}.vatRate`, `${vatR ?? 0}`);

      const totalPrice = Number(price ?? 0) * Number(quantity ?? 0);

      const vatAmount = (totalPrice ?? 0) * (Number(vatR ?? 0) / 100);

      setValue(
        `inventoriesItemDetails.${index}.vatAmount`,
        `${vatAmount ?? 0}`,
      );
    }
  };

  const columnsVat = () => {
    if (
      warehouseImportType ===
      (InventoriesWarehouse.WAREHOUSE_COMMERCE_EXPORT as string)
    ) {
      return [
        {
          header: { render: '%VAT' },
          body: {
            render: ({ data, index }) => (
              <FormSelect
                key={`inventoriesItemDetails.${index}.vatId`}
                control={control}
                name={`inventoriesItemDetails.${index}.vatId`}
                placeholder="Chọn"
                options={vatRates?.data?.map(u => ({
                  label: u?.name,
                  value: u?.id,
                }))}
                disabled={disabled}
                onChanges={e => {
                  calcVatAmount(index, e, data?.price, data?.quantity);
                }}
                isClearable={false}
              />
            ),
          },
        },
        {
          header: { render: 'VAT' },
          body: {
            render: ({ data }) => convertCurrency(data?.vatAmount ?? 0, false),
          },
        },
      ];
    }
    return [];
  };

  const columns = [
    {
      header: { render: `Mã hàng hóa` },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesItemDetails.${index}.itemId`}
            control={control}
            name={`inventoriesItemDetails.${index}.itemId`}
            placeholder="Chọn"
            options={[...(items ?? [])]?.map(i => ({
              label: `${i?.code} - ${i?.name}`,
              value: i?.id,
            }))}
            disabled={disabled}
            onChanges={e => {
              const itemSelected = [...(items ?? [])]?.find(x => x.id === e);
              if (itemSelected)
                setValue(
                  `inventoriesItemDetails.${index}.uomId`,
                  `${itemSelected?.uomId ?? 0}`,
                );
            }}
          />
        ),
      },
    },
    {
      header: { render: 'Đơn vị' },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`inventoriesItemDetails.${index}.uomId`}
            control={control}
            name={`inventoriesItemDetails.${index}.uomId`}
            placeholder="Chọn"
            options={uoms?.map(u => ({ label: u?.name, value: u?.id }))}
            disabled
          />
        ),
      },
    },
    {
      header: { render: 'Tồn' },
      body: {
        render: ({ data }) => {
          const qty = inventories?.find(x => x.itemId === data?.itemId);
          if (qty) return convertCurrency(qty?.totalQuantity, false);
          return 0;
        },
      },
    },
    {
      header: { render: 'Số lượng' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
            key={`inventoriesItemDetails.${index}.quantity`}
            control={control}
            name={`inventoriesItemDetails.${index}.quantity`}
            placeholder="Điền"
            style={{ width: '80px' }}
            disabled={disabled}
            onChange={() => {
              calcVatAmount(index, data?.vatId, data?.price, data?.quantity);
            }}
          />
        ),
      },
    },
    {
      header: { render: 'Đơn giá' },
      body: {
        render: ({ data, index }) => (
          <FormInputV2
            key={`inventoriesItemDetails.${index}.price`}
            control={control}
            name={`inventoriesItemDetails.${index}.price`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '150px' }}
            onChange={() => {
              calcVatAmount(index, data?.vatId, data?.price, data?.quantity);
            }}
          />
        ),
      },
    },
    {
      header: { render: 'Thành tiền (VND)' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesItemDetails.${index}.totalPrice`}
            control={control}
            name={`inventoriesItemDetails.${index}.totalPrice`}
            disabled
            value={convertCurrency(calcTotalAmount(index), false)}
            style={{ width: '120px' }}
          />
        ),
      },
    },
    ...columnsVat(),
    {
      header: { render: 'Tổng tiền' },
      body: {
        render: ({ data, index }) =>
          convertCurrency(
            Number(data?.vatAmount ?? 0) + calcTotalAmount(index),
          ),
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ index }) => (
          <FormInputV2
            key={`inventoriesItemDetails.${index}.note`}
            control={control}
            name={`inventoriesItemDetails.${index}.note`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '250px' }}
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

  const calcTotalAmount = (index: number) => {
    const amount =
      watch(`inventoriesItemDetails.${index}.quantity`) &&
      watch(`inventoriesItemDetails.${index}.price`)
        ? Number(watch(`inventoriesItemDetails.${index}.quantity`)) *
          Number(watch(`inventoriesItemDetails.${index}.price`))
        : 0;

    return amount;
  };

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  const extraRow = () => {
    const totalQty = inventoriesItemDetails?.reduce(
      (acc, item) => (acc += Number(item?.quantity)),
      0,
    );

    const totalAmount = inventoriesItemDetails?.reduce(
      (acc, item) =>
        (acc += Number(item?.price || 0) * Number(item?.quantity || 0)),
      0,
    );

    const totalVatAmount = inventoriesItemDetails?.reduce((acc, item) => {
      const totalPrice = Number(item?.price) * Number(item?.quantity);
      const vatAmount = totalPrice * (Number(item?.vatRate ?? 0) / 100);
      return (acc += vatAmount);
    }, 0);

    const totalAmountAfterTax = inventoriesItemDetails?.reduce(
      (acc, item) =>
        (acc +=
          Number(item?.price || 0) * Number(item?.quantity || 0) +
          Number(item?.vatAmount ?? 0)),
      0,
    );

    return (
      <tr>
        <td></td>
        <td>Tổng</td>
        <td></td>
        <td>{convertCurrency(totalQty, false)}</td>
        <td />
        <td>{convertCurrency(totalAmount, false)}</td>
        {warehouseImportType ===
          (InventoriesWarehouse.WAREHOUSE_COMMERCE_EXPORT as string) && (
          <>
            <td />
            <td>{convertCurrency(totalVatAmount, false)}</td>
          </>
        )}
        <td>{convertCurrency(totalAmountAfterTax, false)}</td>
      </tr>
    );
  };

  return (
    <TableV2
      table_id="inventories-detail-items"
      columns={columns}
      data={inventoriesItemDetails || []}
      custom_body_row={() => extraRow()}
    />
  );
};
