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
import useMixingReportChecklist from 'app/hooks/use-mixing-report-checklist';
import useProductionProcess from 'app/hooks/use-production-process';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MixingReportChecklistFormSchema, mixingReportChecklistSchema } from 'app/validation/production-process.validation';

const { usePatchMixingReportChecklist, usePostMixingReportChecklist } = useMixingReportChecklist;
const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;

interface IReportMixingFishMealFormProps {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const ReportMixingFishMealForm = (props: IReportMixingFishMealFormProps) => {
  const { type, toggle } = props;

  const { id, workItemId } = useParams();

  const { control, handleSubmit, watch, setValue, reset, trigger, formState } = useForm<MixingReportChecklistFormSchema>({
    resolver: zodResolver(mixingReportChecklistSchema),
    defaultValues: {
      finProduct1WeightUnit: 'KILOGRAM',
      finProduct2WeightUnit: 'KILOGRAM',
      bhtWeightUnit: 'KILOGRAM',
      bhtWeightPrdUnit: 'KILOGRAM',
    },
  });

  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { mutate: create } = usePostMixingReportChecklist(toggle);
  const { mutate: update } = usePatchMixingReportChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);


  const onSubmit: SubmitHandler<MixingReportChecklistFormSchema> = async data => {
    const submitValues = {
      checkDate: data.checkDate.toDate().toISOString(),
      finProduct1No: data.finProduct1No,
      finProduct1Weight: Number(data.finProduct1Weight),
      finProduct1WeightUnit: data.finProduct1WeightUnit,
      finProduct2No: data.finProduct2No,
      finProduct2Weight: Number(data.finProduct2Weight),
      finProduct2WeightUnit: data.finProduct2WeightUnit,
      bhtNo: data.bhtNo,
      bhtWeight: Number(data.bhtWeight),
      bhtWeightUnit: data.bhtWeightUnit,
      bhtWeightPrd: Number(data.bhtWeightPrd),
      bhtWeightPrdUnit: data.bhtWeightPrdUnit,
      weightPrdNo: data.weightPrdNo,
      checkImpurity: data.checkImpurity,
      checkImpurityNote: data.checkImpurityNote,
      checkSmell: data.checkSmell,
      checkSmellNote: data.checkSmellNote,
      checkColor: data.checkColor,
      checkColorNote: data.checkColorNote,
      checkEmployeeId: data.checkEmployeeId,
      ...(data.moisture ? { moisture: Number(data.moisture) } : {}),
      ...(data.tvn ? { tvn: Number(data.tvn) } : {}),
      ...(data.ash ? { ash: Number(data.ash) } : {}),
      ...(data.protein ? { protein: Number(data.protein) } : {}),
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

      setValue('checkDate', checkWorkItem && new DateObject(workItem?.checkLists[0]?.zonedCheckDate)?.add(7, 'hours'));
      setValue('finProduct1No', checkWorkItem && workItem?.checkLists[0]?.finProduct1No);
      setValue('finProduct1Weight', checkWorkItem && workItem?.checkLists[0]?.finProduct1Weight?.toString());
      setValue('finProduct1WeightUnit', checkWorkItem && workItem?.checkLists[0]?.finProduct1WeightUnit);
      setValue('finProduct2No', checkWorkItem && workItem?.checkLists[0]?.finProduct2No);
      setValue('finProduct2Weight', checkWorkItem && workItem?.checkLists[0]?.finProduct2Weight?.toString());
      setValue('finProduct2WeightUnit', checkWorkItem && workItem?.checkLists[0]?.finProduct2WeightUnit);
      setValue('bhtNo', checkWorkItem && workItem?.checkLists[0]?.bhtNo);
      setValue('bhtWeight', checkWorkItem && workItem?.checkLists[0]?.bhtWeight?.toString());
      setValue('bhtWeightUnit', checkWorkItem && workItem?.checkLists[0]?.bhtWeightUnit);
      setValue('bhtWeightPrd', checkWorkItem && workItem?.checkLists[0]?.bhtWeightPrd?.toString());
      setValue('bhtWeightPrdUnit', checkWorkItem && workItem?.checkLists[0]?.bhtWeightPrdUnit);
      setValue('weightPrdNo', checkWorkItem && workItem?.checkLists[0]?.weightPrdNo);
      setValue('checkImpurity', checkWorkItem && workItem?.checkLists[0]?.checkImpurity);
      setValue('checkImpurityNote', checkWorkItem && workItem?.checkLists[0]?.checkImpurityNote);
      setValue('checkSmell', checkWorkItem && workItem?.checkLists[0]?.checkSmell);
      setValue('checkSmellNote', checkWorkItem && workItem?.checkLists[0]?.checkSmellNote);
      setValue('checkColor', checkWorkItem && workItem?.checkLists[0]?.checkColor);
      setValue('checkColorNote', checkWorkItem && workItem?.checkLists[0]?.checkColorNote);
      setValue('checkEmployeeId', checkWorkItem && workItem?.checkLists[0]?.checkEmployeeId);
      workItem?.checkLists[0]?.moisture && setValue('moisture', workItem?.checkLists[0]?.moisture?.toString());
      workItem?.checkLists[0]?.tvn && setValue('tvn', workItem?.checkLists[0]?.tvn?.toString());
      workItem?.checkLists[0]?.ash && setValue('ash', workItem?.checkLists[0]?.ash?.toString());
      workItem?.checkLists[0]?.protein && setValue('protein', workItem?.checkLists[0]?.protein?.toString());
    }
  }, [dataProductionProcess]);

  return (
    <Form id={FORM.REPORT_MIXING_FISHMEAL} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        {/* Dòng ngày trộn */}
        <Row>
          <Col md={6}>
            <FormDatePicker setValue={setValue} disabled={type === 'detail'} className="template-input" control={control} label="Ngày trộn" name="checkDate" formState={formState} />
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

        {/* Dòng thành phẩm 1: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Thành phẩm 1:
          </Typography>
          <Row>
            <Col md={6}>
              <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Mã lô" name="finProduct1No" />
            </Col>

            <Col md={6} className="unit-wrapper">
              <FormInput
                disabled={type === 'detail'}
                className="template-input"
                control={control}
                label="Khối lượng"
                name="finProduct1Weight"
                type="number"
              />
              <FormInput control={control} type="select" name="finProduct1WeightUnit" className="unit-input" disabled={type === 'detail'}>
                <option value="KILOGRAM">Kg</option>
                <option value="GRAM">Gr</option>
              </FormInput>
            </Col>
          </Row>
        </Flex>

        {/* Dòng thành phẩm 2: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Thành phẩm 2:
          </Typography>
          <Row>
            <Col md={6}>
              <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Mã lô" name="finProduct2No" />
            </Col>

            <Col md={6} className="unit-wrapper">
              <FormInput
                disabled={type === 'detail'}
                className="template-input"
                control={control}
                label="Khối lượng"
                name="finProduct2Weight"
                type="number"
              />
              <FormInput control={control} type="select" name="finProduct2WeightUnit" className="unit-input" disabled={type === 'detail'}>
                <option value="KILOGRAM">Kg</option>
                <option value="GRAM">Gr</option>
              </FormInput>
            </Col>
          </Row>
        </Flex>

        {/* Dòng BHT */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            BHT:
          </Typography>
          <Flex direction="column" gap={24}>
            <Row>
              <Col md={6}>
                <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Mã lô" name="bhtNo" />
              </Col>
              <Col md={6} className="unit-wrapper">
                <FormInput
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Khối lượng"
                  name="bhtWeight"
                  type="number"
                />
                <FormInput control={control} type="select" name="bhtWeightUnit" className="unit-input" disabled={type === 'detail'}>
                  <option value="KILOGRAM">Kg</option>
                  <option value="GRAM">Gr</option>
                </FormInput>
              </Col>
            </Row>

            <Row>
              <Col md={6} className="unit-wrapper">
                <FormInput
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="KL thành phẩm"
                  name="bhtWeightPrd"
                  type="number"
                />
                <FormInput control={control} type="select" name="bhtWeightPrdUnit" className="unit-input" disabled={type === 'detail'}>
                  <option value="KILOGRAM">Kg</option>
                  <option value="GRAM">Gr</option>
                </FormInput>
              </Col>

              <Col md={6}>
                <FormInput
                  disabled={type === 'detail'}
                  className="template-input"
                  control={control}
                  label="Mã lô thành phẩm"
                  name="weightPrdNo"
                />
              </Col>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng Chỉ tiêu kiểm tra: */}
        <Flex direction="column" gap={16}>
          <Typography level="paragraph" className="bold test-check-heading">
            Chỉ tiêu kiểm tra:
          </Typography>
          <Flex direction="column" gap={24}>
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
                        <FormInput
                          disabled={type === 'detail'}
                          className="template-input-reason"
                          control={control}
                          name="checkImpurityNote"
                        />
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
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="checkSmellNote" />
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
                  <Label>Màu:</Label>
                </Col>
                <Col md={10}>
                  <Flex gap={16}>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkColor')}
                        id="checkColorOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkColor', true);
                          trigger('checkColor');
                        }}
                      />
                      <Label check for="checkColorOk">
                        Đạt
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Input
                        disabled={type === 'detail'}
                        checked={watch('checkColor') !== undefined && !watch('checkColor')}
                        id="checkColorNotOk"
                        type="checkbox"
                        onChange={() => {
                          setValue('checkColor', false);
                          trigger('checkColor');
                        }}
                      />
                      <Label check for="checkColorNotOk">
                        Không đạt
                      </Label>
                    </FormGroup>
                    {watch('checkColor') !== undefined && !watch('checkColor') && (
                      <>
                        <Label>Lý do:</Label>
                        <FormInput disabled={type === 'detail'} className="template-input-reason" control={control} name="checkColorNote" />
                      </>
                    )}
                    <FormInput disabled={type === 'detail'} control={control} name="checkColor" hidden />
                  </Flex>
                </Col>
              </Flex>
            </Row>
          </Flex>
        </Flex>

        {/* Dòng độ ẩm và TVN */}
        <Row>
          <Col md={6}>
            <FormInput
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Độ ẩm %"
              name="moisture"
              type="number"
            />
          </Col>
          <Col md={6}>
            <FormInput disabled={type === 'detail'} className="template-input" control={control} label="TVN" name="tvn" type="number" />
          </Col>
        </Row>

        {/* Dòng độ Tro và Protein */}
        <Row>
          <Col md={6}>
            <FormInput type="number" disabled={type === 'detail'} className="template-input" control={control} label="Tro" name="ash" />
          </Col>
          <Col md={6}>
            <FormInput
              type="number"
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Protein"
              name="protein"
            />
          </Col>
        </Row>

        {/* Dòng Người thực hiện */}
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="checkEmployeeId"
              name="checkEmployeeId"
              placeholder="Chọn người thực hiện"
              label="Người thực hiện"
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
    </Form>
  );
};

export default ReportMixingFishMealForm;
