import { Spin } from 'antd';
import classNames from 'classnames';
import get from 'lodash/get';
import React, { ComponentProps, useState } from 'react';
import { Table } from 'reactstrap';
import './Table.scss';
import { TableClassName, TableStyles } from './type';

const turn_down_and_icon = 'content/images/vuesax/linear/turn-down-and.svg';
const turn_down_icon = 'content/images/vuesax/linear/turn-down.svg';

export type TableColumns<T extends object> = {
  header?: {
    th_class?: string;
    th_style?: React.CSSProperties;
    render: string | React.ReactNode;
  };
  body?: {
    td_class?: string;
    td_props?: ComponentProps<'td'>;
    render: (data_cell: { data: T; index: number }) => React.ReactNode | string;
    sub_render?: (data_cell: {
      data: any;
      index: number;
      icon_path: string;
      parent_index: number;
    }) => React.ReactNode | string;
    sub_key?: string;
  };
}[];

type Props<T extends object> = {
  columns: TableColumns<T>;
  data: T[];
  fixedHeader?: boolean;
  table_id: string;
  rowKey?: string;
  className?: TableClassName;
  styles?: TableStyles;
  isSubRow?: boolean;
  sub_key?: string;
  isBorder?: boolean;
  custom_body_row?: (data: T[]) => React.ReactNode;
  custom_header_row?: (data: T[]) => React.ReactNode;
  isStickyLastRow?: boolean;
  isLoading?: boolean;
  tableFixedLayout?: boolean;
};

const TableV2 = <T extends object>(props: Props<T>) => {
  const {
    columns,
    data,
    fixedHeader,
    className,
    styles,
    table_id,
    isSubRow,
    sub_key,
    isBorder,
    custom_body_row,
    custom_header_row,
    isStickyLastRow,
    isLoading,
  } = props;

  if (!columns?.length) return <></>;

  const [isExpandRow, setIsExpandRow] = useState({});

  const onExpandRow =
    (row_id: string) =>
    (e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) => {
      if (e.target['tagName'] !== 'TD') return;
      setIsExpandRow({ ...isExpandRow, [row_id]: !isExpandRow[row_id] });
    };

  return (
    <Table
      className={classNames('table-v2 table-v2--fixed', className?.table)}
      style={styles?.table}
      hover
      responsive
      bordered
      data-border={isBorder}
      data-sticky-last-row={isStickyLastRow}
      data-fixed-layout={props.tableFixedLayout}
    >
      {columns?.[0]?.header && (
        <thead className={classNames(className?.thead)} style={styles?.thead}>
          <tr>
            {columns.map((column, index) => (
              <th
                key={index}
                id={`${table_id}-thead-${index}`}
                style={column.header.th_style}
                className={column.header.th_class}
              >
                {column.header.render}
              </th>
            ))}
          </tr>
        </thead>
      )}
      {custom_header_row && !columns?.[0]?.header && (
        <thead className={classNames(className?.thead)} style={styles?.thead}>
          {custom_header_row && custom_header_row(data)}
        </thead>
      )}
      <tbody className={classNames(className?.tbody)} style={styles?.tbody}>
        {!!data?.length &&
          columns[0]?.body &&
          data.map((item, index) => {
            let html_for = `${table_id}-trigger-sub-row-${index}`;
            let tr_key = `${table_id}-tbody-tr-${index}`;
            let td_parent_id = `${table_id}-tbody-tr-${index}`;
            let sub_key = undefined;
            return (
              <>
                <tr key={tr_key} data-row onClick={onExpandRow(html_for)}>
                  {columns.map((column, columnIdx) => {
                    sub_key = column.body?.sub_key;
                    td_parent_id = `${td_parent_id}-td-${columnIdx}`;
                    return (
                      <td
                        {...column?.body?.td_props}
                        key={td_parent_id}
                        className={column.body?.td_class}
                      >
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                          {column.body.render({ index, data: item })}
                        </div>
                      </td>
                    );
                  })}
                </tr>
                {!!sub_key &&
                  get(data?.[index], sub_key)?.map(
                    (sub_item: any, sub_index: number) => (
                      <tr
                        key={`${table_id}-sub-tbody-tr-${sub_index}`}
                        data-row-sub
                        data-open={!!isExpandRow[html_for]}
                      >
                        {columns.map(sub_column => (
                          <td
                            key={`${table_id}-sub-tbody-td-${sub_index}`}
                            className={sub_column.body?.td_class}
                          >
                            {sub_column.body.sub_render({
                              index: sub_index,
                              data: sub_item,
                              parent_index: index,
                              icon_path:
                                sub_index ===
                                get(data?.[index], sub_key).length - 1
                                  ? turn_down_icon
                                  : turn_down_and_icon,
                            })}
                          </td>
                        ))}
                      </tr>
                    ),
                  )}
              </>
            );
          })}
        {data.length === 0 && columns[0]?.body && (
          <tr>
            <td colSpan={columns?.length} className="text-center">
              {isLoading ? <Spin /> : <>Không có dữ liệu</>}
            </td>
          </tr>
        )}
        {custom_body_row && custom_body_row(data)}
      </tbody>
    </Table>
  );
};

TableV2.defaultProps = {
  isBorder: true,
  data: [],
};

export default TableV2;
