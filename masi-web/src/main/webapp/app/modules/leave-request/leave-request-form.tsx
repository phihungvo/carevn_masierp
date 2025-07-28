import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { LeaveRequestFormSchema, leaveRequestSchema } from 'app/validation/leave-request.validation';
import React, { useEffect, useRef, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import dayOffMapping from './leave-request-mapping';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import useEmployee from 'app/hooks/use-employee';
import SelectLeaveRequestDayType from './select-leave-request-day-type';
import dayjs from 'dayjs';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import Input from 'app/components/input/input';
import { useAppSelector } from 'app/config/store';
import FormSelect from 'app/components/form/form-select';
import useFile from 'app/hooks/use-file';
import { IBodyFile, IFIle } from 'app/shared/model/file.model';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import Card from 'app/components/card/card';
import { DateObject } from 'react-multi-date-picker';

const { usePostLeaveRequestMutation, useCountDayOff, useGetLeaveRequestCountDayEndOff } = useLeaveRequest;
const { useGetEmployeesQuery } = useEmployee;
const { mapLeaveRequestType } = dayOffMapping;
const { usePostFile } = useFile;
const { useAnnualLeavesEmployee } = useAnnualLeave;

interface ILeaveRequestForm {
  toggle: () => void;
  toggleSuccess: () => void;
  setIscheck?: React.Dispatch<React.SetStateAction<boolean>>;
}

export const LeaveRequestForm = (props: ILeaveRequestForm) => {
  const account = useAppSelector(state => state.authentication.account);

  const { toggle, toggleSuccess, setIscheck } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [file, setFile] = useState<IFIle | null>(null);
  const [files, setFiles] = useState<IBodyFile[] | null>(null);
  const [dataOld, setDataOld] = useState<string>('')

  const { control, handleSubmit, setValue, trigger, watch, formState } = useForm<LeaveRequestFormSchema>({
    resolver: zodResolver(leaveRequestSchema),
    reValidateMode: 'onChange',
    defaultValues: {
      leaveRequestDayType: LEAVE_REQUEST_DAY_TYPE.FULL_DAY,
      leaveRequestType: LEAVE_REQUEST_TYPE.ANNUAL_LEAVE,
      emergency: false,
    },
  });

  const { data, isLoading } = useGetEmployeesQuery();
  const { data: annualLeavesEmployee } = useAnnualLeavesEmployee(account?.id);
  const { mutate } = usePostLeaveRequestMutation(toggle, toggleSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);
  const { mutateAsync: getCountDayEndOff } = useGetLeaveRequestCountDayEndOff();

  const onSubmit = async (values: LeaveRequestFormSchema) => {
    getCountDayEndOff({
      employeeId: account?.id,
      fromDate: watch('fromDate') ? watch('fromDate')?.format(DATE_FORMAT.YEAR_DATE) : new DateObject()?.toDate?.toString(),
      typeLeave: watch('leaveRequestDayType'),
      totalDay: Number(watch('leaveDiff')),
    }).then((data: any) => {
      const payload = {
        employeeId: account?.id,
        substituteId: values?.substituteId,
        fromDate: dayjs(values?.fromDate?.toDate()).format(DATE_FORMAT.YEAR_DATE),
        toDate: data?.data?.dateEnd?.split('T')[0],
        leaveRequestDayType: values?.leaveRequestDayType,
        leaveRequestType: values?.leaveRequestType,
        reason: values?.reason,
        fileId: file?.id,
        status: LEAVE_REQUEST_STATUS.PENDING,
        reviewerIds: [values.reviewerIds?.valueOf()],
        ...(values?.fromTime && { fromTime: dayjs(values?.fromTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
        ...(values?.toTime && { toTime: dayjs(values?.toTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
        files,
        totalDayOff: Number(values.leaveDiff),
      };

      mutate(payload);
      setFiles(null);
    })
  };

  const { mutateAsync: mutateDayOff } = useCountDayOff();

  useEffect(() => {
    if (file) setFiles(prev => [...(prev || []), { id: file?.id, fileName: file?.name }]);
  }, [file]);

  const handleOnChangeLeaveDiff = (value: string) => setValue('leaveDiff', value)

  const handleOnBlur = (value: string) => {
    if (dataOld !== value && value) {
      getCountDayEndOff({
        employeeId: account?.id,
        fromDate: watch('fromDate') ? watch('fromDate')?.format(DATE_FORMAT.YEAR_DATE) : new DateObject()?.toDate?.toString(),
        typeLeave: watch('leaveRequestDayType'),
        totalDay: Number(watch('leaveDiff')),
      }).then((data: any) => {
        const currentDate = new DateObject();

        if (!watch('fromDate') || watch('fromDate')?.format(DATE_FORMAT.YEAR_DATE) === currentDate?.format(DATE_FORMAT.YEAR_DATE)) setValue('fromDate', currentDate);

        setValue('toDate', new DateObject(data?.data?.dateEnd))
        setDataOld(watch('leaveDiff'))
      })
    }
  }

  const handleOnChange = () => {
    if (watch('fromDate') && watch('toDate')) {
      mutateDayOff({
        employeeId: account?.id,
        fromDate: watch('fromDate')?.format(DATE_FORMAT.YEAR_DATE),
        toDate: watch('toDate')?.format(DATE_FORMAT.YEAR_DATE),
        leaveRequestDayType: watch('leaveRequestDayType')
      }).then((data) => {
        setValue('leaveDiff', String(data))
      })
    }
  };

  useEffect(() => {
    if (Number(watch('leaveDiff')) === 0) setIscheck(true);
    else setIscheck(false);
  }, [watch('leaveDiff')])

  return (
    <Form<LeaveRequestFormSchema> id={FORM.LEAVE_REQUEST} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput
              control={control}
              id="type"
              name="leaveRequestType"
              placeholder="Nghỉ không lương"
              type="select"
              label="Loại nghỉ phép"
            >
              <option selected disabled>
                Chọn loại nghỉ phép
              </option>
              <option value={LEAVE_REQUEST_TYPE.ANNUAL_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.ANNUAL_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.MATERNITY_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.MATERNITY_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.SICK_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.SICK_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.UNPAID_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.UNPAID_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.FUNERAL_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.FUNERAL_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.WEDDING_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.WEDDING_LEAVE)}</option>
              <option value={LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE}>{mapLeaveRequestType(LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE)}</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <SelectLeaveRequestDayType control={control} setValue={setValue} trigger={trigger} watch={watch} formState={formState} />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} onChange={handleOnChange} id="from_date" name="fromDate" formState={formState} label="Ngày cuối cùng làm việc" />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} onChange={handleOnChange} id="to_date" name="toDate" formState={formState} label="Ngày trở lại làm việc" />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="reviewerIds"
              name="reviewerIds"
              placeholder="Chọn người duyệt"
              label="Người duyệt"
              options={data?.data?.map(e => ({
                label: `${e?.lastName || ''} ${e?.firstName || ''}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              id="substituteId"
              name="substituteId"
              placeholder="Chọn người thay thế"
              label="Người thay thế"
              options={data?.data?.map(e => ({
                label: `${e?.lastName || ''} ${e?.firstName || ''}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <Label>Số ngày nghỉ</Label>
            <Input value={watch('leaveDiff')} id="leaveDiff" onBlur={(e) => handleOnBlur(e?.target?.value)} onChange={(e) => handleOnChangeLeaveDiff(e?.target?.value)} label="Số ngày nghỉ" name="leaveDiff" />
          </Col >
          <Col md={6} className="mb-4">
            <Label>Số phép năm còn lại</Label>
            <Input value={annualLeavesEmployee?.numberDaysOff} id="annualLeavesEmployee" name="annualLeavesEmployee" disabled />
          </Col>
          <Col md={6}>
            <FormGroup check>
              <Label check for="emergencyCheck">
                Khẩn cấp
              </Label>
              <Input
                type="checkbox"
                id="emergencyCheck"
                name="emergencyCheck"
                onClick={() => {
                  setValue('emergency', !watch('emergency'));
                  trigger('fromDate');
                }}
              />
            </FormGroup>
            <FormInput control={control} id="emergency" name="emergency" hidden />
          </Col>
        </Row >
      </Card >

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
            <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>

        {!!files?.length && (
          <>
            <div className="divider" />
            <Flex flexWrap="wrap" gap={12}>
              {files?.map((item: IBodyFile) => {
                return (
                  <AttachmentPreview
                    name={item?.fileName}
                    onClose={() => setFiles(prev => prev.filter(e => e?.id !== item?.id))}
                    fileUrl={`${FILE_UTIL}/${item?.id}`}
                  />
                );
              })}
            </Flex>
          </>
        )}
      </Card>
    </Form >
  );
};
