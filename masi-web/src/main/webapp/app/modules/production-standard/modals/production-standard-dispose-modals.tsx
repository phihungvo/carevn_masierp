import React from 'react';

import Modal from 'app/components/modal/modal';
import useProductionStandard from 'app/hooks/use-production-standard';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { Typography } from 'app/components/typography/typography';

const { DELETE_PRODUCTION_STANDARD } = MUTATION_KEY;
const { useDisposeProductionStandard } = useProductionStandard;

// MODAL DELETE PRODUCTION STANDARD
interface IModalDisposeProductionStandard {
  isOpen: boolean;
  toggle: () => void;
  toggleDisposeProductionStandardSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (keys: string[]) => void;
}

export const ModalDisposeProductionStandard = ({
  isOpen,
  toggle,
  toggleDisposeProductionStandardSuccess,
  selectedRecord,
  setSelectedRecord,
  selectedRowKeys,
  setSelectedRowKeys,
}: IModalDisposeProductionStandard) => {
  const { mutateAsync } = useDisposeProductionStandard(
    toggleDisposeProductionStandardSuccess,
  );
  const isDeletingProdStand = useIsMutating({
    mutationKey: [DELETE_PRODUCTION_STANDARD],
  });

  const handleDeleteProductionStandard = () => {
    if (selectedRecord) {
      mutateAsync({
        id: selectedRecord,
      }).finally(() => {
        toggle();
      });
      setSelectedRecord(null);
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id =>
        mutateAsync({
          id,
        }),
      );
      Promise.all(promises)
        .catch(() => {})
        .finally(() => {
          toggle();
          setSelectedRowKeys([]);
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isDeletingProdStand}
      isOpen={isOpen}
      toggle={toggle}
      okText="Đồng ý"
      onOk={handleDeleteProductionStandard}
      titleHeader="Hủy định mức sản xuất"
    >
      <Typography level={4}>
        Bạn có chắc chắn muốn hủy kiểm định mức sản xuất này?
      </Typography>
    </Modal>
  );
};
