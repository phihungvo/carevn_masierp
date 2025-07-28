import { useMemo } from 'react';

import { ColumnsTypes } from 'app/components/table/table.d';

export const generateColumnsQualityIndexes = (): ColumnsTypes<{ name: string; value: string }> => {
  const columns: ColumnsTypes<{ name: string; value: string }> = useMemo(() => {
    return [
      {
        title: 'Chỉ tiêu kiểm nghiệm',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: 'Chấp nhận',
        key: 'value',
        dataIndex: 'value',
      },
    ];
  }, []);

  return columns;
};
