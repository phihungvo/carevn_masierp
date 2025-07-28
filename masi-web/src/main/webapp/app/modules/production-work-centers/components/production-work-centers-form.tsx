import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { useAppSelector } from 'app/config/store';
import useWorkCenter from 'app/hooks/use-work-center';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  ProductionWorkCenterFormSchema,
  productionWorkCenterSchema,
} from 'app/validation/production-work-center.validation';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';
import { mapProdWorkCenterStatusOptions } from '../production-work-centers-mapping';

const {
  usePostWorkCenterMutation,
  useUpdateWorkCenterMutation,
  useGetWorkCenterByIdQuery,
} = useWorkCenter;

interface IProductionWorkCentersFormProps {
  type: 'create' | 'update';
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const ProductionWorkCentersForm = (props: IProductionWorkCentersFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const account = useAppSelector(state => state.authentication.account);

  const { control, setValue, handleSubmit, formState, setError } =
    useForm<ProductionWorkCenterFormSchema>({
      resolver: zodResolver(productionWorkCenterSchema),
    });

  const { data } = useGetWorkCenterByIdQuery(selectedRecord);
  const { mutate: create } = usePostWorkCenterMutation(toggle, toggleSuccess);
  const { mutate: update } = useUpdateWorkCenterMutation(
    selectedRecord,
    toggle,
    toggleSuccess,
  );

  const toggleError = error => {
    const errorCode = error.response.data?.message;
    if (errorCode === 'error.CODE_EXISTS') {
      setError('code', { message: 'Mã cụm máy đã tồn tại' });
    }
  };

  const onSubmit: SubmitHandler<ProductionWorkCenterFormSchema> = values => {
    const submitValues = {
      code: values.code,
      name: values.name,
      status: values.status,
      lastCheckedAt: values.lastCheckedAt?.toDate()?.toISOString(),
      note: values.note,
    };
    if (type === 'update') {
      update({ ...submitValues });
      setSelectedRecord(null);
      return;
    }
    create({ ...submitValues }, { onError: toggleError });
  };

  useEffect(() => {
    if (data) {
      setValue('code', data.data?.code);
      setValue('name', data.data?.name);
      setValue('status', data.data?.status);
      setValue(
        'lastCheckedAt',
        data.data?.lastCheckedAt
          ? new DateObject(data.data?.lastCheckedAt).add(7, 'hours')
          : undefined,
      );
      setValue('note', data.data?.note);
    }
  }, [data]);

  return (
    <Form id={FORM.PRODUCTION_WORK_CENTERS} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="code"
            name="code"
            label="Mã"
            placeholder="Vui lòng nhập mã"
            disabled={type === 'update'}
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="name"
            name="name"
            label="Tên"
            placeholder="Vui lòng nhập tên"
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            setValue={setValue}
            control={control}
            id="lastCheckedAt"
            name="lastCheckedAt"
            label="Ngày kiểm tra gần nhất"
            formState={formState}
            placeholder="Vui lòng chọn ngày"
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            label="Trạng thái"
            options={mapProdWorkCenterStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            type="textarea"
            rows={3}
            control={control}
            id="note"
            name="note"
            label="Ghi chú"
            placeholder="Vui lòng nhập ghi chú"
          />
        </Col>
      </Row>
    </Form>
  );
};

export default ProductionWorkCentersForm;
