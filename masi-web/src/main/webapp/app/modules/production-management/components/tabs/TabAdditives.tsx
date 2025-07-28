import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputSelect from 'app/components/formV2/form-input-select/form-input-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import {
  additivesSchema,
  AdditivesSchema,
  ManufactureOrderSchema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { TabTableVerify } from './TabTableVerify';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetEmployeesQuery } = useEmployee;

export const TabAdditives = ({
  onSubmit,
}: {
  onSubmit: (values, complete) => void;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<AdditivesSchema>({
    resolver: zodResolver(additivesSchema),
    defaultValues: {
      sodiumMaterialUom: '1',
      sodiumCarbonateUom: '1',
      sodiumBicarbonateBatchUom: '1',
      bhtUom: '1',
    },
  });
  const { control, setValue, formState, handleSubmit, watch } = methods;
  const attributesWatch = watch('attributes');

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;
  const statusWatch = watchPrimary('status');
  const additivesWatch = watchPrimary('additives');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const rows = ['Kết quả lựa tạp chất'];

  const enumWeight = [
    { value: '1000', label: 'Tấn' },
    { value: '1', label: 'Kg' },
    { value: '0.001', label: 'gram' },
  ];

  const disabled =
    statusWatch === (MANUFACTURE_ORDER_STATUS.PRODUCTION as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  useEffect(() => {
    if (additivesWatch) {
      setValue('id', additivesWatch?.id);
      setValue('inspectionTime', additivesWatch?.inspectionTime);
      setValue('inspectorId', additivesWatch?.inspectorId);
      setValue('batchNumber', additivesWatch?.batchNumber);
      setValue('sodiumMaterial', additivesWatch?.sodiumMaterial);
      setValue('sodiumMaterialUom', additivesWatch?.sodiumMaterialUom ?? '1');
      setValue('sodiumCarbonateBatch', additivesWatch?.sodiumCarbonateBatch);
      setValue('sodiumCarbonateWeight', additivesWatch?.sodiumCarbonateWeight);
      setValue('sodiumCarbonateUom', additivesWatch?.sodiumCarbonateUom ?? '1');
      setValue(
        'sodiumBicarbonateWeight',
        additivesWatch?.sodiumBicarbonateWeight,
      );
      setValue(
        'sodiumBicarbonateBatch',
        additivesWatch?.sodiumBicarbonateBatch,
      );
      setValue(
        'sodiumBicarbonateBatchUom',
        additivesWatch?.sodiumBicarbonateBatchUom ?? '1',
      );
      setValue('bhtWeight', additivesWatch?.bhtWeight);
      setValue('bhtBatch', additivesWatch?.bhtBatch);
      setValue('bhtUom', additivesWatch?.bhtUom ?? '1');
      setValue('notes', additivesWatch?.notes);

      // Kiểm tra cảm quan
      setValue('attributes', additivesWatch?.attributes);
    }
  }, [additivesWatch]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_ADDITIVES}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Flex direction="column" gap={8}>
          <Row>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="inspectionTime"
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
                label="Người thực hiện"
                placeholder="Vui lòng chọn người thực hiện"
                options={employees?.data?.map(x => ({
                  value: x?.id,
                  label: `${x.code} - ${x.employeeProfile?.fullName}`,
                }))}
                disabled={disabled}
              />
            </Col>
            <Col md={4}></Col>

            <Col md={8}>
              <Row>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="batchNumber"
                    label="Số phiếu cân"
                    placeholder="Vui lòng nhập số phiêu cân"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputSelect
                    control={control}
                    name="sodiumMaterial"
                    label="Khối lượng nguyên liệu"
                    placeholder="Vui lòng nhập khối lượng nguyên liệu"
                    selectKey="sodiumMaterialUom"
                    selectValue={watch('sodiumMaterialUom')}
                    setValue={setValue}
                    selectOptions={enumWeight}
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="sodiumCarbonateBatch"
                    label="Số lô Natri cabonat"
                    placeholder="Vui lòng nhập số lô Natri cabonat"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputSelect
                    control={control}
                    name="sodiumCarbonateWeight"
                    label="Khối lượng Natri cabonat"
                    placeholder="Vui lòng nhập khối lượng Natri cabonat"
                    selectKey="sodiumCarbonateUom"
                    selectValue={watch('sodiumCarbonateUom')}
                    setValue={setValue}
                    selectOptions={enumWeight}
                    disabled={disabled}
                  />
                </Col>

                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="sodiumBicarbonateBatch"
                    label="Số lô Natri biocabonat"
                    placeholder="Vui lòng nhập số lô Natri biocabonat"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputSelect
                    control={control}
                    name="sodiumBicarbonateWeight"
                    label="Khối lượng Natri biocabonat"
                    placeholder="Vui lòng nhập khối lượng Natri biocabonat"
                    selectKey="sodiumBicarbonateBatchUom"
                    selectValue={watch('sodiumBicarbonateBatchUom')}
                    setValue={setValue}
                    selectOptions={enumWeight}
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="bhtBatch"
                    label="Số lô BHT"
                    placeholder="Vui lòng nhập số lô BHT"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputSelect
                    control={control}
                    name="bhtWeight"
                    label="Khối lượng BHT"
                    placeholder="Vui lòng nhập khối lượng BHT"
                    selectKey="bhtUom"
                    selectValue={watch('bhtUom')}
                    setValue={setValue}
                    selectOptions={enumWeight}
                    disabled={disabled}
                  />
                </Col>
              </Row>
            </Col>
          </Row>

          <TabTableVerify
            title="Kiểm tra cảm quan"
            rows={rows}
            name="additives.attributes"
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
