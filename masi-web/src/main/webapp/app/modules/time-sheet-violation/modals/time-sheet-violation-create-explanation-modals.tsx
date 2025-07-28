import dayjs from 'dayjs';
import { useParams } from 'react-router';
import React, { useEffect, useState } from 'react';
import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';

import Form from 'app/components/form/form';
import Card from 'app/components/card/card';
import Modal from 'app/components/modal/modal';
import Table from 'app/components/table/table';
import Tooltip from 'app/components/tooltip/tooltip';
import FormInput from 'app/components/form/form-input';
import timeSheetViolationMapping from '../time-sheet-violation-mapping';
import useTimeKeepingViolation from 'app/hooks/use-time-keeping-violation';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { ColumnsTypes } from 'app/components/table/table.d';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IPostTimeKeepingExplanationDto } from 'app/shared/model/time-keeping-explanation.model';
import { ITimeKeepingViolation, ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import { CreateTimeKeepingExplanationSchema, postTimeKeepingExplanationSchema } from 'app/validation/time-keeping-explanation.validation';

const { CREATE_TIME_KEEPING_EXPLANATION } = MUTATION_KEY;
const { usePostTimeKeepingExplanationMutation } = useTimeKeepingExplanation;
const { useGetTimeKeepingViolationsQuery } = useTimeKeepingViolation;
const { mapTimeKeepingViolationTypeToText } = timeSheetViolationMapping;

interface IModalTimeSheetCreateTimeSheetExplanation {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  filterParent: ITimeKeepingViolationsParams;
}

export const ModalTimeSheetCreateTimeSheetExplanation = (props: IModalTimeSheetCreateTimeSheetExplanation) => {
  const { isOpen, toggle, toggleSuccess, filterParent } = props;

  const [disabledOk, setDisabledOk] = useState<boolean>(false);
  const [isCheck, setIsCheck] = useState<number[]>([]);
  const [dataReason, setDataReason] = useState({
    id: '',
    explanation: '',
    reason: '',
    indexCurrent: 0
  });

  const { mutate } = usePostTimeKeepingExplanationMutation(toggle, toggleSuccess);
  const isCreatingExplanation = useIsMutating({ mutationKey: [CREATE_TIME_KEEPING_EXPLANATION] });
  const { id } = useParams<{ id: string }>();
  const [filter, setFilter] = useState<ITimeKeepingViolationsParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    fromDate: '',
    toDate: '',
    type: [],
  });

  const { data, isLoading } = useGetTimeKeepingViolationsQuery(id, { ...filterParent, page: filter?.page, size: filter?.size, explained: false });
  const { control, handleSubmit, reset, setValue, watch } = useForm<CreateTimeKeepingExplanationSchema>({
    resolver: zodResolver(postTimeKeepingExplanationSchema),
  });

  const onSubmit: SubmitHandler<CreateTimeKeepingExplanationSchema> = values => {
    const data: IPostTimeKeepingExplanationDto[] = values?.listOfViolations?.filter(item => item?.explanation?.trim() !== '')
    mutate(data);
    reset();
  };

  const { page, size } = filter;
  const totalCount = data?.totalRecord || 0;

  const columns: ColumnsTypes<ITimeKeepingViolation> = [
    {
      title: 'Ngày',
      key: 'date',
      render: (_, record) => (record.timeKeeping?.date ? dayjs(record.timeKeeping?.date).format(DATE_FORMAT.DATE) : ''),

    },
    {
      title: 'Lỗi vi phạm',
      key: 'violation-error',
      width: 350,
      render: (text, record) => (
        <div style={{ width: '100%' }}>
          <Tooltip label={mapTimeKeepingViolationTypeToText(record.type)} target={`violation-error-${record.id}`}>
            <EllipsisParagraph text={mapTimeKeepingViolationTypeToText(record.type)} width={150} id={`violation-error-${record.id}`} />
          </Tooltip>
        </div>
      )
    },
    {
      title: 'Lý do',
      key: 'reason',
      width: 500,
      render: (text, record, index) => {
        const indexCurrent = filter?.page !== 0 ? (index + 1) + ((filter?.page) * 10) : index + 1
        return (
          <div style={{ width: '100%' }}>
            <FormInput
              control={control}
              name={`listOfViolations.${indexCurrent - 1}.explanation`}
              onChange={e => setDataReason({ id: record?.id, reason: record?.type, explanation: e?.target?.value, indexCurrent: indexCurrent })}
              type='textarea'
            />
          </div>
        )
      }
    },
  ];

  useEffect(() => {
    if (data?.data?.length === 0) setDisabledOk(true);
    else {
      if (isCheck?.length <= 2) {
        const dataConvert = Array.from({ length: data?.totalRecord }, () => ({ id: '', explanation: '', reason: '' }));
        setValue('listOfViolations', dataConvert)
        setIsCheck(prev => [...prev, 0])
      }
      setDisabledOk(false)
    };
  }, [data])

  useEffect(() => {
    const handler = setTimeout(() => {
      setValue('listOfViolations', [...watch('listOfViolations')?.map((item, index) => {
        if ((dataReason?.indexCurrent - 1) === index) return { ...item, explanation: dataReason?.explanation, reason: dataReason?.reason, id: dataReason?.id };
        else return item
      })])
    }, 500);

    return () => clearTimeout(handler);
  }, [dataReason]);

  useEffect(() => {
    if (!isOpen) {
      setValue('listOfViolations', []);
      setIsCheck([]);
      setDisabledOk(null);
    }
  }, [isOpen])

  return (
    <Modal
      disabledOk={!!isCreatingExplanation || disabledOk}
      loadingOk={!!isCreatingExplanation}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-timesheet-create-explanation"
      onOk={handleSubmit(onSubmit)}
      okText="Giải trình"
      cancel={false}
      titleHeader='Tạo giải trình chấm công'
    >
      <Form<CreateTimeKeepingExplanationSchema> onSubmit={handleSubmit(onSubmit)}>
        <Card header='Danh sách vi phạm' className='card-body-padding ' classNameHeader='card-header-bold'>
          <Table<ITimeKeepingViolation>
            rowKey="id"
            loading={isLoading}
            columns={columns}
            dataSource={data?.data}
            pagination={{
              page,
              size,
              totalCount,
              onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
            }}
          // rowSelection={{
          //   type: 'checkbox',
          //   onChange(selectedRowKeys, selectedRows) {
          //     setSelectedRowKeys(selectedRowKeys);
          //     // setSelectedRows(selectedRows);
          //   },
          //   selectedRowKeys,
          // }}
          />
        </Card>

        {/* <div className="divider" /> */}

        {/* <FormInput control={control} name="reason" label="Lý do giải trình" type="select">
          <option value={TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME}>
            {mapTimeKeepingExplanationReasonToText(TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME)}
          </option>
          <option value={TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT}>
            {mapTimeKeepingExplanationReasonToText(TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT)}
          </option>
        </FormInput> */}

        {/* <Card header='Lý do giải trình' className='card-body-padding ' classNameHeader='card-header-bold'>
          <FormGroup>
            <Label for="reason" style={{ width: '100%' }}>
              <Flex justify="space-between">
                <span>Giải trình</span>
                <span className="word-count">{watch('explanation')?.length || 0}/200</span>
              </Flex>
            </Label>
            <FormInput
              control={control}
              type="textarea"
              rows={4}
              name="explanation"
              placeholder="Hãy nhập lý do..."
              onChange={e => e.target.value.length > 200 && setValue('explanation', e.target.value.slice(0, 200))}
            />
          </FormGroup>
        </Card> */}
      </Form>
    </Modal>
  );
};

interface IModalRequestSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalCreateExplanationSuccess = (props: IModalRequestSuccess) => {
  const { isOpen, toggle } = props;

  return (

    <Modal Modal isOpen={isOpen} toggle={toggle} cancel={false}>
      <Typography level={3}>Tạo giải trình thành công</Typography>
      <Typography level={4}>Bạn đã tạo đơn giải trình thành công</Typography>
    </Modal>
  );
};
