export type RecordType<T extends object> = T & { children?: T[] };

export type ColumnType<T extends object> = {
  key?: string;
  title: React.ReactNode;
  className?: string;
  width?: string | number; // Width of column
  dataIndex?: string; // Key of data in dataSource
  render?: (text?: any, record?: RecordType<T>, index?: number) => React.ReactNode; // Custom render cell
  fixed?: 'left' | 'right'; // Fixed column
  align?: 'left' | 'right' | 'center'; // Align text in cell
};

export type ColumnsTypes<T extends object = {}> = ColumnType<T>[];

export type PaginationType = {
  showJumper?: boolean;
  showTotal?: boolean;
  totalLabel?: string;
  page: number;
  totalCount: number;
  size: number;
  onPageChange?: (page: number, size: number) => void;
};

export type RowSelection<T extends object> = {
  type: 'checkbox' | 'radio';
  selectedRowKeys?: string[];
  onChange?: (selectedRowKeys: string[],
    selectedRows: RecordType<T>[] | T[], options?: { row_data: T, index: number, isChecked: boolean }) => void;
  disabledKeys?: string[];
};
