import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { LEAVE_REQUEST_STATUS } from 'app/shared/model/enumerations/leave-request.model';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';
import { set } from 'lodash';
import React from 'react';

const { REVIEW_LEAVE_REQUEST } = MUTATION_KEY;
const { usePatchLeaveRequestReviewMutation } = useLeaveRequest;

// MODAL ACCEPT REQUEST
interface IModalAcceptRequest {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selected: {
    [leaving_id: string]: ILeaveRequest;
  };
  toggleNotices?: () => void;
  setTextNotices: React.Dispatch<React.SetStateAction<string>>;
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRequest[]>>
}

export const AcceptRequestModal = (props: IModalAcceptRequest) => {
  const { isOpen, toggle, selectedRecord, selectedRowKeys, setSelectedRowKeys, selected, setSelectedRecord, toggleNotices, setTextNotices, setSelectedRows } = props;

  const { mutateAsync } = usePatchLeaveRequestReviewMutation();
  const isAcceptingReq = useIsMutating({ mutationKey: [REVIEW_LEAVE_REQUEST] });

  const onOk = () => {
    if (selectedRowKeys.length === 0) {
      mutateAsync({ reviewId: selected[selectedRecord].reviews[0].id, status: LEAVE_REQUEST_STATUS.APPROVED }).finally(() => {
        setSelectedRecord('');
        toggle();
      }).catch((err) => {
        if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
        else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
        else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
        toggleNotices();
      }).finally(() => {
        setSelectedRows([]);
      });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id =>
        mutateAsync({ reviewId: selected[id].reviews[0].id, status: LEAVE_REQUEST_STATUS.APPROVED }),
      );
      Promise.all(promises)
        .catch((err) => {
          if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
          else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
          else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
        })
        .finally(() => {
          toggle();
          setSelectedRowKeys([]);
          setSelectedRows([])
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isAcceptingReq}
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      titleHeader='Đồng ý đơn xét duyệt'
    >
      <Typography level={4}>Bạn muốn đồng ý đơn nghỉ phép này?</Typography>
    </Modal>
  );
};
