import { downloadBase64File } from 'app/components/input/input-file';
import Table from 'app/components/table/table';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { ILeaveRequest, ILeaveRequestParams } from 'app/shared/model/leave-request.model';
import { debounce, DebouncedFunc, keyBy } from 'lodash';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';

const { useGetLeaveRequestsQuery } = useLeaveRequest;

interface ILeaveRequestTable {
  toggleCancelLeaveRequest: () => void;
  toggleDeleteRequest: () => void;
  toggleAcceptRequest: () => void;
  toggleRejectRequest: () => void;
  toggleDetailRequest: () => void;
  setSelectedRecord: (id: string) => void;
  setSelectedRowKeys: (ids: string[]) => void;
  selectedRowKeys?: string[];
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRequest[]>>;
  selected: {
    [leaving_id: string]: ILeaveRequest;
  };
  setSelected: React.Dispatch<
    React.SetStateAction<{
      [leaving_id: string]: ILeaveRequest;
    }>
  >;
  filter: ILeaveRequestParams;
  setFilter: React.Dispatch<React.SetStateAction<ILeaveRequestParams>>;
}

export const LeaveRequestTable = (props: ILeaveRequestTable) => {
  const {
    toggleCancelLeaveRequest,
    toggleDeleteRequest,
    toggleAcceptRequest,
    toggleRejectRequest,
    toggleDetailRequest,
    setSelectedRecord,
    setSelectedRowKeys,
    selectedRowKeys,
    setSelectedRows,
    selected,
    setSelected,
    filter,
    setFilter,
  } = props;

  const { data, isLoading, isRefetching } = useGetLeaveRequestsQuery(filter);
  const handleDownloadFile = (fileBase64: string, fileName: string, fileType: string) => {
    downloadBase64File(fileBase64, fileName, fileType);
  };

  const handleSelectLeaveRequest = (record: ILeaveRequest) => {
    setSelectedRecord(record.id);
    setSelected(pre => ({
      ...pre,
      [record.id]: record,
    }));
  };

  const handleCancelLeaveRequest = (record: ILeaveRequest) => {
    toggleCancelLeaveRequest();
    handleSelectLeaveRequest(record);
  };

  const handleAcceptLeaveReq = (record: ILeaveRequest) => {
    toggleAcceptRequest();
    handleSelectLeaveRequest(record);
  };

  const handleRejectLeaveReq = (record: ILeaveRequest) => {
    toggleRejectRequest();
    handleSelectLeaveRequest(record);
  };

  const handleDeleteLeaveRequest = (record: ILeaveRequest) => {
    toggleDeleteRequest();
    handleSelectLeaveRequest(record);
  };

  const handleDetailLeaveRequest = (record: ILeaveRequest) => {
    toggleDetailRequest();
    handleSelectLeaveRequest(record);
  };

  const columns = generateColumns(
    handleDownloadFile,
    handleCancelLeaveRequest,
    handleAcceptLeaveReq,
    handleRejectLeaveReq,
    handleDeleteLeaveRequest,
    handleDetailLeaveRequest,
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
    <Table<ILeaveRequest>
      rowKey="id"
      rowSelection={{
        type: 'checkbox',
        onChange: (selectedRowKeys: string[], selectedRows) => {
          setSelectedRowKeys(selectedRowKeys);
          setSelectedRows(selectedRows);
        },
        selectedRowKeys,
      }}
      loading={isLoading}
      columns={columns}
      dataSource={data?.data}
      responsive
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
      setSelected={setSelected}
    />
  );
};
