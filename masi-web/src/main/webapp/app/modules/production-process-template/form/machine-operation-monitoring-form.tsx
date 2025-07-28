import dayjs from 'dayjs';
import { useParams } from 'react-router';
import React, { useEffect } from 'react';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { SubmitHandler, useForm } from 'react-hook-form';

import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Input from 'app/components/input/input';
import FormInput from 'app/components/form/form-input';
import FormTimePicker from 'app/components/form/form-time-picker';
import useProductionProcess from 'app/hooks/use-production-process';
import useMachineOperationChecklist from 'app/hooks/use-machine-operation-checklist';
import { DATE_FORMAT } from 'app/constants/common';
import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';
import { MachineOperationChecklistFormSchema, machineOperationChecklistSchema } from 'app/validation/production-process.validation';
import FormDatePicker from 'app/components/form/form-date-picker';
import { DateObject } from 'react-multi-date-picker';

const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;
const { usePostMachineOperationChecklist, usePatchMachineOperationChecklist } = useMachineOperationChecklist;
interface IMachineOperationMonitoringForm {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const MachineOperationMonitoringForm = (props: IMachineOperationMonitoringForm) => {
  const { toggle, type } = props;

  const { id, workItemId } = useParams();

  const { control, handleSubmit, watch, setValue, trigger, formState } = useForm<MachineOperationChecklistFormSchema>({
    resolver: zodResolver(machineOperationChecklistSchema),
  });

  const { mutate: create } = usePostMachineOperationChecklist(toggle);
  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { mutate: update } = usePatchMachineOperationChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);

  const onSubmit: SubmitHandler<MachineOperationChecklistFormSchema> = async data => {
    const submitValues = {
      checkTime: dayjs(data.checkTime, DATE_FORMAT.TIME_ONLY).toISOString(),
      incineratorAirDuct: data.incineratorAirDuct,
      incineratorAirDuctNote: data.incineratorAirDuctNote,
      incinerator: data.incinerator,
      incineratorCheckNote: data.incineratorCheckNote,
      dryingOvenAirDuct: data.dryingOvenAirDuct,
      dryingOvenAirDuctNote: data.dryingOvenAirDuctNote,
      dryingOvenMeter: data.dryingOvenMeter,
      dryingOvenMeterNote: data.dryingOvenMeterNote,
      dryingOvenWall: data.dryingOvenWall,
      dryingOvenWallNote: data.dryingOvenWallNote,
      dryingOvenValve: data.dryingOvenValve,
      dryingOvenValveNote: data.dryingOvenValveNote,
      sieveScreen: data.sieveScreen,
      sieveScreenNote: data.sieveScreenNote,
      crusher: data.crusher,
      crusherNote: data.crusherNote,
      magnet: data.magnet,
      magnetNote: data.magnetNote,
      mixer: data.mixer,
      mixerNote: data.mixerNote,
      packagingMachine: data.packagingMachine,
      packagingMachineNote: data.packagingMachineNote,
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
      setValue('incineratorAirDuct', checkWorkItem && workItem?.checkLists[0]?.incineratorAirDuct);
      setValue('incineratorAirDuctNote', checkWorkItem && workItem?.checkLists[0]?.incineratorAirDuctNote);
      setValue('incinerator', checkWorkItem && workItem?.checkLists[0]?.incinerator);
      setValue('incineratorCheckNote', checkWorkItem && workItem?.checkLists[0]?.incineratorCheckNote);
      setValue('dryingOvenAirDuct', checkWorkItem && workItem?.checkLists[0]?.dryingOvenAirDuct);
      setValue('dryingOvenAirDuctNote', checkWorkItem && workItem?.checkLists[0]?.dryingOvenAirDuctNote);
      setValue('dryingOvenMeter', checkWorkItem && workItem?.checkLists[0]?.dryingOvenMeter);
      setValue('dryingOvenMeterNote', checkWorkItem && workItem?.checkLists[0]?.dryingOvenMeterNote);
      setValue('dryingOvenWall', checkWorkItem && workItem?.checkLists[0]?.dryingOvenWall);
      setValue('dryingOvenWallNote', checkWorkItem && workItem?.checkLists[0]?.dryingOvenWallNote);
      setValue('dryingOvenValve', checkWorkItem && workItem?.checkLists[0]?.dryingOvenValve);
      setValue('dryingOvenValveNote', checkWorkItem && workItem?.checkLists[0]?.dryingOvenValveNote);
      setValue('sieveScreen', checkWorkItem && workItem?.checkLists[0]?.sieveScreen);
      setValue('sieveScreenNote', checkWorkItem && workItem?.checkLists[0]?.sieveScreenNote);
      setValue('crusher', checkWorkItem && workItem?.checkLists[0]?.crusher);
      setValue('crusherNote', checkWorkItem && workItem?.checkLists[0]?.crusherNote);
      setValue('magnet', checkWorkItem && workItem?.checkLists[0]?.magnet);
      setValue('magnetNote', checkWorkItem && workItem?.checkLists[0]?.magnetNote);
      setValue('mixer', checkWorkItem && workItem?.checkLists[0]?.mixer);
      setValue('mixerNote', checkWorkItem && workItem?.checkLists[0]?.mixerNote);
      setValue('packagingMachine', checkWorkItem && workItem?.checkLists[0]?.packagingMachine);
      setValue('packagingMachineNote', checkWorkItem && workItem?.checkLists[0]?.packagingMachineNote);
    }
  }, [dataProductionProcess]);

  return (
    <Form<MachineOperationChecklistFormSchema> id={FORM.MACHINE_OPERATION_MONITORING} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={48}>
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
            {/* Dòng giờ */}
            <FormTimePicker
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Thời gian kiểm tra"
              name="checkTime"
            />
          </Col>
        </Row>

        <Row>
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

        {/* Dòng Lò đốt */}
        <Flex direction="column" gap={16} style={{ width: '100%' }}>
          <Typography level="paragraph" className="bold test-check-heading">
            Lò đốt:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={2}>
                <Label>Ống dẫn khí:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('incineratorAirDuct')}
                      id="incineratorAirDuctOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('incineratorAirDuct', true);
                        trigger('incineratorAirDuct');
                      }}
                    />
                    <Label check for="incineratorAirDuctOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('incineratorAirDuct') !== undefined && !watch('incineratorAirDuct')}
                      id="incineratorAirDuctNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('incineratorAirDuct', false);
                        trigger('incineratorAirDuct');
                      }}
                    />
                    <Label check for="incineratorAirDuctNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('incineratorAirDuct') !== undefined && !watch('incineratorAirDuct') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="incineratorAirDuctNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="incineratorAirDuct" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Lò đốt:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('incinerator')}
                      id="incineratorOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('incinerator', true);
                        trigger('incinerator');
                      }}
                    />
                    <Label check for="incineratorOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('incinerator') !== undefined && !watch('incinerator')}
                      id="incineratorNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('incinerator', false);
                        trigger('incinerator');
                      }}
                    />
                    <Label check for="incineratorNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('incinerator') !== undefined && !watch('incinerator') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="incineratorCheckNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="incinerator" hidden />
                </Flex>
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng Lò sấy */}
        <Flex direction="column" gap={16} style={{ width: '100%' }}>
          <Typography level="paragraph" className="bold test-check-heading">
            Lò sấy:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={2}>
                <Label>Ống dẫn khí:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenAirDuct')}
                      id="dryingOvenAirDuctOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenAirDuct', true);
                        trigger('dryingOvenAirDuct');
                      }}
                    />
                    <Label check for="dryingOvenAirDuctOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenAirDuct') !== undefined && !watch('dryingOvenAirDuct')}
                      id="dryingOvenAirDuctNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenAirDuct', false);
                        trigger('dryingOvenAirDuct');
                      }}
                    />
                    <Label check for="dryingOvenAirDuctNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('dryingOvenAirDuct') !== undefined && !watch('dryingOvenAirDuct') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="dryingOvenAirDuctNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="dryingOvenAirDuct" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Đồng hồ:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenMeter')}
                      id="dryingOvenMeterOk"
                      type="checkbox"
                      onChange={e => {
                        setValue('dryingOvenMeter', true);
                        trigger('dryingOvenMeter');
                      }}
                    />
                    <Label check for="dryingOvenMeterOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenMeter') !== undefined && !watch('dryingOvenMeter')}
                      id="dryingOvenMeterNotOk"
                      type="checkbox"
                      onChange={e => {
                        setValue('dryingOvenMeter', false);
                        trigger('dryingOvenMeter');
                      }}
                    />
                    <Label check for="dryingOvenMeterNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('dryingOvenMeter') !== undefined && !watch('dryingOvenMeter') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="dryingOvenMeterNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="dryingOvenMeter" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label style={{ minWidth: 200 }}>Thành lò sấy:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenWall')}
                      id="dryingOvenWallOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenWall', true);
                        trigger('dryingOvenWall');
                      }}
                    />
                    <Label check for="dryingOvenWallOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenWall') !== undefined && !watch('dryingOvenWall')}
                      id="dryingOvenWallNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenWall', false);
                        trigger('dryingOvenWall');
                      }}
                    />
                    <Label check for="dryingOvenWallNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('dryingOvenWall') !== undefined && !watch('dryingOvenWall') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="dryingOvenWallNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="dryingOvenWall" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label style={{ minWidth: 200 }}>Van xả/ đóng:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenValve')}
                      id="dryingOvenValveOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenValve', true);
                        trigger('dryingOvenValve');
                      }}
                    />
                    <Label check for="dryingOvenValveOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('dryingOvenValve') !== undefined && !watch('dryingOvenValve')}
                      id="dryingOvenValveNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('dryingOvenValve', false);
                        trigger('dryingOvenValve');
                      }}
                    />
                    <Label check for="dryingOvenValveNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('dryingOvenValve') !== undefined && !watch('dryingOvenValve') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="dryingOvenValveNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="dryingOvenValve" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label style={{ minWidth: 200 }}>Lưới máy sàng:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('sieveScreen')}
                      id="sieveScreenOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('sieveScreen', true);
                        trigger('sieveScreen');
                      }}
                    />
                    <Label check for="sieveScreenOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('sieveScreen') !== undefined && !watch('sieveScreen')}
                      id="sieveScreenNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('sieveScreen', false);
                        trigger('sieveScreen');
                      }}
                    />
                    <Label check for="sieveScreenNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('sieveScreen') !== undefined && !watch('sieveScreen') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="sieveScreenNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="sieveScreen" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Nam châm:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnet')}
                      id="magnetOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnet', true);
                        trigger('magnet');
                      }}
                    />
                    <Label check for="magnetOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnet') !== undefined && !watch('magnet')}
                      id="magnetNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnet', false);
                        trigger('magnet');
                      }}
                    />
                    <Label check for="magnetNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('magnet') !== undefined && !watch('magnet') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="magnetNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="magnet" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label style={{ minWidth: 200 }}>Máy đóng gói:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('packagingMachine')}
                      id="packagingMachineOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('packagingMachine', true);
                        trigger('packagingMachine');
                      }}
                    />
                    <Label check for="packagingMachineOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('packagingMachine') !== undefined && !watch('packagingMachine')}
                      id="packagingMachineNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('packagingMachine', false);
                        trigger('packagingMachine');
                      }}
                    />
                    <Label check for="packagingMachineNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('packagingMachine') !== undefined && !watch('packagingMachine') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput
                        disabled={type === 'detail'}
                        className="template-input-reason"
                        control={control}
                        name="packagingMachineNote"
                      />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="packagingMachine" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Máy nghiền:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('crusher')}
                      id="crusherOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('crusher', true);
                        trigger('crusher');
                      }}
                    />
                    <Label check for="crusherOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('crusher') !== undefined && !watch('crusher')}
                      id="crusherNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('crusher', false);
                        trigger('crusher');
                      }}
                    />
                    <Label check for="crusherNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('crusher') !== undefined && !watch('crusher') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="crusherNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="crusher" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Máy trộn:</Label>
              </Col>
              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('mixer')}
                      id="mixerOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('mixer', true);
                        trigger('mixer');
                      }}
                    />
                    <Label check for="mixerOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('mixer') !== undefined && !watch('mixer')}
                      id="mixerNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('mixer', false);
                        trigger('mixer');
                      }}
                    />
                    <Label check for="mixerNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('mixer') !== undefined && !watch('mixer') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="mixerNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="mixer" hidden />
                </Flex>
              </Col>
            </Row>
          </Flex>
        </Flex>
      </Flex>
    </Form>
  );
};

export default MachineOperationMonitoringForm;
