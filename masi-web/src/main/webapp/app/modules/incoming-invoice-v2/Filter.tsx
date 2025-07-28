import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import Modal from 'app/components/modal/modal';
import SelectV3 from 'app/components/select/SelectV3';
import SupplierCodes from 'app/components/suppliersCode/SuppliesCode';
import { BasicSelect } from 'app/shared/model/arr-obj.model';
import { convertToIsoDate } from 'app/shared/util/date-utils';
import { useState } from 'react';
import { Controller, FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { Filter } from './types/Filter';
import { set } from 'lodash';

const billsStatus = [
  {
    label: 'Đã có hóa đơn',
    value: true
  },
  {
    label: 'Chưa có hóa đơn',
    value: false
  }
]

type Props = {
  setQuery: React.Dispatch<any>;
};

const Filter = (props: Props) => {
  const { setQuery } = props;

  const [isOpen, setIsOpen] = useState<boolean>(false);

  const methods = useForm<Filter>();
  const { control, setValue, handleSubmit } = methods

  const onSubmit: SubmitHandler<Filter> = values => {
    if (values?.date) {
      if (values?.date?.[0]) {
        values.startDate = convertToIsoDate(values?.date?.[0]);
      }
      if (values?.date?.[1]) {
        values.endDate = convertToIsoDate(values?.date?.[1]);
      }
      delete values.date;
    }
    setQuery(pre => ({ ...pre, ...values }));
    onToggle();
  };

  const onToggle = () => setIsOpen(!isOpen);

  const onReset = () => {
    setValue('date', null);
    setValue('supplierId', null);
    setValue('hasInvoice', null);
    setQuery({ page: 0, size: 2000000 });
    onToggle()
  };

  const onSelectChange = (value: BasicSelect<boolean>) => {
    setValue('hasInvoice', value?.value)
  }

  return (
    <>
      <ButtonFilter onClick={onToggle} />
      <FormProvider {...methods}>
        <form>
          <Modal
            toggle={onToggle}
            isOpen={isOpen}
            title="Bộ lọc"
            okText="Áp dụng"
            cancelText="Đặt lại"
            onCancel={onReset}
            onOk={handleSubmit(onSubmit)}
          >
            {/* <Row>
              <Col md={12}>
                <FormDatePickerV2
                  control={control}
                  name="date"
                  range
                  placeholder="Ngày bắt đầu - Ngày kết thúc"
                />
              </Col>
            </Row> */}
            <Row>
              <Col md={6}>
                <SupplierCodes<Filter> name='supplierId' />
              </Col>
              <Col md={6}>
                <Controller
                  control={control}
                  name='hasInvoice'
                  render={({ field }) => (
                    <FormWrap
                      label='Trạng thái hóa đơn'
                    >
                      <SelectV3
                        {...field}
                        onChange={onSelectChange}
                        options={billsStatus}
                        value={typeof field?.value === 'boolean' && [{
                          label: field?.value ? 'Đã có hóa đơn' : 'Chưa có hóa đơn',
                          value: field?.value
                        }]}
                      />
                    </FormWrap>
                  )}
                />
              </Col>
            </Row>
          </Modal>
        </form>
      </FormProvider>
    </>
  );
};

export default Filter;
