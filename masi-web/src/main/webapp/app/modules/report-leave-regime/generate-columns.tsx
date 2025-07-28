import { ColumnsTypes } from 'app/components/table/table.d';
import { ILeaveRegimeReport } from 'app/shared/model/report.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { useMemo } from 'react';

export const generateColumns = (): ColumnsTypes<ILeaveRegimeReport> => {
  const columns: ColumnsTypes<ILeaveRegimeReport> = useMemo(() => {
    return [
      {
        title: 'Loại nghỉ phép',
        key: 'leaveTypeDisplay',
        dataIndex: 'leaveTypeDisplay',
      },
      {
        title: 'Số lượng',
        key: 'count',
        dataIndex: 'count',
        render: text => formatDecimalPrecision(text),
      },
    ];
  }, []);

  return columns;
};
