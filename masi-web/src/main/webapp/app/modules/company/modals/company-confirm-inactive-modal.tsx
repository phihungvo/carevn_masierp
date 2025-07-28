import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCompany from 'app/hooks/use-company';
import { useContext } from 'react';
import { CompanyContext } from '../company-provider';

const { useInactiveCompanyMutation } = useCompany;

const CompanyInactiveModal = () => {
  const {
    isOpenConfirmInactive,
    toggleConfirmInactive,
    selectedRecord,
    setSelectedRecord,
    toggleInactiveSuccess,
  } = useContext(CompanyContext);

  const { mutate, isPending } = useInactiveCompanyMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleConfirmInactive();
        toggleInactiveSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenConfirmInactive}
      toggle={toggleConfirmInactive}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận cập nhật không hoạt động"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn cập nhật không hoạt động công ty này không?`}
      </Typography>
    </Modal>
  );
};

export default CompanyInactiveModal;
