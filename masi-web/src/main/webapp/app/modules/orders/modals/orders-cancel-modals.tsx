import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useOrders from 'app/hooks/use-orders';
import React from 'react';

const { useCancelOrderMutation } = useOrders;

interface IOrdersCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const OrdersCancelModals = (props: IOrdersCancelModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useCancelOrderMutation();

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

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));
      Promise.all(promises)
        .then(() => toggleSuccess())
        .catch(() => { })
        .finally(() => {
          toggle();
          setSelectedRowKeys([]);
        });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-cancel-order"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Huỷ đơn đặt hàng'
    >
      <Typography level={4}>Bạn muốn huỷ đơn đặt hàng này?</Typography>
    </Modal>
  );
};

export default OrdersCancelModals;
