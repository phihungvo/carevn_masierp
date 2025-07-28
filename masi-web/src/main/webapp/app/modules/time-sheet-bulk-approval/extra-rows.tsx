import { ColumnsTypes } from 'app/components/table/table.d';
import { ITimeKeepingMonthly } from 'app/shared/model/time-keeping-monthly.model';
import React from 'react';

interface IExtraRowsProps {
  columns: ColumnsTypes<ITimeKeepingMonthly>;
  data: any;
}

const ExtraRows = (props: IExtraRowsProps) => {
  const { columns, data } = props;

  return (
    <tr>
      <td></td>
      {columns.map((col, index) => (
        <td key={`$parent-cel-${col.key}`} className={`${col.fixed ? `td-fixed-${col.fixed}` : ''}`.trim()}>
          <span style={{ justifyContent: 'left', textAlign: 'left' }}>{data?.[index]}</span>
        </td>
      ))}
      <td></td>
    </tr>
  );
};

export default ExtraRows;
