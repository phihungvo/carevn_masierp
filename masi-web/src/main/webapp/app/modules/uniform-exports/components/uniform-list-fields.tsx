import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import useUniform from 'app/hooks/use-uniform';
import { IUniformDetail } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidDecimal, handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { UniformReturnSchema } from 'app/validation/uniform.validation';
import React from 'react';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';
import { Label } from 'reactstrap';

const { useUniforms } = useUniform;

const UniformListFields = () => {
  const { control, setValue, watch } = useFormContext<UniformReturnSchema>();
  const { append } = useFieldArray({
    control,
    name: 'returnDetails',
  });

  const { data, isLoading } = useUniforms();

  const uniformDetails = watch('returnDetails');

  const transformDataSource = uniformDetails
    ?.map(item => ({
      ...item,
      id: item.id,
      quantity: Number(item.quantity),
      initQuantity: Number(item.initQuantity),
      returnedQuantity: Number(item.returnedQuantity),
    }))
    ?.filter(item => item?.initQuantity - item?.returnedQuantity > 0);

  const columns: ColumnsTypes<FieldArrayWithId<IUniformDetail>> = [
    {
      title: 'Loại đồng phục',
      key: 'uniform',
      dataIndex: 'uniform',
      render: (_, record, index) => (
        <FormSelect
          control={control}
          id="uniformId"
          name={`returnDetails.${index}.uniformId`}
          placeholder="Chọn loại đồng phục"
          options={data?.data?.map(e => ({
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
          name={`returnDetails.${index}.quantity`}
          onChange={e =>
            handleValidDecimal<UniformReturnSchema>(e.target.value, `returnDetails.${index}.quantity`, DEFAULT_INTEGER_REGEX, setValue)
          }
          onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
        />
      ),
    },
    {
      title: 'Còn lại',
      key: 'remaining',
      dataIndex: 'remaining',
      render: (_, record: IUniformDetail, index) => formatDecimalPrecision(record?.initQuantity - record?.returnedQuantity),
    },
  ];

  return (
    <Flex direction="column" gap={8}>
      <Flex justify="space-between" align="center">
        <Label>Danh sách đồng phục</Label>
        <Button
          onClick={e => {
            e.preventDefault(), append({});
          }}
          className="btn-filter"
          disabled={!uniformDetails || uniformDetails?.length === 0}
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
