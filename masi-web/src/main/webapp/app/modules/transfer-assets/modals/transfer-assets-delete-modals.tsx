import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useItems from 'app/hooks/use-items';
import useTransferAssets from 'app/hooks/use-transfer-assets';
import React from 'react';

const { useDeleteTransferAssetsMutation } = useTransferAssets;

interface ITransferAssetsDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const TransferAssetsDeleteModals = (props: ITransferAssetsDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteTransferAssetsMutation(selectedRecord);

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
      className="modal-delete-transfer-assets-success"
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onOk}
      titleHeader="Xóa điều chuyển TS"
    >
      <Typography level={4}>Bạn muốn xoá điều chuyển TS này?</Typography>
    </Modal>
  );
};

export default TransferAssetsDeleteModals;
