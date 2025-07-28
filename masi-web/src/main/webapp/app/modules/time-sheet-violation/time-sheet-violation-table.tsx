import Table from 'app/components/table/table';
import useTimeKeepingViolation from 'app/hooks/use-time-keeping-violation';
import { ITimeKeepingViolation, ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { useQueryClient } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import { generateColumns } from './generate-columns';
import { useAppSelector } from 'app/config/store';

const { TIME_KEEPING_VIOLATION } = QUERY_KEY;
const { useGetTimeKeepingViolationsQuery, useGetTimeKeepingViolationsByExplanationQuery } = useTimeKeepingViolation;

interface ITimeSheetViolationTableProps {
  filter: ITimeKeepingViolationsParams;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingViolationsParams>>;
}

const TimeSheetViolationTable = (props: ITimeSheetViolationTableProps) => {
  const { filter, setFilter } = props;

  const queryClient = useQueryClient();
  const authorities = useAppSelector(state => state.authentication.account.authorities);
  const { id } = useParams<{ id: string }>();

  const { data, isLoading } = useGetTimeKeepingViolationsQuery(id, filter);
  const { data: explanationViolations, isLoading: loadingExplanationViolations } = useGetTimeKeepingViolationsByExplanationQuery({
    explanationId: id,
    size: filter?.size,
    page: filter?.page,
  });


  useEffect(() => {
    if (id) {
      queryClient.resetQueries({
        queryKey: [TIME_KEEPING_VIOLATION],
      });
    }
  }, [id]);

  const columns = generateColumns();

  const dataSource = data?.data || explanationViolations?.data;

  const totalCount = data?.totalRecord || explanationViolations?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <Table<ITimeKeepingViolation>
      rowKey="id"
      loading={isLoading || loadingExplanationViolations}
      columns={columns}
      dataSource={dataSource}
      responsive
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};

export default TimeSheetViolationTable;
