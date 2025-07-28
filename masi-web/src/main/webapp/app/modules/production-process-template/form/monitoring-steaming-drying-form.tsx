import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormTimePicker from 'app/components/form/form-time-picker';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useProductionProcess from 'app/hooks/use-production-process';
import useSteamingProcessChecklist from 'app/hooks/use-steaming-process-checklist';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { SteamingProcessChecklistFormSchema, steamingProcessChecklistSchema } from 'app/validation/production-process.validation';

const { usePostSteamingProcessChecklist, usePatchSteamingProcessChecklist } = useSteamingProcessChecklist;
const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;
interface IMonitoringSteamingDryingFormProps {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const MonitoringSteamingDryingForm = (props: IMonitoringSteamingDryingFormProps) => {
  const { type, toggle } = props;

  const { id, workItemId } = useParams();

  const { control, handleSubmit, reset, setValue, formState } = useForm<SteamingProcessChecklistFormSchema>({
    resolver: zodResolver(steamingProcessChecklistSchema),
  });

  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { mutate: create } = usePostSteamingProcessChecklist(toggle);
  const { mutate: update } = usePatchSteamingProcessChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);


  const onSubmit: SubmitHandler<SteamingProcessChecklistFormSchema> = async data => {
    const submitValues = {
      checkDate: data.checkDate.toDate().toISOString(),
      checkTime: dayjs(data.checkTime, DATE_FORMAT.TIME_ONLY).toISOString(),
      weightNumber: data.weightNumber,
      steamerAtm: data.steamerAtm,
      steamerTemp: data.steamerTemp,
      steamerTime: dayjs(data.steamerTime, DATE_FORMAT.TIME_ONLY).toISOString(),
      tub1Atm: data.tub1Atm,
      tub1Temp: data.tub1Temp,
      tub1Time: dayjs(data.tub1Time, DATE_FORMAT.TIME_ONLY).toISOString(),
      tub2Atm: data.tub2Atm,
      tub2Time: dayjs(data.tub2Time, DATE_FORMAT.TIME_ONLY).toISOString(),
      finProductNo: data.finProductNo,
      receiverId: data.receiverId,
      workItemId,
    };

    if (type === 'update') {
      await Promise.all([
        update(submitValues),
        updateProductionProcess({
          ...dataProductionProcess,
          fromDate: data.fromDate.toDate().toISOString(),
          moId: dataProductionProcess?.moId,
        })
      ]);
    }

    else create(submitValues);

    reset();
  };

  useEffect(() => {
    if (dataProductionProcess) {
      setValue('fromDate', new DateObject(dataProductionProcess?.fromDate).add(7, 'hours'))
      setValue('checkDate', new DateObject(dataProductionProcess?.workItem?.checkLists[0]?.checkDate).add(7, 'hours'));
      setValue('checkTime', dayjs(dataProductionProcess?.workItem?.checkLists[0]?.checkTime).format(DATE_FORMAT.TIME_ONLY));
    }

    if (dataProductionProcess?.lastUpdated) {
      const { workItem } = dataProductionProcess;
      const checkWorkItem = workItem?.checkLists?.length !== 0;

      setValue('checkDate', checkWorkItem && new DateObject(workItem?.checkLists[0]?.checkDate));
      setValue('checkTime', checkWorkItem && dayjs(workItem?.checkLists[0]?.checkTime).format(DATE_FORMAT.TIME_ONLY));
      setValue('weightNumber', checkWorkItem && workItem?.checkLists[0]?.weightNumber);
      setValue('steamerAtm', checkWorkItem && workItem?.checkLists[0]?.steamerAtm);
      setValue('steamerTemp', checkWorkItem && workItem?.checkLists[0]?.steamerTemp?.toString());
      setValue('steamerTime', checkWorkItem && dayjs(workItem?.checkLists[0]?.steamerTime).format(DATE_FORMAT.TIME_ONLY));
      setValue('tub1Atm', checkWorkItem && workItem?.checkLists[0]?.tub1Atm);
      setValue('tub1Temp', checkWorkItem && workItem?.checkLists[0]?.tub1Temp?.toString());
      setValue('tub1Time', checkWorkItem && dayjs(workItem?.checkLists[0]?.tub1Time).format(DATE_FORMAT.TIME_ONLY));
      setValue('tub2Atm', checkWorkItem && workItem?.checkLists[0]?.tub2Atm);
      setValue('tub2Time', checkWorkItem && dayjs(workItem?.checkLists[0]?.tub2Time).format(DATE_FORMAT.TIME_ONLY));
      setValue('finProductNo', checkWorkItem && workItem?.checkLists[0]?.finProductNo);
      setValue('receiverId', checkWorkItem && workItem?.checkLists[0]?.receiverId);
    }
  }, [dataProductionProcess]);

  return (
    <Form<SteamingProcessChecklistFormSchema> id={FORM.MONITORING_STEAMING_DRYING} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        {/* Dòng thời ngày và thời gian */}
        <Row>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Ngày kiểm tra"
              name="checkDate"
              formState={formState}
            />
          </Col>
          <Col md={6}>
            <FormTimePicker
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Thời gian kiểm tra"
              name="checkTime"
              formState={formState}
            />
          </Col>
        </Row>

        {/* Dòng số phiếu cân */}
        <Row>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Ngày sản xuất"
              name="fromDate"
            />
          </Col>

          <Col md={6}>
            <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Số phiếu cân" name="weightNumber" />
          </Col>
        </Row>

        {/* Dòng nồi hấp: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Nồi hấp:
          </Typography>

          <Flex direction="column" gap={24}>
            <Row>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Áp suất" name="steamerAtm" />
              </Col>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Nhiệt độ" name="steamerTemp" />
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <FormTimePicker
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Thời gian"
                  name="steamerTime"
                />
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng bồn sấy 1: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Bồn sấy 1:
          </Typography>

          <Flex direction="column" gap={24}>
            <Row>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Áp suất" name="tub1Atm" />
              </Col>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Nhiệt độ" name="tub1Temp" />
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <FormTimePicker
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Thời gian"
                  name="tub1Time"
                />
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng bồn sấy 2: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Bồn sấy 2:
          </Typography>

          <Flex direction="column" gap={24}>
            <Row>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Áp suất" name="tub2Atm" />
              </Col>
              <Col md={6}>
                <FormInput
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Mã thành phẩm"
                  name="finProductNo"
                />
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <FormTimePicker
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Thời gian"
                  name="tub2Time"
                />
              </Col>

              <Col md={6}>
                <FormSelect
                  control={control}
                  id="receiverId"
                  name="receiverId"
                  placeholder="Chọn người kiểm tra"
                  label="Người kiểm tra"
                  options={employees?.data?.map(e => ({
                    label: `${e?.lastName || ''} ${e?.firstName || ''}`,
                    value: e?.id,
                  }))}
                  disabled={type === 'detail'}
                  isLoading={isLoading}
                />
              </Col>
            </Row>
          </Flex>
        </Flex>
      </Flex>
    </Form>
  );
};

export default MonitoringSteamingDryingForm;
