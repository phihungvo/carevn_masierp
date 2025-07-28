import React, { useEffect } from 'react';
import { debounce, DebouncedFunc, keyBy } from 'lodash';

import Table from 'app/components/table/table';
import { generateColumns } from './generate-columns';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import { ColumnsTypes } from 'app/components/table/table.d';
import { ILeaveRegime, ILeaveRegimeParams } from 'app/shared/model/leave-regime.model';

const { useGetLeaveRegimes } = useLeaveRegime;

interface ILeaveRegisterTableProps {
  toggleUpdate: () => void;
  toggleApproveSign: () => void;
  toggleCancel: () => void;
  toggleReject: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleDetail: () => void;
  filter: ILeaveRegimeParams;
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRegimeParams>>;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRows: ILeaveRegime[];
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRegime[]>>;
}

const LeaveRegisterTable = (props: ILeaveRegisterTableProps) => {
  const {
    toggleApproveSign,
    toggleUpdate,
    toggleDelete,
    toggleReject,
    toggleCancel,
    togglePropose,
    toggleDetail,
    filter,
    setFilter,
    setSelectedRecord,
    setSelectedRows,
  } = props;

  const columns: ColumnsTypes = generateColumns(
    toggleApproveSign,
    toggleCancel,
    toggleDelete,
    toggleReject,
    toggleUpdate,
    togglePropose,
    toggleDetail,
    setSelectedRecord,
  );

  const { data, isLoading, isRefetching } = useGetLeaveRegimes(filter);

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
    <Table<ILeaveRegime>
      rowKey="id"
      loading={isLoading}
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

export default LeaveRegisterTable;
