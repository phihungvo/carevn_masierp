import React from 'react';

import useOrders from 'app/hooks/use-orders';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

const { useDeleteOrderMutation } = useOrders;

interface ILogisticsMaterialProposalDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const LogisticsMaterialProposalDeleteModals = (props: ILogisticsMaterialProposalDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutateAsync, isPending } = useDeleteOrderMutation();

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .finally(() => {
          toggle();
          setSelectedRecord(null);
        });
      return;
    }

    // if (selectedRowKeys.length) {
    //   const promises = selectedRowKeys.map(id => mutateAsync(id));
    //   Promise.all(promises)
    //     .then(() => toggleSuccess())
    //     .catch(() => { })
    //     .finally(() => {
    //       toggle();
    //       setSelectedRowKeys([]);
    //     });
    // }
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-delete-modals" okText="Xác nhận" disabledOk={isPending} onOk={onOk}>
      <Typography level={3}>Xóa đơn đề xuất vật tư</Typography>
      <Typography level={4}>Bạn muốn xoá đề xuất vật tư này?</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalDeleteModals;
