import { ColumnsTypes } from 'app/components/table/table.d';
import { IAuthority } from 'app/shared/model/authority.model';
import { useMemo } from 'react';

export const generateColumns = (): ColumnsTypes<IAuthority> => {
  const columns: ColumnsTypes<IAuthority> = useMemo(() => {
    return [
      {
        title: 'Tên quyền',
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
