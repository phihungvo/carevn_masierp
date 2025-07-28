import React from 'react';
import { v4 as uuidv4 } from 'uuid';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';

import Flex from 'app/components/flex/flex';
import Table from 'app/components/table/table';
import Button from 'app/components/button/button';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import { UniformOrderSchema } from 'app/validation/uniform.validation';
import { IUniform, IUniformDetail } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';

interface UniformListFieldsProps {
  data: IUniform[];
  isLoading: boolean;
}

const UniformListFields = (props: UniformListFieldsProps) => {
  const { data, isLoading } = props;
  const { control, setValue, watch } = useFormContext<UniformOrderSchema>();
  const { fields, append, remove } = useFieldArray({
    control,
    name: 'uniformDetails',
  });

  const uniformDetails = watch('uniformDetails');

  const transformDataSource = uniformDetails.map(item => ({
    ...item,
    id: item.id,
    quantity: Number(item.quantity),
    basePrice: item.basePrice ? Number(item.basePrice) : 0,
    actualPrice: item.actualPrice ? Number(item.actualPrice) : 0,
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
          name={`uniformDetails.${index}.uniformId`}
          placeholder="Chọn loại đồng phục"
          options={data?.map(e => ({
            label: e?.name,
            value: e?.id,
            isDisabled: uniformDetails.some(item => item.uniformId === e?.id),
          }))}
          isLoading={isLoading}
          isClearable={false}
        />
      ),
    },
    {
      title: 'Số lượng',
      key: 'quantity',
      dataIndex: 'quantity',
      render: (_, record, index) => (
        <FormInput
          control={control}
          id="quantity"
          name={`uniformDetails.${index}.quantity`}
          onChange={e => setValue(`uniformDetails.${index}.quantity`, formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
          onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
        />
      ),
    },
    {
      title: 'Đơn vị',
      key: 'uom',
      dataIndex: 'uom',
      render: (_, __, index) => data?.find(item => item.id === uniformDetails[index]?.uniformId)?.uomDTO?.name,
    },
    {
      title: 'Đơn giá',
      key: 'basePrice',
      dataIndex: 'basePrice',
      render: (_, __, index) => (
        <>{formatDecimalPrecision(data?.find(item => item.id === uniformDetails[index]?.uniformId)?.basePrice ?? 0)}</>
      ),
    },
    {
      title: 'Giá mua',
      key: 'actualPrice',
      dataIndex: 'actualPrice',
      render: (_, __, index) => (
        <FormInput
          control={control}
          id={`uniformDetails.${index}.actualPrice`}
          name={`uniformDetails.${index}.actualPrice`}
          onChange={e => setValue(`uniformDetails.${index}.actualPrice`, formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
          onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
        />
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      width: 106,
      render: (_, __, index) =>
        transformDataSource?.length > 1 && (
          <ButtonIcon
            icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="trash" />}
            onClick={() => {
              remove(index);
            }}
            className="btn-remove"
            type="button"
          />
        ),
    },
  ];

  return (
    <Flex direction="column" gap={8}>
      <Flex justify="space-between" align="center">
        <Button
          onClick={e => {
            e.preventDefault(),
              append({ uniformId: '', quantity: '', id: uuidv4() });
          }}
          className="btn-filter"
        >
          <img className="pointer" src="content/images/vuesax/linear/add.svg" alt="plus" />
          Thêm mới
        </Button>
      </Flex>

      <Table<FieldArrayWithId<IUniformDetail>> rowKey="id" columns={columns} dataSource={transformDataSource} stickyHeader={false} />
    </Flex>
  );
};

export default UniformListFields;
