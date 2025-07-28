import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormSelect from 'app/components/form/form-select';
import { UNIFORM_NOT_EMP_FOUND, UNIFORM_RETURN_TOO_MUCH } from 'app/constants/error';
import useUniform from 'app/hooks/use-uniform';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { uniformReturnSchema, UniformReturnSchema } from 'app/validation/uniform.validation';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Alert, Col, Row } from 'reactstrap';
import UniformListFields from './uniform-list-fields';
import { UNIFORM_RELEASE_TYPE } from 'app/shared/model/enumerations/uniform.model';

const { usePostUniformReturn, useUniformReleases, useUniformReleaseById } = useUniform;

interface IUniformFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
}

const UniformForm = (props: IUniformFormProps) => {
  const { toggle, toggleSuccess } = props;

  const methods = useForm<UniformReturnSchema>({
    resolver: zodResolver(uniformReturnSchema),
  });

  const { control, setValue, handleSubmit, watch, formState } = methods;

  const releaseId = watch('releaseId');

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
  };

  const { data: uniformReleases, isLoading: uniformReleaseLoading } = useUniformReleases({ type: [UNIFORM_RELEASE_TYPE.SUPPORT] });
  const { data: uniformReleaseDetail } = useUniformReleaseById(watch('releaseId'));
  const { mutate: create, error } = usePostUniformReturn(onOkSuccess);

  useEffect(() => {
    if (releaseId) {
      uniformReleaseDetail?.uniformFormDetails?.forEach((u, index) => {
        setValue(`returnDetails.${index}.uniformId`, u.uniformId);
        setValue(`returnDetails.${index}.initQuantity`, u.quantity?.toString());
        setValue(`returnDetails.${index}.returnedQuantity`, u.returnedQuantity?.toString());
      });
    }
  }, [releaseId, uniformReleaseDetail]);

  const onSubmit: SubmitHandler<UniformReturnSchema> = values => {
    create({
      date: values.date?.toDate()?.toISOString(),
      // employeeId: values.employeeId,
      uniformReleaseId: values.releaseId,
      returnDetails: values?.returnDetails
        ?.filter(e => e.quantity !== undefined)
        ?.map(e => ({
          uniformId: e.uniformId,
          quantity: Number(e.quantity),
        })),
    });
  };

  const isNotEmpFound = error?.response?.data?.detail === UNIFORM_NOT_EMP_FOUND;
  const isReturnTooMuch = error?.response?.data?.message === UNIFORM_RETURN_TOO_MUCH;

  return (
    <>
      {(error && isReturnTooMuch) || uniformReleaseDetail?.remaining === 0 ? (
        <Alert color="danger" className="mb-4">
          Số lượng còn lại không đủ
        </Alert>
      ) : null}

      <FormProvider {...methods}>
        <Form id={FORM.ORDER} onSubmit={handleSubmit(onSubmit)}>
          <Row>
            <Col md={6}>
              <FormDatePicker setValue={setValue} control={control} id="date" name="date" label="Ngày" formState={formState} />
            </Col>

            <Col md={6}>
              <FormSelect
                control={control}
                id="releaseId"
                name="releaseId"
                placeholder="Chọn đơn xuất"
                label="Đơn xuất"
                options={uniformReleases?.data?.map(e => ({
                  label: e?.code,
                  value: e?.id,
                }))}
                isLoading={uniformReleaseLoading}
              />
            </Col>

            <UniformListFields />
          </Row>
        </Form>
      </FormProvider>
    </>
  );
};

export default UniformForm;
