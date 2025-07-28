import React from 'react';

import Table from 'app/components/table/table';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { generateColumnsChangelog } from './generate-columns-change-logs';
import { IEmployeeChangeLog, IEmployeeChangeLogParams } from 'app/shared/model/employee.model';
import { ILogisticsMaterialProposalChangeLog } from 'app/shared/model/logistics-material-proposal';

interface ILogisticsMaterialProposalChangeLogTableProps {
    data: PaginationResponse<ILogisticsMaterialProposalChangeLog>;
    isLoading: boolean;
    filter: IEmployeeChangeLogParams;
    setFilter: React.Dispatch<React.SetStateAction<IEmployeeChangeLog>>;
    setSelectedRecord: (id: string) => void;
    toggleDetail: () => void;
}

const LogisticsMaterialProposalChangeLogTable = (props: ILogisticsMaterialProposalChangeLogTableProps) => {
    const { data, isLoading, filter, setFilter, setSelectedRecord, toggleDetail } = props;

    const totalCount = data?.totalRecord || 0;
    const { page, size } = filter;

    const columns = generateColumnsChangelog(toggleDetail, setSelectedRecord);

    return (
        <Table<ILogisticsMaterialProposalChangeLog>
            rowKey="id"
            dataSource={data?.data}
            columns={columns}
            loading={isLoading}
            pagination={{
                page,
                size,
                totalCount,
                onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
            }}
        />
    );
};

export default LogisticsMaterialProposalChangeLogTable;
