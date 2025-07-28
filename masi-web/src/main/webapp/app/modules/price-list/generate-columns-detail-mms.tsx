import React from 'react';
import Flex from 'app/components/flex/flex';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { IQuotationDetail } from 'app/shared/model/quotation.model';
import dayjs from 'dayjs';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

export const generateColumnsDetailsMMS = (): ColumnsTypes<IQuotationDetail> => {
  const columns: ColumnsTypes<IQuotationDetail> = [
    {
      title: 'Loại hàng',
      key: 'product',
      dataIndex: 'product',
      render: (_, record) => record.material?.name,
    },
    {
      title: 'Thông tin sản phẩm',
      key: 'note',
      dataIndex: 'note',
      render: (_, record) => (
        <Flex direction="column">
          <p>- Ghi chú: {record.note}</p>
          <p>- Đơn giá: {formatDecimalPrecision(record.price)}</p>
          <p>- Khối lượng (Tấn): {formatDecimalPrecision(record.weight)}</p>
        </Flex>
      ),
    },
    {
      title: 'Thông tin giao hàng',
      key: 'delivery',
      dataIndex: 'delivery',
      render: (_, record) => (
        <Flex direction="column">
          <p>- Thời gian: {record.deliveryDate ? dayjs(record.deliveryDate).format(DATE_FORMAT.DATE) : ''}</p>
          <p>- Địa điểm: {record.deliveryLocation}</p>
          <p>- Địa điểm (En): {record.deliveryLocationEn}</p>
        </Flex>
      ),
    },
  ];

  return columns;
};
