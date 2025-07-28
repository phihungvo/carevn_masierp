import React, { useState } from 'react';
import { FormGroup, Label } from 'reactstrap';
import MaterialSelect from './material-select';
import { Control, FieldArrayWithId, Path, useFieldArray, UseFormWatch } from 'react-hook-form';

import Flex from 'app/components/flex/flex';
import Table from 'app/components/table/table';
import FormInput from 'app/components/form/form-input';
import ButtonIcon from 'app/components/button-icon/button-icon';
import UnitSelect from 'app/modules/contracts/components/unit-select';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ContractFormSchema } from 'app/validation/contract.validation';
import { recalculateContractTotal } from 'app/hooks/use-calculate-contract-total';
import { IContract, IContractMaterial, IUnitOption } from 'app/shared/model/contract.model';
import FormNumberic from 'app/components/form/form-numberic';
import { NumericFormat } from 'react-number-format';
import Button from 'app/components/button/button';

interface AdditivesFormProps {
  control: Control<ContractFormSchema>;
  setValue: (name: Path<ContractFormSchema>, value: string) => void;
  watch: UseFormWatch<ContractFormSchema>;
  data: IContractMaterial[];
  isLoading: boolean;
  disabled?: boolean;
}


const AdditivesForm = React.memo((props: AdditivesFormProps) => {
  const { control, setValue, watch, data, isLoading, disabled } = props;

  const [priceMap, setPriceMap] = useState(new Map<number, number>());
  const [quantityMap, setQuantityMap] = useState(new Map<number, number>());

  const { fields, append, remove } = useFieldArray({
    control,
    name: 'additives',
  });

  const exchangeRate = watch('exchangeRate');
  const additives = watch('additives');

  additives?.forEach((additive, index) => {
    if (additive?.price && !priceMap.has(index)) {
      setPriceMap(new Map(priceMap.set(index, Number(additive.price))));
    }

    if (additive?.quantity && !quantityMap.has(index)) {
      setQuantityMap(new Map(quantityMap.set(index, Number(additive.quantity))));
    }
  });

  const columns: ColumnsTypes = [
    {
      title: 'Nguyên liệu',
      key: 'idMaterial',
      width: 370,
      render: (_, __, index) => <div style={{ width: '100%' }}><MaterialSelect data={data} control={control} index={index} isLoading={isLoading} setValue={setValue} disabled={disabled} /></div>,
    },
    {
      title: 'Đơn giá',
      key: 'price',
      width: 150,
      render: (_, __, index) => (
        <FormNumberic
          control={control}
          name={`additives.${index}.price`}
          id={`additives.${index}.price`}
          onChange={e => {
            setValue(`additives.${index}.price`, e.target.value.replace(/,/g, ''));
            recalculateContractTotal<ContractFormSchema>(exchangeRate, additives, 'contractTotal', setValue as any);
            setPriceMap(new Map(priceMap.set(index, Number(e.target.value.replace(/,/g, '')))));
          }}
          disabled={disabled}
        />
      ),
    },
    {
      title: 'Số lượng',
      key: 'quantity',
      width: 150,
      render: (_, __, index) => (
        <FormNumberic
          control={control}
          name={`additives.${index}.quantity`}
          id={`additives.${index}.quantity`}
          onChange={e => {
            setValue(`additives.${index}.quantity`, e.target.value.replace(/,/g, ''));
            recalculateContractTotal<ContractFormSchema>(exchangeRate, additives, 'contractTotal', setValue as any);
            setQuantityMap(new Map(quantityMap.set(index, Number(e.target.value.replace(/,/g, '')))));
          }}
          disabled={disabled}
        />
      ),
    },
    {
      title: 'Đơn vị',
      key: 'unit',
      width: 100,
      render: (_, __, index) => <FormInput control={control} index={index} name={`additives.${index}.unit`} setValue={setValue} disabled />,
    },
    {
      title: 'Thông số',
      key: 'proteinParameters',
      width: 200,
      render: (_, __, index) => <FormInput control={control} type="textarea" name={`additives.${index}.proteinParameters`} disabled={disabled} />,
    },
    {
      title: 'Thành tiền',
      key: 'total',
      width: 120,
      render: (_, __, index) => {
        const price = priceMap.get(index) || 0;
        const quantity = quantityMap.get(index) || 0;
        return (
          <NumericFormat
            className="input-numberic"
            value={parseFloat((price * quantity).toFixed(1))}
            thousandSeparator
            displayType="text"
            disabled={disabled}
          />
        );
      },
    },
    {
      title: 'Thao tác',
      key: 'action',
      width: 106,
      render: (_, __, index) =>
        fields?.length > 1 && (
          <ButtonIcon
            icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="trash" />}
            onClick={() => {
              remove(index);
              priceMap.delete(index);
              quantityMap.delete(index);
            }}
            className="btn-remove"
            disabled={disabled}
          />
        ),
    },
  ];

  return (
    <FormGroup>
      <Flex justify="space-between" className="additive-form-header">
        <Label>Danh sách thành phẩm:</Label>
        <Button
          onClick={e => {
            e.preventDefault(),
              append({
                idMaterial: '',
                price: '',
                quantity: '',
                proteinParameters: '',
              });
          }}
          className="btn-filter"
          disabled={disabled}
        >
          <img className="pointer" src="content/images/vuesax/linear/add.svg" alt="plus" />
          Thêm mới
        </Button>
      </Flex>

      <div className="additives-container">
        <Table<FieldArrayWithId<IContract>> rowKey="id" columns={columns} dataSource={fields} />
      </div>
    </FormGroup>
  );
});

AdditivesForm.displayName = 'AdditivesForm';

export default AdditivesForm;
