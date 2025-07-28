import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useProductionRoutings from 'app/hooks/use-production-routings';
import React from 'react';

const { DELETE_PRODUCTION_ROUTING } = MUTATION_KEY;

const { useDeleteProductionRoutingMutation } = useProductionRoutings;

interface IProductionRoutingsDeleteModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ProductionRoutingsDeleteModals = (
  props: IProductionRoutingsDeleteModals,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const { mutateAsync } = useDeleteProductionRoutingMutation();
  const isDeleting = useIsMutating({
    mutationKey: [DELETE_PRODUCTION_ROUTING],
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
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      disabledOk={!!isDeleting}
      onOk={onOk}
      titleHeader="Xóa định tuyến kho sản xuất"
    >
      <Typography level={4}>
        Bạn muốn xóa định tuyến kho sản xuất này?
      </Typography>
    </Modal>
  );
};

export default ProductionRoutingsDeleteModals;
