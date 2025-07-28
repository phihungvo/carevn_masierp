import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import useUniform from 'app/hooks/use-uniform';
import { IUniformDetail } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidDecimal, handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { UniformStockSchema } from 'app/validation/uniform.validation';
import React from 'react';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';

const { useUniforms } = useUniform;

const UniformStockFields = () => {
  const { control, setValue, watch } = useFormContext<UniformStockSchema>();
  const { append } = useFieldArray({
    control,
    name: 'uniformFormDetailDTO',
  });

  const { data, isLoading } = useUniforms();

  const uniformDetails = watch('uniformFormDetailDTO');

  const transformDataSource = uniformDetails?.map(item => ({
    ...item,
    id: item.id,
    quantity: item.quantity ? Number(item.quantity) : undefined,
    initQuantity: Number(item.initQuantity),
    returnedQuantity: Number(item.returnedQuantity),
  }));

  const columns: ColumnsTypes<FieldArrayWithId<IUniformDetail>> = [
    {
      title: 'Loại đồng phục',
      key: 'uniform',
      dataIndex: 'uniform',
      render: (_, record, index) => (
        <FormSelect
          control={control}
          id="uniformId"
          name={`uniformFormDetailDTO.${index}.uniformId`}
          placeholder="Chọn loại đồng phục"
          options={data?.data?.map(e => ({
            label: e?.name,
            value: e?.id,
            isDisabled: uniformDetails.some(item => item.uniformId === e?.id),
          }))}
          isLoading={isLoading}
          isClearable={false}
          disabled={true}
        />
      ),
    },
    {
      title: 'Số lượng',
      key: 'quantity',
      dataIndex: 'quantity',
      width: 150,
      render: (_, __, index) => (
        <FormInput
          control={control}
          id="quantity"
          name={`uniformFormDetailDTO.${index}.quantity`}
          onChange={e =>
            handleValidDecimal<UniformStockSchema>(
              e.target.value,
              `uniformFormDetailDTO.${index}.quantity`,
              DEFAULT_INTEGER_REGEX,
              setValue,
            )
          }
          onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
        />
      ),
    },
    {
      title: 'Đã nhập',
      key: 'returned',
      dataIndex: 'returned',
      render: (_, record: IUniformDetail) => formatDecimalPrecision(record?.returnedQuantity),
    },
    {
      title: 'Còn lại',
      key: 'remaining',
      dataIndex: 'remaining',
      render: (_, record: IUniformDetail) => formatDecimalPrecision(record?.initQuantity - record?.returnedQuantity),
    },
  ];

  return <Table<FieldArrayWithId<IUniformDetail>> rowKey="id" columns={columns} dataSource={transformDataSource} stickyHeader={false} />;
};

export default UniformStockFields;
