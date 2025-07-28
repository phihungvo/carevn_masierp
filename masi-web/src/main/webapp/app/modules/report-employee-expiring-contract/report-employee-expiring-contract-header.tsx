import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { useDownloadXlsx } from 'app/hooks/use-download';
import useReports from 'app/hooks/use-reports';
import React from 'react';
import { CardSubtitle } from 'reactstrap';

const { useEmployeeExpiringContractReportsExcel } = useReports;

const ReportEmployeeExpiringContractHeader = () => {
  const { trigger, data } = useEmployeeExpiringContractReportsExcel();

  const handleDownload = () => {
    trigger();
  };

  useDownloadXlsx(data?.data, 'employee-expiring-contracts-reports', 'xlsx');

  return (
    <>
      <CardSubtitle className="card-header-subtitle"></CardSubtitle>
      <div className="card-header-extra">
        <AuthGuard permissionKey='REPORT_EMPLOYEE_EXPIRING_CONTRACT.EXPORT' >
        <ButtonIcon
          onClick={() => handleDownload()}
          icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
        />
        </AuthGuard>
      </div>
    </>
  );
};

export default ReportEmployeeExpiringContractHeader;
