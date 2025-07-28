import { zodResolver } from '@hookform/resolvers/zod';
import ButtonFilter from 'app/components/ButtonV2/ButtonFilter';
import Modal from 'app/components/modal/modal';
import WrapDate from 'app/components/wrap-date/WrapDate';
import dayjs from 'dayjs';
import { useState } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { Filter } from '../Types/Filter';
import { TableFilter, TableFilterSchema } from '../validations/filter.validate';

type Props = {
  setQuery: React.Dispatch<any>;
  query: Filter;
};

const Filter = (props: Props) => {
  const { setQuery, query } = props;

  const [isOpen, setIsOpen] = useState<boolean>(false);

  const methods = useForm<Filter>({
    resolver: zodResolver(TableFilterSchema),
    defaultValues: {
      deliveryDate: '2024-12-04T17:00:00.000Z'
    }
  })
  const { control, setValue, handleSubmit, watch } = methods
  const { date } = watch();

  const onSubmit: SubmitHandler<Filter> = values => {
    setQuery(pre => ({ ...pre, table: values }));
    onToggle();
  };

  const onToggle = () => setIsOpen(!isOpen);

  const onReset = () => {
    setQuery({
      page: 0,
      size: 2000000,
      table: {
        'deliveryDate': dayjs().add(-2, 'days').toISOString(),
        'expectedReceiveDate': dayjs().endOf('month').toISOString()
      },
    });
    onToggle()
  };

  return (
    <>
      <ButtonFilter onClick={onToggle} />
      <FormProvider {...methods}>
        <Modal
          toggle={onToggle}
          isOpen={isOpen}
          title="Bộ lọc"
          okText="Áp dụng"
          cancelText="Đặt lại"
          onCancel={onReset}
          onOk={handleSubmit(onSubmit)}
        >
          <Row>
            <Col md={6}>
              <WrapDate<TableFilter>
                label='Ngày bắt đầu'
                name='deliveryDate'
                onDateChange={(date) => {
                  console.log('lakjsdflksdjf', date);
                }}
              />
            </Col>
            <Col md={6}>
              <WrapDate<TableFilter>
                label='Ngày kết thúc'
                name='expectedReceiveDate'
              />
            </Col>
          </Row>
        </Modal>
      </FormProvider>
    </>
  );
};

export default Filter;
