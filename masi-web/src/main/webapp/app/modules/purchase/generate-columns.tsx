import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import purchaseMapping from './purchase-mapping';
import { IPurchase } from 'app/shared/model/purchase.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { purchaseColorMapping, purchaseStatusTextMapping, purchaseUnitTextMapping } = purchaseMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleRequest: () => void,
  toggleApprove: () => void,
  toggleStatus: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IPurchase> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IPurchase> = useMemo(() => {
    return [
      {
        title: 'Tên hàng hóa',
        key: 'productName',
        dataIndex: 'productName',
        render: (text, record) => (
          <Tooltip label={text} target={`productName-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record.id)}>
              <EllipsisParagraph width={180} text={
                text
              } id={`productName-${record.id}`} />
            </p>
          </Tooltip>
        ),
      },
      {
        title: 'Đơn vị',
        key: 'unit',
        dataIndex: 'unit',
        width: 100,
        render: text => purchaseUnitTextMapping(text),
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
        width: 100,
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Đơn giá',
        key: 'unitPrice',
        dataIndex: 'unitPrice',
        width: 100,
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Thành tiền',
        key: 'totalPrice',
        dataIndex: 'totalPrice',
        width: 100,
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Nhà cung cấp',
        key: 'supplier',
        dataIndex: 'supplier',
        render: (text, record) => (
          <Tooltip label={text} target={`supplier-${record.id}`}>
            <EllipsisParagraph text={text} width={140} id={`supplier-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Trạng thái',
        key: 'requestStatus',
        dataIndex: 'requestStatus',
        render: text => <Badge color={purchaseColorMapping(text)}>{purchaseStatusTextMapping(text)}</Badge>,
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleRequest={toggleRequest}
            toggleApprove={toggleApprove}
            toggleStatus={toggleStatus}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
