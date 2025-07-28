import { ColumnsTypes } from 'app/components/table/table.d';
import { IHrChangeReport } from 'app/shared/model/report.model';
import dayjs from 'dayjs';
import { useMemo } from 'react';

export const generateColumns = (): ColumnsTypes<IHrChangeReport> => {
  const columns: ColumnsTypes<IHrChangeReport> = useMemo(() => {
    return [
      {
        title: 'SL NV đã nghỉ',
        key: 'totalLeave',
        dataIndex: 'totalLeave',
      },
      {
        title: 'SL NV mới',
        key: 'totalJoin',
        dataIndex: 'totalJoin',
      },
      {
        title: 'Tháng',
        key: 'month',
        dataIndex: 'month',
        render: value => dayjs(value).format('MM/YYYY'),
      },
    ];
  }, []);

  return columns;
};
