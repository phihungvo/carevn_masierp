import Badge from 'app/components/badge/badge';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { IUniformOrder } from 'app/shared/model/uniform.model';
import uniformOrdersMapping from './uniform-orders-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { uniformOrderStatusMapping, uniformOrderStatusColorMapping } = uniformOrdersMapping;

export const generateColumns = (
  toggleDetail: () => void,
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleApprove: () => void,
  toggleCancel: () => void,
  toggleStock: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IUniformOrder> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IUniformOrder> = useMemo(() => {
    return [
      {
        title: 'Tên đơn hàng',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`companyName-${record.id}`}>
            <EllipsisParagraph text={
              <p className="attachment-link" onClick={() => handleDetail(record.id)}>
                {text}
              </p>
            } id={`companyName-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
        render: (_, record) => formatDecimalPrecision(record?.quantity),
      },
      {
        title: 'Đã nhập',
        key: 'stockedQuantity',
        dataIndex: 'stockedQuantity',
        render: (_, record) => formatDecimalPrecision(record?.quantity - record?.remainQuantity),
      },
      {
        title: 'Còn lại',
        key: 'remainQuantity',
        dataIndex: 'remainQuantity',
        render: text => formatDecimalPrecision(text),
      },

      {
        title: 'Ngày',
        key: 'date',
        dataIndex: 'date',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status',
        render: text => <Badge color={uniformOrderStatusColorMapping(text)}>{uniformOrderStatusMapping(text)}</Badge>,
      },

      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        dataIndex: 'action',
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleApprove={toggleApprove}
            toggleCancel={toggleCancel}
            toggleStock={toggleStock}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
