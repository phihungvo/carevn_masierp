// MODAL CANCEL REQUEST
import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';
import React from 'react';

const { CANCEL_LEAVE_REQUEST } = MUTATION_KEY;
const { usePatchLeaveRequestCancelMutation } = useLeaveRequest;

interface IModalCancelRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleError: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  toggleNotices?: () => void;
  setTextNotices?: React.Dispatch<React.SetStateAction<string>>;
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRequest[]>>

}

export const CancelRequestModal = (props: IModalCancelRequest) => {
  const { isOpen, toggle, toggleError, selectedRecord, selectedRowKeys, setSelectedRowKeys, setSelectedRecord, toggleNotices, setTextNotices, setSelectedRows } = props;

  const { mutateAsync } = usePatchLeaveRequestCancelMutation();
  const isCancelingReq = useIsMutating({ mutationKey: [CANCEL_LEAVE_REQUEST] });

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord).finally(() => {
        setSelectedRecord('');
        toggle();
      })
        .catch((err) => {
          if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
          else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
          else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
          toggleNotices();
        })
        .finally(() => {
          setSelectedRecord(null);
          setSelectedRows([]);
        });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id => mutateAsync(id));
      Promise.all(promises)
        .catch((err) => {
          if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
          else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
          else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
          toggleNotices();
        })
        .finally(() => {
          toggle();
          setSelectedRecord(null);
          setSelectedRowKeys([]);
          setSelectedRows([]);
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isCancelingReq}
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      titleHeader='Huỷ đơn nghỉ phép'
    >
      <Typography level={4}>Bạn có chắc rằng muốn huỷ đơn nghỉ phép này?</Typography>
    </Modal>
  );
};

// MODAL ERROR CANCEL REQUEST
interface IModalErrorCancelRequest {
  isOpen: boolean;
  toggle: () => void;
  cancel: boolean;
}

export const ErrorCancelRequestModal = (props: IModalErrorCancelRequest) => {
  const { isOpen, toggle, cancel } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={cancel}
      titleHeader='Không thể hủy đơn'
    >
      <Typography level={4}>Không thể hủy đơn khi ngày hiện tại bằng hoặc sau ngày nghỉ.</Typography>
    </Modal>
  );
};
