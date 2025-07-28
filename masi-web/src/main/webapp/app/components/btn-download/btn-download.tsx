import React from 'react';
import { Col } from 'reactstrap';

import './btn-download.scss';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import FormInput from 'app/components/form/form-input';
import useLeaveTracking from 'app/hooks/use-leave-tracking';
import FormDatePicker from 'app/components/form/form-date-picker';
import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useDownloadXlsx } from 'app/hooks/use-download';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';
import { LeaveTrackingFormSchema, leaveTrackingSchema } from 'app/validation/leave-tracking.validation';
import { DATE_FORMAT } from 'app/constants/common';

const { useGetLeaveTracking } = useLeaveTracking;

interface IBtnDownloadProps {
  isOpen: boolean;
  toggle: () => void;
  toggleDownloadSuccessful: () => void;
}

function BtnDownload({ isOpen, toggle, toggleDownloadSuccessful }: IBtnDownloadProps) {
  const { control, handleSubmit, watch, setValue, formState } = useForm<LeaveTrackingFormSchema>({
    resolver: zodResolver(leaveTrackingSchema),
  });

  const onSuccess = () => {
    toggle();
    toggleDownloadSuccessful();
  };

  const { trigger, data, isFetching } = useGetLeaveTracking(watch('year')?.year?.toString(), watch('workspaceType'), onSuccess);

  const onSubmit: SubmitHandler<LeaveTrackingFormSchema> = async () => {
    trigger();
  };

  useDownloadXlsx(data?.data, 'leave_tracking', 'xlsx');

  return (
    <Modal
      className="modals-leave-tracking"
      isOpen={isOpen}
      okSubmitForm={FORM.LEAVE_TRACKING}
      toggle={toggle}
      disabledOk={isFetching}
      titleHeader='Theo dõi đăng ký nghỉ chế độ'
    >
      <Form id={FORM.LEAVE_TRACKING} onSubmit={handleSubmit(onSubmit)}>
        <Flex direction="column" gap={16}>
          <Col span={6}>
            <FormInput control={control} id="type" name="workspaceType" placeholder="Loại" type="select" label="Loại">
              <option selected disabled>
                Loại
              </option>
              <option value="OFFICE">Văn phòng</option>
              <option value="FACTORY">Nhà máy</option>
            </FormInput>
          </Col>
          <Col span={6}>
            <Typography level={6}>Chọn năm</Typography>
            <FormDatePicker name="year" control={control} setValue={setValue} format={DATE_FORMAT.YEAR} formState={formState} onlyYearPicker={true} />
          </Col>
        </Flex>
      </Form>
    </Modal>
  );
}

export default BtnDownload;
