import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useEffect, useRef, useState } from 'react';
import Form from 'app/components/form/form';
import { SubmitHandler, useForm } from 'react-hook-form';
import FormInput from 'app/components/form/form-input';
import { FORM } from 'app/shared/model/enumerations/form.model';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { getCurrentDateMonth } from 'app/shared/util/get-current-date-month';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  ApproveTimeKeepingMonthlySchema,
  RejectTimeKeepingMonthlySchema,
  approveTimeKeepingMonthSchema,
  rejectTimeKeepingMonthSchema,
} from 'app/validation/time-sheet-bulk-approval.validation';
import { FormGroup, Label } from 'reactstrap';
import useTimeKeepingMonthly from 'app/hooks/use-time-keeping-monthly';
import { TIME_KEEPING_MONTHLY_REVIEW } from 'app/shared/model/enumerations/time-keeping-monthly.model';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { FILE_UTIL } from 'app/constants/common';
import { ITimeKeepingMonthlyParams } from 'app/shared/model/time-keeping-monthly.model';
import dayjs from 'dayjs';
import { useAppSelector } from 'app/config/store';

const { REVIEW_TIME_KEEPING_MONTHLY } = MUTATION_KEY;
const { usePostTimeKeepingMonthlyReview, usePostTimeKeepingMonthlyApprove, usePostTimeKeepingMonthlyReject } = useTimeKeepingMonthly;
const { usePostFile } = useFile;

interface IModalAcceptBulkTimeSheet {
  isOpen: boolean;
  toggle: () => void;
  filter: ITimeKeepingMonthlyParams;
  selectedRowKeys: string[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
}

export const ModalAcceptBulkTimeSheet = (props: IModalAcceptBulkTimeSheet) => {
  const { isOpen, toggle, filter, selectedRowKeys, setSelectedRowKeys } = props;

  const { control, handleSubmit, reset } = useForm<ApproveTimeKeepingMonthlySchema>({
    resolver: zodResolver(approveTimeKeepingMonthSchema),
    defaultValues: {
      month: getCurrentDateMonth(dayjs(filter.month).month() + 1).date,
    },
  });

  const isApproving = useIsMutating({
    mutationKey: [REVIEW_TIME_KEEPING_MONTHLY],
  });

  const account = useAppSelector(state => state.authentication.account);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [isDirty, setIsDirty] = useState(false);
  const [file, setFile] = useState<IFIle | null>(null);

  const onSuccess = () => {
    toggle();
    reset();
    setFile(null);
    setSelectedRowKeys([]);
  };

  const { mutate } = usePostTimeKeepingMonthlyReview(toggle);
  const { mutate: approve, isPending: isPendingApprove } = usePostTimeKeepingMonthlyApprove(onSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  useEffect(() => {
    file && setIsDirty(false);
  }, [file]);

  useEffect(() => {
    if (account?.signatureId) {
      setFile({
        id: account?.signatureId,
        name: account?.signatureFileName,
      });
    }
  }, [account, isOpen]);

  const onSubmit: SubmitHandler<ApproveTimeKeepingMonthlySchema> = async data => {
    if (!file) {
      setIsDirty(true);
      return;
    }

    if (selectedRowKeys.length > 0) {
      approve({
        signatureFile: file?.id,
        ids: selectedRowKeys,
      });

      return;
    }

    mutate(
      {
        month: data.month,
        status: TIME_KEEPING_MONTHLY_REVIEW.APPROVED,
        signatureFile: file?.id,
      },
      {
        onSuccess: () => {
          setFile(null);
          reset();
        },
      },
    );
  };

  const disabledOkBtn = !!isApproving || isPendingApprove;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      disabledOk={disabledOkBtn}
      okSubmitForm={FORM.APPROVE_TIMESHEET}
      titleHeader='Xác nhận duyệt công tháng'
    >
      <Form<ApproveTimeKeepingMonthlySchema> id={FORM.APPROVE_TIMESHEET} onSubmit={handleSubmit(onSubmit)}>
        <FormInput control={control} id="month" label="Tháng" name="month" type="select">
          <option selected disabled>
            Chọn tháng
          </option>
          {Array(12)
            .fill('')
            .map((_, index) => (
              <option key={index} value={getCurrentDateMonth(index + 1).date}>
                Tháng {index + 1}
              </option>
            ))}
        </FormInput>

        <Button
          key={new Date().getMilliseconds()}
          style={{ marginBottom: 24 }}
          type="button"
          color="primary"
          onClick={() => fileInputRef.current?.click()}
          className="btn-upload"
          loading={loadingUpload}
        >
          <Flex align="center" gap={8}>
            <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
            Đính kèm
            <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>

        {file && <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />}
        {!file && isDirty && <p className="text-danger">Vui lòng chọn chữ ký</p>}
      </Form>
    </Modal>
  );
};

interface IModalRejectBulkTimeSheet {
  isOpen: boolean;
  toggle: () => void;
  filter: ITimeKeepingMonthlyParams;
  selectedRowKeys: string[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
}

export const ModalRejectBulkTimeSheet = (props: IModalRejectBulkTimeSheet) => {
  const { isOpen, toggle, filter, selectedRowKeys, setSelectedRowKeys } = props;

  const { control, setValue, watch, handleSubmit, reset } = useForm<RejectTimeKeepingMonthlySchema>({
    resolver: zodResolver(rejectTimeKeepingMonthSchema),
    defaultValues: {
      month: getCurrentDateMonth(dayjs(filter.month).month() + 1).date,
    },
  });

  const onSuccess = () => {
    toggle();
    reset();
    setSelectedRowKeys([]);
  };

  const { mutate } = usePostTimeKeepingMonthlyReview(toggle);
  const { mutate: reject, isPending: isPendingReject } = usePostTimeKeepingMonthlyReject(onSuccess);
  const isRejecting = useIsMutating({
    mutationKey: [REVIEW_TIME_KEEPING_MONTHLY],
  });

  const disabledOkBtn = !!isRejecting || isPendingReject;

  const onSubmit: SubmitHandler<RejectTimeKeepingMonthlySchema> = data => {
    if (selectedRowKeys.length > 0) {
      reject({
        note: data.note,
        ids: selectedRowKeys,
      });

      return;
    }

    mutate({
      month: data.month,
      status: TIME_KEEPING_MONTHLY_REVIEW.REJECTED,
      note: data.note,
    });
    reset();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      disabledOk={disabledOkBtn}
      okSubmitForm={FORM.REJECT_TIMESHEET}
      titleHeader='Từ chối duyệt công tháng'
    >
      <Form<RejectTimeKeepingMonthlySchema> id={FORM.REJECT_TIMESHEET} onSubmit={handleSubmit(onSubmit)}>
        <FormInput control={control} id="month" label="Tháng" name="month" type="select">
          <option selected disabled>
            Chọn tháng
          </option>
          {Array(12)
            .fill('')
            .map((_, index) => (
              <option key={index} value={getCurrentDateMonth(index + 1).date}>
                Tháng {index + 1}
              </option>
            ))}
        </FormInput>

        <FormGroup>
          <Label for="note" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Ghi chú</span>
              <span className="word-count">{watch('note')?.length || 0}/200</span>
            </Flex>
          </Label>
          <FormInput
            id="note"
            rows={5}
            control={control}
            name="note"
            type="textarea"
            onChange={e => e.target.value.length > 200 && setValue('note', e.target.value.slice(0, 200))}
          />
        </FormGroup>
      </Form>
    </Modal>
  );
};
