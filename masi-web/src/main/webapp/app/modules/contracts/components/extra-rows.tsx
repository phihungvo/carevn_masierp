import { ColumnsTypes } from 'app/components/table/table.d';
import { IContract } from 'app/shared/model/contract.model';
import React from 'react';

interface IExtraRowsProps {
  columns: ColumnsTypes<IContract>;
  data: any;
}

const ExtraRows = (props: IExtraRowsProps) => {
  const { columns, data } = props;

  return (
    <tr>
      {columns.map((col, index) => (
        <td key={`$parent-cel-${col.key}`} className={`${col.fixed ? `td-fixed-${col.fixed}` : ''}`.trim()}>
          <span style={{ justifyContent: 'left', textAlign: 'left' }}>{data?.[index]}</span>
        </td>
      ))}
    </tr>
  );
};

export default ExtraRows;
