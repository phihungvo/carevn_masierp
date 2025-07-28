import { ILeaveRegimeReport } from 'app/shared/model/report.model';
import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';

interface IReportLeaveRegimeTableProps {
  data: ILeaveRegimeReport[];
  isLoading: boolean;
}

const ReportLeaveRegimeTable = (props: IReportLeaveRegimeTableProps) => {
  const { data, isLoading } = props;

  const columns = generateColumns();

  return <Table<ILeaveRegimeReport> rowKey="leaveType" loading={isLoading} dataSource={data} columns={columns} />;
};

export default ReportLeaveRegimeTable;
