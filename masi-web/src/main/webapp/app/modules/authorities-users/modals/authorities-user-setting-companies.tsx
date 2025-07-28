import { zodResolver } from '@hookform/resolvers/zod';
import Modal from 'app/components/modal/modal';
import WrapCheckbox from 'app/components/wrap-input-checkbox/WrapCheckbox';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import useAccount from 'app/hooks/use-account';
import useCompany from 'app/hooks/use-company';
import {
  setCompanySchema,
  SetCompanySchema,
} from 'app/validation/account.validation';
import { isEmpty } from 'lodash';
import { useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetCompanies } = useCompany;
const { useSetCompanyUserMutation } = useAccount;

type Props = {
    isOpen: boolean,
    toggle: () => void
    selectedRecord?: any
}

const AuthoritiesUserSettingCompanies = (props: Props) => {
  const { selectedRecord, isOpen, toggle } = props;

  const companies = useGetCompanies();
  const setCompanyMutation = useSetCompanyUserMutation();
  const companyForm = useForm<SetCompanySchema>({
    resolver: zodResolver(setCompanySchema),
    defaultValues: {
      company: []
    },
  });

  const { control, reset, setValue }  = companyForm

  const onSubmit = (values: any) => {
    toggle();
    let payload = []
    values?.company?.forEach(item => {
      if (item?.isChecked) {
        payload.push(item.id)
      }
    })
    setCompanyMutation.mutate({id: selectedRecord?.id, data: payload});
  }

  useEffect (() => {
    setValue('company', companies?.data?.data?.map(item => {
      return {
        id: item?.normalizedName,
        isChecked: selectedRecord?.companyJson?.indexOf(item?.normalizedName) !== -1,
      }
    }))
  }, [selectedRecord, companies])

  return (
    <Modal
        title='Cài đặt công ty'
        isOpen={isOpen}
        toggle={toggle}
        okText='Lưu'
        cancelText='Trở về'
        onOk={companyForm.handleSubmit(onSubmit)}
    >
        <FormProvider {...companyForm}>
          <Row>
            {isEmpty(companies?.data?.data) && <p>Không có dữ liệu</p>}
            {!isEmpty(companies?.data?.data) &&
              companies?.data?.data?.map((company, index) => (
                <Col md={4} style={{ marginBottom: '15px' }}>
                  <WrapCheckbox
                    name={`company.${index}.isChecked`}
                    label={company?.name}
                    direction="row-reverse"
                    justify="start"
                    onChange={() =>
                      companyForm.setValue(
                        `company.${index}.id`,
                        company?.normalizedName,
                      )
                    }
                  >
                    {company?.name}
                  </WrapCheckbox>
                </Col>
              ))}
          </Row>
          <WrapInputText<any> name="company.root" hidden />
        </FormProvider>
    </Modal>
  );
};

export default AuthoritiesUserSettingCompanies;
