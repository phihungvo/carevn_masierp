import React from 'react';
import { useIsMutating } from '@tanstack/react-query';

import Modal from 'app/components/modal/modal';
import useProductionPackage from 'app/hooks/use-production-package';
import { MUTATION_KEY } from 'app/constants/query-key';
import { Typography } from 'app/components/typography/typography';
import { IProductionPackage } from 'app/shared/model/production-package.model';

const { DELETE_PRODUCTION_PACKAGE } = MUTATION_KEY;

const { useDeleteProductionPackage } = useProductionPackage;

interface IProductionPackagesDeleteModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRows: React.Dispatch<React.SetStateAction<IProductionPackage[]>>
  setSelectedRowKeys: (ids: string[]) => void;
}

const ProductionPackagesDeleteModals = (props: IProductionPackagesDeleteModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys, setSelectedRows } = props;

  const { mutateAsync } = useDeleteProductionPackage();
  const isDeleting = useIsMutating({
    mutationKey: [DELETE_PRODUCTION_PACKAGE],
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
          setSelectedRows([])
        });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-prod-packages"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={!!isDeleting}
      titleHeader='Xóa quản lý đóng gói sản phẩm'
    >
      <Typography level={4}>Bạn muốn xóa quản lý đóng gói sản phẩm này?</Typography>
    </Modal>
  );
};

export default ProductionPackagesDeleteModals;
