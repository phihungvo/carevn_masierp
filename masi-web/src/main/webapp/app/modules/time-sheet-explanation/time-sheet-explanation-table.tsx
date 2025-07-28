import './time-sheet-explanation.scss';
import React, { useEffect } from 'react';
import Table from 'app/components/table/table';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import { ITimeKeepingExplanation, ITimeKeepingExplanationParams } from 'app/shared/model/time-keeping-explanation.model';
import { generateColumns } from './generate-columns';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import { debounce, DebouncedFunc, keyBy } from 'lodash';

const { useGetTimeKeepingExplanationsQuery } = useTimeKeepingExplanation;

interface ITimeSheetExplanationTable {
  toggleAcceptReq: () => void;
  toggleRejectReq: () => void;
  toggleUpdateReq: () => void;
  toggleCancelReq: () => void;
  toggleDeleteReq: () => void;
  setSelectedRecord: (record: string) => void;
  setSelectedRowKeys: (keys: string[]) => void;
  selectedRowKeys: string[];
  setSelectedRows: React.Dispatch<React.SetStateAction<ITimeKeepingExplanation[]>>;
  filter: ITimeKeepingExplanationParams;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingExplanationParams>>;
  searchText: string;
}

export const TimeSheetExplanationTable = (props: ITimeSheetExplanationTable) => {
  const {
    toggleAcceptReq,
    toggleRejectReq,
    toggleUpdateReq,
    toggleCancelReq,
    toggleDeleteReq,
    setSelectedRecord,
    setSelectedRowKeys,
    selectedRowKeys,
    setSelectedRows,
    filter,
    setFilter,
    searchText,
  } = props;

  const searchString = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  const { data, isLoading, isRefetching } = useGetTimeKeepingExplanationsQuery(filter);

  const columns = generateColumns(
    toggleAcceptReq,
    toggleRejectReq,
    toggleUpdateReq,
    toggleCancelReq,
    toggleDeleteReq,
    setSelectedRecord,
    setSelectedRows,
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

  return (
    <Table<ITimeKeepingExplanation>
      loading={isLoading}
      rowKey="id"
      rowSelection={{
        type: 'checkbox',
        onChange: (selectedRowKeys: string[], selectedRows) => {
          setSelectedRowKeys(selectedRowKeys);
          setSelectedRows(selectedRows);
        },
        selectedRowKeys,
      }}
      className="explanation-table"
      columns={columns}
      dataSource={data?.data}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};
