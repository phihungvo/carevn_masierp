import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useFactoryLogistics from 'app/hooks/use-factory-logistics';
import React from 'react';

const { useDeleteFactoryMutation } = useFactoryLogistics;

interface IFactoriesDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const FactoriesDeleteModals = (props: IFactoriesDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteFactoryMutation(selectedRecord);

  const onOk = () => {
    if (selectedRecord)
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .finally(() => {
          toggle();
        });

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));

      Promise.all(promises)
        .then(() => toggleSuccess())
        .catch(() => {})
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
      className="modal-delete-factories-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader="Xóa nhà máy"
    >
      <Typography level={4}>Bạn muốn xoá nhà máy này?</Typography>
    </Modal>
  );
};

export default FactoriesDeleteModals;
