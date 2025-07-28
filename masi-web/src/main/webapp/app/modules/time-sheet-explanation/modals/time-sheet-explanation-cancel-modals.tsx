import React from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';

const { CANCEL_TIME_KEEPING_EXPLANATION } = MUTATION_KEY;
const { usePatchTimeKeepingExplanationCancel } = useTimeKeepingExplanation;

// MODAL CANCEL REQUEST
interface IModalCancelRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleError: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (rowKeys: string[]) => void;
  setSelectedRows: React.Dispatch<React.SetStateAction<ITimeKeepingExplanation[]>>
}

export const CancelRequestModal = (props: IModalCancelRequest) => {
  const { isOpen, toggle, toggleError, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys, setSelectedRows } = props;

  const { mutateAsync } = usePatchTimeKeepingExplanationCancel();
  const isCancellingReq = useIsMutating({ mutationKey: [CANCEL_TIME_KEEPING_EXPLANATION] });

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => {
          toggleSuccess();
        })
        .catch(() => {
          toggleError();
        })
        .finally(() => {
          setSelectedRows([]);
          setSelectedRecord(null);
          toggle();
        });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));
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
      disabledOk={!!isCancellingReq}
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      cancel={false}
      titleHeader='Huỷ giải trình chấm công'
    >
      <Typography level={4}>Bạn có chắc rằng muốn huỷ giải trình chấm công này?</Typography>
    </Modal>
  );
};

// MODAL ERROR CANCEL REQUEST
interface IModalErrorCancelRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const ErrorCancelRequestModal = (props: IModalErrorCancelRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Không thể huỷ giải trình'
    >
      <Typography level={4}>Không thể huỷ giải trình chấm công khi đã ở trạng thái “Được duyệt” / “Từ chối”</Typography>
    </Modal>
  );
};

// MODAL CANCEL REQUEST SUCCESS
interface IModalCancelRequestSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const CancelRequestSuccessModal = (props: IModalCancelRequestSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Huỷ giải trình chấm công'
    >
      <Typography level={4}>Huỷ giải trình chấm công thành công</Typography>
    </Modal>
  );
};
