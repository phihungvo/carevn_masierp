import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IContract, IContractParams } from 'app/shared/model/contract.model';
import { generateColumns } from './generate-columns';
import useContracts from 'app/hooks/use-contracts';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import { DebouncedFunc, debounce, keyBy } from 'lodash';
import ExtraRows from './components/extra-rows';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

const { useGetContracts } = useContracts;

interface IContractsTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleRestore: () => void;
  toggleProposeApprove: () => void;
  toggleApprove: () => void;
  toggleApproveLiquid: () => void;
  toggleProposeLiquid: () => void;
  searchText: string;
  filter: IContractParams;
  setFilter: React.Dispatch<React.SetStateAction<IContractParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRows: IContract[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IContract[]>>;
  toggleMaskFinished: () => void;
  setContractStatus: React.Dispatch<React.SetStateAction<string>>
  setRecord: React.Dispatch<React.SetStateAction<IContract>>
}

const ContractsTable = (props: IContractsTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleRestore,
    toggleProposeApprove,
    toggleApprove,
    toggleApproveLiquid,
    toggleProposeLiquid,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    setSelectedRows,
    toggleMaskFinished,
    setContractStatus,
    setRecord
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading, isRefetching } = useGetContracts(filter);

  const columns = generateColumns(
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleRestore,
    toggleProposeApprove,
    toggleApprove,
    toggleApproveLiquid,
    toggleProposeLiquid,
    setSelectedRecord,
    toggleMaskFinished,
    setContractStatus,
    setRecord
  );

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

  const dataSource = data?.data?.reduce((acc, item, index) => {
    if (index !== data.data.length - 1) acc.push(item);
    return acc;
  }, []);

  const dataContractTotal = data?.data?.[data?.data?.length - 1];

  return (
    <Table<IContract>
      rowKey="id"
      loading={isLoading}
      dataSource={dataSource}
      columns={columns}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
        showTotal: true,
      }}
      extraRows={
        <>
          <ExtraRows
            columns={columns}
            data={['', <b>Tổng giá trị</b>, '', <b>{formatDecimalPrecision(dataContractTotal?.contractTotal)}</b>]}
          />
        </>
      }
    />
  );
};

export default ContractsTable;
