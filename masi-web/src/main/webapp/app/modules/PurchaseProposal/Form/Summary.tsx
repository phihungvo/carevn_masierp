import CreatedBySelect from 'app/components/CreatedBySelect/CreatedBySelect';
import FormSelect from 'app/components/form/form-select';
import FormSelectV3 from 'app/components/form/form-select-v3';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import { BasicSelect } from 'app/shared/model/arr-obj.model';
import { SuppliersRequestSchemaV2Type } from 'app/validation/supplies-request.validation';
import { useEffect } from 'react';
import { Controller, useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { useGetSuppliesRequestsType, useNextCodeSuppliesRequest } = useSuppliesRequest

type Props = {
};

const Summary = ({}: Props) => {
  const { control, setValue, getValues, watch } = useFormContext<SuppliersRequestSchemaV2Type>();
  const isApproved = watch('tmp.isApproved');
  const isWaiting = watch('tmp.isWaiting');

  const param = useParams();
  const id_detail = param?.id;
  const isEditMode: boolean = !!id_detail;

  const supplies_type_query = useGetSuppliesRequestsType({ page: 0, size: 2000000 }, {
    select: (res) => {
      return res?.['data']?.data.map((item) => ({ label: item.name, value: item.id }));
    }
  })

  const nextCode = useNextCodeSuppliesRequest()

  useEffect(() => {
    nextCode?.data && !isEditMode && setValue('code', nextCode.data)
  }, [nextCode])

  return (
    <>
      <Row>
        <Col>
          <Typography level={5}>Thông tin chung</Typography>
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          <FormInputV2
            label="Mã đề xuất"
            control={control}
            name="code"
            disabled
          />
        </Col>
        <Col md={4}>
          <FormDatePickerV2
            label="Ngày lập"
            control={control}
            name="requestDate"
            disabled={isApproved || isWaiting}
            setValue={setValue}
          />
        </Col>
        <Col md={4}>
          <FormSelect
            label="Loại đề xuất"
            control={control}
            name="requestTypeId"
            placeholder="Nhập"
            options={supplies_type_query?.data || []}
            disabled={isApproved || isWaiting}
          />
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          <Controller
            control={control}
            name='createdByEmployeeId'
            render={({ field }) => (
              <FormSelectV3
                {...field}
                label='Người tạo'
                SelectComponent={
                  <CreatedBySelect
                    selectedKey={field?.value}
                    onChange={(data: BasicSelect) => field.onChange(data?.value)}
                    isDisabled={true}
                  />
                }
              />
            )}
          />
        </Col>
        <Col md={4}>
          <FormInputV2
            label="Bộ phận"
            control={control}
            name="tmp.staffName"
            disabled
          />
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <FormInputV2
            label="Nội dung"
            control={control}
            name="note"
            placeholder="Nhập nội dung"
            disabled={isApproved || isWaiting}
          />
        </Col>
      </Row>
    </>
  );
};

export default Summary;
