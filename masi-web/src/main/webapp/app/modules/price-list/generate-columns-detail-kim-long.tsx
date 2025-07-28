import React from 'react';
import Flex from 'app/components/flex/flex';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IQuotationDetail } from 'app/shared/model/quotation.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

export const generateColumnsDetailsKimLong = (): ColumnsTypes<IQuotationDetail> => {
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
          <p>- Thông tin: {record?.note}</p>
          <p>- Khối lượng (Kg): {formatDecimalPrecision(record?.weight)}</p>
        </Flex>
      ),
    },
    {
      title: 'Thông tin giá',
      key: 'price',
      dataIndex: 'price',
      render: (_, record) => (
        <Flex direction="column">
          {/* <p>- 180 mgN/100g: {record?.nitrogen180Price}</p>
          <p>- 150 mgN/100g: {record?.nitrogen150Price}</p> */}
          <p>- Đơn giá: {formatDecimalPrecision(record.price)}</p>
        </Flex>
      ),
    },
  ];

  return columns;
};
