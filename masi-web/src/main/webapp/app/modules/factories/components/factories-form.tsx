import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import FormSelectV2 from 'app/components/formV2/form-select/form-select';
import useEmployee from 'app/hooks/use-employee';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import { FactoriesIsActiveOptions } from 'app/shared/model/enumerations/factories.enum';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IFactoryLogistics } from 'app/shared/model/factory-logistics.model';
import {
  factoriesSchema,
  FactoriesSchema,
} from 'app/validation/factories.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { useGetEmployeeProfilesQuery } = useEmployee;
const { useCreateFactory, useGetFactoryByIdQuery, useUpdateFactoryMutation } =
  useFactoryLogistics;

interface IFactoriesFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const FactoriesForm = (props: IFactoriesFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const { id } = useParams();
  const { control, setValue, handleSubmit, reset } = useForm<FactoriesSchema>({
    resolver: zodResolver(factoriesSchema),
  });

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: detail } = useGetFactoryByIdQuery(selectedRecord || id);
  const { mutate: update } = useUpdateFactoryMutation(
    selectedRecord || id,
    onOkSuccess,
  );
  const { mutate: create } = useCreateFactory(onOkSuccess);

  const { data: employee } = useGetEmployeeProfilesQuery();

  const onSubmit: SubmitHandler<FactoriesSchema> = values => {
    const factoryData: IFactoryLogistics = {
      code: values.code,
      name: values.name,
      address: values.address,
      attribute: { location: { x: values.locationX, y: values.locationY } },
      employeeOwnerId: values.employeeOwnerId,
      company: values.company,
      note: values.note,
      isActive: values.status === '1' ? true : false,
    };
    if (type === 'update') {
      update(factoryData, {
        onSuccess: () => {
          reset();
        },
      });
      return;
    }
    create(factoryData, {
      onSuccess: () => {
        reset();
      },
    });
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail.code);
      setValue('name', detail.name);
      setValue('address', detail.address);
      setValue('employeeOwnerId', detail.employeeOwnerId);
      setValue('locationX', detail?.attribute?.location?.x);
      setValue('locationY', detail?.attribute?.location?.y);
      setValue('company', detail.company);
      setValue('note', detail.note);
      setValue('status', detail.isActive ? '1' : '0');
    }
  }, [detail]);

  return (
    <Form id={FORM.FACTORIES} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="code"
            name="code"
            label="Mã nhà máy"
            placeholder="Vui lòng nhập mã nhà máy"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="name"
            name="name"
            label="Tên nhà máy"
            placeholder="Vui lòng nhập tên nhà máy"
          />
        </Col>
        <Col md={3}>
          <FormSelect
            control={control}
            id="company"
            name="company"
            label="Trực thuộc"
            placeholder="Chọn"
            options={[
              { value: 'MASI', label: 'Masi' },
              { value: 'MMS', label: 'MMS' },
            ]}
            disabled={type === 'update'}
          />
        </Col>
        <Col md={3}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            label="Tình trạng"
            placeholder="Chọn"
            options={FactoriesIsActiveOptions?.map(e => ({
              value: e.value.toString(),
              label: e.label,
            }))}
          />
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="address"
            name="address"
            label="Địa chỉ"
            placeholder="Vui lòng nhập địa chỉ"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="locationX"
            name="locationX"
            label="Vĩ độ"
            placeholder="Điền"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="locationY"
            name="locationY"
            label="Kinh độ"
            placeholder="Điền"
          />
        </Col>
      </Row>

      <Row>
        <Col md={3}>
          <FormSelect
            control={control}
            id="employeeOwnerId"
            name="employeeOwnerId"
            label="Người quản lý"
            placeholder="Người quản lý"
            options={employee?.data?.map(e => ({
              value: e?.id,
              label: `${e.employeeCode} - ${e.fullName}`,
            }))}
          />
        </Col>
      </Row>

      <Row>
        <Col md={12}>
          <FormInputV2
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

export default FactoriesForm;
