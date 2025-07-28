import Modal from 'app/components/modal/modal';
import React from 'react';
import { Typography } from 'app/components/typography/typography';
import useProductionProcess from 'app/hooks/use-production-process';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { STOP_PRODUCTION_PROCESS } = MUTATION_KEY;
const { usePatchProductionProcessStop } = useProductionProcess;

// MODAL STOP PROCESS
interface IModalStopProcess {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

export const ModalStopProcess = (props: IModalStopProcess) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { mutate } = usePatchProductionProcessStop();
  const isStoppingProcess = useIsMutating({ mutationKey: [STOP_PRODUCTION_PROCESS] });

  const onOkStop = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
      },
    });
  };

  return (
    <Modal
      disabledOk={!!isStoppingProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-stop-process"
      okText="Xác nhận"
      onOk={onOkStop}
    >
      <Typography level={3}>Ngừng công đoạn sản xuất</Typography>
      <Typography level={4}>Bạn muốn ngừng công đoạn sản xuất?</Typography>
    </Modal>
  );
};
