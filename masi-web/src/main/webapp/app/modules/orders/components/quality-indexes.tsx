import ButtonIcon from 'app/components/button-icon/button-icon';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IOrder } from 'app/shared/model/order.model';
import React from 'react';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';
import { Label } from 'reactstrap';
import { v4 } from 'uuid';

const QualityIndexes = () => {
  const { control, watch } = useFormContext<IOrder>();
  const { append, remove } = useFieldArray({
    control,
    name: 'qualityIndexes',
  });

  const qualityIndexes = watch('qualityIndexes');

  const columns: ColumnsTypes<FieldArrayWithId<IOrder>> = [
    {
      title: 'Chỉ tiêu kiểm nghiệm',
      key: 'name',
      dataIndex: 'name',
      render: (_, record, index) => <FormInput control={control} id="name" name={`qualityIndexes.${index}.name`} />,
    },
    {
      title: 'Chấp nhận',
      key: 'value',
      dataIndex: 'value',
      render: (_, record, index) => <FormInput control={control} id="value" name={`qualityIndexes.${index}.value`} />,
    },

    {
      title: 'Thao tác',
      key: 'action',
      width: 106,
      render: (_, __, index) =>
        qualityIndexes?.length > 1 && (
          <ButtonIcon
            icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="trash" />}
            onClick={() => {
              remove(index);
            }}
            className="btn-remove"
          />
        ),
    },
  ];

  return (
    <Flex direction="column" gap={8}>
      <Flex justify="space-between" align="center">
        <Label>Chỉ tiêu chất lượng/ an toàn</Label>
        <Button
          onClick={e => {
            e.preventDefault(),
              append({
                id: v4(),
                name: '',
                value: '',
              });
          }}
          className="btn-filter"
        >
          <img className="pointer" src="content/images/vuesax/linear/add.svg" alt="plus" />
          Thêm mới
        </Button>
      </Flex>

      <Table rowKey="id" columns={columns} dataSource={qualityIndexes} stickyHeader={false} />
    </Flex>
  );
};

export default QualityIndexes;
