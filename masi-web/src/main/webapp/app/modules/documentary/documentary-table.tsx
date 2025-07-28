import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useDocumentary from 'app/hooks/use-documentary';
import { IDocumentary, IDocumentaryParams } from 'app/shared/model/documentary.model';

const { useGetDocumentariesQuery } = useDocumentary;

interface IDocumentaryTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  searchText: string;
  filter: IDocumentaryParams;
  setFilter: React.Dispatch<React.SetStateAction<IDocumentaryParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IDocumentary[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IDocumentary[]>>;
}

const DocumentaryTable = (props: IDocumentaryTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    togglePropose,
    toggleApprove,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
  } = props;

  const columns = generateColumns(toggleDetail, toggleUpdate, toggleDelete, togglePropose, toggleApprove, setSelectedRecord);

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useGetDocumentariesQuery(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  // useEffect(() => {
  //   var statusDebounce: DebouncedFunc<() => void>;
  //   if (data) {
  //     statusDebounce = debounce(() => {
  //       var obj = keyBy(data.data, 'id');
  //       setSelectedRows(pre => [...pre.map(e => obj[e.id])]);
  //     }, 500);

  //     statusDebounce();
  //   }

  //   return () => statusDebounce && statusDebounce.cancel();
  // }, [isRefetching]);

  return (
    <Table<IDocumentary>
      rowKey="id"
      loading={isLoading}
      // rowSelection={{
      //   type: 'checkbox',
      //   onChange: (selectedRowKeys: string[], selectedRows) => {
      //     setSelectedRowKeys(selectedRowKeys);
      //     setSelectedRows(selectedRows);
      //   },
      //   selectedRowKeys,
      // }}
      dataSource={data?.data}
      columns={columns}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default DocumentaryTable;
