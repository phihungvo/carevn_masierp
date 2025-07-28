import Form from 'app/components/form/form';
import useEmployee from 'app/hooks/use-employee';
import React, { useEffect, useRef, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Alert, Col, Label, Row } from 'reactstrap';
import FormInput from 'app/components/form/form-input';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import leaveRequestMapping from 'app/modules/leave-request/leave-request-mapping';
import FormDatePicker from 'app/components/form/form-date-picker';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Input from 'app/components/input/input';
import { LeaveRegimeFormSchema, leaveRegimeSchema } from 'app/validation/leave-regime.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import { DateObject } from 'react-multi-date-picker';
import dayjs from 'dayjs';
import FormSelect from 'app/components/form/form-select';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import useFile from 'app/hooks/use-file';
import { IBodyFile, IFIle } from 'app/shared/model/file.model';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useTimeSheet from 'app/hooks/use-time-sheet';
import Card from 'app/components/card/card';
import SelectLeaveRegisterDayType from './select-leave-register-day-type';
import useLeaveRequest from 'app/hooks/use-leave-request';

const { mapLeaveRequestType } = leaveRequestMapping;
const { usePostLeaveRegime, usePatchLeaveRegime, useGetLeaveRegimeById } = useLeaveRegime;
const { useGetEmployeesQuery } = useEmployee;
const { useAnnualLeavesEmployee } = useAnnualLeave;
const { usePostFile } = useFile;
const { useTimeKeepingOffTracking } = useTimeSheet;
const { useCountDayOff, useGetLeaveRequestCountDayEndOff } = useLeaveRequest;


interface ILeaveRegisterFormProps {
  type: 'update' | 'create';
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectRecord?: (record: string) => void;
  setIscheck?: React.Dispatch<React.SetStateAction<boolean>>;
}

const LeaveRegisterForm = (props: ILeaveRegisterFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectRecord, setIscheck } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [file, setFile] = useState<IFIle | null>(null);
  const [files, setFiles] = useState<IBodyFile[] | null>(null);
  const [dataOld, setDataOld] = useState<string>('')

  const [isCheck, setIsCheck] = useState<boolean>(false);

  const { control, handleSubmit, setValue, watch, trigger, formState } = useForm<LeaveRegimeFormSchema>({
    resolver: zodResolver(leaveRegimeSchema),
    defaultValues: {
      leaveType: LEAVE_REQUEST_TYPE.MATERNITY_LEAVE,
      lastWorkDate: new DateObject(),
      leaveRegisterDayType: LEAVE_REQUEST_DAY_TYPE.FULL_DAY,
    },
  });

  const { mutateAsync: mutateDayOff } = useCountDayOff();

  const { data, isLoading } = useGetEmployeesQuery();
  const { data: detail } = useGetLeaveRegimeById(selectedRecord);
  const { data: annualLeavesEmployee } = useAnnualLeavesEmployee(watch('employeeId'));
  const { data: offTracking } = useTimeKeepingOffTracking(
    watch('employeeId'),
    watch('lastWorkDate')?.format(DATE_FORMAT.YEAR_DATE)?.toString(),
    watch('returnWorkDate')?.format(DATE_FORMAT.YEAR_DATE)?.toString(),
  );
  const { mutate: create } = usePostLeaveRegime(toggle, toggleSuccess);
  const { mutate: update } = usePatchLeaveRegime(selectedRecord, toggle, toggleSuccess, setFiles);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);
  const { mutateAsync: getCountDayEndOff } = useGetLeaveRequestCountDayEndOff();

  const isOff = offTracking?.data?.length > 0;

  const onSubmit: SubmitHandler<LeaveRegimeFormSchema> = async values => {
    if (isOff) return;

    if (type === 'update') {
      update({
        leaveType: values?.leaveType,
        lastWorkDate: values?.lastWorkDate?.toDate()?.toISOString(),
        returnWorkDate: values?.returnWorkDate?.toDate()?.toISOString(),
        substituteId: values?.substituteId,
        employeeId: values?.employeeId,
        ...(values?.fromTime && { fromTime: dayjs(values?.fromTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
        ...(values?.toTime && { toTime: dayjs(values?.toTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
        leaveRequestDayType: values.leaveRegisterDayType,
        files,
        totalDayOff: Number(values?.leaveDays),
      });
      // setSelectRecord(null);
      return;
    }

    create({
      leaveType: values?.leaveType,
      lastWorkDate: values?.lastWorkDate?.toDate()?.toISOString(),
      returnWorkDate: values?.returnWorkDate?.toDate()?.toISOString(),
      substituteId: values?.substituteId,
      employeeId: values?.employeeId,
      ...(values?.fromTime && { fromTime: dayjs(values?.fromTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
      ...(values?.toTime && { toTime: dayjs(values?.toTime, DATE_FORMAT.TIME_ONLY).toISOString() }),
      leaveRequestDayType: values.leaveRegisterDayType,
      files,
      totalDayOff: Number(values?.leaveDays),
    });
  };

  useEffect(() => {
    if (file) setFiles(prev => [...(prev || []), { id: file?.id, fileName: file?.name }]);
  }, [file]);

  useEffect(() => {
    if (detail) {
      setFiles(null);
      setValue('leaveType', detail?.leaveType);
      setValue('returnWorkDate', new DateObject(detail?.returnWorkDate));
      setValue('lastWorkDate', new DateObject(detail?.lastWorkDate));
      setValue('substituteId', detail?.substituteId);
      setValue('employeeId', detail?.employeeId);
      setValue('fromTime', dayjs(detail?.fromTime).format("HH:mm"));
      setValue('toTime', dayjs(detail?.toTime).format("HH:mm"))
      setValue('leaveRegisterDayType', detail?.leaveRequestDayType);
      if (detail?.files)
        detail?.files?.map((item: IBodyFile) => {
          setFiles(prev => [...(prev || []), { id: item?.id, fileName: item?.fileName }]);
        });
    }
  }, [detail]);

  const handleOnChangeLeaveDays = (value: string) => setValue('leaveDays', value)

  const handleOnChange = (value: any) => {
    if (watch('lastWorkDate') && watch('returnWorkDate') && watch('employeeId')) {
      mutateDayOff({
        employeeId: watch('employeeId'),
        fromDate: watch('lastWorkDate').format(DATE_FORMAT.YEAR_DATE),
        toDate: watch('returnWorkDate').format(DATE_FORMAT.YEAR_DATE),
        leaveRequestDayType: watch('leaveRegisterDayType')
      }).then((data) => {
        setValue('leaveDays', data ? String(data) : '0')
      })
    }
  };

  const handleOnBlur = (value: string) => {
    if (dataOld !== value && value) {
      getCountDayEndOff({
        employeeId: watch('employeeId'),
        fromDate: watch('lastWorkDate') ? watch('lastWorkDate').format(DATE_FORMAT.YEAR_DATE) : new DateObject()?.toDate?.toString(),
        typeLeave: watch('leaveRegisterDayType'),
        totalDay: Number(watch('leaveDays')),
      }).then((data: any) => {
        const currentDate = new DateObject();

        if (!watch('lastWorkDate') || watch('lastWorkDate').format(DATE_FORMAT.YEAR_DATE) === currentDate.format(DATE_FORMAT.YEAR_DATE)) setValue('lastWorkDate', currentDate);

        setValue('returnWorkDate', new DateObject(data?.data?.dateEnd))
        setDataOld(watch('leaveDays'))
      })
    }
  }

  useEffect(() => {
    if (Number(watch('leaveDays')) === 0) setIscheck(true);
    else setIscheck(false);
  }, [watch('leaveDays')])

  return (
    <Form id={FORM.LEAVE_REGISTER} onSubmit={handleSubmit(onSubmit)}>
      {isOff && <Alert color="danger">Bạn đã nghỉ phép trong khoảng thời gian này. Vui lòng chọn lại thời gian nghỉ phép khác.</Alert>}
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="type" name="leaveType" placeholder="Nghỉ không lương" type="select" label="Loại nghỉ phép">
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
            <SelectLeaveRegisterDayType control={control} setValue={setValue} trigger={trigger} watch={watch} formState={formState} isCheck={isCheck} setIsCheck={setIsCheck} type={type} />
          </Col>



          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="from_date" onChange={handleOnChange} name="lastWorkDate" label="Ngày cuối cùng làm việc" formState={formState} />
          </Col>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="to_date" onChange={handleOnChange} name="returnWorkDate" label="Ngày trở lại làm việc" formState={formState} />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="substituteId"
              name="substituteId"
              placeholder="Chọn người thay thế"
              label="Người thay thế"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId"
              name="employeeId"
              placeholder="Chọn nhân viên"
              label="Nhân viên"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
              onChanges={handleOnChange}
            />
          </Col>

          <Col md={6}>
            <Label>Tổng số ngày nghỉ</Label>
            <Input value={watch('leaveDays')} id="leaveDays" onBlur={(e) => handleOnBlur(e?.target?.value)} onChange={(e) => handleOnChangeLeaveDays(e?.target?.value)} label="Số ngày nghỉ" name="leaveDays" />
          </Col>

          <Col>
            <Label>Số phép năm còn lại</Label>
            <Input value={annualLeavesEmployee?.numberDaysOff} id="annualLeavesEmployee" name="annualLeavesEmployee" disabled />
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
          className="btn-upload mt-4"
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
            <Flex gap={12} flexWrap="wrap">
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
    </Form>
  );
};

export default LeaveRegisterForm;
