import dayjs from 'dayjs';
import React, { useMemo } from 'react';

import Badge from 'app/components/badge/badge';
import contractsMapping from './contracts-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import ActionsDropdown from './components/actions-dropdown';
import PopoverApproval from './components/popover-approval';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { DATE_FORMAT } from 'app/constants/common';
import { IContract } from 'app/shared/model/contract.model';
import { ColumnsTypes } from 'app/components/table/table.d';
import { Color } from 'app/shared/model/enumerations/color.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { CONTRACT_STATUS } from 'app/shared/model/enumerations/contract.model';

const { contractStatusMapping, contractTypeColorMapping, contractStatusTextMapping, contractTypeTextMapping } = contractsMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleRestore: () => void,
  toggleProposeApprove: () => void,
  toggleApprove: () => void,
  toggleApproveLiquid: () => void,
  toggleProposeLiquid: () => void,
  setSelectedRecord: (id: string) => void,
  toggleMaskFinished: () => void,
  setContractStatus: React.Dispatch<React.SetStateAction<string>>,
  setRecord: React.Dispatch<React.SetStateAction<IContract>>
): ColumnsTypes<IContract> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IContract> = useMemo(() => {
    return [
      {
        title: 'Số HĐ',
        key: 'contractName',
        dataIndex: 'contractName',
        render: (text, record) => (
          <Tooltip label={text} target={`contractName-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record.id)}>
              <EllipsisParagraph text={text} width={100} id={`contractName-${record.id}`} />
            </p>
          </Tooltip>
        ),
      },
      {
        title: 'Công ty',
        key: 'customer',
        render: (text, record) => (
          <Tooltip label={record?.customer?.companyName} target={`customer-${record.id}`}>
            <EllipsisParagraph text={record?.customer?.companyName} id={`customer-${record.id}`} />
          </Tooltip>
        )
      },


      {
        title: 'Giá trị HĐ (VND)',
        key: 'contractTotal',
        dataIndex: 'contractTotal',
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Người phụ trách',
        key: 'owner',
        render: (text, record) => (
          <Tooltip label={(record?.owner?.lastName || '') + ' ' + (record?.owner?.firstName || '')} target={`contractTotal-${record.id}`}>
            <EllipsisParagraph text={(record?.owner?.lastName || '') + ' ' + (record?.owner?.firstName || '')} id={`contractTotal-${record.id}`} />
          </Tooltip>
        )
      },

      // {
      //   title: 'Thông số đạm',
      //   key: 'proteinPercent',
      //   dataIndex: 'proteinPercent',
      // },
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
        render: (text, record) => {
          const isExpired = record?.contractValidTo && dayjs(record?.contractValidTo).isBefore(dayjs());
          return record?.status === CONTRACT_STATUS?.WAITING_LIQUIDATION || record?.status === CONTRACT_STATUS?.WAITING_APPROVAL ? (
            <PopoverApproval id={`review-${record.id}`} data={record?.status === CONTRACT_STATUS.WAITING_APPROVAL ? record?.normalApprovals : record?.requestApprovals}>
              <Badge id={`review-${record.id}`} color={contractStatusMapping(text)}>
                {contractStatusTextMapping(text)}
              </Badge>
            </PopoverApproval>
          ) : <Badge color={isExpired && ![CONTRACT_STATUS.FINISHED, CONTRACT_STATUS.LIQUIDATED, CONTRACT_STATUS.LIQUIDATE_CANCELLED]?.includes(record?.status) ? Color.ERROR : contractStatusMapping(text)}>{contractStatusTextMapping(text)}</Badge>;
        },
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleRestore={toggleRestore}
            toggleProposeApprove={toggleProposeApprove}
            toggleApprove={toggleApprove}
            toggleApproveLiquid={toggleApproveLiquid}
            toggleProposeLiquid={toggleProposeLiquid}
            record={record}
            setSelectedRecord={setSelectedRecord}
            toggleMaskFinished={toggleMaskFinished}
            setContractStatus={setContractStatus}
            setRecord={setRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
