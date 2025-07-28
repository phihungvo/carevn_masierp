import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import ExtraHeaders from './components/extra-headers';
import useAnnualLeave from 'app/hooks/use-annual-leave';
import { IAnnualLeaveResponse } from 'app/shared/model/annual-leave.model';

const { useAnnualLeaves } = useAnnualLeave;

const AnnualLeaveTable = () => {
  const { data } = useAnnualLeaves();

  const columns: ColumnsTypes<IAnnualLeaveResponse> = [
    {
      title: 'Số phép sau thử việc',
      key: 'leaveAfterProbation',
      render: (_, record) => record?.annualLeave_OFFICE?.leaveAfterProbation,
    },
    {
      title: 'Số phép sau mỗi năm',
      key: 'leavePerYear',
      render: (_, record) => record?.annualLeave_OFFICE?.leavePerYear,
    },
    {
      title: 'Ngày phép được cộng dồn đến tháng',
      key: 'carryForwardMonth',
      render: (_, record) => record?.annualLeave_OFFICE?.carryForwardMonth,
    },
    {
      title: 'Số phép sau thử việc',
      key: 'leaveAfterProbation',
      render: (_, record) => record?.annualLeave_FACTORY?.leaveAfterProbation,
    },
    {
      title: 'Số phép sau mỗi năm',
      key: 'leavePerYear',
      render: (_, record) => record?.annualLeave_FACTORY?.leavePerYear,
    },
    {
      title: 'Ngày phép được cộng dồn đến tháng',
      key: 'carryForwardMonth',
      render: (_, record) => record?.annualLeave_FACTORY?.carryForwardMonth,
    },
  ];

  const dataSource = [data];

  return (
    <Table
      className="annual-leave-table"
      dataSource={dataSource}
      columns={columns}
      extraHeaders={<ExtraHeaders />}
      pagination={{
        page: 0,
        size: 10,
        totalCount: 10,
      }}
    />
  );
};

export default AnnualLeaveTable;
