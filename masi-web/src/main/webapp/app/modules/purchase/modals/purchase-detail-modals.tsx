import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePurchase from 'app/hooks/use-purchase';
import React from 'react';
import purchaseMapping from '../purchase-mapping';
import dayjs from 'dayjs';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import Descriptions from '@uiw/react-descriptions';
import Badge from 'app/components/badge/badge';

const { purchaseStatusTextMapping, purchaseUnitTextMapping, purchaseColorMapping } = purchaseMapping;
const { useGetPurchaseById, useGetPurchaseReviewByPurchaseRequestId } = usePurchase;

interface IPurchaseDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const PurchaseDetailModals = (props: IPurchaseDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetPurchaseById(selectedRecord);
  const { data: dataReviews } = useGetPurchaseReviewByPurchaseRequestId(selectedRecord);

  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={false} ok={false} className="modals-detail-purchase">
      <Typography level={4}>Chi tiết hàng hoá</Typography>

      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên hàng hóa">
            <Typography level="text" className="fw-bolder">
              {data?.productName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số lượng">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.quantity)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn vị">
            <Typography level="text" className="fw-bolder">
              {purchaseUnitTextMapping(data?.unit)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn giá">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.unitPrice)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thành tiền">
            <Typography level="text" className="fw-bolder">
              {formatDecimalPrecision(data?.totalPrice)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhà cung cấp">
            <Typography level="text" className="fw-bolder">
              {data?.supplier}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Trạng thái">
            <Typography level="text" className="fw-bolder">
              <Badge color={purchaseColorMapping(data?.requestStatus)}>{purchaseStatusTextMapping(data?.requestStatus)}</Badge>
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày tạo">
            <Typography level="text" className="fw-bolder">
              {data?.createDate ? dayjs(data?.createDate).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ghi chú" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Hình ảnh" span={2}>
            {data?.purchaseRequestFiles && (
              <Flex gap={8}>
                {data?.purchaseRequestFiles?.map((file, index) => (
                  <React.Fragment key={file?.id}>
                    <AttachmentPreview name={file?.name} fileUrl={`${FILE_UTIL}/${file?.id}`} />
                  </React.Fragment>
                ))}
              </Flex>
            )}
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />
        <Descriptions title="Thông tin xét duyệt" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Lý do">
            <Typography level="text" className="fw-bolder">
              {dataReviews?.[0]?.approvalStatusNote}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Chữ ký">
            {dataReviews?.[0]?.approvalStatusFile && (
              <AttachmentPreview name={'Chữ ký'} fileUrl={`${FILE_UTIL}/${dataReviews?.[0]?.approvalStatusFile}`} />
            )}
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default PurchaseDetailModals;
