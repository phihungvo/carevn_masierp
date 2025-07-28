import { zodResolver } from '@hookform/resolvers/zod';
import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, FormGroup, Label, Row } from 'reactstrap';

import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormTimePicker from 'app/components/form/form-time-picker';
import Input from 'app/components/input/input';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useProductionProcess from 'app/hooks/use-production-process';
import useReceiveMaterialChecklist from 'app/hooks/use-receive-material-checklist';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IListOptionMaterialType, mapMaterialTypes } from 'app/shared/model/enumerations/production-process.model';
import { MAX_NOTE_LENGTH, ReceiveMaterialCheckListFormSchema, receiveMaterialCheckListSchema } from 'app/validation/production-process.validation';

const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;
const { usePostReceiveMaterialChecklist, usePatchReceiveMaterialChecklist } = useReceiveMaterialChecklist;

interface IMaterialReceiptMonitoringForm {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const MaterialReceiptMonitoringForm = (props: IMaterialReceiptMonitoringForm) => {
  const listOptionMaterialType: IListOptionMaterialType[] = mapMaterialTypes('vi');

  const { toggle, type } = props;

  const { id, workItemId } = useParams();


  const { control, handleSubmit, setValue, watch, reset, trigger, formState } = useForm<ReceiveMaterialCheckListFormSchema>({
    resolver: zodResolver(receiveMaterialCheckListSchema),
  });

  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { mutate: create } = usePostReceiveMaterialChecklist(toggle);
  const { mutate: update } = usePatchReceiveMaterialChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);
  const { data: employees, isLoading } = useGetEmployeesQuery();

  const onSubmit: SubmitHandler<ReceiveMaterialCheckListFormSchema> = async data => {
    const submitValues = {
      checkDate: data.checkDate.toDate().toISOString(),
      checkTime: dayjs(data.checkTime, DATE_FORMAT.TIME_ONLY).toISOString(),
      weightNumber: data.weightNumber,
      transportCondition: data.transportCondition,
      checkStatus: data.checkStatus,
      statusNote: data.statusNote,
      checkSmell: data.checkSmell,
      smellNote: data.smellNote,
      checkImpurity: data.checkImpurity,
      impurityNote: data.impurityNote,
      checkPoison: data.checkPoison,
      poisonNote: data?.poisonNote,
      receiverId: data.receiverId,
      note: data.note,
      workItemId,
      transportNote: data.transportNote,
      weight: data.weight,
      materialType: data.materialType,
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

    if (dataProductionProcess?.workItem?.checkLists[0]?.lastUpdated) {
      const { workItem } = dataProductionProcess;
      const checkWorkItem = workItem?.checkLists?.length !== 0;

      setValue('checkDate', checkWorkItem && new DateObject(workItem?.checkLists[0]?.checkDate).add(7, 'hours'));
      setValue('checkTime', checkWorkItem && dayjs(workItem?.checkLists[0]?.checkTime).format(DATE_FORMAT.TIME_ONLY));
      setValue('weightNumber', checkWorkItem && workItem?.checkLists[0]?.weightNumber);
      setValue('transportCondition', checkWorkItem && workItem?.checkLists[0]?.transportCondition);
      setValue('checkStatus', checkWorkItem && workItem?.checkLists[0]?.checkStatus);
      setValue('statusNote', checkWorkItem && workItem?.checkLists[0]?.statusNote);
      setValue('checkSmell', checkWorkItem && workItem?.checkLists[0]?.checkSmell);
      setValue('smellNote', checkWorkItem && workItem?.checkLists[0]?.smellNote);
      setValue('checkImpurity', checkWorkItem && workItem?.checkLists[0]?.checkImpurity);
      setValue('impurityNote', checkWorkItem && workItem?.checkLists[0]?.impurityNote);
      setValue('checkPoison', checkWorkItem && workItem?.checkLists[0]?.checkPoison);
      setValue('receiverId', checkWorkItem && workItem?.checkLists[0]?.receiverId);
      setValue('note', checkWorkItem && workItem?.checkLists[0]?.note);
      setValue('transportNote', checkWorkItem && workItem?.checkLists[0]?.transportNote);
      setValue('weight', checkWorkItem && workItem?.checkLists[0]?.weight);
      setValue('materialType', checkWorkItem && workItem?.checkLists[0]?.materialType);
      setValue('poisonNote', checkWorkItem && workItem?.checkLists[0]?.poisonNote);
    }

  }, [dataProductionProcess]);
  const note = watch('note');

  React.useLayoutEffect(() => {
    if (note && note.length > MAX_NOTE_LENGTH) setValue('note', note.slice(0, MAX_NOTE_LENGTH));
  }, [note]);

  return (
    <Form<ReceiveMaterialCheckListFormSchema> id={FORM.MATERIAL_RECEIPT_MONITORING} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        {/* Dòng ngày kiểm tra */}
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
            <FormDatePicker
              setValue={setValue}
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Ngày sản xuất"
              name="fromDate"
              formState={formState}
            />
          </Col>
        </Row>

        {/* Dòng thời gian kiểm tra và số phiếu cân */}
        <Row>
          <Col md={6}>
            <FormTimePicker
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Thời gian kiểm tra"
              name="checkTime"
            />
          </Col>

          <Col md={6}>
            <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Số phiếu cân" name="weightNumber" />
          </Col>
        </Row>

        {/* Dòng điều kiện phương tiện vận chuyển */}
        <Flex gap={16}>
          <Label>ĐK phương tiện vận chuyển:</Label>
          <FormGroup check>
            <Input
              disabled={type === 'detail'}
              checked={watch('transportCondition')}
              id="transportConditionOk"
              type="checkbox"
              onChange={() => {
                setValue('transportCondition', true);
                trigger('transportCondition');
              }}
            />
            <Label check for="transportConditionOk">
              Đạt
            </Label>
          </FormGroup>
          <FormGroup check>
            <Input
              disabled={type === 'detail'}
              checked={watch('transportCondition') !== undefined && !watch('transportCondition')}
              id="transportConditionNotOk"
              type="checkbox"
              onChange={() => {
                setValue('transportCondition', false);
                trigger('transportCondition');
              }}
            />
            <Label check for="transportConditionNotOk">
              Không đạt
            </Label>
          </FormGroup>
          {watch('transportCondition') !== undefined && !watch('transportCondition') && (
            <>
              <Label>Lý do:</Label>
              <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="transportNote" />
            </>
          )}
          <FormInput disabled={type === 'detail'} control={control} name="transportCondition" hidden />
        </Flex>

        {/* Dòng kiểm tra cảm quan */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Kiểm tra cảm quan:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Flex gap={16}>
                <Col md={2}>
                  <Label>Trạng thái:</Label>
                </Col>
                <Col md={10}>
                  <Flex gap={16}>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkStatus')}
                        id="checkStatusOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkStatus', true);
                          trigger('checkStatus');
                        }}
                      />
                      <Label check for="checkStatusOk">
                        Đạt
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkStatus') !== undefined && !watch('checkStatus')}
                        id="checkStatusNotOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkStatus', false);
                          trigger('checkStatus');
                        }}
                      />
                      <Label check for="checkStatusNotOk">
                        Không đạt
                      </Label>
                    </FormGroup>
                    {watch('checkStatus') !== undefined && !watch('checkStatus') && (
                      <>
                        <Label>Lý do:</Label>
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="statusNote" />
                      </>
                    )}
                    <FormInput disabled={type === 'detail'} control={control} name="checkStatus" hidden />
                  </Flex>
                </Col>
              </Flex>
            </Row>
            <Row>
              <Flex gap={16}>
                <Col md={2}>
                  <Label>Mùi:</Label>
                </Col>
                <Col md={10}>
                  <Flex gap={16}>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkSmell')}
                        id="checkSmellOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkSmell', true);
                          trigger('checkSmell');
                        }}
                      />
                      <Label check for="checkSmellOk">
                        Đạt
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkSmell') !== undefined && !watch('checkSmell')}
                        id="checkSmellNotOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkSmell', false);
                          trigger('checkSmell');
                        }}
                      />
                      <Label check for="checkSmellNotOk">
                        Không đạt
                      </Label>
                    </FormGroup>
                    {watch('checkSmell') !== undefined && !watch('checkSmell') && (
                      <>
                        <Label>Lý do:</Label>
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="smellNote" />
                      </>
                    )}
                    <FormInput disabled={type === 'detail'} control={control} name="checkSmell" hidden />
                  </Flex>
                </Col>
              </Flex>
            </Row>
            <Row>
              <Flex gap={16}>
                <Col md={2}>
                  <Label>Tạp chất:</Label>
                </Col>
                <Col md={10}>
                  <Flex gap={16}>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkImpurity')}
                        id="checkImpurityOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkImpurity', true);
                          trigger('checkImpurity');
                        }}
                      />
                      <Label check for="checkImpurityOk">
                        Đạt
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkImpurity') !== undefined && !watch('checkImpurity')}
                        id="checkImpurityNotOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkImpurity', false);
                          trigger('checkImpurity');
                        }}
                      />
                      <Label check for="checkImpurityNotOk">
                        Không đạt
                      </Label>
                    </FormGroup>
                    {watch('checkImpurity') !== undefined && !watch('checkImpurity') && (
                      <>
                        <Label>Lý do:</Label>
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="impurityNote" />
                      </>
                    )}
                    <FormInput disabled={type === 'detail'} control={control} name="checkImpurity" hidden />
                  </Flex>
                </Col>
              </Flex>
            </Row>
            <Row>
              <Flex gap={16}>
                <Col md={2}>
                  <Label>Các loại cá độc:</Label>
                </Col>
                <Col md={10}>
                  <Flex gap={16}>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkPoison')}
                        id="checkPoisonOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkPoison', true);
                          trigger('checkPoison');
                        }}
                      />
                      <Label check for="checkPoisonOk">
                        Có
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkPoison') !== undefined && !watch('checkPoison')}
                        id="checkPoisonNotOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkPoison', false);
                          trigger('checkPoison');
                        }}
                      />
                      <Label check for="checkPoisonNotOk">
                        Không
                      </Label>
                    </FormGroup>
                    {watch('checkPoison') && (
                      <>
                        <Label>Lý do:</Label>
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="poisonNote" />
                      </>
                    )}
                    <FormInput disabled={type === 'detail'} control={control} name="checkPoison" hidden />
                  </Flex>
                </Col>
              </Flex>
            </Row>
            <Row>
              <Col>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Số cân" name="weight" />
              </Col>
            </Row>
          </Flex>
        </Flex>
        <Row>
          {/* Dòng người tiếp nhận */}
          <Col md={6}>
            <FormSelect
              control={control}
              id="receiverId"
              name="receiverId"
              placeholder="Chọn người tiếp nhận"
              label="Người tiếp nhận"
              options={employees?.data?.map(e => ({
                label: `${e?.lastName || ''} ${e?.firstName || ''}`,
                value: e?.id,
              }))}
              disabled={type === 'detail'}
              isLoading={isLoading}
            />
          </Col>
          {/* Loại nguyên liệu */}
          <Col md={6}>
            <FormSelect
              control={control}
              id="materialType"
              name="materialType"
              placeholder="Chọn loại nguyên liệu"
              label="Loại nguyên liệu"
              options={listOptionMaterialType}
              disabled={type === 'detail'}
              isLoading={isLoading}
            />
          </Col>
        </Row>

        {/* Dòng chú thích */}
        <Row>
          <Label for="reason" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Chú thích</span>
              <span className="word-count">{note?.length || 0}/{MAX_NOTE_LENGTH}</span>
            </Flex>
          </Label>
          <Col>
            <FormInput
              disabled={type === 'detail'}
              rows={5}
              control={control}
              name="note"
              type="textarea"
            />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default MaterialReceiptMonitoringForm;
