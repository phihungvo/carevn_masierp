import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import React from 'react';

const { useGetItemByIdQuery } = useItems;

interface ISuppliesDetailsModalProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const SuppliesDetailsModal = (props: ISuppliesDetailsModalProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetItemByIdQuery(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      ok={false}
      cancel={false}
      className="supplies-detail-modal"
      titleHeader="Chi tiết mã VT - CCDC"
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã VT">
            <Typography level="text" className="fw-bolder">
              {data?.code}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên VT">
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên tiếng Anh">
            <Typography level="text" className="fw-bolder">
              {data?.attribute?.['nameEng'] || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn vị tính">
            <Typography level="text" className="fw-bolder">
              {data?.uom?.name || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Xuất xứ">
            <Typography level="text" className="fw-bolder">
              {data?.attribute?.['origin'] || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Thuế VAT">
            <Typography level="text" className="fw-bolder">
              {data?.vatRate}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Loại" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.itemCategory?.name || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhóm doanh thu" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.itemTypes?.name || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn giá" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.unitPrice}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhà cung cấp" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.supplier?.name || ''}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ghi chú" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.notes}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default SuppliesDetailsModal;
