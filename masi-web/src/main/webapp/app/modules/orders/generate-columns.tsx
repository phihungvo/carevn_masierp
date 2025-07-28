import React from 'react';
import dayjs from 'dayjs';
import { useMemo } from 'react';

import ordersMapping from './orders-mapping';
import Badge from 'app/components/badge/badge';
import ActionsDropdown from './components/actions-dropdown';
import PopoverApproval from './components/popover-approval';
import { DATE_FORMAT } from 'app/constants/common';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IOrder, IOrderParams } from 'app/shared/model/order.model';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { ordersColorMapping, ordersTextMapping } = ordersMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  togglePropose: () => void,
  toggleApprove: () => void,
  toggleCancel: () => void,
  setSelectedRecord: (id: string) => void,
  filter: IOrderParams,
  toggleDownloadSuccessful: () => void,
): ColumnsTypes<IOrder> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IOrder> = useMemo(() => {
    return [
      {
        title: 'Tên KH/Tên công ty',
        key: 'companyName',
        render: (text, record) => (
          <Tooltip label={record?.contract?.customer?.companyName} target={`companyName-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record.id)}>
              <EllipsisParagraph text={record?.contract?.customer?.companyName} id={`companyName-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Mã số',
        key: 'orderCode',
        dataIndex: 'orderCode',
        render: (text, record) => (
          <Tooltip label={text} target={`orderCode-${record.id}`}>
            <EllipsisParagraph text={text} id={`orderCode-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Ngày tạo',
        key: 'dateOrder',
        dataIndex: 'dateOrder',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      // {
      //   title: 'Lần ban hành',
      //   key: 'numberOrder',
      //   dataIndex: 'numberOrder',
      // },
      {
        title: 'Hợp đồng',
        key: 'contract',
        dataIndex: 'contract',
        render: (_, record) => (
          <Tooltip label={record?.contract?.contractName} target={`contract-${record.id}`}>
            <EllipsisParagraph text={record?.contract?.contractName} id={`contract-${record.id}`} />
          </Tooltip>
        )
      },
      // {
      //   title: 'Chờ đến ngày',
      //   key: 'waitUntil',
      //   dataIndex: 'waitUntil',
      //   render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      // },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: (text, record) => (
          <>
            {record?.status === ORDER_STATUS.WAITING_APPROVAL ? (
              <PopoverApproval id={`status-${record.id}`} data={record?.orderReviews}>
                <Badge id={`status-${record.id}`} color={ordersColorMapping(text)}>
                  {ordersTextMapping(text)}
                </Badge>
              </PopoverApproval>
            ) : (
              <Badge color={ordersColorMapping(text)}>{ordersTextMapping(text)}</Badge>
            )}
          </>
        ),
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
            togglePropose={togglePropose}
            toggleApprove={toggleApprove}
            toggleCancel={toggleCancel}
            record={record}
            setSelectedRecord={setSelectedRecord}
            toggleDownloadSuccessful={toggleDownloadSuccessful}
          />
        ),
      },
    ];
  }, [filter]);

  return columns;
};
