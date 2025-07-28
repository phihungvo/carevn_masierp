import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { IContract } from 'app/shared/model/contract.model';
import contractsMapping from '../contracts/contracts-mapping';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { contractStatusMapping, contractTypeColorMapping, contractStatusTextMapping, contractTypeTextMapping } = contractsMapping;

export const generateColumns = (toggleRestore: () => void, setSelectedRecord: (id: string) => void): ColumnsTypes<IContract> => {
  const columns: ColumnsTypes<IContract> = useMemo(() => {
    return [
      {
        title: 'Số HĐ',
        key: 'contractName',
        dataIndex: 'contractName',
        render: (text, record) => (
          <Tooltip label={text} target={`contractName-${record.id}`}>
            <EllipsisParagraph text={text} id={`contractName-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Công ty',
        key: 'customer',
        render: (text, record) => (
          <Tooltip label={(record?.customer?.lastName || '') + ' ' + record?.customer?.firstName} target={`customer-${record.id}`}>
            <EllipsisParagraph text={(record?.customer?.lastName || '') + ' ' + record?.customer?.firstName} id={`customer-${record.id}`} />
          </Tooltip>
        ),
      },

      {
        title: 'Giá trị HĐ (VND)',
        key: 'contractTotal',
        dataIndex: 'contractTotal',
        width: 150,
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Người phụ trách',
        key: 'owner',
        render: (text, record) => (
          <Tooltip label={(record?.owner?.lastName || '') + ' ' + record?.owner?.firstName} target={`owner-${record.id}`}>
            <EllipsisParagraph text={(record?.owner?.lastName || '') + ' ' + record?.owner?.firstName} id={`owner-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Thời hạn',
        key: 'contractValid',
        dataIndex: 'contractValid',
        width: 240,
        render: (_, record) =>
          (record?.contractValidFrom ? dayjs(record?.contractValidFrom).format(DATE_FORMAT.DATE) : '') +
          (record?.contractValidFrom && record?.contractValidTo ? ' - ' : '') +
          (record?.contractValidTo ? dayjs(record?.contractValidTo).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Loại hợp đồng',
        key: 'contractType',
        dataIndex: 'contractType',
        width: 150,
        render: text => <Badge color={contractTypeColorMapping(text)}>{contractTypeTextMapping(text)}</Badge>,
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        width: 150,
        render: text => <Badge color={contractStatusMapping(text)}>{contractStatusTextMapping(text)}</Badge>,
      },
      {
        title: 'Thao tác',
        width: 106,
        key: 'action',
        dataIndex: 'action',
        render: (_, record) => <ActionsDropdown toggleRestore={toggleRestore} record={record} setSelectedRecord={setSelectedRecord} />,
      },
    ];
  }, []);

  return columns;
};
