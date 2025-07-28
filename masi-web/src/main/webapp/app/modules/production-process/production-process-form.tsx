import { zodResolver } from '@hookform/resolvers/zod';
import Card from 'app/components/card/card';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormSelect from 'app/components/form/form-select';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionProcess from 'app/hooks/use-production-process';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ProductionProcessFormSchema, productionProcessSchema } from 'app/validation/production-process.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';
import productionProcessMapping from 'app/modules/production-process/production-process-mapping';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import { PRODUCTION_COMMAND_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { CHECKLIST_TYPE } from 'app/shared/model/enumerations/production-process.model';

const { useGetProductionCommandsQuery, useGetProductionCommandsWithProcessById } = useProductionCommand;
const { usePostProductionProcess, useGetProductionProcessById, usePatchProductionProcess } = useProductionProcess;

const { mapProductionProcessType, mapProductionProcessNamePath } = productionProcessMapping;

interface IProductionProcessForm {
  type: 'create' | 'update';
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
}

export const ProductionProcessForm = (props: IProductionProcessForm) => {
  const { type, toggle, toggleSuccess, selectedRecord } = props;

  const navigate = useNavigate();

  const { control, handleSubmit, setValue, watch, formState } = useForm<ProductionProcessFormSchema>({
    resolver: zodResolver(productionProcessSchema),
    defaultValues: {
      fromDate: new DateObject(),
    },
  });

  const { data, isLoading } = useGetProductionCommandsQuery();
  const { data: dataProductionProcess } = useGetProductionProcessById(selectedRecord);
  const { trigger, data: productionProcessDetail } = useGetProductionCommandsWithProcessById(watch('moId'));
  const { mutateAsync: create } = usePostProductionProcess(toggle, toggleSuccess);
  const { mutate: update } = usePatchProductionProcess(selectedRecord, toggle, toggleSuccess);

  useEffect(() => {
    if (dataProductionProcess) {
      setValue('fromDate', new DateObject(dataProductionProcess.fromDate).add(7, 'hours'));
      setValue('moId', dataProductionProcess.moId);
    }
  }, [dataProductionProcess]);

  const onSubmit = async (values: ProductionProcessFormSchema) => {
    if (type === 'update') {
      update({
        fromDate: values.fromDate.toDate().toISOString(),
        moId: values.moId,
      });
      return;
    }

    const dataCreate = await create({
      fromDate: values.fromDate.toDate().toISOString(),
      moId: values.moId,
      checklistType: values.checklistType,
    });

    navigate(
      PATH.PRODUCTION_PROCESS_DETAIL.replace(':id', productionProcessDetail?.data?.id).replace(
        ':workItemId',
        dataCreate?.data?.workItemId,
      ) + `/${mapProductionProcessNamePath(watch('checklistType'))}`,
    );
  };

  useEffect(() => {
    if (watch('moId')) trigger();
  }, [watch('moId')]);

  useEffect(() => {
    if (productionProcessDetail?.data?.workOrders) setValue('workOrders', productionProcessDetail?.data?.workOrders ?? []);
  }, [productionProcessDetail]);

  const productionProcessType = Object.keys(CHECKLIST_TYPE).map(item => ({
    label: mapProductionProcessType(item),
    value: CHECKLIST_TYPE[item as CHECKLIST_TYPE],
  }));

  return (
    <Form<ProductionProcessFormSchema> id={FORM.PRODUCTION_PROCESS} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="date" label="Ngày bắt đầu" name="fromDate" placeholder="16:05, ngày 14 tháng 3 năm 2024" formState={formState} />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="moId"
              name="moId"
              placeholder="Chọn lệnh sản xuất"
              label="Lệnh sản xuất"
              options={data?.data?.map(i => ({
                label: i?.name,
                value: i?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="checklistType"
              name="checklistType"
              placeholder="Chọn loại biểu mẫu"
              label="Loại biểu mẫu"
              options={
                watch('moId')
                  ? productionProcessType?.filter(
                    item => !productionProcessDetail?.data?.workOrders?.map(item => item.checklistType)?.includes(item?.value),
                  )
                  : []
              }
            />
          </Col>
        </Row>
      </Card>
    </Form>
  );
};
