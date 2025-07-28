import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useUniform from 'app/hooks/use-uniform';
import React from 'react';

const { useDeleteUniformOrder } = useUniform;

interface IUniformOrdersDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const UniformOrdersDeleteModals = (props: IUniformOrdersDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteUniformOrder();

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
      titleHeader='Xóa đơn hàng đồng phục'
    >
      <Typography level={4}>Bạn muốn xoá đơn hàng đồng phục này?</Typography>
    </Modal>
  );
};

export default UniformOrdersDeleteModals;
