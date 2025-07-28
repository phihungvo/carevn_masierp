import dayjs from 'dayjs';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import AuthGuard from 'app/components/guards/auth-guard';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { IContract } from 'app/shared/model/contract.model';
import { CONTRACT_STATUS } from 'app/shared/model/enumerations/contract.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';


interface IActionsDropdownProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleRestore: () => void;
  toggleProposeApprove: () => void;
  toggleApprove: () => void;
  toggleApproveLiquid: () => void;
  toggleProposeLiquid: () => void;
  record: IContract;
  setSelectedRecord: (id: string) => void
  toggleMaskFinished: () => void;
  setContractStatus: React.Dispatch<React.SetStateAction<string>>
  setRecord: React.Dispatch<React.SetStateAction<IContract>>
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleRestore,
    toggleProposeApprove,
    toggleApprove,
    toggleApproveLiquid,
    toggleProposeLiquid,
    record,
    setSelectedRecord,
    toggleMaskFinished,
    setContractStatus,
    setRecord
  } = props;


  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    setContractStatus(record?.status)
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  // const handleRestore = () => {
  //   setSelectedRecord(record.id);
  //   toggleRestore();
  // };

  const handleProposeApprove = () => {
    setSelectedRecord(record.id);
    toggleProposeApprove();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApprove();
    setRecord(record)
  };

  const handleApproveLiquid = () => {
    setSelectedRecord(record.id);
    toggleApproveLiquid();
    setRecord(record)
  };

  const handleProposeLiquid = () => {
    setSelectedRecord(record.id);
    toggleProposeLiquid();
  };

  const handleMaskFinished = () => {
    setSelectedRecord(record.id);
    toggleMaskFinished()
  }

  const disabledUpdate = record?.status === CONTRACT_STATUS.APPROVED ||
    record?.status === CONTRACT_STATUS.LIQUIDATED ||
    record?.status === CONTRACT_STATUS.FINISHED;

  const disabledDelete =
    record?.status === CONTRACT_STATUS.APPROVED ||
    record?.status === CONTRACT_STATUS.LIQUIDATED ||
    record?.status === CONTRACT_STATUS.CANCELLED ||
    record?.status === CONTRACT_STATUS.FINISHED ||
    record?.status === CONTRACT_STATUS.WAITING_LIQUIDATION;

  const disabledPropose =
    record?.status === CONTRACT_STATUS.APPROVED ||
    record?.status === CONTRACT_STATUS.LIQUIDATED ||
    record?.status === CONTRACT_STATUS.CANCELLED ||
    record?.status !== CONTRACT_STATUS.REJECTED &&
    record?.status !== CONTRACT_STATUS.DRAFT;

  const isExpired = record?.contractValidTo && dayjs(record?.contractValidTo).isBefore(dayjs());


  const disabledApprove = record?.status !== CONTRACT_STATUS.WAITING_APPROVAL;

  // const disabledProposeLiquid = !(
  //   record?.status === CONTRACT_STATUS.APPROVED ||
  //   (record?.status === CONTRACT_STATUS.DRAFT && isExpired) ||
  //   (record?.status === CONTRACT_STATUS.WAITING_APPROVAL && isExpired) || record?.status === CONTRACT_STATUS.FINISHED
  // );

  const disabledProposeLiquid = record?.status !== CONTRACT_STATUS.APPROVED && record?.status !== CONTRACT_STATUS.FINISHED;

  const disabledLiquid = record?.status !== CONTRACT_STATUS.WAITING_LIQUIDATION;

  const disabledMaskFinished = record?.status !== CONTRACT_STATUS.APPROVED;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
        <AuthGuard permissionKey='CONTRACTS.EDIT'>
          <DropdownItem onClick={handleDetail}>
            <ButtonIcon icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>Chi tiết</ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
            <ButtonIcon
              className="update"
              icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
            >
              Cập nhật
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleDelete} disabled={disabledDelete}>
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
        <AuthGuard permissionKey='CONTRACTS.CREATE'>
          <DropdownItem onClick={handleProposeApprove} disabled={disabledPropose}>
            <ButtonIcon
              className="propose-approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/folder-2.svg" alt="propose-approve" />}
            >
              Đề xuất xét duyệt
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
        <AuthGuard permissionKey='CONTRACTS.EDIT'>
          <DropdownItem onClick={handleApprove} disabled={disabledApprove}>
            <ButtonIcon
              className="approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
            >
              Xét duyệt HĐ
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
        <AuthGuard permissionKey='CONTRACTS.CREATE'>
          <DropdownItem onClick={handleProposeLiquid} disabled={disabledProposeLiquid}>
            <ButtonIcon
              className="propose-liquidate"
              icon={<img className="pointer" src="content/images/vuesax/linear/money-send.svg" alt="propose-liquidate" />}
            >
              Đề xuất thanh lý
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
        <AuthGuard permissionKey='CONTRACTS.EDIT'>
          <DropdownItem onClick={handleApproveLiquid} disabled={disabledLiquid}>
            <ButtonIcon
              className="approve-liquidate"
              icon={<img className="pointer" src="content/images/vuesax/linear/archive-book.svg" alt="approve-liquidate" />}
            >
              Xét duyệt thanh lý
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleMaskFinished} disabled={disabledMaskFinished}>
            <ButtonIcon
              className="finished"
              icon={<img className="pointer" src="content/images/vuesax/linear/stickynote.svg" alt="finished" />}
            >
              Hoàn tất
            </ButtonIcon>
          </DropdownItem>

        </ AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
