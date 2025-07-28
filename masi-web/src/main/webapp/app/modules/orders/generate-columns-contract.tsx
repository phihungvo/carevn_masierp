import { useMemo } from 'react';

import { ColumnsTypes } from 'app/components/table/table.d';
import { IContractMaterial } from 'app/shared/model/contract.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

export const generateColumnsContract = (): ColumnsTypes<IContractMaterial> => {
  const columns: ColumnsTypes<IContractMaterial> = useMemo(() => {
    return [
      {
        title: 'Mặt hàng',
        key: 'materialName',
        dataIndex: 'materialName',
      },
      {
        title: 'Đơn giá',
        key: 'price',
        dataIndex: 'price',
        render: (value: string): string => formatDecimalPrecision(value),
      },
      {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity',
        render: (value: string): string => formatDecimalPrecision(value),
      },
      {
        title: 'Đơn vị',
        key: 'unit',
        dataIndex: 'unit',
      },
      {
        title: 'Thành tiền',
        key: 'total',
        dataIndex: 'total',
        render: (_, record): string => formatDecimalPrecision(parseFloat((record.price * record.quantity).toFixed(1))),
      },
      {
        title: 'Thông số',
        key: 'proteinParameters',
        dataIndex: 'proteinParameters',
        width: 300,
      },
    ];
  }, []);

  return columns;
};
