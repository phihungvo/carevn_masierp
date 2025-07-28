import React from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';

const { DELETE_TIME_KEEPING_EXPLANATION } = MUTATION_KEY;
const { useDeleteTimeKeepingExplanationMutation } = useTimeKeepingExplanation;

// MODAL DELETE REQUEST
interface IModalDeleteRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleError: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (keys: string[]) => void;
  setSelectedRows: React.Dispatch<React.SetStateAction<ITimeKeepingExplanation[]>>
}

export const DeleteRequestModal = (props: IModalDeleteRequest) => {
  const { isOpen, toggle, toggleError, toggleSuccess, selectedRowKeys, setSelectedRowKeys, selectedRecord, setSelectedRecord, setSelectedRows } = props;

  const { mutateAsync } = useDeleteTimeKeepingExplanationMutation();
  const isDeletingReq = useIsMutating({ mutationKey: [DELETE_TIME_KEEPING_EXPLANATION] });

  const handleDeleteExplanation = () => {
    if (selectedRecord) {
      mutateAsync({
        id: selectedRecord,
      })
        .then(() => {
          toggleSuccess();
        })
        .catch(() => {
          toggleError();
        })
        .finally(() => {
          setSelectedRecord(null);
          setSelectedRows([]);
          toggle();
        });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync({ id }));
      Promise.all(promises)
        .then(() => {
          toggleSuccess();
        })
        .catch(() => {
          toggleError();
        })
        .finally(() => {
          toggle();
          setSelectedRows([]);
          setSelectedRowKeys([]);
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isDeletingReq}
      isOpen={isOpen}
      toggle={toggle}
      onOk={handleDeleteExplanation}
      cancel={false}
      titleHeader='Xoá giải trình chấm công'
    >
      <Typography level={4}>Bạn có chắc rằng muốn xoá giải trình chấm công này?</Typography>
    </Modal>
  );
};

// MODAL ERROR DELETE REQUEST
interface IModalErrorDeleteRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const ErrorDeleteRequestModal = (props: IModalErrorDeleteRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Không thể xoá giải trình'
    >
      <Typography level={4}>Chỉ có thể xóa giải trình chấm công khi đang ở trạng thái “Hủy”</Typography>
    </Modal>
  );
};

// MODAL DELETE REQUEST SUCCESS
interface IModalDeleteRequestSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const DeleteRequestSuccessModal = (props: IModalDeleteRequestSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Xoá giải trình chấm công'
    >
      <Typography level={4}>Xoá giải trình chấm công thành công</Typography>
    </Modal>
  );
};
