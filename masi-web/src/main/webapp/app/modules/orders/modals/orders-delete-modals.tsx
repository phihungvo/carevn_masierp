import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useOrders from 'app/hooks/use-orders';
import React from 'react';

const { useDeleteOrderMutation } = useOrders;

interface IOrdersDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const OrdersDeleteModals = (props: IOrdersDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

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
      className="modal-delete-order-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa đơn đặt hàng'
    >
      <Typography level={4}>Bạn muốn xoá đơn đặt hàng này?</Typography>
    </Modal>
  );
};

export default OrdersDeleteModals;
