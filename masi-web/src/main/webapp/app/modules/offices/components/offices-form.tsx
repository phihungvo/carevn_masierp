import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import useWorkspace from 'app/hooks/use-workspace';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { WorkspaceFormSchema, workspaceSchema } from 'app/validation/workspace.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';

const { useGetWorkspaceByIdQuery, usePostWorkspaceMutation, useUpdateWorkspaceMutation } = useWorkspace;

interface IOrdersFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const OfficesForm = (props: IOrdersFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const { control, setValue, handleSubmit } = useForm<WorkspaceFormSchema>({
    resolver: zodResolver(workspaceSchema),
  });

  const { data: detail } = useGetWorkspaceByIdQuery(selectedRecord);
  const { mutate: create, error } = usePostWorkspaceMutation(toggle, toggleSuccess);
  const { mutate: update } = useUpdateWorkspaceMutation(selectedRecord, toggle, toggleSuccess);

  const onSubmit: SubmitHandler<WorkspaceFormSchema> = data => {
    if (type === 'update') {
      update(data);
      setSelectedRecord(null);
      return;
    }
    create(data);
  };

  useEffect(() => {
    if (detail) {
      setValue('workspaceType', detail.data?.workspaceType);
      setValue('name', detail.data?.name);
      setValue('description', detail.data?.description || '');
    }
  }, [detail]);

  return (
    <Form id={FORM.OFFICES} onSubmit={handleSubmit(onSubmit)}>
      <FormInput control={control} id="workspaceType" name="workspaceType" label="Loại" type="select">
        <option selected disabled>
          Loại
        </option>
        <option value={WORKSPACE_TYPE.OFFICE}>Văn phòng</option>
        <option value={WORKSPACE_TYPE.FACTORY}>Nhà máy</option>
      </FormInput>

      <FormInput control={control} id="name" name="name" label="Bộ phận" errorMsg={error ? 'Bộ phận đã tồn tại' : ''} />

      <FormInput control={control} id="description" name="description" label="Ghi chú" type="textarea" />
    </Form>
  );
};

export default OfficesForm;
