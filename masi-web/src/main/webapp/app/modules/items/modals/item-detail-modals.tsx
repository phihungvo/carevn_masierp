import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import Descriptions from '@uiw/react-descriptions';
import useItems from 'app/hooks/use-items';

interface IItemDetailModal {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const ItemDetailModals = (props: IItemDetailModal) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { useGetItemByIdQuery } = useItems;
  const { data: item } = useGetItemByIdQuery(selectedRecord);
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-item"
      titleHeader='Chi tiết đăng ký nghỉ chế độ'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item className="bold" label="Mã vật phẩm">
            <Typography level="text" className="fw-bolder">
              {item?.code}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên vật phẩm">
            <Typography level="text" className="fw-bolder">
              {item?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Đơn vị">
            <Typography level="text" className="fw-bolder">
              {item?.uom?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Loại vật phẩm">
            <Typography level="text" className="fw-bolder">
              {item?.itemCategory?.name}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Descriptions bordered>
          <Descriptions.Item label="Ghi chú">
            <Typography level="text" className="fw-bolder">
              {item?.notes}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default ItemDetailModals;
