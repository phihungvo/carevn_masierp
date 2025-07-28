import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import Input from 'app/components/input/input';
import { DEFAULT_DECIMAL_REGEX, DEFAULT_NUMBER_REGEX } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useEmployee from 'app/hooks/use-employee';
import useWorkspace from 'app/hooks/use-workspace';
import recruitmentMapping from 'app/modules/recruitment/recruitment-mapping';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import { EMPLOYEE_STATUS } from 'app/shared/model/enumerations/employee.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { GENDER, RECRUITMENT_CONTRACT_TYPE, RECRUITMENT_POSITION } from 'app/shared/model/enumerations/recruitment.model';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { EmployeeFormSchema, employeeSchema } from 'app/validation/employee.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import { getWorkDurationDisplay } from '../util/official-work-duration';
import { handleNumberOnlyInput } from 'app/shared/util/handle-number-only-input';

const { recruitmentPositionTextMapping, recruitmentContractTypeTextMapping } = recruitmentMapping;
const { useGetWorkspacesQuery } = useWorkspace;
const {
  useGetEmployeesQuery,
  usePostEmployeeProfileMutation,
  useGetEmployeeProfileByIdQuery,
  usePatchEmployeeProfileMutation,
  useGetEmployeeSequenceIdQuery,
  useGetEmployeeByTaxCodeQuery,
  useGetEmployeeByCitizenIdQuery,
  useGetEmployeeByBankNumberQuery,
} = useEmployee;
// const { useEmployeeSeniority } = useAnnualLeave; // tạm bỏ qua

interface EmployeesFormProps {
  toggle?: () => void;
}

const EmployeesForm = (props: EmployeesFormProps) => {
  const { toggle } = props;

  const { id } = useParams();

  const { control, handleSubmit, watch, setValue, formState } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeSchema),
  });

  const debounceTaxCode = useDebounce(watch('taxCode'), 500);
  const debounceCitizenId = useDebounce(watch('citizenId'), 500);
  const debounceBankNumber = useDebounce(watch('bankNumber'), 500);

  // const workspaceId = watch('workspaceId');
  const startWorkDate = watch('startWorkDate');
  const probationEndDate = watch('probationDate');

  const { data: workspaces, isLoading: wspLoading } = useGetWorkspacesQuery();
  const { data: employees, isLoading: empLoading } = useGetEmployeesQuery();
  const { data: detail } = useGetEmployeeProfileByIdQuery(id);
  const { data: employeeCode } = useGetEmployeeSequenceIdQuery(
    {
      gender: watch('gender'),
    },
    !id,
  );
  // const { data: dataSeniority } = useEmployeeSeniority(workspaceId, startWorkDate?.format(DATE_FORMAT.YEAR_DATE));
  const { isSuccess: emByTaxSuccess } = useGetEmployeeByTaxCodeQuery(debounceTaxCode);
  const { isSuccess: emByCitizenSuccess } = useGetEmployeeByCitizenIdQuery(debounceCitizenId);
  const { isSuccess: emByBankSuccess } = useGetEmployeeByBankNumberQuery(debounceBankNumber);
  const { mutate: create } = usePostEmployeeProfileMutation(toggle);
  const { mutate: update } = usePatchEmployeeProfileMutation(id, toggle);

  const isWarningEmByCitizen = id
    ? debounceCitizenId && debounceCitizenId !== detail?.citizenId && emByCitizenSuccess
    : debounceCitizenId && emByCitizenSuccess;
  const isWarningEmByTax = id
    ? debounceTaxCode && debounceTaxCode !== detail?.taxCode && emByTaxSuccess
    : debounceTaxCode && emByTaxSuccess;
  const isWarningEmByBank = id
    ? debounceBankNumber && debounceBankNumber !== detail?.bankNumber && emByBankSuccess
    : debounceBankNumber && emByBankSuccess;

  const onSubmit: SubmitHandler<EmployeeFormSchema> = values => {
    if (isWarningEmByCitizen || isWarningEmByTax || isWarningEmByBank) return;

    const submitValues: IEmployeeProfiles = {
      fullName: values.fullName,
      employeeCode: values.employeeCode,
      gender: values?.gender,
      workspaceId: values?.workspaceId,
      citizenId: values?.citizenId,
      citizenIssueDate: values?.citizenIssueDate?.toDate()?.toISOString(),
      citizenIssuePlace: values?.citizenIssuePlace,
      residenceAddress: values?.residenceAddress,
      temporaryAddress: values?.temporaryAddress,
      birthday: values?.birthday?.toDate()?.toISOString(),
      phone: values?.phone,
      taxCode: values?.taxCode,
      startWorkDate: values?.startWorkDate?.toDate()?.toISOString(),
      role: values?.role,
      position: values?.position,
      bankCode: values?.bankCode,
      bankNumber: values?.bankNumber,
      contractType: values?.contractType,
      contractTerm: values?.contractType === RECRUITMENT_CONTRACT_TYPE.UNDEFINED_TERM ? '' : values?.contractTerm,
      contractNumber: values?.contractNumber,
      contractDate: values?.contractDate?.toDate()?.toISOString(),
      contractEndDate: values?.contractEndDate?.toDate()?.toISOString(),
      level: values?.level,
      parkingCard: values?.parkingCard,
      insuranceCard: values?.insuranceCard,
      referrerId: values?.referrerId,
      referrerDate: values?.referrerDate?.toDate()?.toISOString(),
      email: values?.email,
      note: values?.note,
      status: values?.status,
      probationDateFrom: values?.probationDate?.[0]?.toDate()?.toISOString(),
      probationDateTo: values?.probationDate?.[1]
        ? values?.probationDate?.[1]?.toDate()?.toISOString()
        : values?.probationDate?.[0]?.toDate()?.toISOString(),
      insurancePaymentLevel: values?.insurancePaymentLevel && Number(values?.insurancePaymentLevel),
    };

    if (id) {
      update(submitValues);

      return;
    }

    create(submitValues);
  };

  useEffect(() => {
    if (detail) {
      setValue('fullName', detail?.fullName);
      setValue('employeeCode', detail?.employeeCode);
      setValue('gender', detail?.gender);
      setValue('workspaceId', detail?.workspaceId);
      setValue('citizenId', detail?.citizenId);
      setValue('citizenIssueDate', new DateObject(detail?.citizenIssueDate).add(7, 'hours'));
      setValue('citizenIssuePlace', detail?.citizenIssuePlace);
      setValue('residenceAddress', detail?.residenceAddress);
      setValue('temporaryAddress', detail?.temporaryAddress);
      setValue('birthday', new DateObject(detail?.birthday).add(7, 'hours'));
      setValue('phone', detail?.phone);
      setValue('taxCode', detail?.taxCode);
      setValue('startWorkDate', new DateObject(detail?.startWorkDate).add(7, 'hours'));
      setValue('role', detail?.role);
      setValue('position', detail?.position);
      setValue('bankCode', detail?.bankCode);
      setValue('bankNumber', detail?.bankNumber);
      setValue('contractType', detail?.contractType);
      setValue('contractTerm', detail?.contractTerm);
      setValue('contractNumber', detail?.contractNumber);
      setValue('contractDate', new DateObject(detail?.contractDate).add(7, 'hours'));
      setValue('contractEndDate', new DateObject(detail?.contractEndDate).add(7, 'hours'));
      setValue('level', detail?.level);
      setValue('parkingCard', detail?.parkingCard);
      setValue('insuranceCard', detail?.insuranceCard);
      setValue('referrerId', detail?.referrerId);
      setValue('referrerDate', new DateObject(detail?.referrerDate).add(7, 'hours'));
      setValue('email', detail?.email);
      setValue('note', detail?.note);
      setValue('status', detail?.status);
      setValue(
        'probationDate',
        detail?.probationDateFrom && detail?.probationDateTo
          ? [new DateObject(detail?.probationDateFrom).add(7, 'hours'), new DateObject(detail?.probationDateTo).add(7, 'hours')]
          : undefined,
      );
      setValue('officialWorkTypeDuration', detail?.officialWorkTypeDuration?.toString() || '');
      setValue('insurancePaymentLevel', detail?.insurancePaymentLevel?.toString() || '');
    }
  }, [detail]);

  useEffect(() => {
    if (id) {
      setValue('employeeCode', detail?.employeeCode);
      return;
    }
    setValue('employeeCode', employeeCode?.[0]?.nextEmployeeId);
  }, [id, detail, employeeCode]);
  useEffect(() => {
    if (startWorkDate) {
      setValue('officialWorkTypeDuration', getWorkDurationDisplay({
        probationEndDate: probationEndDate?.[1]?.toDate(),
        startWorkDate: startWorkDate?.toDate(),
      }));
    } else {
      setValue('officialWorkTypeDuration', '0');
    }
  }, [startWorkDate, probationEndDate]);

  return (
    <Form id={FORM.EMPLOYEES_CREATE} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        <Row >
          <Col md={12}>
            <Label className="fw-bold">Thông tin chung:</Label>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Mã nhân viên" name="employeeCode" disabled />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Tên nhân viên" name="fullName" />
          </Col>

          <Col md={6}>
            <FormDatePicker control={control} setValue={setValue} label="Sinh ngày" name="birthday" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Giới tính" name="gender" type="select">
              <option selected disabled>
                Chọn giới tính
              </option>
              <option value={GENDER.MALE}>Nam</option>
              <option value={GENDER.FEMALE}>Nữ</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="workspaceId"
              name="workspaceId"
              placeholder="Chọn BP/Nhà máy"
              label="BP / Nhà máy"
              options={workspaces?.data?.map(wsp => ({
                label: wsp?.name,
                value: wsp?.id,
              }))}
              isLoading={wspLoading}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Chức vụ" name="role" />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Vị trí" name="position" type="select">
              <option selected disabled>
                Chọn vị trí
              </option>
              <option value={RECRUITMENT_POSITION.EMPLOYEE}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.EMPLOYEE)}</option>
              <option value={RECRUITMENT_POSITION.TEAM_LEADER}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.TEAM_LEADER)}</option>
              <option value={RECRUITMENT_POSITION.SUPERVISOR}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.SUPERVISOR)}</option>
              <option value={RECRUITMENT_POSITION.DIRECTOR}>{recruitmentPositionTextMapping(RECRUITMENT_POSITION.DIRECTOR)}</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Bậc" name="level" type="select">
              <option selected disabled>
                Chọn bậc
              </option>
              <option value="0">0</option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
            </FormInput>
          </Col>
        </Row>

        <div className="divider" />

        <Row>
          <Col md={12}>
            <Label className="fw-bold">Thông tin liên hệ:</Label>
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="Số điện thoại"
              name="phone"
              type='text'
              onChange={e => handleNumberOnlyInput(e, setValue, 'phone')}
              onPaste={e => handleValidatePaste(e, DEFAULT_NUMBER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Email" name="email" />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Địa chỉ thường trú" name="residenceAddress" />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Địa chỉ tạm trú" name="temporaryAddress" />
          </Col>
        </Row>

        <div className="divider" />

        <Row>
          <Col md={12}>
            <Label className="fw-bold">Thông tin khác:</Label>
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="Số CCCD"
              name="citizenId"
              className={`${isWarningEmByCitizen ? 'invalid' : ''}`}
              {...(isWarningEmByCitizen && { errorMsg: 'Số CCCD đã tồn tại' })}
              onChange={e => handleNumberOnlyInput(e, setValue, 'citizenId')}
              onPaste={e => handleValidatePaste(e, DEFAULT_NUMBER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker control={control} setValue={setValue} label="Ngày cấp" name="citizenIssueDate" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Nơi cấp" name="citizenIssuePlace" />
          </Col>


          <Col md={6}>
            <FormInput control={control} label="Mã ngân hàng" name="bankCode" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="TK ngân hàng"
              name="bankNumber"
              className={`${isWarningEmByBank ? 'invalid' : ''}`}
              {...(isWarningEmByBank && { errorMsg: 'Mã ngân hàng đã tồn tại' })}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Thẻ BH" name="insuranceCard" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="Mức đóng bảo hiểm"
              name="insurancePaymentLevel"
              onChange={e =>
                handleValidDecimal<EmployeeFormSchema>(e.target.value, 'insurancePaymentLevel', DEFAULT_DECIMAL_REGEX, setValue)
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Thẻ xe" name="parkingCard" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="MST"
              name="taxCode"
              className={`${isWarningEmByTax ? 'invalid' : ''}`}
              {...(isWarningEmByTax && { errorMsg: 'Mã số thuế đã tồn tại' })}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Ghi chú" name="note" />
          </Col>

        </Row>

        <div className="divider" />

        <Row>
          <Col md={12}>
            <Label className="fw-bold">Thông tin công việc:</Label>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Số HĐ" name="contractNumber" />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Loại HĐ" name="contractType" type="select">
              <option selected disabled>
                Chọn loại HĐ
              </option>
              <option value={RECRUITMENT_CONTRACT_TYPE.TRIAL}>{recruitmentContractTypeTextMapping(RECRUITMENT_CONTRACT_TYPE.TRIAL)}</option>
              <option value={RECRUITMENT_CONTRACT_TYPE.FIXED_TERM}>
                {recruitmentContractTypeTextMapping(RECRUITMENT_CONTRACT_TYPE.FIXED_TERM)}
              </option>
              <option value={RECRUITMENT_CONTRACT_TYPE.UNDEFINED_TERM}>
                {recruitmentContractTypeTextMapping(RECRUITMENT_CONTRACT_TYPE.UNDEFINED_TERM)}
              </option>
              <option value={RECRUITMENT_CONTRACT_TYPE.SEASONAL}>
                {recruitmentContractTypeTextMapping(RECRUITMENT_CONTRACT_TYPE.SEASONAL)}
              </option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Thời hạn" name="contractTerm" />
          </Col>

          <Col md={6}>
            <FormDatePicker control={control} setValue={setValue} label="Ngày vào làm" name="startWorkDate" formState={formState} />
          </Col>

          <Col md={6}>
            <FormDatePicker control={control} setValue={setValue} label="Ngày HĐ" name="contractDate" formState={formState} />
          </Col>

          {watch('contractType') != RECRUITMENT_CONTRACT_TYPE.UNDEFINED_TERM && (
            <Col md={6}>
              <FormDatePicker control={control} setValue={setValue} label="Ngày kết thúc" name="contractEndDate" formState={formState} />
            </Col>
          )}

          <Col md={6}>
            <FormDatePicker range control={control} setValue={setValue} label="Thời gian thử việc" name="probationDate" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              label="Thâm niên"
              name="officialWorkTypeDuration"
              onChange={e =>
                handleValidDecimal<EmployeeFormSchema>(e.target.value, 'officialWorkTypeDuration', DEFAULT_DECIMAL_REGEX, setValue)
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
              disabled
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="referrerId"
              name="referrerId"
              placeholder="Chọn người giới thiệu"
              label="Người giới thiệu"
              options={employees?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={empLoading}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker control={control} setValue={setValue} label="Ngày chi tiền giới thiệu" name="referrerDate" formState={formState} />
          </Col>

          <Col md={6}>
            <FormGroup>
              <Label>Tình trạng</Label>
              <Row>
                <Col md={4} style={{ marginTop: 12 }}>
                  <FormGroup check>
                    <Input
                      checked={watch('status') !== undefined && watch('status') === EMPLOYEE_STATUS.WORKING}
                      id="isActive"
                      name="isActive"
                      type="checkbox"
                      onChange={() => setValue('status', EMPLOYEE_STATUS.WORKING)}
                    />
                    <Label for="isActive">Đang làm</Label>
                  </FormGroup>
                </Col>
                <Col md={6} style={{ marginTop: 12 }}>
                  <FormGroup check>
                    <Input
                      checked={watch('status') !== undefined && watch('status') === EMPLOYEE_STATUS.RESIGNED}
                      id="notIsActive"
                      name="notIsActive"
                      type="checkbox"
                      onChange={() => setValue('status', EMPLOYEE_STATUS.RESIGNED)}
                    />
                    <Label for="notIsActive">Đã nghỉ</Label>
                  </FormGroup>
                </Col>
              </Row>
            </FormGroup>
            <FormInput hidden control={control} name="status" />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default EmployeesForm;
