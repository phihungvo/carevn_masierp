import React from 'react';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import Descriptions from '@uiw/react-descriptions';
import useWarehouse from 'app/hooks/use-warehouse';

const { useGetWarehouseById } = useWarehouse;

interface IWarehouseDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const WarehouseDetailModals = (props: IWarehouseDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetWarehouseById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-warehouse"
      titleHeader='Chi tiết kho'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên kho" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Địa chỉ">
            <Typography level="text" className="fw-bolder">
              {data?.address}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default WarehouseDetailModals;
