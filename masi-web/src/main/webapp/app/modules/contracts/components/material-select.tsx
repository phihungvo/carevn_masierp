import FormCreatableSelect from 'app/components/form/form-creatable-select';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useItems from 'app/hooks/use-items';
import { IContractMaterial } from 'app/shared/model/contract.model';
import { ContractFormSchema } from 'app/validation/contract.validation';
import React, { useEffect, useState } from 'react';
import { Control, Path } from 'react-hook-form';

const createOption = (label: string) => ({
  label,
  value: label,
});

interface MaterialSelectProps {
  control: Control<ContractFormSchema>;
  setValue: (name: Path<ContractFormSchema>, value: string) => void;
  index: number;
  data: IContractMaterial[];
  isLoading: boolean;
  disabled?: boolean;
}

const { useGetItemsQuery } = useItems;

const MaterialSelect = React.memo((props: MaterialSelectProps) => {
  const { control, setValue, index, data, isLoading, disabled } = props;

  const [listOptions, setListOptions] = useState<any>([]);

  const { data: items } = useGetItemsQuery({ size: DEFAULT_PAGE_SIZE_NAX });

  useEffect(() => {
    setListOptions(
      data?.map(c => ({
        label: c?.name,
        value: c?.id,
      })),
    );
  }, [data]);

  const handleCreate = (inputValue: string) => {
    const newOption = createOption(inputValue);
    setListOptions(prev => [...prev, newOption]);
    setValue(`additives.${index}.idMaterial`, newOption?.value);
  };

  return (
    <FormCreatableSelect
      control={control}
      id={`additives.${index}.idMaterial`}
      name={`additives.${index}.idMaterial`}
      placeholder="Chọn nguyên liệu"
      options={listOptions}
      isLoading={isLoading}
      onCreateOption={value => handleCreate(value)}
      // className="material-select"
      disabled={disabled}
      onChanges={e => {
        const material = data?.find(x => x.id === e);
        if (material) {
          const itemSelected = items?.data?.find(x => x.id === material?.itemId);
          setValue(`additives.${index}.unit`, itemSelected?.uom?.name);
        }
      }}
    />
  );
});

MaterialSelect.displayName = 'MaterialSelect';

export default MaterialSelect;
