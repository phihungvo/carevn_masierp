import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  ManufactureOrderSchema,
  rawMaterialSchema,
  RawMaterialSchema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { enableDirectAdditives } from '../../production-ultis';
import { TabTableVerify } from './TabTableVerify';

const { useGetEmployeesQuery } = useEmployee;
export const TabMaterial = ({
  onSubmit,
}: {
  onSubmit: (values, complete) => void;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<RawMaterialSchema>({
    resolver: zodResolver(rawMaterialSchema),
  });
  const { control, setValue, formState, handleSubmit, watch } = methods;
  const attributesWatch = watch('attributes');

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;

  const statusWatch = watchPrimary('status');
  const rawMaterialWatch = watchPrimary('rawMaterial');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const rows = [
    'Trạng thái',
    'Mùi',
    'Tạp chất',
    'Các loại cá độc',
    'Điều kiện phương tiện vận chuyển',
  ];

  const disabled = enableDirectAdditives(statusWatch);

  useEffect(() => {
    if (rawMaterialWatch) {
      setValue('inspectionDate', rawMaterialWatch?.inspectionDate);
      setValue('inspectorId', rawMaterialWatch?.inspectorId);
      setValue('volume', rawMaterialWatch?.volume);
      setValue('weight', rawMaterialWatch?.weight);
      setValue('fishHead', rawMaterialWatch?.fishHead);
      setValue('freshFish', rawMaterialWatch?.freshFish);
      setValue('attributes', rawMaterialWatch?.attributes);
      setValue('notes', rawMaterialWatch?.notes);
    }
  }, [rawMaterialWatch]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  useEffect(() => {
    console.log(formState.errors);
  }, [formState.errors]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_RAW_MATERIAL}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Flex direction="column" gap={8}>
          <Row>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="inspectionDate"
                label="Ngày giờ kiểm tra"
                placeholder="Vui lòng chọn ngày kiểm tra"
                includeTimePicker
                disabled={disabled}
              />
            </Col>
            <Col md={4}>
              <FormSelect
                control={control}
                name="inspectorId"
                label="Người tiếp nhận"
                placeholder="Vui lòng chọn người tiếp nhận"
                options={employees?.data?.map(x => ({
                  value: x?.id,
                  label: `${x.code} - ${x.employeeProfile?.fullName}`,
                }))}
                disabled={disabled}
              />
            </Col>
            <Col md={4}></Col>
            <Col md={4}>
              <FormInputV2
                control={control}
                name="volume"
                label="Số phiếu cân"
                placeholder="Vui lòng nhập số phiêu cân"
                disabled={disabled}
              />
            </Col>
            <Col md={4}>
              <FormInputV2
                control={control}
                name="weight"
                label="Khối lượng nguyên liệu"
                placeholder="Vui lòng nhập khối lượng nguyên liệu"
                disabled={disabled}
              />
            </Col>
          </Row>

          <Flex direction="column" gap={16}>
            <Typography level="paragraph" className="bold test-check-heading">
              Loại nguyên liệu
            </Typography>

            <Row>
              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="fishHead"
                  label="Đầu cá"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="freshFish"
                  label="Cá tươi"
                  disabled={disabled}
                />
              </Col>
            </Row>
          </Flex>

          <TabTableVerify
            rows={rows}
            title="Kiểm tra cảm quan"
            name="rawMaterial.attributes"
            disabled={disabled}
            onChange={values => setValue('attributes', values)}
            data={attributesWatch}
            errors={formState?.errors?.attributes}
          />

          <FormInputV2
            control={control}
            name="notes"
            label="Ghi chú"
            placeholder="Vui lòng nhập ghi chú"
            disabled={disabled}
          />
        </Flex>
        <div className="production-card__footer">
          <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT">
            <ButtonV2
              type="submit"
              onClick={() => setCompleteState(true)}
              disabled={disabled}
              style={{ borderColor: '#027A48', color: '#027A48' }}
            >
              Hoàn thành
            </ButtonV2>
            <ButtonV2
              variant="solid"
              color="blue"
              type="submit"
              disabled={disabled}
            >
              Lưu
            </ButtonV2>
          </AuthGuard>
        </div>
      </Form>
    </FormProvider>
  );
};
