import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';

const { DELETE_LEAVE_REQUEST } = MUTATION_KEY;
const { useDeleteLeaveRequestMutation } = useLeaveRequest;

// MODAL DELETE REQUEST
interface IModalDeleteRequest {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRequest[]>>
}

export const DeleteRequestModal = (props: IModalDeleteRequest) => {
  const { isOpen, toggle, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys, setSelectedRows } = props;

  const { mutateAsync } = useDeleteLeaveRequestMutation();
  const isDeletingReq = useIsMutating({ mutationKey: [DELETE_LEAVE_REQUEST] });

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync({
        id: selectedRecord,
      }).finally(() => {
        toggle();
        setSelectedRecord(null);
        setSelectedRows([]);
      });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id =>
        mutateAsync({
          id,
        }),
      );
      Promise.all(promises)
        .catch(() => { })
        .finally(() => {
          toggle();
          setSelectedRowKeys([]);
          setSelectedRecord(null);
          setSelectedRows([]);
        });
    }
  };

  return (
    <Modal
      disabledOk={!!isDeletingReq}
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      titleHeader='Xoá đơn nghỉ phép'
    >
      <Typography level={4}>Đơn nghỉ phép sẽ bị xóa vĩnh viễn và không thể phục hồi sau khi xác nhận xóa</Typography>
    </Modal>
  );
};
