import React, { useMemo } from 'react';

import { ColumnsTypes } from 'app/components/table/table.d';
import { IReportRecruitment } from 'app/shared/model/report.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

export const generateColumns = (): ColumnsTypes<IReportRecruitment> => {
  const columns: ColumnsTypes<IReportRecruitment> = useMemo(() => {
    return [
      {
        title: 'Vị trí',
        key: 'position',
        dataIndex: 'position',
      },
      {
        title: 'Tổng số yêu cầu',
        key: 'totalRequest',
        dataIndex: 'totalRequest',
        render: (text) => <p>{formatDecimalPrecision(text)}</p>
      },
      {
        title: 'Đã tuyển',
        key: 'totalRecruited',
        dataIndex: 'totalRecruited',
      },
      {
        title: 'Đang tuyển',
        key: 'totalRecruiting',
        dataIndex: 'totalRecruiting',
      },
    ];
  }, []);

  return columns;
};
