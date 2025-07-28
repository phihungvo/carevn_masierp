import dayjs from 'dayjs';
import React, { useMemo } from 'react';

import ActionsDropdown from './components/ActionsDropdown'
import { DATE_FORMAT } from 'app/constants/common';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ILogisticsMaterialProposal } from 'app/shared/model/logistics-material-proposal';
import logisticsMaterialProposalMapping from 'app/modules/logistics-material-proposal/logistics-material-proposal-mapping'
import Badge from 'app/components/badge/badge';

const { logisticsMaterialProposalColorMapping, logisticsMaterialProposalTextMapping } = logisticsMaterialProposalMapping

export const generateColumns = (
  setSelectedRecord: (id: string) => void,
  toggleUpdate: () => void,
  toggleDetail: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  toggleDelete: () => void,
  toggleCancel: () => void,
): ColumnsTypes<ILogisticsMaterialProposal> => {
  const columns: ColumnsTypes<ILogisticsMaterialProposal> = useMemo(() => {
    return [
      {
        title: 'Số CT',
        dataIndex: 'numberVoucher',
        key: 'numberVoucher',
      },
      {
        title: 'Ngày lập',
        dataIndex: 'voucherDate',
        key: 'voucherDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Tên HH',
        dataIndex: 'productName',
        key: 'productName',
      },
      {
        title: 'Tổng tiền thanh toán',
        dataIndex: 'unitPrice',
        key: 'unitPrice',
      },
      {
        title: 'Nhà cung cấp',
        dataIndex: 'supplier',
        key: 'supplier',
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: (text, record) => (
          <Badge color={logisticsMaterialProposalColorMapping(text)}>{logisticsMaterialProposalTextMapping(text)}</Badge>
        ),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown record={record} setSelectedRecord={setSelectedRecord} toggleUpdate={toggleUpdate} toggleDetail={toggleDetail} togglePropose={togglePropose} toggleApprove={toggleApprove} toggleDelete={toggleDelete} toggleCancel={toggleCancel} />
        ),
      },
    ];
  }, []);

  return columns;
};
