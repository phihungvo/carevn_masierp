import Button from 'app/components/button/button';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { IContract } from 'app/shared/model/contract.model';
import React, { useEffect, useState } from 'react';

interface IContractApproveLiquidModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
  record?: IContract
}

const ContractApproveLiquidModals = (props: IContractApproveLiquidModalsProps) => {
  const { isOpen, toggle, toggleApprove, toggleReject, record } = props;

  const [isCheck, setIsCheck] = useState<boolean>(false);

  const account = useAppSelector(state => state.authentication.account);

  const handleApprove = () => {
    if (record?.requestApprovals?.map(item => item?.employeeId).includes(account?.id)) {
      toggle();
      toggleApprove();
      return;
    }
    setIsCheck(true);
  };

  const handleReject = () => {
    if (record?.requestApprovals?.map(item => item?.employeeId).includes(account?.id)) {
      toggle();
      toggleReject();
      return;
    }
    setIsCheck(true);
  };

  useEffect(() => {
    if (!isOpen) setIsCheck(false)
  }, [isOpen])

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className='contracts-approve-liquid-modals'
      footer={
        <>
          {
            !isCheck && <>
              <Button color="primary" onClick={handleReject}>
                Từ chối
              </Button>
              <Button color="primary" onClick={handleApprove}>
                Đồng ý
              </Button>
            </>
          }
        </>
      }
      titleHeader='Xét duyệt thanh lý HĐ'
    >
      {
        isCheck ? <p className="text-danger">Bạn không phải là người xét duyệt!</p> :
          <Typography level={4}>Bạn có muốn xét duyệt thanh lý hợp đồng</Typography>
      }
    </Modal>
  );
};

export default ContractApproveLiquidModals;
