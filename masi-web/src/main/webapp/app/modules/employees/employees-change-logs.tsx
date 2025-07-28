import { Typography } from 'app/components/typography/typography';
import './employees.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import EmployeesChangeLogTable from './employees-change-logs-table';
import { IEmployeeChangeLogParams } from 'app/shared/model/employee.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useParams } from 'react-router';
import useEmployee from 'app/hooks/use-employee';
import { useModalsEmployeeChangeLog } from 'app/hooks/use-modals-employee';
import EmployeeDetailsChangelogModal from './modals/employee-details-changelog-modal';
import CardV2 from 'app/components/CardV2/CardV2';

const { useGetEmployeeChangeLog } = useEmployee;

const EmployeesChangeLogs = () => {
  const [{ openDetail, toggleDetail }] = useModalsEmployeeChangeLog();
  const { id } = useParams();

  const { data, isLoading } = useGetEmployeeChangeLog({
    employeeId: id,
  });

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [filter, setFilter] = useState<IEmployeeChangeLogParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    employeeId: '',
  });

  return (
    <CardV2
      header={<Typography level={5}>Lịch sử thay đổi nhân viên</Typography>}
    >
      <Card>
        <EmployeesChangeLogTable
          data={data}
          isLoading={isLoading}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          toggleDetail={toggleDetail}
        />
      </Card>

      <EmployeeDetailsChangelogModal isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
    </CardV2>
  );
};

export default EmployeesChangeLogs;
