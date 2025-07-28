import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { useAppSelector } from 'app/config/store';
import useEmployee from 'app/hooks/use-employee';
import useProductionQualityDisposal from 'app/hooks/use-production-quality-disposal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { ISampleDisposal } from 'app/shared/model/production-quality-control.model';
import {
  ProductionQualityCancelFormSchema,
  productionQualityCancelSchema,
} from 'app/validation/production-quality.validation';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { usePostQualityDisposal, usePatchQualityDisposal } =
  useProductionQualityDisposal;
const { useGetEmployeesQuery } = useEmployee;

interface ICancelTestingTemplateFormProps {
  toggle: () => void;
  selectRecord?: string;
  detail?: ISampleDisposal;
  status: PRODUCTION_QUALITY_STATUS;
  statusM: MANUFACTURE_ORDER_STATUS;
  parentId?: string;
}

const CancelTestingTemplateForm = (props: ICancelTestingTemplateFormProps) => {
  const { toggle, detail, selectRecord, status, parentId, statusM } = props;

  const account = useAppSelector(state => state.authentication.account);

  const { id } = useParams();

  const { control, handleSubmit, setValue, formState } =
    useForm<ProductionQualityCancelFormSchema>({
      resolver: zodResolver(productionQualityCancelSchema),
      defaultValues: {
        requestDate: new DateObject(),
        requesterId: account?.id,
        quantitySampleNo: detail?.quantitySampleNo,
        quantitySaveDate: detail?.quantitySaveDate,
      },
    });

  const { data: employees } = useGetEmployeesQuery();
  const { mutate } = usePostQualityDisposal(toggle);
  const { mutate: update } = usePatchQualityDisposal(toggle);

  const onSubmit: SubmitHandler<ProductionQualityCancelFormSchema> = data => {
    const submitValues = {
      requestDate: data.requestDate.toDate().toISOString(),
      disposalNote: data.disposalNote,
      quantitySampleNo: data.quantitySampleNo,
      quantitySaveDate: data.quantitySaveDate.toDate().toISOString(),
      quantityReleaseDate: data.quantityReleaseDate.toDate().toISOString(),
      disposalMethod: data.disposalMethod,
      disposalResult: data.disposalResult,
      reviewerId: data.reviewerId,
      requesterId: data.requesterId,
      qualitySampleCheckId: parentId || id,
      involveEmployee: account?.id,
    };
    if (selectRecord) update({ ...submitValues, id: data?.id });
    else mutate({ ...submitValues });
  };

  const disabled =
    statusM === MANUFACTURE_ORDER_STATUS.CANCELLED ||
    status === PRODUCTION_QUALITY_STATUS.DISPOSED ||
    status === PRODUCTION_QUALITY_STATUS.REJECTED;

  useEffect(() => {
    if (detail) {
      setValue('id', detail.id);
      setValue('disposalNote', detail.disposalNote);
      setValue('quantitySampleNo', detail.quantitySampleNo);
      setValue('quantitySaveDate', new DateObject(detail.quantitySaveDate));
      setValue(
        'quantityReleaseDate',
        new DateObject(detail.quantityReleaseDate),
      );
      setValue('disposalMethod', detail.disposalMethod);
      setValue('disposalResult', detail.disposalResult);
      setValue('reviewerId', detail.reviewerId);
    }
  }, [detail]);

  return (
    <Form<ProductionQualityCancelFormSchema>
      id={FORM.CANCEL_TESTING_TEMPLATE}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Row>
        <Col md={6}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            setValue={setValue}
            name="requestDate"
            label="Ngày tạo đơn"
            placeholder="Vui lòng chọn ngày tạo đơn"
            disabled
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="requesterId"
            name="requesterId"
            label="Người tạo"
            placeholder="Vui lòng chọn người tạo"
            options={employees?.data?.map(x => ({
              value: x?.id,
              label: `${x?.code} - ${x?.employeeProfile?.fullName}`,
            }))}
            disabled
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            control={control}
            id="disposalNote"
            name="disposalNote"
            label="Lý do"
            placeholder="Vui lòng nhập lý do"
            disabled={disabled}
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="quantitySampleNo"
            name="quantitySampleNo"
            label="Mã mẫu"
            placeholder="Vui lòng nhập mã mẫu"
            disabled
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            control={control}
            id="quantitySaveDate"
            name="quantitySaveDate"
            label="Ngày lưu"
            placeholder="Vui lòng chọn ngày lưu"
            disabled
          />
        </Col>

        <Col md={6}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            setValue={setValue}
            id="quantityReleaseDate"
            name="quantityReleaseDate"
            label="Ngày xả"
            placeholder="Vui lòng chọn ngày ngày xả"
            disabled={disabled}
          />
        </Col>

        <Col md={12}>
          <FormInputV2
            type="textarea"
            rows={2}
            control={control}
            id="disposalMethod"
            name="disposalMethod"
            label="Phương pháp hủy"
            placeholder="Vui lòng nhập phương pháp hủy"
            disabled={disabled}
          />
        </Col>

        <Col md={6}>
          <FormInputV2
            control={control}
            id="disposalResult"
            name="disposalResult"
            label="Kết quả hủy"
            placeholder="Vui lòng nhập kết quả hủy"
            disabled={disabled}
          />
        </Col>

        <Col md={6}>
          <FormSelect
            control={control}
            id="reviewerId"
            name="reviewerId"
            label="Người phê duyệt"
            placeholder="Chọn người phê duyệt"
            options={employees?.data?.map(x => ({
              value: x?.id,
              label: `${x?.code} - ${x?.employeeProfile?.fullName}`,
            }))}
            disabled={disabled}
          />
        </Col>
      </Row>
    </Form>
  );
};

export default CancelTestingTemplateForm;
