import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useProductionMaintain from 'app/hooks/use-production-maintain';
import React from 'react';

const { DELETE_PRODUCTION_MAINTAIN } = MUTATION_KEY;

const { useDeleteProductionMaintain } = useProductionMaintain;

interface IProductionMaintenanceDeleteModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const ProductionMaintenanceDeleteModals = (props: IProductionMaintenanceDeleteModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const { mutateAsync } = useDeleteProductionMaintain();
  const isDeleting = useIsMutating({
    mutationKey: [DELETE_PRODUCTION_MAINTAIN],
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
      className="modal-delete-prod-routings"
      okText="Xác nhận"
      disabledOk={!!isDeleting}
      onOk={onOk}
      titleHeader='Xóa quản lý bảo trì sản phẩm'
    >
      <Typography level={4}>Bạn muốn xóa quản lý bảo trì sản phẩm này?</Typography>
    </Modal>
  );
};

export default ProductionMaintenanceDeleteModals;
