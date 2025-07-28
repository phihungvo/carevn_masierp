import { ColumnsTypes } from 'app/components/table/table.d';
import { IUniformSupport } from 'app/shared/model/report.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { useMemo } from 'react';
import reportUniformsReimbursementMapping from './report-uniforms-reimbursement-mapping';
import Badge from 'app/components/badge/badge';
import React from 'react';

const { reportUniformsReimbursementTextMapping, reportUniformsReimbursementTypeColorMapping } = reportUniformsReimbursementMapping;

export const generateColumns = (): ColumnsTypes<IUniformSupport> => {
  const columns: ColumnsTypes<IUniformSupport> = useMemo(() => {
    return [
      {
        title: 'Loại đồng phục',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: 'Số lượng',
        key: 'total',
        dataIndex: 'total',
        render: text => formatDecimalPrecision(text),
      },
      {
        title: 'Trạng thái',
        key: 'type',
        dataIndex: 'type',
        render: text => (
          <Badge color={reportUniformsReimbursementTypeColorMapping(text)}>{reportUniformsReimbursementTextMapping(text)}</Badge>
        ),
      },
    ];
  }, []);

  return columns;
};
