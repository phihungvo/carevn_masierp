import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import FormSelectV2 from 'app/components/formV2/form-select/form-select';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useCompany from 'app/hooks/use-company';
import { IPostCompanyDto } from 'app/shared/model/company.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { CompanySchema } from 'app/validation/company.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import { CompanyContext } from '../company-provider';
import UploadLogoCompany from './upload-logo';

const { usePostCompanyMutation, useUpdateCompanyMutation, useGetCompanies } =
  useCompany;

const CompanyForm = () => {
  const { id } = useParams();

  const { toggleCreateSuccess, toggleUpdateSuccess } =
    useContext(CompanyContext);

  const { control, handleSubmit, setValue, formState, reset } =
    useFormContext<CompanySchema>();

  const { data: companyParent } = useGetCompanies({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { mutate: create } = usePostCompanyMutation(() => {
    toggleCreateSuccess();
    reset();
  });
  const { mutate: update } = useUpdateCompanyMutation(id, () => {
    toggleUpdateSuccess();
    reset();
  });

  const onSubmit = (values: CompanySchema) => {
    const submitValues: IPostCompanyDto = {
      ...values,
      representativeDob: values.representativeDob.toDate(),
    };
    if (!id) create(submitValues);
    else update(submitValues);
  };

  const disabled = false;

  return (
    <Form id={FORM.COMPANY} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={20}>
        <Row>
          <Col md={3}>
            <Typography level={5}>Thông tin cơ bản</Typography>
          </Col>
          <Col md={9}>
            <Row>
              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="name"
                  label="Tên công ty"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="normalizedName"
                  label="Mã công ty"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="taxCode"
                  label="Mã số thuế"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={5}>
                <FormSelectV2
                  control={control}
                  name="parentId"
                  label="Thuộc công ty mẹ"
                  placeholder="Điền"
                  disabled={disabled}
                  options={companyParent?.data?.map(x => ({
                    value: x?.id,
                    label: x?.name,
                  }))}
                />
              </Col>
            </Row>
          </Col>
        </Row>
        <div className="divider" />
        <Row>
          <Col md={3}>
            <Typography level={5}>Logo</Typography>
          </Col>
          <Col md={9}>
            <UploadLogoCompany name="imageId" />
          </Col>
        </Row>
        <div className="divider" />
        <Row>
          <Col md={3}>
            <Typography level={5}>Thông tin liên hệ</Typography>
          </Col>
          <Col md={9}>
            <Row>
              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="website"
                  label="Website"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="callcenter"
                  label="Call center"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={12}>
                <FormInputV2
                  control={control}
                  name="address"
                  label="Địa chỉ"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>
            </Row>
          </Col>
        </Row>
        <div className="divider" />
        <Row>
          <Col md={3}>
            <Typography level={5}>Người đại diện</Typography>
          </Col>
          <Col md={9}>
            <Row>
              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="representativeName"
                  label="Người đại diện"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormDatePickerV2
                  control={control}
                  formState={formState}
                  setValue={setValue}
                  name="representativeDob"
                  label="Ngày sinh"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="representativeIdNumber"
                  label="CCCD"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="representativePhone"
                  label="Số điện thoại"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="representativeEmail"
                  label="Email"
                  placeholder="Điền"
                  disabled={disabled}
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default CompanyForm;
