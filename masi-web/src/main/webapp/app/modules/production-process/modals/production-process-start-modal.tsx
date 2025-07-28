import Modal from 'app/components/modal/modal';
import React from 'react';
import { Typography } from 'app/components/typography/typography';
import useProductionProcess from 'app/hooks/use-production-process';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { START_PRODUCTION_PROCESS } = MUTATION_KEY;
const { usePatchProductionProcessStart } = useProductionProcess;

// MODAL START PROCESS
interface IModalStartProcess {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

export const ModalStartProcess = (props: IModalStartProcess) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { mutate } = usePatchProductionProcessStart();
  const isStartingProdProcess = useIsMutating({ mutationKey: [START_PRODUCTION_PROCESS] });

  const onOkStart = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
      },
    });
  };

  return (
    <Modal
      disabledOk={!!isStartingProdProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-start-process"
      okText="Bắt đầu"
      onOk={onOkStart}
    >
      <Typography level={3}>Bắt đầu công đoạn sản xuất</Typography>
      <Typography level={4}>Bạn muốn bắt đầu công đoạn sản xuất?</Typography>
    </Modal>
  );
};
