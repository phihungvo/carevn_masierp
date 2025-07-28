import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, FormGroup, Label, Row } from 'reactstrap';

import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Input from 'app/components/input/input';
import { Typography } from 'app/components/typography/typography';
import useEmployee from 'app/hooks/use-employee';
import useMetalDetectionChecklist from 'app/hooks/use-material-detection-checklist';
import useProductionProcess from 'app/hooks/use-production-process';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MAX_NOTE_LENGTH, MetalDetectionChecklistFormSchema, metalDetectionChecklistSchema } from 'app/validation/production-process.validation';

const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;
const { usePostMetalDetectionChecklist, usePatchMetalDetectionChecklist } = useMetalDetectionChecklist;
interface IMagnetMeshTestForm {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const MagnetMeshTestForm = (props: IMagnetMeshTestForm) => {
  const { toggle, type } = props;

  const { id, workItemId } = useParams();

  const { control, handleSubmit, watch, reset, setValue, trigger, formState } = useForm<MetalDetectionChecklistFormSchema>({
    resolver: zodResolver(metalDetectionChecklistSchema),
  });

  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { mutate: create } = usePostMetalDetectionChecklist(toggle);
  const { mutate: update } = usePatchMetalDetectionChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);


  const onSubmit: SubmitHandler<MetalDetectionChecklistFormSchema> = async data => {
    const submitValues = {
      checkDate: data.checkDate.toDate().toISOString(),
      finProductNo: data.finProductNo,
      magnetBegin: data.magnetBegin,
      magnetBeginNote: data.magnetBeginNote,
      magnetEnd: data.magnetEnd,
      magnetEndNote: data.magnetEndNote,
      screen4Begin: data.screen4Begin,
      screen4BeginNote: data.screen4BeginNote,
      screen4End: data.screen4End,
      screen4EndNote: data.screen4EndNote,
      screen3Begin: data.screen3Begin,
      screen3BeginNote: data.screen3BeginNote,
      screen3End: data.screen3End,
      screen3EndNote: data.screen3EndNote,
      checkedBy: data.checkedBy,
      auditedBy: data.auditedBy,
      note: data.note,
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
    }

    if (dataProductionProcess?.lastUpdated) {
      const { workItem } = dataProductionProcess;
      const checkWorkItem = workItem?.checkLists?.length !== 0;

      setValue('checkDate', checkWorkItem && new DateObject(workItem?.checkLists[0]?.checkDate).add(7, 'hours'));
      setValue('finProductNo', checkWorkItem && workItem?.checkLists[0]?.finProductNo);
      setValue('magnetBegin', checkWorkItem && workItem?.checkLists[0]?.magnetBegin);
      setValue('magnetBeginNote', checkWorkItem && workItem?.checkLists[0]?.magnetBeginNote);
      setValue('magnetEnd', checkWorkItem && workItem?.checkLists[0]?.magnetEnd);
      setValue('magnetEndNote', checkWorkItem && workItem?.checkLists[0]?.magnetEndNote);
      setValue('screen4Begin', checkWorkItem && workItem?.checkLists[0]?.screen4Begin);
      setValue('screen4BeginNote', checkWorkItem && workItem?.checkLists[0]?.screen4BeginNote);
      setValue('screen4End', checkWorkItem && workItem?.checkLists[0]?.screen4End);
      setValue('screen4EndNote', checkWorkItem && workItem?.checkLists[0]?.screen4EndNote);
      setValue('screen3Begin', checkWorkItem && workItem?.checkLists[0]?.screen3Begin);
      setValue('screen3BeginNote', checkWorkItem && workItem?.checkLists[0]?.screen3BeginNote);
      setValue('screen3End', checkWorkItem && workItem?.checkLists[0]?.screen3End);
      setValue('screen3EndNote', checkWorkItem && workItem?.checkLists[0]?.screen3EndNote);
      setValue('checkedBy', checkWorkItem && workItem?.checkLists[0]?.checkedBy);
      setValue('auditedBy', checkWorkItem && workItem?.checkLists[0]?.auditedBy);
      setValue('note', checkWorkItem && workItem?.checkLists[0]?.note);
    }
  }, [dataProductionProcess]);
  const note = watch('note');

  React.useLayoutEffect(() => {
    if (note && note.length > MAX_NOTE_LENGTH) setValue('note', note.slice(0, MAX_NOTE_LENGTH));
  }, [note]);

  return (
    <Form<MetalDetectionChecklistFormSchema> id={FORM.MAGNET_MESH_TEST} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        {/* Dòng ngày sản xuất và mã lô thành phẩm */}
        <Row>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Ngày sản xuất"
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

        <Row>
          <Col md={6}>
            <FormInput
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Mã lô thành phẩm"
              name="finProductNo"
            />
          </Col>
        </Row>

        {/* Dòng tình trạng nam châm  */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Tình trạng nam châm:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={2}>
                <Label>Đầu ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnetBegin')}
                      id="magnetBeginOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnetBegin', true);
                        trigger('magnetBegin');
                      }}
                    />
                    <Label check for="magnetBeginOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnetBegin') !== undefined && !watch('magnetBegin')}
                      id="magnetBeginNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnetBegin', false);
                        trigger('magnetBegin');
                      }}
                    />
                    <Label check for="magnetBeginNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('magnetBegin') !== undefined && !watch('magnetBegin') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="magnetBeginNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="magnetBegin" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Cuối ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnetEnd')}
                      id="magnetEndOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnetEnd', true);
                        trigger('magnetEnd');
                      }}
                    />
                    <Label check for="magnetEndOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('magnetEnd') !== undefined && !watch('magnetEnd')}
                      id="magnetEndNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('magnetEnd', false);
                        trigger('magnetEnd');
                      }}
                    />
                    <Label check for="magnetEndNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('magnetEnd') !== undefined && !watch('magnetEnd') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="magnetEndNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="magnetEnd" hidden />
                </Flex>
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng tình trạng lưới sàng 4mm */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Tình trạng lưới sàng 4mm:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={2}>
                <Label>Đầu ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen4Begin')}
                      id="screen4BeginOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen4Begin', true);
                        trigger('screen4Begin');
                      }}
                    />
                    <Label check for="screen4BeginOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen4Begin') !== undefined && !watch('screen4Begin')}
                      id="screen4BeginNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen4Begin', false);
                        trigger('screen4Begin');
                      }}
                    />
                    <Label check for="screen4BeginNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('screen4Begin') !== undefined && !watch('screen4Begin') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="screen4BeginNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="screen4Begin" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Cuối ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen4End')}
                      id="screen4EndOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen4End', true);
                        trigger('screen4End');
                      }}
                    />
                    <Label check for="screen4EndOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen4End') !== undefined && !watch('screen4End')}
                      id="screen4EndNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen4End', false);
                        trigger('screen4End');
                      }}
                    />
                    <Label check for="screen4EndNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('screen4End') !== undefined && !watch('screen4End') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="screen4EndNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="screen4End" hidden />
                </Flex>
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng tình trạng lưới nghiền 3mm:*/}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Tình trạng lưới nghiền 3mm:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={2}>
                <Label>Đầu ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen3Begin')}
                      id="screen3BeginOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen3Begin', true);
                        trigger('screen3Begin');
                      }}
                    />
                    <Label check for="screen3BeginOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen3Begin') !== undefined && !watch('screen3Begin')}
                      id="screen3BeginNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen3Begin', false);
                        trigger('screen3Begin');
                      }}
                    />
                    <Label check for="screen3BeginNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('screen3Begin') !== undefined && !watch('screen3Begin') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="screen3BeginNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="screen3Begin" hidden />
                </Flex>
              </Col>
            </Row>

            <Row>
              <Col md={2}>
                <Label>Cuối ca:</Label>
              </Col>

              <Col md={10}>
                <Flex gap={16}>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen3End')}
                      id="screen3EndOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen3End', true);
                        trigger('screen3End');
                      }}
                    />
                    <Label check for="screen3EndOk">
                      Đạt
                    </Label>
                  </FormGroup>
                  <FormGroup check>
                    <Input
                      disabled={type === 'detail'}
                      checked={watch('screen3End') !== undefined && !watch('screen3End')}
                      id="screen3EndNotOk"
                      type="checkbox"
                      onChange={() => {
                        setValue('screen3End', false);
                        trigger('screen3End');
                      }}
                    />
                    <Label check for="screen3EndNotOk">
                      Không đạt
                    </Label>
                  </FormGroup>
                  {watch('screen3End') !== undefined && !watch('screen3End') && (
                    <>
                      <Label>Lý do:</Label>
                      <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="screen3EndNote" />
                    </>
                  )}
                  <FormInput disabled={type === 'detail'} control={control} name="screen3End" hidden />
                </Flex>
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng người kiểm tra và người thẩm tra*/}
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="checkedBy"
              name="checkedBy"
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
          <Col md={6}>
            <FormInput
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Người thẩm tra"
              name="auditedBy"
              type="select"
            >
              <option selected disabled>
                Chọn người thẩm tra
              </option>
              {employees?.data?.map(employee => (
                <option key={employee.id} value={employee.id}>
                  {(employee?.lastName || '') + ' ' + (employee?.firstName || '')}
                </option>
              ))}
            </FormInput>
          </Col>
        </Row>

        {/* Dòng ghi chú */}
        <Row>
          <Label for="note" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Ghi chú</span>
              <span className="word-count">{watch('note')?.length || 0}/{MAX_NOTE_LENGTH}</span>
            </Flex>
          </Label>
          <Col>
            <FormInput rows={5} control={control} name="note" type="textarea" />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default MagnetMeshTestForm;
