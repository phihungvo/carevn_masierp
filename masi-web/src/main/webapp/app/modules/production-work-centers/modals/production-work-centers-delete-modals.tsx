import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useWorkCenter from 'app/hooks/use-work-center';
import React from 'react';

const { useDeleteWorkCenterMutation } = useWorkCenter;
const { DELETE_WORK_CENTER } = MUTATION_KEY;

interface IProductionWorkCentersDeleteModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const ProductionWorkCentersDeleteModals = (props: IProductionWorkCentersDeleteModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync } = useDeleteWorkCenterMutation();
  const isDeleting = useIsMutating({
    mutationKey: [DELETE_WORK_CENTER],
  });

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
      className="modal-delete-prod-machines"
      okText="Xác nhận"
      onOk={onOk} disabledOk={!!isDeleting}
      titleHeader='Xóa cụm máy sản xuất'
    >
      <Typography level={4}>Bạn muốn xóa cụm máy sản xuất này?</Typography>
    </Modal>
  );
};

export default ProductionWorkCentersDeleteModals;
