import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePurchase from 'app/hooks/use-purchase';
import React from 'react';

const { useDeletePurchase } = usePurchase;

interface IPurchaseDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  toggleError: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PurchaseDeleteModals = (props: IPurchaseDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, toggleError, selectedRecord, setSelectedRecord } = props;

  const { mutateAsync, isPending } = useDeletePurchase();

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .catch(() => toggleError())
        .finally(() => {
          toggle();
          setSelectedRecord(null);
        });
      return;
    }
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-purchase-success" okText="Xác nhận" disabledOk={isPending} onOk={onOk}>
      <Typography level={3}>Xoá đơn đề nghị</Typography>
      <Typography level={4}>Bạn chắc chắn muốn xóa đơn đề nghị này?</Typography>
    </Modal>
  );
};

export default PurchaseDeleteModals;
