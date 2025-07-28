import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useContracts from 'app/hooks/use-contracts';
import React from 'react';

const { useDeleteContract } = useContracts;

interface IContractDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const ContractDeleteModals = (props: IContractDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync, isPending } = useDeleteContract();

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
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xoá hợp đồng'
    >
      <Typography level={4}>Bạn có chắc rằng muốn xoá hợp đồng này?</Typography>
    </Modal>
  );
};

export default ContractDeleteModals;
