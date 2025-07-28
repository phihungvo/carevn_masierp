import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { RECRUITMENT_CONTRACT_TYPE, RECRUITMENT_POSITION } from 'app/shared/model/enumerations/recruitment.model';
import recruitmentMapping from '../recruitment-mapping';
import FormDatePicker from 'app/components/form/form-date-picker';
import useRecruitment from 'app/hooks/use-recruitment';
import { RecruitmentFormSchema, recruitmentSchema } from 'app/validation/recruitment.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { DEFAULT_DECIMAL_REGEX, DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import { DateObject } from 'react-multi-date-picker';
import FormSelect from 'app/components/form/form-select';
import useWorkspace from 'app/hooks/use-workspace';
import useEmployee from 'app/hooks/use-employee';
import { UNIT } from 'app/shared/model/enumerations/unit.model';
import { checkHighlightDeadline } from 'app/modules/recruitment/check-highlight-deadline';
import Card from 'app/components/card/card';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

const { recruitmentPositionTextMapping, recruitmentContractTypeTextMapping } = recruitmentMapping;

const { useGetRecruitmentById, usePatchRecruitment, usePostRecruitment } = useRecruitment;
const { useGetWorkspacesQuery } = useWorkspace;
const { useGetEmployeeProfilesQuery } = useEmployee;

interface IRecruitmentFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const RecruitmentForm = (props: IRecruitmentFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, handleSubmit, watch, formState } = useForm<RecruitmentFormSchema>({
    resolver: zodResolver(recruitmentSchema),
    defaultValues: {
      salaryUnit: 'VND',
    },
  });

  const { data } = useGetRecruitmentById(selectedRecord);
  const { data: workspaces, isLoading: wspLoading } = useGetWorkspacesQuery();
  const { data: employeesProfiles, isLoading: empProfilesLoading } = useGetEmployeeProfilesQuery();
  const { mutate: create } = usePostRecruitment(toggle, toggleSuccess);
  const { mutate: update } = usePatchRecruitment(selectedRecord, toggle, toggleSuccess);
  

  const onSubmit = (data: RecruitmentFormSchema) => {
    const payload = {
      departmentId: data.departmentId,
      position: data.position,
      jobTitle: data.jobTitle,
      quantity: Number(data.quantity.replace(/,/g, '').replace(/\./g, '')),
      level: Number(data.level),
      wage: Number(data.wage.replace(/,/g, '').replace(/\./g, '')),
      startDate: data.startDate.toDate().toISOString(),
      recruitmentPurposes: data.recruitmentPurposes,
      requestNotes: data.requestNotes,
      description: data.description,
      contractType: data.contractType,
      salaryUnit: data.salaryUnit === UNIT.OTHER ? data.salaryUnitNote : data.salaryUnit,
      replaceForId: data.replaceForId,
      deadline: data.deadline.toDate().toISOString(),
    };
    if (type === 'update') {
      update(payload);
      setSelectedRecord(null);
      return;
    }
    create(payload);
  };

  useEffect(() => {
    if (data) {
      setValue('departmentId', data?.departmentId);
      setValue('position', data?.position);
      setValue('jobTitle', data?.jobTitle);
      setValue('quantity', formatDecimalPrecision(data?.quantity));
      setValue('level', data?.level?.toString());
      setValue('wage', formatDecimalPrecision(data?.wage));
      setValue('startDate', new DateObject(data?.startDate).add(7, 'hours'));
      setValue('recruitmentPurposes', data?.recruitmentPurposes);
      setValue('requestNotes', data?.requestNotes || '');
      setValue('description', data?.description || '');
      setValue('contractType', data?.contractType);
      setValue('replaceForId', data?.replaceForId);
      setValue('deadline', new DateObject(data?.deadline).add(7, 'hours'));
      setValue('deadlineOld', new DateObject(data?.deadline).add(7, 'hours'));

      if (data?.salaryUnit !== 'VND' && data?.salaryUnit !== 'USD') {
        setValue('salaryUnit', UNIT.OTHER);
        setValue('salaryUnitNote', data?.salaryUnit);
      } else {
        setValue('salaryUnit', data?.salaryUnit);
      }
    }
  }, [data]);

  return (
    <Form id={FORM.RECRUITMENT} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="departmentId"
              name="departmentId"
              placeholder="Chọn BP/Nhà máy"
              label="BP yêu cầu"
              options={workspaces?.data?.map(wsp => ({
                label: wsp?.name,
                value: wsp?.id,
              }))}
              isLoading={wspLoading}
            />
          </Col>
          <Col md={6}>
            <FormInput control={control} id="position" name="position" label="Vị trí" type="select">
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
            <FormInput control={control} id="jobTitle" name="jobTitle" label="Chức vụ" />
          </Col>
          <Col md={6}>
            <FormInput
              control={control}
              id="quantity"
              name="quantity"
              label="Số lượng"
              onChange={e => setValue('quantity', formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>
          <Col md={6}>
            <FormInput control={control} id="level" name="level" label="Cấp bậc" type="select">
              <option selected disabled>
                Chọn cấp bậc
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
          <Col md={6} className="recruitment-wage">
            <FormInput
              control={control}
              id="wage"
              name="wage"
              label="Mức lương"
              onChange={e => setValue('wage', formatDecimalPrecision(Number(e?.target?.value?.replace(/,/g, ''))))}
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
            />
            <FormInput control={control} id="salaryUnit" name="salaryUnit" type="select" className="recruitment-wage-unit">
              <option value={UNIT.VND}>VND</option>
              <option value={UNIT.USD}>USD</option>
              <option value={UNIT.OTHER}>Khác</option>
            </FormInput>
          </Col>
          <Col md={6} />
          <Col md={6}>
            {watch('salaryUnit') === UNIT.OTHER && <FormInput control={control} id="salaryUnitNote" name="salaryUnitNote" label="Ghi chú" />}
          </Col>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="startDate" name="startDate" label="Ngày bắt đầu đi làm" formState={formState} />
          </Col>
          {!checkHighlightDeadline(watch('deadlineOld')?.toString() ?? '') && (
            <Col md={6}>
              <FormDatePicker setValue={setValue} control={control} id="deadline" name="deadline" label="Ngày hết hạn" formState={formState} />
            </Col>
          )}
          <Col md={6}>
            <FormInput control={control} id="recruitmentPurpose" name="recruitmentPurposes" label="Mục đích tuyển dụng" type="select">
              <option selected disabled>
                Chọn mục đích
              </option>
              <option value="Thay thế">Thay thế</option>
              <option value="Tuyển mới">Tuyển mới</option>
              <option value="Vị trí đang trống">Vị trí đang trống</option>
            </FormInput>
          </Col>
          <Col md={6}>
            <FormInput control={control} id="contractType" name="contractType" label="Loại HĐ" type="select">
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
            {watch('recruitmentPurposes') === 'Thay thế' && (
              <FormSelect
                control={control}
                id="replaceForId"
                name="replaceForId"
                placeholder="Chọn người thay thế"
                label="Người thay thế"
                options={employeesProfiles?.data?.map(e => ({
                  label: `${e?.fullName || ''}`,
                  value: e?.id,
                }))}
                isLoading={empProfilesLoading}
                isClearable={false}
              />
            )}
          </Col>
          {!checkHighlightDeadline(watch('deadlineOld')?.toString() ?? '') && <Col span={6} />}
          <Col md={6}>
            <FormInput control={control} id="requestNotes" name="requestNotes" label="Yêu cầu cho ứng viên" type="textarea" rows={5} />
          </Col>
          <Col md={6}>
            <FormInput control={control} id="description" name="description" label="Mô tả công việc" type="textarea" rows={5} />
          </Col>
        </Row>
      </Card>
    </Form>
  );
};

export default RecruitmentForm;
