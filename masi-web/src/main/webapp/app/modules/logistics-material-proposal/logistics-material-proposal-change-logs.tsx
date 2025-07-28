import React, { useState } from 'react';
import { useParams } from 'react-router';

import './logistics-material-proposal.scss';
import Card from 'app/components/card/card';
// import useEmployee from 'app/hooks/use-employee';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IEmployeeChangeLogParams } from 'app/shared/model/employee.model';
import { ILogisticsMaterialProposalChangeLog } from 'app/shared/model/logistics-material-proposal';
import LogisticsMaterialProposalChangeLogTable from './logistics-material-proposal-change-logs-table';
import LogisticsMaterialProposalChangelogsHeader from './logistics-material-proposal-header-change-logs';
import { useModalsVoucherRequestPaymentChangeLog } from 'app/hooks/use-modals-logistics-material-proposal';
import LogisticsMaterialProposalDetailsChangelogModals from './modals/logistics-material-proposal-details-changelog-modal';

const mockLogisticsMaterialProposalChangeLogs: PaginationResponse<ILogisticsMaterialProposalChangeLog> = {
    data: [
        {
            id: '1',
            changeDate: '2024-08-01T10:30:00Z',
            change: [
                {
                    newValue: 'John Doe',
                    oldValue: 'Johnny Doe',
                    fieldName: 'Full Name',
                },
                {
                    newValue: 'Manager',
                    oldValue: 'Assistant Manager',
                    fieldName: 'Position',
                },
            ],
            changeBy: 'HR Admin',
            employeeId: 'E001',
        },
        {
            id: '2',
            changeDate: '2024-09-15T14:00:00Z',
            change: [
                {
                    newValue: '2000 USD',
                    oldValue: '1800 USD',
                    fieldName: 'Salary',
                },
                {
                    newValue: 'Marketing Department',
                    oldValue: 'Sales Department',
                    fieldName: 'Department',
                },
            ],
            changeBy: 'Finance Team',
            employeeId: 'E002',
        },
        {
            id: '3',
            changeDate: '2024-07-25T08:45:00Z',
            change: [
                {
                    newValue: 'Active',
                    oldValue: 'Inactive',
                    fieldName: 'Status',
                },
                {
                    newValue: 'Permanent',
                    oldValue: 'Contract',
                    fieldName: 'Employment Type',
                },
            ],
            changeBy: 'HR Manager',
            employeeId: 'E003',
        },
    ],
    totalRecord: 3,
};


// const { useGetEmployeeChangeLog } = useEmployee;

const LogisticsMaterialProposalChangeLogs = () => {
    const [{ openDetail, toggleDetail }] = useModalsVoucherRequestPaymentChangeLog()

    const { id } = useParams();

    // const { data, isLoading } = useGetEmployeeChangeLog({
    //   employeeId: id,
    // });

    const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
    const [filter, setFilter] = useState<IEmployeeChangeLogParams>({
        page: DEFAULT_PAGE,
        size: DEFAULT_PAGE_SIZE,
        employeeId: '',
    });

    return (
        <>
            <Typography level={3}>Lịch sử thay đổi đề xuất vật tư</Typography>

            <Card header={<LogisticsMaterialProposalChangelogsHeader />}>
                <LogisticsMaterialProposalChangeLogTable
                    data={mockLogisticsMaterialProposalChangeLogs}
                    isLoading={false}
                    filter={filter}
                    setFilter={setFilter}
                    setSelectedRecord={setSelectedRecord}
                    toggleDetail={toggleDetail}
                />
            </Card>

            <LogisticsMaterialProposalDetailsChangelogModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
        </>
    );
};

export default LogisticsMaterialProposalChangeLogs;
