import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { WORK_SPACE_HAS_EMPLOYEE } from 'app/constants/error';
import useWorkspace from 'app/hooks/use-workspace';
import React, { useEffect, useState } from 'react';
import { Alert } from 'reactstrap';

const { useDeleteWorkspaceMutation, useGetWorkspaceByIdQuery } = useWorkspace;

interface IOfficesDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
}

const OfficesDeleteModals = (props: IOfficesDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, selectedRowKeys, setSelectedRowKeys } = props;

  const [isWorkspaceHasEmp, setIsWorkspaceHasEmp] = useState(false);
  const { data } = useGetWorkspaceByIdQuery(selectedRecord);

  const onSuccess = () => {
    toggle();
    toggleSuccess();
    setSelectedRecord(null);
  };

  const { mutate, isPending } = useDeleteWorkspaceMutation(onSuccess);

  const onOk = () => {
    mutate(selectedRecord, {
      onError: error => {
        if (error?.response?.data?.message === WORK_SPACE_HAS_EMPLOYEE) {
          setIsWorkspaceHasEmp(true);
        }
      },
    });
  };

  useEffect(() => {
    !isOpen && setIsWorkspaceHasEmp(false);
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-offices-success"
      disabledOk={isPending || !data?.data?.canDelete}
      loadingOk={isPending}
      onOk={onOk}
      cancel={false}
    >
      <Typography level={3}>Xoá văn phòng/nhà máy</Typography>
      {!data?.data?.canDelete ? (
        <Alert color="danger">Không thể xoá văn phòng/nhà máy này vì đang còn những dữ liệu liên quan</Alert>
      ) : (
        <Typography level={4}>Bạn có chắc rằng muốn xoá văn/ nhà máy này?</Typography>
      )}

      {isWorkspaceHasEmp && <Alert color="danger">Không thể xoá văn phòng/nhà máy này vì đang có nhân viên</Alert>}
    </Modal>
  );
};

export default OfficesDeleteModals;
