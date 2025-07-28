import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IOrder, IOrderParams } from 'app/shared/model/order.model';
import { generateColumns } from './generate-columns';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import { DebouncedFunc, debounce, keyBy } from 'lodash';

const { useOrdersQuery } = useOrders;

interface IOrdersTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleCancel: () => void;
  searchText: string;
  filter: IOrderParams;
  setFilter: React.Dispatch<React.SetStateAction<IOrderParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IOrder[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IOrder[]>>;
  toggleDownloadSuccessful: () => void;
}

const OrdersTable = (props: IOrdersTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    togglePropose,
    toggleApprove,
    toggleCancel,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    setSelectedRows,
    toggleDownloadSuccessful,
  } = props;

  const columns = generateColumns(
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    togglePropose,
    toggleApprove,
    toggleCancel,
    setSelectedRecord,
    filter,
    toggleDownloadSuccessful,
  );

  const searchString = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  const { data, isLoading, isRefetching } = useOrdersQuery(filter);

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
    <Table<IOrder>
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
        showTotal: true,
        totalLabel: 'Tổng số đơn hàng',
      }}
    />
  );
};

export default OrdersTable;
