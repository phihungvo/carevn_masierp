import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { HISTORY_KEYS, IChangeItem } from 'app/shared/model/employee.model';
import dayjs from 'dayjs';
import React from 'react';
import employeesMapping from '../employees-mapping';
import Descriptions from '@uiw/react-descriptions';

const { historyFieldNameMapping, historyFieldValuesMapping } = employeesMapping;
const { useGetEmployeeChangeLogById } = useEmployee;

const listHistoryKeys: HISTORY_KEYS[] = [
  'probationDateFrom',
  'probationDateTo',
  'officialWorkTypeDuration',
  'insurancePaymentLevel',
  'contractType',
];

const renderChangeLog = (data: IChangeItem) => {
  const { fieldName, oldValue, newValue } = data;

  if (oldValue === newValue || !listHistoryKeys?.includes(fieldName)) return null;

  return (
    <Descriptions.Item label={historyFieldNameMapping(fieldName)} key={fieldName}>
      <Typography level="text" className="fw-bolder">
        {historyFieldValuesMapping(fieldName, oldValue ? oldValue : 'Trống')}
      </Typography>{' '}
      {' -> '}
      <Typography level="text" className="fw-bolder">
        {historyFieldValuesMapping(fieldName, newValue ? newValue : 'Trống')}
      </Typography>
      <></>
    </Descriptions.Item>
  );
};

interface IEmployeeDetailsChangelogModalProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const EmployeeDetailsChangelogModal = (props: IEmployeeDetailsChangelogModalProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetEmployeeChangeLogById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      ok={false}
      cancel={false}
      className="employee-detail-modal"
      titleHeader={`Lịch sử thay đổi ngày ${data?.changeDate ? dayjs(data?.changeDate).format(DATE_FORMAT.DATE) : ''}`}
    >
      <Flex direction="column" gap={16}>
        <Descriptions size="large" bordered column={2}>
          {data?.change?.map(item => renderChangeLog(item))}
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default EmployeeDetailsChangelogModal;
