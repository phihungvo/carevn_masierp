import { zodResolver } from '@hookform/resolvers/zod';
import Card from 'app/components/card/card';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import { WORK_PLACE } from 'app/shared/model/annual-leave.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { AnnualLeaveFormSchema, annualLeaveSchema } from 'app/validation/annual-leave.validation';
import React from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePatchAnnualLeave } = useAnnualLeave;

interface IAnnualLeaveForm {
  toggle: () => void;
  toggleSuccess: () => void;
}

const AnnualLeaveForm = (props: IAnnualLeaveForm) => {
  const { toggle, toggleSuccess } = props;

  const { control, setValue, handleSubmit } = useForm<AnnualLeaveFormSchema>({
    resolver: zodResolver(annualLeaveSchema),
  });

  const { mutate } = usePatchAnnualLeave(toggle, toggleSuccess);

  const onSubmit: SubmitHandler<AnnualLeaveFormSchema> = data => {
    mutate({
      annualLeave_FACTORY: {
        leaveAfterProbation: parseInt(data.annualLeave_FACTORY.leaveAfterProbation),
        leavePerYear: parseInt(data.annualLeave_FACTORY.leavePerYear),
        carryForwardMonth: parseInt(data.annualLeave_FACTORY.carryForwardMonth),
        workPlace: WORK_PLACE.FACTORY,
      },
      annualLeave_OFFICE: {
        leaveAfterProbation: parseInt(data.annualLeave_OFFICE.leaveAfterProbation),
        leavePerYear: parseInt(data.annualLeave_OFFICE.leavePerYear),
        carryForwardMonth: parseInt(data.annualLeave_OFFICE.carryForwardMonth),
        workPlace: WORK_PLACE.OFFICE,
      },
    });
  };

  return (
    <Form id={FORM.ANNUAL_LEAVE} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <b>Văn phòng:</b>
          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_OFFICE.leaveAfterProbation"
              name="annualLeave_OFFICE.leaveAfterProbation"
              label=" Số phép sau thử việc"
              onChange={e =>
                handleValidDecimal<AnnualLeaveFormSchema>(
                  e.target.value,
                  'annualLeave_OFFICE.leaveAfterProbation',
                  DEFAULT_INTEGER_REGEX,
                  setValue,
                )
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_OFFICE.leavePerYear"
              name="annualLeave_OFFICE.leavePerYear"
              label="Số phép sau mỗi năm"
              onChange={e =>
                handleValidDecimal<AnnualLeaveFormSchema>(e.target.value, 'annualLeave_OFFICE.leavePerYear', DEFAULT_INTEGER_REGEX, setValue)
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_OFFICE.carryForwardMonth"
              name="annualLeave_OFFICE.carryForwardMonth"
              label="Ngày phép được cộng dồn đến tháng"
              type="select"
            >
              <option selected disabled>
                Chọn tháng
              </option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
              <option value="8">8</option>
              <option value="9">9</option>
              <option value="10">10</option>
              <option value="11">11</option>
              <option value="12">12</option>
            </FormInput>
          </Col>
        </Row>

        <Row>
          <b>Nhà máy:</b>
          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_FACTORY.leaveAfterProbation"
              name="annualLeave_FACTORY.leaveAfterProbation"
              label=" Số phép sau thử việc"
              onChange={e =>
                handleValidDecimal<AnnualLeaveFormSchema>(
                  e.target.value,
                  'annualLeave_FACTORY.leaveAfterProbation',
                  DEFAULT_INTEGER_REGEX,
                  setValue,
                )
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_FACTORY.leavePerYear"
              name="annualLeave_FACTORY.leavePerYear"
              label="Số phép sau mỗi năm"
              onChange={e =>
                handleValidDecimal<AnnualLeaveFormSchema>(e.target.value, 'annualLeave_FACTORY.leavePerYear', DEFAULT_INTEGER_REGEX, setValue)
              }
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="annualLeave_FACTORY.carryForwardMonth"
              name="annualLeave_FACTORY.carryForwardMonth"
              label="Ngày phép được cộng dồn đến tháng"
              type="select"
            >
              <option selected disabled>
                Chọn tháng
              </option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
              <option value="8">8</option>
              <option value="9">9</option>
              <option value="10">10</option>
              <option value="11">11</option>
              <option value="12">12</option>
            </FormInput>
          </Col>
        </Row>

      </Card>
    </Form>
  );
};

export default AnnualLeaveForm;
