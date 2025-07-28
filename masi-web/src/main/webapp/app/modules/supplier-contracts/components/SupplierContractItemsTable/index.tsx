import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2 from 'app/components/table-v2/Table';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import useUom from 'app/hooks/use-uom';
import useVatRate from 'app/hooks/use-vat-rate';
import { ItemType } from 'app/shared/model/enumerations/item-category.model';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';
import { convertCurrency } from 'app/shared/util/format';
import { SupplierContractsSchema } from 'app/validation/supplier-contracts.validation';
import { useFieldArray, useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';

const { useGetItemsQuery } = useItems;
const { useGetUoms } = useUom;
const { useGetVatRates } = useVatRate;

export const SupplierContractsItemsTable = () => {
  const [searchParams] = useSearchParams();

  const { control, watch, formState, setValue } =
    useFormContext<SupplierContractsSchema>();

  const supplierContractDetails = watch('supplierContractDetails', []);
  const statusWatch = watch('status');
  const vatId = watch('supplierContractDetails.0.vatId');

  const setAll = (index: number, vatId: string, price, quantity) => {
    for (let i = 1; i < (supplierContractDetails?.length ?? 0); i++) {
      setValue(`supplierContractDetails.${i}.vatId`, vatId);
      calcVatAmount(
        i,
        vatId,
        supplierContractDetails[i].price,
        supplierContractDetails[i].quantity,
      );
    }
  };

  const { append, remove } = useFieldArray<SupplierContractsSchema>({
    control: control,
    name: 'supplierContractDetails',
  });

  const { data: uoms } = useGetUoms();
  const { data: vatRates } = useGetVatRates();
  const { data: items } = useGetItemsQuery({
    'itemType.contains': searchParams.get('itemType') ?? ItemType.ITEM,
  });

  const disabled = false;
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.CANCELLED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.APPROVED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.COMPLETED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.LIQUIDATED as string) ||
  // statusWatch === (SUPPLIER_CONTRACT_STATUS.EXPIRED as string);

  const calcTotalAmount = (index: number) => {
    const details = supplierContractDetails[index];
    return Number(details?.price) * Number(details?.quantity);
  };

  const calcVatAmount = (index: number, vatId: string, price, quantity) => {
    const vatSelected = vatRates?.data?.find(x => x.id === vatId);
    if (vatSelected) {
      const vatR = vatSelected?.value ?? 0;
      setValue(`supplierContractDetails.${index}.vatRate`, `${vatR ?? 0}`);

      const totalPrice = Number(price ?? 0) * Number(quantity ?? 0);

      const vatAmount = (totalPrice ?? 0) * (Number(vatR ?? 0) / 100);

      setValue(
        `supplierContractDetails.${index}.vatAmount`,
        `${vatAmount ?? 0}`,
      );
    }
  };

  const columns = [
    {
      header: { render: `Hàng hóa` },
      body: {
        render: ({ index }) => (
          <FormSelect
            key={`supplierContractDetails.${index}.itemId`}
            control={control}
            name={`supplierContractDetails.${index}.itemId`}
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
                  `supplierContractDetails.${index}.uomId`,
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
            key={`supplierContractDetails.${index}.quantity`}
            control={control}
            name={`supplierContractDetails.${index}.quantity`}
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
      header: { render: 'Đơn vị' },
      body: {
        render: ({ data, index }) => (
          <FormSelect
            key={`supplierContractDetails.${index}.uomId`}
            control={control}
            name={`supplierContractDetails.${index}.uomId`}
            placeholder="Chọn"
            options={uoms?.data?.map(u => ({ label: u?.name, value: u?.id }))}
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
            key={`supplierContractDetails.${index}.price`}
            control={control}
            name={`supplierContractDetails.${index}.price`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '175px' }}
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
        render: ({ data }) =>
          convertCurrency(Number(data?.price) * Number(data?.quantity), false),
      },
    },
    {
      header: { render: '%VAT' },
      body: {
        render: ({ data, index }) => {
          if (index > 0) {
            return (
              <FormSelect
                key={`supplierContractDetails.${index}.vatId`}
                control={control}
                name={`supplierContractDetails.0.vatId`}
                placeholder="Chọn"
                options={vatRates?.data?.map(u => ({
                  label: u?.name,
                  value: u?.id,
                }))}
                disabled={index > 0}
                onChanges={e => {
                  calcVatAmount(index, e, data?.price, data?.quantity);
                }}
                isClearable={false}
              />
            );
          } else {
            return (
              <FormSelect
                key={`supplierContractDetails.${index}.vatId`}
                control={control}
                name={`supplierContractDetails.0.vatId`}
                placeholder="Chọn"
                options={vatRates?.data?.map(u => ({
                  label: u?.name,
                  value: u?.id,
                }))}
                disabled={index > 0}
                onChanges={e => {
                  setAll(index, e, data.price, data?.quantity);
                  calcVatAmount(index, e, data?.price, data?.quantity);
                }}
                isClearable={false}
              />
            );
          }
        },
      },
    },
    {
      header: { render: 'VAT' },
      body: {
        render: ({ data }) => convertCurrency(data?.vatAmount ?? 0, false),
      },
    },
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
            key={`supplierContractDetails.${index}.note`}
            control={control}
            name={`supplierContractDetails.${index}.note`}
            placeholder="Điền"
            disabled={disabled}
            style={{ width: '200px' }}
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

  const extraRow = () => {
    const totalQty = supplierContractDetails?.reduce(
      (acc, item) => (acc += Number(item?.quantity)),
      0,
    );

    const totalAmount = supplierContractDetails?.reduce(
      (acc, item) =>
        (acc += Number(item?.price || 0) * Number(item?.quantity || 0)),
      0,
    );

    const totalVatAmount = supplierContractDetails?.reduce((acc, item) => {
      const totalPrice = Number(item?.price) * Number(item?.quantity);
      const vatAmount = totalPrice * (Number(item?.vatRate ?? 0) / 100);
      return (acc += vatAmount);
    }, 0);

    const totalAmountAfterTax = supplierContractDetails?.reduce(
      (acc, item) =>
        (acc +=
          Number(item?.price || 0) * Number(item?.quantity || 0) +
          Number(item?.vatAmount ?? 0)),
      0,
    );

    return (
      <tr>
        <td colSpan={4} style={{ textAlign: 'right' }}>
          Tổng
        </td>
        <td>{convertCurrency(totalAmount, false)}</td>
        <td />
        <td>{convertCurrency(totalVatAmount, false)}</td>
        <td>{convertCurrency(totalAmountAfterTax, false)}</td>
      </tr>
    );
  };

  return (
    <Flex direction="column" gap={16}>
      <Flex align="center" gap={16}>
        <Typography level={5} style={{ marginBottom: 0 }}>
          Danh sách hàng hoá
        </Typography>
        <ButtonAdd
          onClick={() => append({ quantity: '0', vatId: vatId })}
          text="Thêm"
          disabled={disabled}
        />
      </Flex>
      <TableV2
        table_id="incoming_invoice"
        columns={columns}
        data={supplierContractDetails || []}
        custom_body_row={() => extraRow()}
      />
      {(formState.errors?.supplierContractDetails?.message ||
        formState.errors?.supplierContractDetails?.root) && (
        <FormError
          message={
            formState.errors?.supplierContractDetails?.message ||
            formState.errors?.supplierContractDetails?.root?.message
          }
        />
      )}
    </Flex>
  );
};
function useEffect(arg0: () => void, arg1: undefined[]) {
  throw new Error('Function not implemented.');
}
