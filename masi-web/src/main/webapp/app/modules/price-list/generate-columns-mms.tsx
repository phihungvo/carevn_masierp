import './price-list.scss';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { IQuotationDetail } from 'app/shared/model/quotation.model';
import Flex from 'app/components/flex/flex';
import ButtonIcon from 'app/components/button-icon/button-icon';
import Tooltip from 'app/components/tooltip/tooltip';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { useFormContext } from 'react-hook-form';
import { useAppSelector } from 'app/config/store';
import useContracts from 'app/hooks/use-contracts';
import FormSelect from 'app/components/form/form-select';
import FormInput from 'app/components/form/form-input';
import { QuotationMMSSchema } from 'app/validation/quotation.validation';
import FormDatePicker from 'app/components/form/form-date-picker';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';

const { useGetContractMaterials } = useContracts;

export const generateColumnsMMS = (
  status: QUOTATION_STATUS,
  toggle: () => void,
  setSelectedIndex: (index: number) => void,
): ColumnsTypes<IQuotationDetail> => {
  const { control, watch, setValue, formState } =
    useFormContext<QuotationMMSSchema>();

  const isUpdate = useAppSelector(state => state.quotation.isUpdate);

  const { data: contractMaterials, isLoading: loadingMaterial } =
    useGetContractMaterials();

  const fields = watch('quotationDetails');

  const handleDelete = (
    e: React.MouseEvent<HTMLButtonElement>,
    index: number,
  ) => {
    e.preventDefault();
    toggle();
    setSelectedIndex(index);
  };

  const columns: ColumnsTypes<IQuotationDetail> = [
    {
      title: 'Loại hàng',
      key: 'product',
      dataIndex: 'product',
      width: 120,
      render: (_, record, index) => (
        <FormSelect
          label="Loại hàng"
          control={control}
          name={`quotationDetails.${index}.materialId`}
          placeholder="Chọn loại hàng"
          options={contractMaterials?.data?.map(c => ({
            label: c?.name,
            value: c?.id,
          }))}
          isLoading={loadingMaterial}
        />
      ),
    },
    {
      title: 'Thông tin sản phẩm',
      key: 'note',
      dataIndex: 'note',
      render: (_, record, index) => (
        <Flex direction="column" gap={4}>
          <FormInput
            label="Ghi chú"
            control={control}
            name={`quotationDetails.${index}.note`}
            type="textarea"
          />
          <FormInput
            label="Đơn giá (VNĐ/kg)"
            control={control}
            name={`quotationDetails.${index}.price`}
            onChange={e =>
              setValue(
                `quotationDetails.${index}.price`,
                formatDecimalPrecision(
                  Number(e?.target?.value?.replace(/,/g, '')),
                ),
              )
            }
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
          />
          <FormInput
            label="Khối lượng (Tấn)"
            control={control}
            name={`quotationDetails.${index}.weight`}
            onChange={e =>
              setValue(
                `quotationDetails.${index}.weight`,
                formatDecimalPrecision(
                  Number(e?.target?.value?.replace(/,/g, '')),
                ),
              )
            }
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
          />
        </Flex>
      ),
    },
    {
      title: 'Thông tin giao hàng',
      key: 'delivery',
      dataIndex: 'delivery',
      render: (text, _, index) => (
        <Flex direction="column" gap={8}>
          <FormDatePicker
            label="Thời gian giao hàng"
            setValue={setValue}
            control={control}
            name={`quotationDetails.${index}.deliveryDate`}
            portal
            formState={formState}
          />
          <FormInput
            label="Địa điểm giao hàng"
            control={control}
            name={`quotationDetails.${index}.deliveryLocation`}
          />
          <FormInput
            label="Địa điểm giao hàng (En)"
            control={control}
            name={`quotationDetails.${index}.deliveryLocationEn`}
          />
        </Flex>
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      dataIndex: 'action',
      width: 106,
      render: (_, record, index) => (
        <Flex gap={12}>
          {fields.length > 1 && (
            <Tooltip placement="top" label="Xóa" target={`delete-${record.id}`}>
              <ButtonIcon
                id={`delete-${record.id}`}
                icon={
                  <img
                    className="pointer"
                    src="content/images/vuesax/linear/trash.svg"
                    alt="delete"
                  />
                }
                onClick={e => handleDelete(e, index)}
              />
            </Tooltip>
          )}
        </Flex>
      ),
    },
  ];

  return columns;
};
