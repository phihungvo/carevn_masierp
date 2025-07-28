import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCompany from 'app/hooks/use-company';
import { useContext } from 'react';
import { CompanyContext } from '../company-provider';

const { useActiveCompanyMutation } = useCompany;

const CompanyActiveModal = () => {
  const {
    isOpenConfirmActive,
    toggleConfirmActive,
    selectedRecord,
    setSelectedRecord,
    toggleActiveSuccess,
  } = useContext(CompanyContext);

  const { mutate, isPending } = useActiveCompanyMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleConfirmActive();
        toggleActiveSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenConfirmActive}
      toggle={toggleConfirmActive}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận cập nhật hoạt động"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn cập nhật hoạt động công ty này không?`}
      </Typography>
    </Modal>
  );
};

export default CompanyActiveModal;
