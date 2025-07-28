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
  productionSchema,
  ProductionSchema,
  productionStep1Schema,
  ProductionStep1Schema,
  productionStep2Schema,
  ProductionStep2Schema,
  productionStep3Schema,
  ProductionStep3Schema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { enableDirectPackaging } from '../../production-ultis';
import { TabTableVerify } from './TabTableVerify';

const { useGetEmployeesQuery } = useEmployee;
export const TabProduction = ({
  onSubmit,
  handleCompleteWithoutCall,
}: {
  onSubmit: (values, complete) => void;
  handleCompleteWithoutCall: (res: boolean) => void;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);
  const [completeStep, setCompleteStep] = useState<boolean>(false);

  const [tab, setTab] = useState<string>('0');
  const [tabDone, setTabDone] = useState<number>(0);

  const methods = useForm<ProductionSchema>({
    resolver: zodResolver(productionSchema),
  });
  const { setValue, watch, formState } = methods;
  const stepDoneWatch = watch('attributes.stepDone');

  const methodsStep1 = useForm<ProductionStep1Schema>({
    resolver: zodResolver(productionStep1Schema),
  });
  const {
    control: cStep1,
    setValue: svStep1,
    formState: fsStep1,
    handleSubmit: hsStep1,
    watch: wStep1,
  } = methodsStep1;

  const methodsStep2 = useForm<ProductionStep2Schema>({
    resolver: zodResolver(productionStep2Schema),
  });
  const {
    control: cStep2,
    setValue: svStep2,
    formState: fsStep2,
    handleSubmit: hsStep2,
  } = methodsStep2;

  const methodsStep3 = useForm<ProductionStep3Schema>({
    resolver: zodResolver(productionStep3Schema),
  });
  const {
    control: cStep3,
    setValue: svStep3,
    formState: fsStep3,
    handleSubmit: hsStep3,
    watch: wStep3,
  } = methodsStep3;

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;

  const statusWatch = watchPrimary('status');
  const productionWatch = watchPrimary('production');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const generateTabItem = (text: string, value: string) => {
    return (
      <Typography
        level={6}
        style={{
          color: tab === value ? '#365FBF' : undefined,
          cursor:
            Number(value) > (stepDoneWatch ?? 0) ? 'not-allowed' : 'pointer',
        }}
        onClick={() => {
          if (!(Number(value) > (stepDoneWatch ?? 0))) setTab(value);
        }}
      >
        {text}
      </Typography>
    );
  };

  const rowsFurnace = ['Ống dẫn khí', 'Lò đốt'];
  const rowsDryingFurnace = [
    'Ống dẫn khí',
    'Đồng hồ',
    'Thành lò sấy',
    'Van xả/đóng',
    'Lưới máy dàng',
    'Nam châm',
    'Máy đóng gói',
    'Máy nghiền',
    'Máy trộn',
  ];
  const rows = ['Đầu ca', 'Cuối ca'];

  const handlePreSubmitStep1 = (
    values: ProductionStep1Schema,
    isComplete: boolean,
  ) => {
    const submitValues = watch();
    submitValues.attributes.stepDone = completeStep
      ? Number(tab) + 1
      : Number(tab);
    submitValues.attributes.step = Number(tab) + 1;
    submitValues.monitorMachineOperation = { ...values };
    onSubmit(submitValues, isComplete);
  };

  const handlePreSubmitStep2 = (
    values: ProductionStep2Schema,
    isComplete: boolean,
  ) => {
    const submitValues = watch();
    submitValues.attributes.stepDone = completeStep
      ? Number(tab) + 1
      : Number(tab);
    submitValues.attributes.step = Number(tab) + 1;
    submitValues.steamDryingMonitoring = { ...values };
    onSubmit(submitValues, isComplete);
  };

  const handlePreSubmitStep3 = (
    values: ProductionStep3Schema,
    isComplete: boolean,
  ) => {
    const submitValues = watch();
    submitValues.attributes.stepDone = completeStep
      ? Number(tab) + 1
      : Number(tab);
    submitValues.attributes.step = Number(tab) + 1;
    submitValues.checkMagnetGrid = { ...values };
    onSubmit(submitValues, isComplete);
  };

  const disabled = enableDirectPackaging(statusWatch);

  const disabledStep1 = disabled || stepDoneWatch >= 1;
  const disabledStep2 = disabled || stepDoneWatch >= 2;
  const disabledStep3 = disabled || stepDoneWatch >= 3;

  useEffect(() => {
    if (productionWatch) {
      setValue('id', productionWatch?.id);
      setValue('monitorMachineOperation.inspectionTime', productionWatch.monitorMachineOperation?.inspectionTime);
      setValue('monitorMachineOperation.manufactureDate', productionWatch.monitorMachineOperation?.manufactureDate );
      setValue('monitorMachineOperation.furnace', productionWatch.monitorMachineOperation?.furnace);
      setValue('monitorMachineOperation.dryingFurnace',productionWatch.monitorMachineOperation?.dryingFurnace);
      setValue('steamDryingMonitoring.inspectionTime', productionWatch?.steamDryingMonitoring?.inspectionTime);
      setValue('steamDryingMonitoring.manufactureDate', productionWatch?.steamDryingMonitoring?.manufactureDate);
      setValue('steamDryingMonitoring.volume', productionWatch?.steamDryingMonitoring?.volume);
      setValue('steamDryingMonitoring.note', productionWatch?.steamDryingMonitoring?.note);
      setValue('steamDryingMonitoring.steamer', productionWatch?.steamDryingMonitoring?.steamer);
      setValue('steamDryingMonitoring.dryer1', productionWatch?.steamDryingMonitoring?.dryer1);
      setValue('steamDryingMonitoring.dryer2', productionWatch?.steamDryingMonitoring?.dryer2);
      setValue('checkMagnetGrid.inspectionTime', productionWatch?.checkMagnetGrid?.inspectionTime);
      setValue('checkMagnetGrid.manufactureDate', productionWatch?.checkMagnetGrid?.manufactureDate);
      setValue('checkMagnetGrid.manufactureBy', productionWatch?.checkMagnetGrid?.manufactureBy);
      setValue('checkMagnetGrid.examiner', productionWatch?.checkMagnetGrid?.examiner);
      setValue('checkMagnetGrid.description', productionWatch?.checkMagnetGrid?.description);
      setValue('checkMagnetGrid.note', productionWatch?.checkMagnetGrid?.note);
      setValue('checkMagnetGrid.magnet', productionWatch?.checkMagnetGrid?.magnet);
      setValue('checkMagnetGrid.floorGrid', productionWatch?.checkMagnetGrid?.floorGrid);
      setValue('checkMagnetGrid.grindingGrid', productionWatch?.checkMagnetGrid?.grindingGrid);

      svStep1('inspectionTime', productionWatch.monitorMachineOperation?.inspectionTime);
      svStep1('manufactureDate', productionWatch.monitorMachineOperation?.manufactureDate);
      svStep1('furnace', productionWatch.monitorMachineOperation?.furnace);
      svStep1('dryingFurnace', productionWatch.monitorMachineOperation?.dryingFurnace);

      svStep2('inspectionTime', productionWatch?.steamDryingMonitoring?.inspectionTime);
      svStep2('manufactureDate', productionWatch?.steamDryingMonitoring?.manufactureDate);
      svStep2('volume', productionWatch?.steamDryingMonitoring?.volume);
      svStep2('note', productionWatch?.steamDryingMonitoring?.note);
      svStep2('steamer', productionWatch?.steamDryingMonitoring?.steamer);
      svStep2('dryer1', productionWatch?.steamDryingMonitoring?.dryer1);
      svStep2('dryer2', productionWatch?.steamDryingMonitoring?.dryer2);

      svStep3('inspectionTime', productionWatch?.checkMagnetGrid?.inspectionTime);
      svStep3('manufactureDate', productionWatch?.checkMagnetGrid?.manufactureDate);
      svStep3('manufactureBy', productionWatch?.checkMagnetGrid?.manufactureBy);
      svStep3('examiner', productionWatch?.checkMagnetGrid?.examiner);
      svStep3('description', productionWatch?.checkMagnetGrid?.description);
      svStep3('note', productionWatch?.checkMagnetGrid?.note);
      svStep3('magnet', productionWatch?.checkMagnetGrid?.magnet);
      svStep3('floorGrid', productionWatch?.checkMagnetGrid?.floorGrid);
      svStep3('grindingGrid', productionWatch?.checkMagnetGrid?.grindingGrid);

      setValue('attributes', productionWatch?.attributes);
      setTabDone(productionWatch?.attributes?.stepDone ?? 0);
    }
  }, [productionWatch]);

  useEffect(() => {
    if (completeState) {
      setCompleteState(false);
      handleCompleteWithoutCall(false);
    }
    if (completeStep) {
      setCompleteStep(false);
      handleCompleteWithoutCall(false);
    }
  }, [watch()]);

  const renderButtonFooter = (key: number) => {
    const disabled = tabDone >= key;
    return (
      <div className="production-card__footer">
        <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT">
          <ButtonV2
            type="submit"
            onClick={() => {
              setCompleteStep(true);
              if (Number(tab) + 1 === 3) setCompleteState(true);
              else handleCompleteWithoutCall(true);
            }}
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
    );
  };

  return (
    <Flex direction="row" gap={16}>
      <Flex
        direction="column"
        style={{ width: '300px' }}
        className="productiontableft"
        gap={8}
      >
        {generateTabItem('Giám sát hoạt động máy', '0')}
        {generateTabItem('Giám sát hấp sấy', '1')}
        {generateTabItem('Kiểm tra nam châm - lưới', '2')}
      </Flex>

      {tab === '0' && (
        <Flex direction="column" style={{ width: '100%' }}>
          <FormProvider {...methodsStep1}>
            <Form
              id={FORM.MANUFACTURE_ORDER_PRODUCTION_STEP1}
              onSubmit={hsStep1(values =>
                handlePreSubmitStep1(values, completeState),
              )}
            >
              <Row>
                <Col md={4}>
                  <FormDatePickerV2
                    control={cStep1}
                    formState={fsStep1}
                    setValue={svStep1}
                    name="inspectionTime"
                    label="Thời gian kiểm tra"
                    placeholder="Vui lòng chọn thời gian kiểm tra"
                    includeTimePicker
                    disabled={disabledStep1}
                  />
                </Col>
                <Col md={4}>
                  <FormDatePickerV2
                    control={cStep1}
                    formState={fsStep1}
                    setValue={svStep1}
                    name="manufactureDate"
                    label="Ngày sản xuất"
                    placeholder="Vui lòng chọn ngày sản xuất"
                    disabled={disabledStep1}
                  />
                </Col>
              </Row>

              <TabTableVerify
                title="Lò đốt"
                rows={rowsFurnace}
                name="production.monitorMachineOperation.furnace"
                disabled={disabledStep1}
                onChange={values => svStep1('furnace', values)}
                data={wStep1('furnace')}
                errors={fsStep1?.errors?.furnace}
              />

              <div style={{ height: '8px' }} />

              <TabTableVerify
                title="Lò sấy"
                rows={rowsDryingFurnace}
                name="production.monitorMachineOperation.dryingFurnace"
                disabled={disabledStep1}
                onChange={values => svStep1('dryingFurnace', values)}
                data={wStep1('dryingFurnace')}
                errors={fsStep1?.errors?.dryingFurnace}
              />

              {renderButtonFooter(1)}
            </Form>
          </FormProvider>
        </Flex>
      )}

      {tab === '1' && (
        <Flex direction="column" gap={8} style={{ width: '100%' }}>
          <FormProvider {...methodsStep2}>
            <Form
              id={FORM.MANUFACTURE_ORDER_PRODUCTION_STEP2}
              onSubmit={hsStep2(values =>
                handlePreSubmitStep2(values, completeState),
              )}
            >
              <Row>
                <Col md={3}>
                  <FormDatePickerV2
                    control={cStep2}
                    formState={fsStep2}
                    setValue={svStep2}
                    name="inspectionTime"
                    label="Thời gian kiểm tra"
                    placeholder="Vui lòng chọn thời gian kiểm tra"
                    includeTimePicker
                    disabled={disabledStep2}
                  />
                </Col>
                <Col md={3}>
                  <FormDatePickerV2
                    control={cStep2}
                    formState={fsStep2}
                    setValue={svStep2}
                    name="manufactureDate"
                    label="Ngày sản xuất"
                    placeholder="Vui lòng chọn ngày sản xuất"
                    disabled={disabledStep2}
                  />
                </Col>
                <Col md={3}>
                  <FormInputV2
                    control={cStep2}
                    name="volume"
                    label="Số phiếu cân"
                    placeholder="Vui lòng nhập số phiếu cân"
                    disabled={disabledStep2}
                  />
                </Col>
              </Row>

              <Flex direction="column" gap={16}>
                <Typography
                  level="paragraph"
                  className="bold test-check-heading"
                >
                  Nồi hấp
                </Typography>

                <Row>
                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="steamer.pressure"
                      label="Áp suất"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="steamer.temperature"
                      label="Nhiệt độ"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="steamer.testTime"
                      label="Thời gian kiểm tra"
                      disabled={disabledStep2}
                    />
                  </Col>
                </Row>
              </Flex>

              <Flex direction="column" gap={16}>
                <Typography
                  level="paragraph"
                  className="bold test-check-heading"
                >
                  Bồn sấy 1
                </Typography>

                <Row>
                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer1.pressure"
                      label="Áp suất"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer1.temperature"
                      label="Nhiệt độ"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer1.testTime"
                      label="Thời gian kiểm tra"
                      disabled={disabledStep2}
                    />
                  </Col>
                </Row>
              </Flex>

              <Flex direction="column" gap={16}>
                <Typography
                  level="paragraph"
                  className="bold test-check-heading"
                >
                  Bồn sấy 2
                </Typography>

                <Row>
                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer2.pressure"
                      label="Áp suất"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer2.temperature"
                      label="Nhiệt độ"
                      disabled={disabledStep2}
                    />
                  </Col>

                  <Col md={3}>
                    <FormInputV2
                      control={cStep2}
                      name="dryer2.testTime"
                      label="Thời gian kiểm tra"
                      disabled={disabledStep2}
                    />
                  </Col>
                </Row>
              </Flex>

              <FormInputV2
                control={cStep2}
                name="note"
                label="Ghi chú"
                placeholder="Vui lòng nhập ghi chú"
                disabled={disabledStep2}
              />

              {renderButtonFooter(2)}
            </Form>
          </FormProvider>
        </Flex>
      )}

      {tab === '2' && (
        <Flex direction="column" gap={8} style={{ width: '100%' }}>
          <FormProvider {...methodsStep3}>
            <Form
              id={FORM.MANUFACTURE_ORDER_PRODUCTION_STEP3}
              onSubmit={hsStep3(values =>
                handlePreSubmitStep3(values, completeState),
              )}
            >
              <Row>
                <Col md={4}>
                  <FormDatePickerV2
                    control={cStep3}
                    formState={fsStep3}
                    setValue={svStep3}
                    name="inspectionTime"
                    label="Ngày kiểm tra"
                    placeholder="Vui lòng chọn ngày kiểm tra"
                    disabled={disabledStep3}
                  />
                </Col>
                <Col md={4}>
                  <FormDatePickerV2
                    control={cStep3}
                    formState={fsStep3}
                    setValue={svStep3}
                    name="manufactureDate"
                    label="Ngày sản xuất"
                    placeholder="Vui lòng chọn ngày sản xuất"
                    disabled={disabledStep3}
                  />
                </Col>
                <Col md={4}>
                  <FormInputV2
                    control={cStep3}
                    name="description"
                    label="Mô tả thành phẩm"
                    placeholder="Vui lòng nhập mô tả thành phẩm"
                    disabled={disabledStep3}
                  />
                </Col>
                <Col md={4}>
                  <FormSelect
                    control={cStep3}
                    name="manufactureBy"
                    label="Người kiểm tra"
                    placeholder="Vui lòng chọn người kiểm tra"
                    options={employees?.data?.map(x => ({
                      value: x?.id,
                      label: `${x.code} - ${x.employeeProfile?.fullName}`,
                    }))}
                    disabled={disabledStep3}
                  />
                </Col>
                <Col md={4}>
                  <FormSelect
                    control={cStep3}
                    name="examiner"
                    label="Người thẩm tra"
                    placeholder="Vui lòng chọn người thẩm tra"
                    options={employees?.data?.map(x => ({
                      value: x?.id,
                      label: `${x.code} - ${x.employeeProfile?.fullName}`,
                    }))}
                    disabled={disabledStep3}
                  />
                </Col>
              </Row>

              <TabTableVerify
                title="Tình trạng nam châm"
                rows={rows}
                name="production.checkMagnetGrid.magnet"
                disabled={disabledStep3}
                onChange={values => svStep3('magnet', values)}
                data={wStep3('magnet')}
                errors={fsStep3?.errors?.magnet}
              />

              <div style={{ height: '8px' }} />

              <TabTableVerify
                title="Tình trạng lưới sàn 4mm"
                rows={rows}
                name="production.checkMagnetGrid.floorGrid"
                disabled={disabledStep3}
                onChange={values => svStep3('floorGrid', values)}
                data={wStep3('floorGrid')}
                errors={fsStep3?.errors?.floorGrid}
              />

              <div style={{ height: '8px' }} />

              <TabTableVerify
                title="Tình trạng lưới nghiền 3mm"
                rows={rows}
                name="production.checkMagnetGrid.grindingGrid"
                disabled={disabledStep3}
                onChange={values => svStep3('grindingGrid', values)}
                data={wStep3('grindingGrid')}
                errors={fsStep3?.errors?.grindingGrid}
              />

              <FormInputV2
                control={cStep3}
                name="note"
                label="Ghi chú"
                placeholder="Vui lòng nhập ghi chú"
                disabled={disabledStep3}
              />

              {renderButtonFooter(3)}
            </Form>
          </FormProvider>
        </Flex>
      )}
    </Flex>
  );
};
