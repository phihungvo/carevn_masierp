import dayjs from 'dayjs';
import { Col, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import React, { useEffect, useRef, useState } from 'react';

import useFile from 'app/hooks/use-file';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import useEmployee from 'app/hooks/use-employee';
import Button from 'app/components/button/button';
import useRecruitment from 'app/hooks/use-recruitment';
import FormInput from 'app/components/form/form-input';
import recruitmentMapping from '../recruitment-mapping';
import InputFile from 'app/components/input/input-file';
import FormSelect from 'app/components/form/form-select';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormTimePicker from 'app/components/form/form-time-picker';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import { IFIle } from 'app/shared/model/file.model';
import { DateObject } from 'react-multi-date-picker';
import { zodResolver } from '@hookform/resolvers/zod';
import { DATE_FORMAT, DEFAULT_NUMBER_REGEX, FILE_UTIL } from 'app/constants/common';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { interviewSchema, InterviewSchema } from 'app/validation/recruitment.validation';
import { INTERVIEW_MODE, RECRUITMENT_PROCESS } from 'app/shared/model/enumerations/recruitment.model';
import Card from 'app/components/card/card';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { handleNumberOnlyInput } from 'app/shared/util/handle-number-only-input';

const { recruitmentProcessTextMapping, interviewModeTextMapping } = recruitmentMapping;
const { useGetEmployeesQuery } = useEmployee;
const { usePostRecruitmentInterview, useGetInterviewSchedule, usePatchInterviewSchedule } = useRecruitment;
const { usePostFile } = useFile;

interface IScheduleFormProps {
  type: 'update' | 'create';
  isOpen?: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const ScheduleForm = (props: IScheduleFormProps) => {
  const { isOpen, type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, setValue, reset, watch, formState } = useForm<InterviewSchema>({
    resolver: zodResolver(interviewSchema),
    defaultValues: {
      interviewMode: INTERVIEW_MODE.OFFLINE,
    },
  });

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [file, setFile] = useState<IFIle | null>(null);

  const { data, isLoading } = useGetEmployeesQuery();
  const { mutate } = usePostRecruitmentInterview(selectedRecord, toggle, toggleSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);
  const { data: interviewSchedule } = useGetInterviewSchedule(selectedRecord)
  const { mutate: patchSchedule } = usePatchInterviewSchedule(selectedRecord, setFile, toggle, toggleSuccess);

  const onSubmit = async (data: InterviewSchema) => {
    if (type === 'update') {
      patchSchedule({
        candidateName: data.candidateName,
        interviewDate:
          data.interviewDate?.toDate()?.toISOString().split('T')[0] +
          'T' +
          dayjs(data?.interviewTime, DATE_FORMAT.TIME_ONLY)
            ?.toISOString()
            .split('T')[1],
        interviewerId: data.interviewerId,
        process: data.process,
        interviewMode: data.interviewMode,
        cvFile: file?.id,
        email: data?.email,
        phoneNumber: data?.phoneNumber,
      });
      setSelectedRecord(null);
      return;
    }

    mutate({
      candidateName: data.candidateName,
      interviewDate:
        data.interviewDate?.toDate()?.toISOString().split('T')[0] +
        'T' +
        dayjs(data?.interviewTime, DATE_FORMAT.TIME_ONLY)
          ?.toISOString()
          .split('T')[1],
      interviewerId: data.interviewerId,
      process: data.process,
      interviewMode: data.interviewMode,
      cvFile: file?.id,
      email: data?.email,
      phoneNumber: data?.phoneNumber
    });
    setSelectedRecord(null);
  };

  const handleOnChangeUploadFile = (file) => {
    uploadFile(file)
    setValue('cvFile', file?.name)
  }

  useEffect(() => {
    if (interviewSchedule) {
      setValue('candidateName', interviewSchedule?.candidateName);
      setValue('email', interviewSchedule?.email);
      setValue('phoneNumber', interviewSchedule?.phoneNumber);
      setValue('interviewDate', new DateObject(interviewSchedule?.interviewDate));
      setValue('interviewTime', interviewSchedule?.interviewDate && dayjs(interviewSchedule?.interviewDate).format(DATE_FORMAT.TIME_ONLY));;
      setValue('interviewerId', interviewSchedule?.interviewerId);
      setValue('process', interviewSchedule?.process);
      setValue('interviewMode', interviewSchedule?.interviewMode);
      setValue('cvFile', interviewSchedule?.cvFile)
      if (interviewSchedule?.cvFileAttachment)
        setFile({
          id: interviewSchedule?.cvFileAttachment?.id,
          name: interviewSchedule?.cvFileAttachment?.name
        })
    }

  }, [interviewSchedule])

  useEffect(() => {
    if (!isOpen) {
      reset()
      setFile(null);
    }
  }, [isOpen])

  return (
    <Form id={FORM.RECRUITMENT} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="candidateName" name="candidateName" label="Tên ứng viên" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="email" name="email" label="Email" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="phoneNumber"
              name="phoneNumber"
              label="Số điện thoại"
              type='text'
              onChange={e => handleNumberOnlyInput(e, setValue, 'phoneNumber')}
              onPaste={e => handleValidatePaste(e, DEFAULT_NUMBER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="interviewDate" name="interviewDate" label="Ngày phỏng vấn" formState={formState} />
          </Col>

          <Col md={6}>
            <FormTimePicker control={control} id="interviewTime" name="interviewTime" label="Giờ phỏng vấn" style={{ width: 322 }} />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="interviewerId"
              name="interviewerId"
              placeholder="Chọn người phỏng vấn"
              label="Người phỏng vấn"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="process" name="process" label="Tiến trình" type="select" disabled={type === 'update'}>
              <option selected disabled>
                Chọn tiến trình
              </option>
              <option value={RECRUITMENT_PROCESS.INTERVIEWED}>{recruitmentProcessTextMapping(RECRUITMENT_PROCESS.INTERVIEWED)}</option>
              <option value={RECRUITMENT_PROCESS.WAITING_INTERVIEW}>
                {recruitmentProcessTextMapping(RECRUITMENT_PROCESS.WAITING_INTERVIEW)}
              </option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} id="interviewMode" name="interviewMode" label="Hình thức" type="select">
              <option selected disabled>
                Chọn hình thức
              </option>
              <option value={INTERVIEW_MODE.ONLINE}>{interviewModeTextMapping(INTERVIEW_MODE.ONLINE)}</option>
              <option value={INTERVIEW_MODE.OFFLINE}>{interviewModeTextMapping(INTERVIEW_MODE.OFFLINE)}</option>
            </FormInput>
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Tải tệp đính kèm' className='card-body-padding' classNameHeader='card-header-bold'>
        <Button
          key={new Date().getMilliseconds()}
          type="button"
          color="primary"
          onClick={() => fileInputRef.current?.click()}
          className="btn-upload"
          loading={loadingUpload}
        >
          <Flex align="center" gap={8}>
            <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
            Đính kèm
            <InputFile onFileChange={file => handleOnChangeUploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>

        {file && watch('cvFile') && (
          <>
            <div className="divider" />
            <AttachmentPreview name={file?.name} onClose={() => setFile(null)} fileUrl={`${FILE_UTIL}/${file?.id}`} />
          </>
        )}

      </Card>
    </Form>
  );
};

export default ScheduleForm;
