import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IContract, IContractParams } from 'app/shared/model/contract.model';
import { generateColumns } from './generate-columns';
import useContracts from 'app/hooks/use-contracts';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import { DebouncedFunc, debounce, keyBy } from 'lodash';

const { useGetContractsDeleted } = useContracts;

interface IContractsTableProps {
  toggleRestore: () => void;
  searchText: string;
  filter: IContractParams;
  setFilter: React.Dispatch<React.SetStateAction<IContractParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: IContract[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IContract[]>>;
}

const ContractTableDeleted = (props: IContractsTableProps) => {
  const {
    toggleRestore,

    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useGetContractsDeleted(filter);

  const columns = generateColumns(toggleRestore, setSelectedRecord);

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
    <Table<IContract>
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
      }}
    />
  );
};

export default ContractTableDeleted;
