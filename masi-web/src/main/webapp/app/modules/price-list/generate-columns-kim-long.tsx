import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { IQuotationDetail } from 'app/shared/model/quotation.model';
import Flex from 'app/components/flex/flex';
import ButtonIcon from 'app/components/button-icon/button-icon';
import Tooltip from 'app/components/tooltip/tooltip';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { useAppSelector } from 'app/config/store';
import FormInput from 'app/components/form/form-input';
import { useFormContext } from 'react-hook-form';
import FormSelect from 'app/components/form/form-select';
import useContracts from 'app/hooks/use-contracts';
import { QuotationFormKimLongSchema } from 'app/validation/quotation.validation';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import './price-list.scss'

const { useGetContractMaterials } = useContracts;

export const generateColumnsKimLong = (
  status: QUOTATION_STATUS,
  toggle: () => void,
  setSelectedIndex: (index: number) => void,
): ColumnsTypes<IQuotationDetail> => {
  const { control, watch, setValue } = useFormContext<QuotationFormKimLongSchema>();
  const isUpdate = useAppSelector(state => state.quotation.isUpdate);

  const { data: contractMaterials, isLoading: loadingMaterial } = useGetContractMaterials();

  const fields = watch('quotationDetails');

  const handleDelete = (e: React.MouseEvent<HTMLButtonElement>, index: number) => {
    e.preventDefault();
    toggle();
    setSelectedIndex(index);
  };

  const columns: ColumnsTypes<IQuotationDetail> = [
    {
      title: 'Loại hàng',
      key: 'product',
      dataIndex: 'product',
      render: (_, record, index) => (
        <>
          {
            <FormSelect
              label="Loại hàng"
              control={control}
              name={`quotationDetails.${index}.materialId`}
              styles={{ menuPortal: (base) => ({ ...base, zIndex: 9999 }) }}
              menuPortalTarget={document.body}
              placeholder="Chọn loại hàng"
              options={contractMaterials?.data?.map(c => ({
                label: c?.name,
                value: c?.id,
              }))}
              isLoading={loadingMaterial}
            />
          }
        </>
      ),
    },
    {
      title: 'Thông tin sản phẩm',
      key: 'note',
      dataIndex: 'note',
      render: (_, record, index) => (
        <Flex direction="column" gap={8}>
          <FormInput label="Thông tin" control={control} className='mb' name={`quotationDetails.${index}.note`} type="textarea" />
          <FormInput
            label="Khối lượng (Kg)"
            control={control}
            className='mb'
            name={`quotationDetails.${index}.weight`}
            onChange={e => setValue(`quotationDetails.${index}.weight`, formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
          />
        </Flex>
      ),
    },
    {
      title: 'Thông tin giá',
      key: 'price',
      dataIndex: 'price',
      render: (_, record, index) => (
        <Flex direction="column" gap={8}>
          {/* <FormInput
            label="180 mgN/100g"
            showError={!watch(`quotationDetails.${index}.nitrogen150Price`)}
            control={control}
            name={`quotationDetails.${index}.nitrogen180Price`}
          />
          <FormInput
            label="150 mgN/100g"
            showError={!watch(`quotationDetails.${index}.nitrogen180Price`)}
            control={control}
            name={`quotationDetails.${index}.nitrogen150Price`}
          /> */}
          <FormInput
            label="Đơn giá (VNĐ/kg)"
            control={control}
            name={`quotationDetails.${index}.price`}
            onChange={e => setValue(`quotationDetails.${index}.price`, formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
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
          {fields?.length > 1 && (
            <Tooltip placement='top' label="Xóa" target={`delete-${record.id}`}>
              <ButtonIcon
                key={index}
                id={`delete-${record.id}`}
                icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}
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
