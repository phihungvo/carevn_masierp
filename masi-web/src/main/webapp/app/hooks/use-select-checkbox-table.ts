import { RecordType, RowSelection } from 'app/components/table/table.d';
import { getDistinctObjects } from 'app/shared/util/is-subset-array-string';
import { uniqBy } from 'lodash';
import { useEffect, useState } from 'react';

export const useSelectCheckboxTable = <T extends object>(
  dataSource: T[] | RecordType<T>[],
  rowSelection: RowSelection<T>,
  rowKey: string,
  setSelected?: React.Dispatch<
    React.SetStateAction<{
      [id: string]: T;
    }>
  >,
) => {
  const [selectedRecords, setSelectedRecords] = useState<{ selectedRowKeys: string[]; selectedRows: T[] | RecordType<T>[] }>({
    selectedRowKeys: rowSelection?.selectedRowKeys ?? [],
    selectedRows: dataSource?.filter(data => rowSelection?.selectedRowKeys?.includes(data[rowKey as string])) || [],
  });

  const handleSelectRow = (e: React.ChangeEvent<HTMLInputElement>, data: T | RecordType<T>, index: number) => {
    if (e.target.checked) {
      const checkedRecords = {
        selectedRowKeys: [...selectedRecords.selectedRowKeys, e.target.value],
        selectedRows: [...selectedRecords.selectedRows, data],
      };
      setSelectedRecords(checkedRecords);
      rowSelection?.onChange?.
      (checkedRecords.selectedRowKeys, checkedRecords.selectedRows, { row_data: data, index, isChecked: e.target.checked });
      if (setSelected && data['id']) {
        setSelected(pre => ({
          ...pre,
          [data['id']]: data,
        }));
      }
    } else {
      const uncheckedRecords = {
        selectedRowKeys: selectedRecords.selectedRowKeys.filter(key => key !== e.target.value),
        selectedRows: selectedRecords.selectedRows.filter(row => row[rowKey as string] !== e.target.value),
      };
      setSelectedRecords(uncheckedRecords);
      rowSelection?.onChange?.(uncheckedRecords.selectedRowKeys,
        uncheckedRecords.selectedRows, { row_data: data, index, isChecked: e.target.checked });
      if (setSelected && data['id']) {
        setSelected(pre => {
          delete pre[data['id']];
          return {
            ...pre,
          };
        });
      }
    }
  };

  const handleSelectAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      const checkedRecords = {
        selectedRowKeys: Array.from(
          new Set([...selectedRecords.selectedRowKeys, ...dataSource.map((data: T | RecordType<T>) => data[rowKey as string])]),
        ),
        selectedRows: getDistinctObjects([...selectedRecords.selectedRows, ...dataSource], rowKey as keyof T),
      };
      setSelectedRecords(checkedRecords);
      rowSelection?.onChange?.(checkedRecords.selectedRowKeys, checkedRecords.selectedRows);
    } else {
      const uncheckedRecords = {
        selectedRowKeys: [
          ...selectedRecords.selectedRowKeys.filter(
            key => !dataSource.map((data: T | RecordType<T>) => data[rowKey as string]).includes(key),
          ),
        ],
        selectedRows: [
          ...selectedRecords.selectedRows.filter(
            row => !dataSource.map((data: T | RecordType<T>) => data[rowKey as string]).includes(row[rowKey as string]),
          ),
        ],
      };
      setSelectedRecords(uncheckedRecords);
      rowSelection?.onChange?.(uncheckedRecords.selectedRowKeys, uncheckedRecords.selectedRows);
    }
  };

  // useEffect(() => {
  //   // rowSelection?.onChange?.(selectedRecords.selectedRowKeys, selectedRecords.selectedRows);
  // }, [selectedRecords]);

  useEffect(() => {
    dataSource &&
      setSelectedRecords(prev => ({
        selectedRowKeys: rowSelection?.selectedRowKeys ?? [],
        selectedRows:
          uniqBy([...prev.selectedRows, ...dataSource], 'id')?.filter(
            data => rowSelection?.selectedRowKeys?.includes(data[rowKey as string]),
          ) || prev.selectedRows,
      }));
  }, [rowSelection?.selectedRowKeys, dataSource]);

  return { selectedRecords, handleSelectRow, handleSelectAll };
};
