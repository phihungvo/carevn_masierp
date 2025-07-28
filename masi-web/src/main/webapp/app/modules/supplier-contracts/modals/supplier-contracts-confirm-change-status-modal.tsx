import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useChangeStatusSupplierContracts } from 'app/hooks/use-supplier-contract';
import { useContext } from 'react';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';
import { supplierContractStatusTextMapping } from '../supplier-contracts-mapping';

const SupplierContractsConfirmChangeStatusModal = () => {
  const {
    isOpenChangeStatus,
    toggleChangeStatus,
    selectedRecord,
    setSelectedRecord,
    toggleChangeStatusSuccess,
    status,
  } = useContext(SupplierContractsContext);

  const { mutate, isPending } = useChangeStatusSupplierContracts(selectedRecord);

  const onOk = () => {
    mutate(status, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleChangeStatus(null);
        toggleChangeStatusSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenChangeStatus}
      toggle={()=>toggleChangeStatus(status)}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận thay đổi trạng thái"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn thay đổi sang trạng thái ${supplierContractStatusTextMapping(status as any)} này không?`}
      </Typography>
    </Modal>
  );
};

export default SupplierContractsConfirmChangeStatusModal;
