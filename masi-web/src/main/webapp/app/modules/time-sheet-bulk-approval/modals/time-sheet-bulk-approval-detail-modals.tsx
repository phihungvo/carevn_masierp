import Descriptions from '@uiw/react-descriptions';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FILE_UTIL } from 'app/constants/common';
import useTimeKeepingMonthly from 'app/hooks/use-time-keeping-monthly';
import React from 'react';

const { useGetTimeKeepingMonthlyDetailQuery } = useTimeKeepingMonthly;

interface ITimeSheetBulkApprovalDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const TimeSheetBulkApprovalDetailModals = (props: ITimeSheetBulkApprovalDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetTimeKeepingMonthlyDetailQuery(selectedRecord);

  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={false} ok={false} className="modals-detail-time-sheet-bulk">
      <Typography level={4}>Chi tiết duyệt chấm công tháng</Typography>
      <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
        <Descriptions.Item label="Lý do">
          <Typography level="text" className="fw-bolder">
            {data?.review?.note}
          </Typography>
        </Descriptions.Item>
        <Descriptions.Item label="Chữ ký">
          <Typography level="text" className="fw-bolder">
            {data?.review?.signatureFile && (
              <AttachmentPreview name={data?.review?.signature?.name} fileUrl={`${FILE_UTIL}/${data?.review?.signatureFile}`} />
            )}
          </Typography>
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default TimeSheetBulkApprovalDetailModals;
