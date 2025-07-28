import React from 'react'
import Tabs from '@uiw/react-tabs';
import { DateObject } from 'react-multi-date-picker';

import Card from 'app/components/card/card';
import ReportUniformsExportsTable from 'app/modules/report-uniforms-exports/report-uniforms-exports-table';
import ReportUniformsExportsHeader from 'app/modules/report-uniforms-exports/report-uniforms-exports-header';
import ReportUniformsExportChart from 'app/modules/report-uniforms-exports/components/report-uniforms-exports-chart';
import { EREPORTS, EREPORTS_TEXT } from 'app/shared/model/enumerations/reports';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';
import { IUniformImport, IUniformImportParams } from 'app/shared/model/report.model';
import { REPORT_TYPE } from 'app/modules/report-uniforms-exports/report-uniforms-exports';

interface IReportUniformsExportsBody {
    toggleFilter: () => void;
    selectedDate: DateObject[];
    setSelectedDate: (value: DateObject[]) => void;
    reportType: REPORT_TYPE;
    setReportType: (value: REPORT_TYPE) => void;
    type: REPORT_TYPE;
    filter: IUniformImportParams;
    setFilter: React.Dispatch<React.SetStateAction<IUniformImportParams>>;
    isLoading?: boolean;
    totalCount?: number;
    dataSource?: IUniformImport[];
    setSelectedRecord?: (id: string) => void;
    toggleDetail?: () => void;
    setType?: (type: UNIFORM_REPORT_TYPE) => void;
    data?: IUniformImport[]
}

function ReportUniformsExportsBody({
    toggleFilter,
    selectedDate,
    setSelectedDate,
    reportType,
    setReportType,
    type,
    filter,
    setFilter,
    isLoading,
    totalCount,
    dataSource,
    setSelectedRecord,
    toggleDetail,
    setType,
    data
}: IReportUniformsExportsBody) {
    return (
        <Tabs type='card' activeKey={EREPORTS.DATATABLE}>
            <Tabs.Pane label={EREPORTS_TEXT.DATATABLE} key={EREPORTS.DATATABLE}>
                <Card
                    header={
                        <ReportUniformsExportsHeader
                            toggleFilter={toggleFilter}
                            selectedDate={selectedDate}
                            setSelectedDate={setSelectedDate}
                            reportType={reportType}
                            setReportType={setReportType}
                        />
                    }
                >
                    <ReportUniformsExportsTable
                        filter={filter}
                        setFilter={setFilter}
                        type={reportType}
                        dataSource={data}
                        isLoading={isLoading}
                        totalCount={totalCount}
                        setSelectedRecord={setSelectedRecord}
                        toggleDetail={toggleDetail}
                        setType={setType}
                    />
                </Card>
            </Tabs.Pane>
            <Tabs.Pane label={EREPORTS_TEXT.CHART} key={EREPORTS.CHART}>
                <ReportUniformsExportChart filter={filter} setFilter={setFilter} />
            </Tabs.Pane>
        </Tabs>
    )
}

export default ReportUniformsExportsBody