import React from 'react';
import Descriptions from '@uiw/react-descriptions';

import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { IChangeItem } from 'app/shared/model/logistics-material-proposal';

interface VoucherRequestPaymentDetailsChangelogModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const dataMockIChangeItem: IChangeItem[] = [
  {
    newValue: 'John Doe',
    oldValue: 'Johnny Doe',
    fieldName: 'Full Name',
  },
  {
    newValue: 'Manager',
    oldValue: 'Assistant Manager',
    fieldName: 'Position',
  },
]

const LogisticsMaterialProposalDetailsChangelogModals = (props: VoucherRequestPaymentDetailsChangelogModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} footer={null} ok={false} cancel={false} className="logistics-material-proposal-detail-changelog-modals">
      <Typography level={4}>Lịch sử thay đổi ngày 28-02-2024</Typography>
      <Flex direction="column" gap={16}>
        <Descriptions size="large" bordered column={2}>
          {dataMockIChangeItem.map((item: IChangeItem) => (
            <Descriptions.Item label={item.fieldName} key={item.fieldName}>
              <Typography level="text" className="fw-bolder">
                {item.oldValue}
              </Typography>
              {' -> '}
              <Typography level="text" className="fw-bolder">
                {item.newValue}
              </Typography>
            </Descriptions.Item>
          ))}
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default LogisticsMaterialProposalDetailsChangelogModals;

