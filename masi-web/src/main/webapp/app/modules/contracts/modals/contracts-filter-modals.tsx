import DatePicker from 'app/components/date-picker/date-picker';
import Input from 'app/components/input/input';
import Modal from 'app/components/modal/modal';
import useOnClickOutside from 'app/hooks/use-click-outside';
import { IContractParams } from 'app/shared/model/contract.model';
import { CONTRACT_STATUS, CONTRACT_TYPE } from 'app/shared/model/enumerations/contract.model';
import React, { useCallback, useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { Col, FormGroup, Label, Row } from 'reactstrap';
import contractsMapping from '../contracts-mapping';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import Select from 'app/components/select/select';
import useEmployee from 'app/hooks/use-employee';
import useCustomers from 'app/hooks/use-customers';
import { BaseOption } from 'app/shared/model/pagination.model';

const { contractStatusTextMapping, contractTypeTextMapping } = contractsMapping;
const { useGetEmployeesQuery } = useEmployee;
const { useGetEnabledCustomers } = useCustomers;

interface IContractsFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IContractParams>>;
  deleted?: boolean;
}

const ContractsFilterModals = (props: IContractsFilterModalsProps) => {
  const { isOpen, toggle, setFilter, deleted } = props;

  const [contractType, setContractType] = useState<CONTRACT_TYPE | undefined>();
  const [contractStatus, setContractStatus] = useState<CONTRACT_STATUS[]>([]);
  const [proteinPercent, setProteinPercent] = useState<string>('');
  const [selectedDate, setSelectedDate] = useState<DateObject[]>();
  const [isExpired, setIsExpired] = useState<boolean>(false);
  const [contractOwner, setContractOwner] = useState<BaseOption[]>([]);
  const [customerId, setCustomerId] = useState<BaseOption[]>([]);

  const datePickerRef = useRef<DatePickerRef | null>(null);

  const { data, isLoading } = useGetEmployeesQuery({size: DEFAULT_PAGE_SIZE_NAX});
  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX
  });

  const onChangeSelectedDate = (value: DateObject[]) => {
    setSelectedDate(value);
  };

  const handleDatePickerClose = useCallback(() => datePickerRef?.current?.closeCalendar(), [datePickerRef]);

  useOnClickOutside(datePickerRef, handleDatePickerClose);

  const onChangeStatus = (e: React.ChangeEvent<HTMLInputElement>, value: CONTRACT_STATUS) => {
    if (e.target.checked && e.target.value === value) {
      setContractStatus(prev => [...prev, value]);
    } else {
      setContractStatus(prev => prev.filter(item => item !== value));
    }
  };

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      contractType,
      contractStatusList: contractStatus,
      proteinPercent: proteinPercent !== '' ? proteinPercent : undefined,
      contractValidFrom: selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE),
      contractValidTo: selectedDate?.[1]
        ? selectedDate?.[1]?.format(DATE_FORMAT.YEAR_DATE)
        : selectedDate?.[0]?.format(DATE_FORMAT.YEAR_DATE),
      isExpired,
      companyName: customerId?.map(c => c.value) as string[],
      employeeOwner: contractOwner?.map(e => e.value) as string[],
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setContractType(undefined);
    setContractStatus([]);
    setProteinPercent('');
    setSelectedDate(undefined);
    setIsExpired(undefined);
    setContractOwner([]);
    setCustomerId([]);
    setFilter(prev => ({
      ...prev,
      contractType: undefined,
      contractStatusList: undefined,
      proteinPercent: undefined,
      contractValidFrom: undefined,
      contractValidTo: undefined,
      isExpired: undefined,
      page: DEFAULT_PAGE,
      companyName: undefined,
      employeeOwner: undefined,
    }));
    toggle();
  };

  const closeExportCalendar = () => {
    setSelectedDate(null)
    datePickerRef?.current?.closeCalendar()
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="modals-filter-contracts"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Label>Loại hợp đồng</Label>
        <Input type="select" name="select" id="type" value={contractType} onChange={e => setContractType(e.target.value as CONTRACT_TYPE)}>
          <option selected disabled>
            Chọn loại hợp đồng
          </option>
          <option value={CONTRACT_TYPE.NEW}>{contractTypeTextMapping(CONTRACT_TYPE.NEW)}</option>
          <option value={CONTRACT_TYPE.PRICE_UP}>{contractTypeTextMapping(CONTRACT_TYPE.PRICE_UP)}</option>
          <option value={CONTRACT_TYPE.PRICE_DOWN}>{contractTypeTextMapping(CONTRACT_TYPE.PRICE_DOWN)}</option>
          <option value={CONTRACT_TYPE.EXTEND}>{contractTypeTextMapping(CONTRACT_TYPE.EXTEND)}</option>
          <option value={CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX}>
            {contractTypeTextMapping(CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX)}
          </option>
        </Input>
      </FormGroup>
      {
        !deleted &&
        <FormGroup>
          <Label for="status">Trạng thái:</Label>
          <Row>
            <Col md={3}>
              <FormGroup check>
                <Label check for="draft">
                  {contractStatusTextMapping(CONTRACT_STATUS.DRAFT)}
                </Label>
                <Input
                  id="draft"
                  name="draft"
                  type="checkbox"
                  value={CONTRACT_STATUS.DRAFT}
                  checked={contractStatus.includes(CONTRACT_STATUS.DRAFT)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.DRAFT)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="pending">
                  {contractStatusTextMapping(CONTRACT_STATUS.WAITING_APPROVAL)}
                </Label>
                <Input
                  id="pending"
                  name="pending"
                  type="checkbox"
                  value={CONTRACT_STATUS.WAITING_APPROVAL}
                  checked={contractStatus.includes(CONTRACT_STATUS.WAITING_APPROVAL)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.WAITING_APPROVAL)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="approved">
                  {contractStatusTextMapping(CONTRACT_STATUS.APPROVED)}
                </Label>
                <Input
                  id="approved"
                  name="approved"
                  type="checkbox"
                  value={CONTRACT_STATUS.APPROVED}
                  checked={contractStatus.includes(CONTRACT_STATUS.APPROVED)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.APPROVED)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="completed">
                  {contractStatusTextMapping(CONTRACT_STATUS.FINISHED)}
                </Label>
                <Input
                  id="completed"
                  name="completed"
                  type="checkbox"
                  value={CONTRACT_STATUS.FINISHED}
                  checked={contractStatus.includes(CONTRACT_STATUS.FINISHED)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.FINISHED)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="pending-liquidate">
                  {contractStatusTextMapping(CONTRACT_STATUS.WAITING_LIQUIDATION)}
                </Label>
                <Input
                  id="pending-liquidate"
                  name="pending-liquidate"
                  type="checkbox"
                  value={CONTRACT_STATUS.WAITING_LIQUIDATION}
                  checked={contractStatus.includes(CONTRACT_STATUS.WAITING_LIQUIDATION)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.WAITING_LIQUIDATION)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="liquidated">
                  {contractStatusTextMapping(CONTRACT_STATUS.LIQUIDATED)}
                </Label>
                <Input
                  id="liquidated"
                  name="liquidated"
                  type="checkbox"
                  value={CONTRACT_STATUS.LIQUIDATED}
                  checked={contractStatus.includes(CONTRACT_STATUS.LIQUIDATED)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.LIQUIDATED)}
                />
              </FormGroup>
            </Col>

            <Col md={3}>
              <FormGroup check>
                <Label check for="liquidate-cancelled">
                  {contractStatusTextMapping(CONTRACT_STATUS.LIQUIDATE_CANCELLED)}
                </Label>
                <Input
                  id="liquidate-cancelled"
                  name="liquidate-cancelled"
                  type="checkbox"
                  value={CONTRACT_STATUS.LIQUIDATE_CANCELLED}
                  checked={contractStatus.includes(CONTRACT_STATUS.LIQUIDATE_CANCELLED)}
                  onChange={e => onChangeStatus(e, CONTRACT_STATUS.LIQUIDATE_CANCELLED)}
                />
              </FormGroup>
            </Col>

            {/* <Col md={3}>
            <FormGroup check>
              <Label check for="cancelled">
                {contractStatusTextMapping(CONTRACT_STATUS.CANCELLED)}
              </Label>
              <Input
                id="cancelled"
                name="cancelled"
                type="checkbox"
                value={CONTRACT_STATUS.CANCELLED}
                checked={contractStatus.includes(CONTRACT_STATUS.CANCELLED)}
                onChange={e => onChangeStatus(e, CONTRACT_STATUS.CANCELLED)}
              />
            </FormGroup>
          </Col> */}

            <Col md={3}>
              <FormGroup check>
                <Label check for="isExpired">
                  Hết hạn
                </Label>
                <Input id="isExpired" name="isExpired" type="checkbox" checked={isExpired} onChange={e => setIsExpired(e.target.checked)} />
              </FormGroup>
            </Col>
          </Row>
        </FormGroup>
      }
      <FormGroup>
        <Label>Thông số đạm</Label>
        <Input id="quantity" name="quantity" onChange={e => setProteinPercent(e.target.value)} value={proteinPercent} />
      </FormGroup>

      <FormGroup>
        <Label>Thời hạn hợp đồng</Label>
        <DatePicker value={selectedDate} ref={datePickerRef} onChange={onChangeSelectedDate} name="date" closeExportCalendar={closeExportCalendar} range/>
      </FormGroup>

      <FormGroup>
        <Label>Người phụ trách</Label>
        <Select
          value={contractOwner}
          id="contractOwner"
          name="contractOwner"
          placeholder=""
          options={data?.data?.map(e => ({
            label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
            value: e?.id,
          }))}
          isLoading={isLoading}
          isMulti
          onChange={option => setContractOwner(option as BaseOption[])}
        />
      </FormGroup>

      <FormGroup>
        <Label>Công ty</Label>
        <Select
          value={customerId}
          id="customerId"
          name="customerId"
          placeholder=""
          options={customers?.data?.map(c => ({
            label: c?.companyName,
            value: c?.id,
          }))}
          isLoading={cusLoading}
          isMulti
          onChange={option => setCustomerId(option as BaseOption[])}
        />
      </FormGroup>
    </Modal>
  );
};

export default ContractsFilterModals;
