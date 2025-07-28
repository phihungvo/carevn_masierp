import React, { useState } from 'react';
import { Control, Path } from 'react-hook-form';

import { IUnitOption } from 'app/shared/model/contract.model';
import FormCreatableSelect from 'app/components/form/form-creatable-select';
import { LogisticsMaterialProposalSchema } from 'app/validation/logistics-material-proposal';
import { Label } from 'reactstrap';

const createOption = (label: string) => ({
  label,
  value: label,
});

interface MaterialSelectProps {
  control: Control<LogisticsMaterialProposalSchema>;
  setValue: (name: Path<LogisticsMaterialProposalSchema>, value: string) => void;
  index: number;
  data: IUnitOption[];
  isLoading: boolean;
  label?: string;
}

const UnitSelect = React.memo((props: MaterialSelectProps) => {
  const { control, setValue, index, data, isLoading, label } = props;

  const [listOptions, setListOptions] = useState<IUnitOption[]>(data);

  const handleCreate = (inputValue: string) => {
    const newOption = createOption(inputValue);
    setListOptions(prev => [...prev, newOption]);
    // setValue(`additives.${index}.unit`, newOption?.value);
  };

  return (
    <>
      {label && (
        <Label for='units' >
          {label}
        </Label>
      )}
      <FormCreatableSelect
        control={control}
        id={`additives.${index}.unit`}
        // name={`additives.${index}.unit`}
        name='units'
        placeholder=""
        options={listOptions}
        isLoading={isLoading}
        onCreateOption={value => handleCreate(value)}
      />
    </>
  );
});

UnitSelect.displayName = 'UnitSelect';

export default UnitSelect;
