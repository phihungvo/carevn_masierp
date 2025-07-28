import React, { useState } from 'react';
import { Control, Path } from 'react-hook-form';

import { IUnitOption } from 'app/shared/model/contract.model';
import { ContractFormSchema } from 'app/validation/contract.validation';
import FormCreatableSelect from 'app/components/form/form-creatable-select';

const createOption = (label: string) => ({
  label,
  value: label,
});

interface MaterialSelectProps {
  control: Control<ContractFormSchema>;
  setValue: (name: Path<ContractFormSchema>, value: string) => void;
  index: number;
  data: IUnitOption[];
  isLoading: boolean;
  disabled: boolean;
}

const UnitSelect = React.memo((props: MaterialSelectProps) => {
  const { control, setValue, index, data, isLoading, disabled } = props;

  const [listOptions, setListOptions] = useState<IUnitOption[]>(data);

  const handleCreate = (inputValue: string) => {
    const newOption = createOption(inputValue);
    setListOptions(prev => [...prev, newOption]);
    setValue(`additives.${index}.unit`, newOption?.value);
  };

  return (
    <FormCreatableSelect
      control={control}
      id={`additives.${index}.unit`}
      name={`additives.${index}.unit`}
      placeholder=""
      options={listOptions}
      isLoading={isLoading}
      onCreateOption={value => handleCreate(value)}
      className="unit-select"
      disabled={disabled}
    />
  );
});

UnitSelect.displayName = 'UnitSelect';

export default UnitSelect;
