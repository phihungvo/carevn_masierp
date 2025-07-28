import Modal from 'app/components/modal/modal';
import React from 'react';
import { Typography } from 'app/components/typography/typography';
import useProductionProcess from 'app/hooks/use-production-process';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { DELETE_PRODUCTION_PROCESS } = MUTATION_KEY;
const { useDeleteProductionProcess } = useProductionProcess;

// MODAL DELETE PROCESS
interface IModalDeleteProcess {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
}

export const ModalDeleteProcess = (props: IModalDeleteProcess) => {
  const { isOpen, toggle, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutate, mutateAsync } = useDeleteProductionProcess();
  const isDeletingProdProcess = useIsMutating({ mutationKey: [DELETE_PRODUCTION_PROCESS] });

  const onOkDelete = () => {
    if (selectedRecord) {
      mutate(selectedRecord);
      toggle();
      setSelectedRecord(null);
      return;
    }

    if (selectedRowKeys.length > 0) {
      const promises = selectedRowKeys.map(key => {
        mutateAsync(key);
      });

      Promise.all(promises)
        .catch(() => {})
        .finally(() => {
          setSelectedRowKeys([]);
          toggle();
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isDeletingProdProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOkDelete}
    >
      <Typography level={3}>Xoá công đoạn sản xuất</Typography>
      <Typography level={4}>Công đoạn sẽ bị xóa vĩnh viễn và không thể phục hồi sau khi xóa?</Typography>
    </Modal>
  );
};
