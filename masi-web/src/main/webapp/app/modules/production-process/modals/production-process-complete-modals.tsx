import React from 'react';
import { useIsMutating } from '@tanstack/react-query';

import Modal from 'app/components/modal/modal';
import useProductionProcess from 'app/hooks/use-production-process';
import { MUTATION_KEY } from 'app/constants/query-key';
import { Typography } from 'app/components/typography/typography';

const { COMPLETE_PRODUCTION_PROCESS } = MUTATION_KEY;
const { usePatchProductionProcessComplete } = useProductionProcess;

// MODAL COMPLETE PROCESS
interface IModalCompleteProcess {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

export const ModalCompleteProcess = (props: IModalCompleteProcess) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { mutate } = usePatchProductionProcessComplete();
  const isCompletingProcess = useIsMutating({ mutationKey: [COMPLETE_PRODUCTION_PROCESS] });

  const onOkComplete = () => {
    mutate(selectedRecord, {
      onSuccess: () => toggle()
    });
  };

  return (
    <Modal
      disabledOk={!!isCompletingProcess}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-complete-process"
      okText="Xác nhận"
      onOk={onOkComplete}
    >
      <Typography level={3}>Hoàn thành công đoạn</Typography>
      <Typography level={4}>Bạn muốn hoàn thành công đoạn sản xuất?</Typography>
    </Modal>
  );
};
