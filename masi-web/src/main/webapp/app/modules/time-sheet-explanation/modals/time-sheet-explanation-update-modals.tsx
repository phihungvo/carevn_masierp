import React, { useEffect, useState } from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FormGroup, Label } from 'reactstrap';
import Flex from 'app/components/flex/flex';
import { SubmitHandler, useForm } from 'react-hook-form';
import { UpdateTimeKeepingExplanationSchema, patchTimeKeepingExplanationSchema } from 'app/validation/time-keeping-explanation.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import FormInput from 'app/components/form/form-input';
import Form from 'app/components/form/form';
import Card from 'app/components/card/card';
import Table from 'app/components/table/table';
import { ITimeKeepingViolation, ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import useTimeKeepingViolation from 'app/hooks/use-time-keeping-violation';
import timeSheetViolationMapping from 'app/modules/time-sheet-violation/time-sheet-violation-mapping';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import dayjs from 'dayjs';
import { ColumnsTypes } from 'app/components/table/table.d';

const { usePatchTimeKeepingExplanationMutation, useGetTimeKeepingExplanationByIdQuery } = useTimeKeepingExplanation;
const { useGetTimeKeepingViolationsByExplanationQuery } = useTimeKeepingViolation;
const { mapTimeKeepingViolationTypeToText } = timeSheetViolationMapping;
// MODAL UPDATE REQUEST
interface IModalUpdateRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleError: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (record: string | null) => void;
}

export const UpdateRequestModal = (props: IModalUpdateRequest) => {
  const { isOpen, toggle, toggleError, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { data } = useGetTimeKeepingExplanationByIdQuery(selectedRecord);
  const [filter, setFilter] = useState<ITimeKeepingViolationsParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    explanationId: selectedRecord,
  });

  useEffect(() => {
    if (selectedRecord) setFilter(prev => ({ ...prev, explanationId: selectedRecord }))
  }, [selectedRecord])

  const { data: explanationViolations, isLoading: loadingExplanationViolations } = useGetTimeKeepingViolationsByExplanationQuery(filter);

  const { mutate } = usePatchTimeKeepingExplanationMutation(selectedRecord, toggle, toggleError, toggleSuccess);

  const { control, handleSubmit, setValue, watch } = useForm<UpdateTimeKeepingExplanationSchema>({
    resolver: zodResolver(patchTimeKeepingExplanationSchema),
  });

  useEffect(() => {
    if (data) {
      setValue('reason', data?.data?.reason);
      setValue('explanation', data?.data?.explanation);
    }
  }, [data]);

  const onSubmit: SubmitHandler<UpdateTimeKeepingExplanationSchema> = values => {
    mutate({
      explanation: values.explanation,
    });

    setSelectedRecord(null);
  };


  const columns: ColumnsTypes<ITimeKeepingViolation> = [
    {
      title: 'Ngày',
      key: 'date',
      render: (_, record) => (record.timeKeeping?.date ? dayjs(record.timeKeeping?.date).format(DATE_FORMAT.DATE) : ''),

    },
    {
      title: 'Lỗi vi phạm',
      key: 'violation-error',
      render: (text, record) => (
        <Tooltip label={mapTimeKeepingViolationTypeToText(record.type)} target={`violation-error-${record.id}`}>
          <EllipsisParagraph text={mapTimeKeepingViolationTypeToText(record.type)} width={150} id={`violation-error-${record.id}`} />
        </Tooltip>
      )
    },
  ];

  const { page, size } = filter;
  const totalCount = explanationViolations?.totalRecord || 0;

  return (
    <Modal
      onOk={handleSubmit(onSubmit)}
      className="modal-update-req-explanation"
      isOpen={isOpen}
      okText='Cập nhật'
      toggle={toggle}
      cancel={false}
      titleHeader='Cập nhật giải trình'
    >
      <Form<UpdateTimeKeepingExplanationSchema> onSubmit={handleSubmit(onSubmit)}>
        <Card header='Danh sách vi phạm' className='card-body-padding ' classNameHeader='card-header-bold'>
          <Table<ITimeKeepingViolation>
            rowKey="id"
            loading={loadingExplanationViolations}
            columns={columns}
            dataSource={explanationViolations?.data}
            pagination={{
              page,
              size,
              totalCount,
              onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
            }}
          />
        </Card>

        <div className="divider" />
        <Card header='Lý do giải trình' className='card-body-padding ' classNameHeader='card-header-bold'>
          <FormGroup>
            <Label for="explanation" style={{ width: '100%' }}>
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
              placeholder="Lý do giải trình"
              onChange={e => e.target.value.length > 200 && setValue('explanation', e.target.value.slice(0, 200))}
            />
          </FormGroup>
        </Card>
      </Form>
    </Modal>
  );
};

// MODAL ERROR UPDATE REQUEST
interface IModalErrorUpdateRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const ErrorUpdateRequestModal = (props: IModalErrorUpdateRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Không thể cập nhật'
    >
      <Typography level={4}>Không thể cập nhật giải trình chấm công khi đã được xét duyệt hoặc từ chối</Typography>
    </Modal>
  );
};

// MODAL UPDATE SUCCESS REQUEST
interface IModalUpdateSuccessRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const UpdateSuccessRequestModal = (props: IModalUpdateSuccessRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Cập nhật giải trình'
    >
      <Typography level={4}>Cập nhật giải trình chấm công thành công</Typography>
    </Modal>
  );
};
