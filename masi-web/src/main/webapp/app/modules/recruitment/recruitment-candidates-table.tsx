import Table from 'app/components/table/table';
import React, { useEffect } from 'react';
import { IInterviewSchedule, IInterviewScheduleParams, IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { useDebounce } from 'app/hooks/use-debounce';
import { DEFAULT_PAGE } from 'app/constants/common';
import useRecruitment from 'app/hooks/use-recruitment';
import { generateColumnsCandidates } from './generate-columns-candidates';
import { useLocation } from "react-router";

const { useGetRecruitmentsCandidates } = useRecruitment;

interface IRecruitmentCandidatesTableProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  searchText: string;
  filter: IInterviewScheduleParams;
  setFilter: React.Dispatch<React.SetStateAction<IRecruitmentParams>>;
  setSelectedRecord: (id: string) => void;
  toggleUpdateCandidates: () => void;
}

const RecruitmentCandidatesTable = (props: IRecruitmentCandidatesTableProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    searchText,
    filter,
    setFilter,
    setSelectedRecord,
    toggleUpdateCandidates
  } = props;

  const { search } = useLocation();
  const idQuery = search?.split('=')[1];

  const searchData = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, search: searchData, page: DEFAULT_PAGE }));
  }, [searchData]);

  const { data, isLoading } = useGetRecruitmentsCandidates(idQuery ? { ...filter, recruitmentId: idQuery } : filter);

  const columns = generateColumnsCandidates(toggleDetail, toggleUpdate, setSelectedRecord, toggleUpdateCandidates);

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<IInterviewSchedule>
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

export default RecruitmentCandidatesTable;
