import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import useTransactionType from 'app/hooks/use-transaction-type';
import { IsActiveOptions } from 'app/shared/model/enumerations/isActive.enum';
import { IItemParams } from 'app/shared/model/item.model';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import DatePicker from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';
const { useGetTransactionTypeQuery } = useTransactionType;
interface ITransferAssetsFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IItemParams>>;
}

const TransferAssetsFilterModals = (props: ITransferAssetsFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const methods = useForm();
  const { control, setValue, getValues } = methods
  const { data: transactionType } = useGetTransactionTypeQuery();

  const onOk = () => {
    const data = getValues();
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'transferDate.greaterThanOrEqual': data?.fromDate ? dayjs(data?.fromDate).startOf('day').toISOString() : undefined,
      'transferDate.lessThanOrEqual': data?.toDate ? dayjs(data?.toDate).endOf('day').toISOString() : undefined,
      'transactionTypeId.equals': data?.transactionTypeId
    }));
    toggle();
  };

  const onCancel = () => {
    setValue('fromDate', null)
    setValue('toDate', null)
    setValue('transactionTypeId', null)
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'transferDate.greaterThanOrEqual': undefined,
      'transferDate.lessThanOrEqual': undefined,
      'transactionTypeId.equals': undefined
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="supplies-filter-modals"
      style={{ width: '500px' }}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Row>
          <Col md={6}>
            <FormDatePickerV2
              control={control}
              setValue={setValue}
              label="Từ ngày"
              id="fromDate"
              name="fromDate"
            />
          </Col>
          <Col md={6}>
            <FormDatePickerV2
              control={control}
              setValue={setValue}
              label="Đến ngày"
              id="toDate"
              name="toDate"
            />
          </Col>
          <Col md={6} style={{
            paddingTop: "0.3rem"
          }}>
            <FormSelect
              control={control}
              id="transactionTypeId"
              name="transactionTypeId"
              label="Loại PS"
              isClearable={false}
              options={transactionType?.data?.map(s => ({
                value: s?.id,
                label: `${s?.code} - ${s?.name}`,
              }))}
            />
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default TransferAssetsFilterModals;
