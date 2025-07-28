import React from 'react';
import { generateColumns } from './generate-columns';
import Table from 'app/components/table/table';
import { REPORT_TYPE } from './report-uniforms-exports';
import { IUniformImport, IUniformImportParams } from 'app/shared/model/report.model';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';

interface IReportUniformsExportsTableProps {
  type: REPORT_TYPE;
  filter: IUniformImportParams;
  setFilter: React.Dispatch<React.SetStateAction<IUniformImportParams>>;
  isLoading?: boolean;
  totalCount?: number;
  dataSource?: IUniformImport[];
  setSelectedRecord?: (id: string) => void;
  toggleDetail?: () => void;
  setType?: (type: UNIFORM_REPORT_TYPE) => void;
}

const ReportUniformsExportsTable = (props: IReportUniformsExportsTableProps) => {
  const { type, filter, setFilter, isLoading, dataSource, totalCount, setSelectedRecord, toggleDetail, setType } = props;

  const columns = generateColumns(toggleDetail, setSelectedRecord, setType);

  const { page, size } = filter;

  return (
    <Table<IUniformImport>
      loading={isLoading}
      dataSource={dataSource}
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

export default ReportUniformsExportsTable;
