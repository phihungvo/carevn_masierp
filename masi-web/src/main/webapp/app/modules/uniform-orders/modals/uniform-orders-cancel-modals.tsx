import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useUniform from 'app/hooks/use-uniform';
import React from 'react';

const { useCancelUniformOrder } = useUniform;

interface IUniformOrdersCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const UniformOrdersCancelModals = (props: IUniformOrdersCancelModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useCancelUniformOrder();

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
      titleHeader='Huỷ đơn hàng đồng phục'
    >
      <Typography level={4}>Bạn muốn huỷ đơn hàng đồng phục này?</Typography>
    </Modal>
  );
};

export default UniformOrdersCancelModals;
