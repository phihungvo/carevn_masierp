import dayjs from 'dayjs';
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
import FormTimePicker from 'app/components/form/form-time-picker';
import Input from 'app/components/input/input';
import { DATE_FORMAT, DEFAULT_DECIMAL_REGEX } from 'app/constants/common';
import useAdditiveMaterialChecklist from 'app/hooks/use-additive-material-checklist';
import useEmployee from 'app/hooks/use-employee';
import useProductionProcess from 'app/hooks/use-production-process';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { AdditiveMaterialChecklistFormSchema, additiveMaterialChecklistSchema, MAX_NOTE_LENGTH } from 'app/validation/production-process.validation';

const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;
const { usePatchAdditiveMaterialChecklist, usePostAdditiveMaterialChecklist } = useAdditiveMaterialChecklist;
interface ISelectingAddingAdditivesFormProps {
  toggle?: () => void;
  type: 'create' | 'update' | 'detail';
}

const SelectingAddingAdditivesForm = (props: ISelectingAddingAdditivesFormProps) => {
  const { type, toggle } = props;

  const { id, workItemId } = useParams();

  const { control, handleSubmit, setValue, watch, reset, trigger, formState } = useForm<AdditiveMaterialChecklistFormSchema>({
    resolver: zodResolver(additiveMaterialChecklistSchema),
    defaultValues: {
      weightMaterialUnit: 'KILOGRAM',
      bicacbonatWeightUnit: 'KILOGRAM',
    },
  });

  const { data: dataProductionProcess } = useGetProductionProcessById(id);
  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { mutate: create } = usePostAdditiveMaterialChecklist(toggle);
  const { mutate: update } = usePatchAdditiveMaterialChecklist(dataProductionProcess?.checklist?.id, toggle);
  const { mutate: updateProductionProcess } = usePatchProductionProcess(id);

  const onSubmit: SubmitHandler<AdditiveMaterialChecklistFormSchema> = async data => {
    const submitValues = {
      checkDate: data.checkDate.toDate().toISOString(),
      checkTime: dayjs(data.checkTime, DATE_FORMAT.TIME_ONLY).toISOString(),
      weightNumber: data.weightNumber,
      checkImpurity: data.checkImpurity,
      impurityNote: data.impurityNote,
      weightMaterial: Number(data.weightMaterial),
      weightMaterialUnit: data.weightMaterialUnit,
      bicabonatLotNumber: data.bicabonatLotNumber,
      bicacbonatWeight: Number(data.bicacbonatWeight),
      bicacbonatWeightUnit: data.bicacbonatWeightUnit,
      receiverId: data.receiverId,
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

  const handleValidDecimal = (e: React.ChangeEvent<HTMLInputElement>, name: keyof AdditiveMaterialChecklistFormSchema) => {
    const decimalRegex = DEFAULT_DECIMAL_REGEX;

    if (decimalRegex.test(e.target.value) || e.target.value === '') setValue(name, e.target.value);
    else setValue(name, e.target.value.slice(0, -1));

  };

  const handleValidatePaste = (e: React.ClipboardEvent<HTMLInputElement>, name: keyof AdditiveMaterialChecklistFormSchema) => {
    const clipboardData = e.clipboardData || window['clipboardData'];
    const pastedData = clipboardData.getData('text');

    const decimalRegex = DEFAULT_DECIMAL_REGEX;

    if (!decimalRegex.test(pastedData)) e.preventDefault();

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

      setValue('checkDate', checkWorkItem && new DateObject(workItem?.checkLists[0]?.zonedCheckDate)?.add(7, 'hours'));
      setValue('checkTime', checkWorkItem && dayjs(workItem?.checkLists[0]?.checkTime).format(DATE_FORMAT.TIME_ONLY));
      setValue('weightNumber', checkWorkItem && workItem?.checkLists[0]?.weightNumber);
      setValue('checkImpurity', checkWorkItem && workItem?.checkLists[0]?.checkImpurity);
      setValue('impurityNote', checkWorkItem && workItem?.checkLists[0]?.impurityNote);
      setValue('weightMaterial', checkWorkItem && workItem?.checkLists[0]?.weightMaterial?.toString());
      setValue('weightMaterialUnit', checkWorkItem && workItem?.checkLists[0]?.weightMaterialUnit);
      setValue('bicabonatLotNumber', checkWorkItem && workItem?.checkLists[0]?.bicabonatLotNumber);
      setValue('bicacbonatWeight', checkWorkItem && workItem?.checkLists[0]?.bicacbonatWeight?.toString());
      setValue('bicacbonatWeightUnit', checkWorkItem && workItem?.checkLists[0]?.bicacbonatWeightUnit);
      setValue('receiverId', checkWorkItem && workItem?.checkLists[0]?.receiverId);
      setValue('note', checkWorkItem && workItem?.checkLists[0]?.note);
    }
  }, [dataProductionProcess]);

  const note = watch('note');

  React.useLayoutEffect(() => {
    if (note && note.length > MAX_NOTE_LENGTH) setValue('note', note.slice(0, MAX_NOTE_LENGTH));
  }, [note]);

  return (
    <Form<AdditiveMaterialChecklistFormSchema> id={FORM.SELECTING_ADDING_ADDITIVES} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        {/* Dòng thời ngày và thời gian */}
        <Row>
          <Col md={6}>
            <FormDatePicker setValue={setValue} disabled={type === 'detail'} className="template-input" control={control} label="Ngày" name="checkDate" formState={formState} />
          </Col>

          <Col md={6}>
            <FormTimePicker disabled={type === 'detail'} className="template-input" control={control} label="Thời gian" name="checkTime" />
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
              formState={formState}
            />
          </Col>

          <Col md={6}>
            <FormInput disabled={type === 'detail'} className="template-input" control={control} label="Số phiếu cân" name="weightNumber" />
          </Col>
        </Row>

        {/* Dòng kết quả lựa tạp chất: */}
        <Flex gap={16}>
          <Label>Kết quả lựa tạp chất:</Label>
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

        {/* Dòng khối lượng nguyên liệu và số lô nanatri bicacbonat */}
        <Row>
          <Col md={6} className="unit-wrapper">
            <Flex align="center">
              <FormInput
                disabled={type === 'detail'}
                className="template-input"
                control={control}
                label="Khối lượng nguyên liệu"
                name="weightMaterial"
                onChange={e => handleValidDecimal(e, 'weightMaterial')}
                onPaste={e => handleValidatePaste(e, 'weightMaterial')}
              />
              <FormInput control={control} type="select" name="weightMaterialUnit" className="unit-input" disabled={type === 'detail'}>
                <option value="KILOGRAM">Kg</option>
                <option value="GRAM">Gr</option>
              </FormInput>
            </Flex>
          </Col>
          <Col md={6}>
            <FormInput
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Số lô Natri bicacbonat"
              name="bicabonatLotNumber"
            />
          </Col>
        </Row>

        {/* Dòng Khối lượng Natri cacbonat */}
        <Row>
          <Col md={6} className="unit-wrapper">
            <FormInput
              disabled={type === 'detail'}
              className="template-input"
              control={control}
              label="Khối lượng Natri cacbonat"
              name="bicacbonatWeight"
            />
            <FormInput control={control} type="select" name="bicacbonatWeightUnit" className="unit-input" disabled={type === 'detail'}>
              <option value="KILOGRAM">Kg</option>
              <option value="GRAM">Gr</option>
            </FormInput>
          </Col>
        </Row>

        {/* Dòng Người thực hiện */}
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="receiverId"
              name="receiverId"
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

        {/* Dòng chú thích */}
        <Row>
          <Label for="note" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Chú thích</span>
              <span className="word-count">{note?.length || 0}/{MAX_NOTE_LENGTH}</span>
            </Flex>
          </Label>
          <Col>
            <FormInput disabled={type === 'detail'} rows={5} maxLength={200} control={control} name="note" type="textarea" />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default SelectingAddingAdditivesForm;
