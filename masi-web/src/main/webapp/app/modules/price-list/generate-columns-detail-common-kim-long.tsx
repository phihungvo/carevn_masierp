import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import { IQuotation } from 'app/shared/model/quotation.model';
import Flex from 'app/components/flex/flex';

export const generateColumnsDetailCommonKimLong = (): ColumnsTypes<IQuotation> => {
  const columns: ColumnsTypes<IQuotation> = useMemo(() => {
    return [
      {
        title: 'Tên bảng báo giá',
        key: 'name',
        dataIndex: 'name',
        render: text => <p>{text}</p>,
      },
      {
        title: 'Khách hàng',
        key: 'customer',
        dataIndex: 'customer',
        render: (_, record) => record?.customer?.companyName,
      },
      {
        title: 'Thanh toán',
        key: 'payment',
        dataIndex: 'payment',
        render: (_, record) => (
          <Flex direction="column">
            <p>- Phương thức thanh toán: {record?.paymentMethod}</p>
            <p>- Phương thức thanh toán (En): {record?.paymentMethodEn}</p>
          </Flex>
        ),
      },
      {
        title: 'Thông tin giao hàng',
        key: 'delivery',
        dataIndex: 'delivery',
        render: (_, record) => (
          <Flex direction="column">
            {/* <p>- Khối lượng giao hàng tối thiểu: {record?.minimumWeight}</p> */}
            <p>- Địa điểm giao hàng: {record?.deliveryLocation}</p>
            <p>- Địa điểm giao hàng (En): {record?.deliveryLocationEn}</p>
            <p>- Đóng gói: {record?.packaging}</p>
            <p>- Đóng gói (En): {record?.packagingEn}</p>
            {/* <p>- Thời gian giao hàng: {record?.deliveryDate ? dayjs(record?.deliveryDate).format(DATE_FORMAT.DATE) : ''}</p> */}
          </Flex>
        ),
      },
      // {
      //   title: 'Hình thức giá',
      //   key: 'priceType',
      //   dataIndex: 'priceType',
      //   render: (_, record) => (
      //     <Flex direction="column">
      //       <p>- Hình thức giá: {record?.priceType}</p>
      //       <p>- Hình thức giá (En): {record?.priceTypeEn}</p>
      //     </Flex>
      //   ),
      // },
    ];
  }, []);

  return columns;
};
