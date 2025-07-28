import Modal from 'app/components/modal/modal';
import Table from 'app/components/table/table';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import useReports from 'app/hooks/use-reports';
import { UNIFORM_REPORT_TYPE } from 'app/shared/model/enumerations/uniform.model';
import { IUniformChangeReportParams } from 'app/shared/model/report.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import React, { useEffect } from 'react';
import { DateObject } from 'react-multi-date-picker';

const { useGetUniformChangeReports } = useReports;

interface IReportUiformDetailModals {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
  selectedDate: DateObject[];
  type: UNIFORM_REPORT_TYPE;
}

const ReportUniformDetailModals = (props: IReportUiformDetailModals) => {
  const { isOpen, toggle, selectedRecord, selectedDate, type } = props;

  const [filterRelease, setFilterRelease] = React.useState<IUniformChangeReportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    type,
    fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
    toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
  });

  const [filterStock, setFilterStock] = React.useState<IUniformChangeReportParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    type,
    fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
    toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
  });

  useEffect(() => {
    setFilterRelease(prev => ({
      ...prev,
      type,
      fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      page: DEFAULT_PAGE,
    }));

    setFilterStock(prev => ({
      ...prev,
      type,
      fromDate: selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      toDate: selectedDate[1] ? selectedDate[1]?.format(DATE_FORMAT.YEAR_DATE) : selectedDate[0]?.format(DATE_FORMAT.YEAR_DATE),
      page: DEFAULT_PAGE,
    }));
  }, [selectedDate, type]);

  const { data: dataRelease } = useGetUniformChangeReports(selectedRecord, filterRelease);
  const { data: dataStocked } = useGetUniformChangeReports(selectedRecord, filterStock);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-uniform-report"
      titleHeader='Chi tiết nhập xuất tồn'
    >
      {type === UNIFORM_REPORT_TYPE.RELEASE && (
        <>
          <p className="fw-bold">- Danh sách xuất kho:</p>
          <Table
            rowKey="employeeCode"
            columns={[
              { title: 'Đồng phục', dataIndex: 'uniformName' },
              { title: 'Mã NV', dataIndex: 'employeeCode' },
              { title: 'Tên NV', dataIndex: 'employeeName' },
              { title: 'Số lượng', dataIndex: 'quantity', render: text => formatDecimalPrecision(text) },
            ]}
            dataSource={dataRelease?.data}
            pagination={{
              page: filterRelease.page,
              size: filterRelease.size,
              totalCount: dataRelease?.totalRecord,
              onPageChange: (page, size) => setFilterRelease(prev => ({ ...prev, page, size })),
            }}
          />
        </>
      )}

      {type === UNIFORM_REPORT_TYPE.STOCKED && (
        <>
          <p className="fw-bold">- Danh sách nhập kho:</p>
          <Table
            rowKey="employeeCode"
            columns={[
              { title: 'Đồng phục', dataIndex: 'uniformName' },
              { title: 'Mã NV', dataIndex: 'employeeCode' },
              { title: 'Tên NV', dataIndex: 'employeeName' },
              { title: 'Số lượng', dataIndex: 'quantity', render: text => formatDecimalPrecision(text) },
            ]}
            dataSource={dataStocked?.data}
            pagination={{
              page: filterStock.page,
              size: filterStock.size,
              totalCount: dataStocked?.totalRecord,
              onPageChange: (page, size) => setFilterStock(prev => ({ ...prev, page, size })),
            }}
          />
        </>
      )}
    </Modal>
  );
};

export default ReportUniformDetailModals;
