import { useMemo } from 'react';

import { IGroup } from 'app/shared/model/group.model';
import { ColumnsTypes } from 'app/components/table/table.d';

export const generateColumnsAuthoritiesGroup = (
): ColumnsTypes<IGroup> => {
  const columns: ColumnsTypes<IGroup> = useMemo(() => {
    return [
      {
        title: 'Tên nhóm quyền',
        key: 'name',
        dataIndex: 'name',
      },
      {
        title: 'Mô tả',
        key: 'description',
        dataIndex: 'description',
      },
    ];
  }, []);

  return columns;
};
