import Badge from 'app/components/badge/badge';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from 'app/components/tooltip/tooltip';
import { UNIFORM_STATUS } from 'app/shared/model/enumerations/uniform.model';
import { IUniformStock } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import React, { useMemo } from 'react';

export const generateColumns = (): ColumnsTypes<IUniformStock> => {
  const columns: ColumnsTypes<IUniformStock> = useMemo(() => {
    return [
      {
        title: 'Mã đồng phục',
        key: 'code',
        render: (text, record) => <p>{record?.uniform?.code}</p>,
      },
      {
        title: 'Loại đồng phục',
        key: 'type',
        dataIndex: 'type',
        render: (text, record) => (
          <Tooltip label={record?.uniform?.name} target={`type-${record.id}`}>
            <EllipsisParagraph text={record?.uniform?.name} id={`type-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Số lượng tồn kho',
        key: 'stock',
        dataIndex: 'stock',
        render: (text, record) => (
          <Tooltip label={formatDecimalPrecision(text)} target={`stock-${record.id}`}>
            <EllipsisParagraph text={formatDecimalPrecision(text)} id={`stock-${record.id}`} />
          </Tooltip>
        ),
      },
      {
        title: 'Đơn giá',
        key: 'basePrice',
        render: (text, record) => formatDecimalPrecision(record?.uniform?.basePrice),
      },
      {
        title: 'Đơn vị',
        key: 'uom',
        dataIndex: 'uom',
        render: (_, record) => record?.uniform?.uomDTO?.name,
      },
      {
        title: 'Trạng thái',
        key: 'status',
        render: (_, record) =>
          record?.uniform?.status === UNIFORM_STATUS.ENABLE ? (
            <Badge color={'success'}>Hoạt động</Badge>
          ) : (
            <Badge color={'error'}>Không sử dụng</Badge>
          ),
      },
    ];
  }, []);

  return columns;
};
