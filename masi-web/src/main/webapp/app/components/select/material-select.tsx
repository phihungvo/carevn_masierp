import FormCreatableSelect from 'app/components/form/form-creatable-select';
import { IContractMaterial } from 'app/shared/model/contract.model';
import { ContractFormSchema } from 'app/validation/contract.validation';
import React, { useEffect, useState } from 'react';
import { Control, Path } from 'react-hook-form';
import ModalMaterial from '../modal/modal-material';

interface MaterialSelectProps<T> {
  control: Control<ContractFormSchema>;
  setValue?: (name: Path<T>, value: string) => void;
  index?: number;
  data: IContractMaterial[];
  isLoading: boolean;
}

const MaterialSelect = React.memo(<T extends object>(props: MaterialSelectProps<T>) => {
  const { control, setValue, index, data, isLoading } = props;

  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [listOptions, setListOptions] = useState<any>([]);

  const toggleOpen = () => setIsOpen(prev => !prev);

  useEffect(() => {
    setListOptions(
      data?.map(c => ({
        label: c?.name,
        value: c?.id,
      })),
    );
  }, [data]);

  const handleCreate = (e: React.KeyboardEvent<HTMLDivElement>) => {
    e.key === 'Enter' && toggleOpen();
  };

  return (
    <>
      <FormCreatableSelect
        control={control}
        id={`additives.${index}.idMaterial`}
        name={`additives.${index}.idMaterial`}
        placeholder="Chọn nguyên liệu"
        options={listOptions}
        isLoading={isLoading}
        className="material-select"
        onKeyDown={e => handleCreate(e)}
      />
      <ModalMaterial isOpen={isOpen} toggle={toggleOpen} setListOptions={setListOptions} />
    </>
  );
});

MaterialSelect.displayName = 'MaterialSelect';

export default MaterialSelect;
