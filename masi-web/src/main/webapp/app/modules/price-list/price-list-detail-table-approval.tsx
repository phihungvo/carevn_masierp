import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import SignDetailPopover from 'app/components/sign-table/sign-detail-popover';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { IQuotation } from 'app/shared/model/quotation.model';
import React from 'react';

interface IPriceListDetailTableApprovalProps {
  data: IQuotation;
}
const { useGetListProfileByIds } = useEmployee

const PriceListDetailTableApproval = (props: IPriceListDetailTableApprovalProps) => {
  const { data } = props;
  const { data: employees } = useGetListProfileByIds(data?.approverId ? [data.approverId] : []);

  const columns: ColumnsTypes<IQuotation> = [
    {
      title: 'Chữ ký',
      dataIndex: 'approvalSignFile',
      key: 'approvalSignFile',
      render: (fileId: string, record: IQuotation) =>
        fileId && (
          <SignDetailPopover
            id={`popover-sign${fileId}`}
            employee={employees?.data?.find(e => e.id === data.approverId)} signAt={data.processAt}>
            <AttachmentPreview name={record.approvalSignName} fileUrl={`${FILE_UTIL}/${record.approvalSignFile}`} />
          </SignDetailPopover>
        ),
    },
    {
      title: 'Lý do từ chối',
      dataIndex: 'customerRejectNote',
      key: 'customerRejectNote',
    },
    {
      title: 'Lý do từ chối nội bộ',
      dataIndex: 'rejectNote',
      key: 'rejectNote',
    },
  ];

  if (!data) return null;
  return <Table<IQuotation> showIndex={false} rowKey="id" dataSource={data.approverId ? [data] : null} columns={columns} />;
};

export default PriceListDetailTableApproval;
