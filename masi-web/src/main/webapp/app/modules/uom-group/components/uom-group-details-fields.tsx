import ButtonIcon from 'app/components/button-icon/button-icon';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IUom, IUomGroupDetail } from 'app/shared/model/uom.model';
import { UomGroupSchema } from 'app/validation/uom.validation';
import React from 'react';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';
import { FormGroup, Label } from 'reactstrap';
import { v4 } from 'uuid';

interface UomGroupDetailsFieldsProps {
  data: IUom[];
  isLoading: boolean;
}

const UomGroupDetailsFields = (props: UomGroupDetailsFieldsProps) => {
  const { data, isLoading } = props;

  const { control, watch } = useFormContext<UomGroupSchema>();

  const { append, remove } = useFieldArray({
    control,
    name: 'uomGroupDetailsDTOs',
  });

  const uomGroupDetailsDTOs = watch('uomGroupDetailsDTOs')?.map(item => ({
    ...item,
    id: item.id,
    baseQty: Number(item.baseQty),
    altQty: Number(item.altQty),
  }));

  const columns: ColumnsTypes<FieldArrayWithId<IUomGroupDetail>> = [
    {
      title: 'Tên',
      dataIndex: 'name',
      render: (_, __, index) => (
        <FormInput control={control} id={`uomGroupDetailsDTOs.${index}.name`} name={`uomGroupDetailsDTOs.${index}.name`} />
      ),
    },
    {
      title: 'Đơn vị',
      dataIndex: 'baseUomId',
      render: (_, __, index) => (
        <FormSelect
          control={control}
          id={`uomGroupDetailsDTOs.${index}.baseUomId`}
          name={`uomGroupDetailsDTOs.${index}.baseUomId`}
          placeholder="Chọn đơn vị"
          options={data?.map(u => ({
            label: u?.name,
            value: u?.id,
          }))}
          isLoading={isLoading}
        />
      ),
    },
    {
      title: 'Số lượng',
      dataIndex: 'baseQty',
      render: (_, __, index) => (
        <FormInput control={control} id={`uomGroupDetailsDTOs.${index}.baseQty`} name={`uomGroupDetailsDTOs.${index}.baseQty`} />
      ),
    },
    {
      title: 'Đơn vị quy đổi',
      dataIndex: 'altUomId',
      render: (_, __, index) => (
        <FormSelect
          control={control}
          id={`uomGroupDetailsDTOs.${index}.altUomId`}
          name={`uomGroupDetailsDTOs.${index}.altUomId`}
          placeholder="Chọn đơn vị"
          options={data?.map(u => ({
            label: u?.name,
            value: u?.id,
          }))}
          isLoading={isLoading}
        />
      ),
    },
    {
      title: 'Số lượng quy đổi',
      dataIndex: 'altQty',
      render: (_, __, index) => (
        <FormInput control={control} id={`uomGroupDetailsDTOs.${index}.altQty`} name={`uomGroupDetailsDTOs.${index}.altQty`} />
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      dataIndex: 'action',
      width: 106,
      render: (_, record, index) => (
        <Flex gap={12}>
          {uomGroupDetailsDTOs?.length > 1 && (
            <ButtonIcon
              key={index}
              icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}
              onClick={() => remove(index)}
            />
          )}
        </Flex>
      ),
    },
  ];

  return (
    <FormGroup>
      <Flex justify="space-between" className="mb-2">
        <Label>Danh sách đơn vị quy đổi</Label>
        <Button
          onClick={e => {
            e.preventDefault();
            append({
              id: v4(),
              baseUomId: '',
              baseQty: '',
              altUomId: '',
              altQty: '',
            });
          }}
          className="btn-filter"
        >
          <img className="pointer" src="content/images/vuesax/linear/add.svg" alt="plus" />
          Thêm mới
        </Button>
      </Flex>

      <div className="additives-container">
        <Table<FieldArrayWithId<IUomGroupDetail>> rowKey="id" columns={columns} dataSource={uomGroupDetailsDTOs} stickyHeader={false} />
      </div>
    </FormGroup>
  );
};

export default UomGroupDetailsFields;
