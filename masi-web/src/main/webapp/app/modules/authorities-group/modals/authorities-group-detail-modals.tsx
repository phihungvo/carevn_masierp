import React from 'react';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import Descriptions from '@uiw/react-descriptions';
import useGroup from 'app/hooks/use-group';

const { useGroupById } = useGroup;

interface IAuthoritiesGroupDetailModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const AuthoritiesGroupDetailModals = (props: IAuthoritiesGroupDetailModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGroupById(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className="modals-detail-group modal-default"
      titleHeader='Chi tiết nhóm quyền'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhóm quyền" span={2}>
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Mô tả">
            <Typography level="text" className="fw-bolder">
              {data?.description}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default AuthoritiesGroupDetailModals;
