import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { generateColumns } from './generate-columns';
import { IRecruitment, IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useRecruitment from 'app/hooks/use-recruitment';

const { useGetRecruitments } = useRecruitment;

interface IRecruitmentTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
  toggleSchedule: () => void;
  toggleDelete: () => void;
  toggleRenew: () => void;
  toggleHistory: () => void;
  searchText: string;
  filter: IRecruitmentParams;
  setFilter: React.Dispatch<React.SetStateAction<IRecruitmentParams>>;
  setSelectedRecord: (id: string) => void;
  setRecord: React.Dispatch<React.SetStateAction<IRecruitment>>
}

const RecruitmentTable = (props: IRecruitmentTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleApprove,
    toggleReject,
    toggleSchedule,
    toggleDelete,
    toggleRenew,
    toggleHistory,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    setRecord
  } = props;

  const search = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
  }, [search]);

  const { data, isLoading } = useGetRecruitments(filter);

  const columns = generateColumns(toggleDetail, toggleUpdate, toggleApprove, toggleReject, toggleSchedule, toggleDelete, toggleRenew, toggleHistory, setSelectedRecord, setRecord);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IRecruitment>
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

export default RecruitmentTable;
