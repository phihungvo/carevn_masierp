import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IQuotation, IQuotationParams } from 'app/shared/model/quotation.model';
import { generateColumns } from './generate-columns';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useQuotations from 'app/hooks/use-quotations';
import { debounce, DebouncedFunc, keyBy } from 'lodash';

const { useGetQuotations } = useQuotations;

interface IPriceListTableProps {
  toggleDelete: () => void;
  toggleInApprove: () => void;
  toggleCancel: () => void;
  toggleCusSend: () => void;
  toggleApproveInternal: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
  searchText: string;
  filter: IQuotationParams;
  setFilter: React.Dispatch<React.SetStateAction<IQuotationParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IQuotation[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IQuotation[]>>;
}

const PriceListTable = (props: IPriceListTableProps) => {
  const {
    toggleDelete,
    toggleInApprove,
    toggleCancel,
    toggleCusSend,
    toggleApproveInternal,
    toggleApprove,
    toggleReject,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
  } = props;

  const columns = generateColumns(
    toggleDelete,
    toggleInApprove,
    toggleCancel,
    toggleCusSend,
    toggleApproveInternal,
    toggleApprove,
    toggleReject,
    setSelectedRecord,
  );

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useGetQuotations(filter);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  useEffect(() => {
    var statusDebounce: DebouncedFunc<() => void>;
    if (data) {
      statusDebounce = debounce(() => {
        var obj = keyBy(data.data, 'id');
        setSelectedRows(pre => [...pre.map(e => obj[e.id])]);
      }, 500);

      statusDebounce();
    }

    return () => statusDebounce && statusDebounce.cancel();
  }, [isRefetching]);

  return (
    <Table<IQuotation>
      rowKey="id"
      loading={isLoading}
      rowSelection={{
        type: 'checkbox',
        onChange: (selectedRowKeys: string[], selectedRows) => {
          setSelectedRowKeys(selectedRowKeys);
          setSelectedRows(selectedRows);
        },
        selectedRowKeys,
      }}
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

export default PriceListTable;
