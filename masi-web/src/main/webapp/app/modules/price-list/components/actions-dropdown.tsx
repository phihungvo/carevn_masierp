import React from 'react';
import { useNavigate } from 'react-router';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { useAppSelector } from 'app/config/store';
import { IQuotation } from 'app/shared/model/quotation.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { checkCompanyDetailPath, checkCompanyUpdatePath } from '../util/check-company-path';

interface IActionsDropdownProps {
  toggleDelete: () => void;
  toggleInApprove: () => void;
  toggleCancel: () => void;
  toggleCusSend: () => void;
  toggleApproveInternal: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
  record: IQuotation;
  setSelectedRecord: (record: string | null) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const {
    toggleDelete,
    toggleInApprove,
    toggleCancel,
    toggleCusSend,
    toggleApproveInternal,
    toggleApprove,
    toggleReject,
    record,
    setSelectedRecord,
  } = props;

  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);

  const handleDetail = () => {
    navigate(`${checkCompanyDetailPath(account?.company?.normalizedName, record?.id)}`);
  };

  const handleUpdate = () => {
    navigate(`${checkCompanyUpdatePath(account?.company?.normalizedName, record?.id)}`);
  };

  const handleDelete = () => {
    setSelectedRecord(record?.id);
    toggleDelete();
  };

  const handleInApprove = () => {
    setSelectedRecord(record?.id);
    toggleInApprove();
  };

  // const handleCancel = () => {
  //   setSelectedRecord(record?.id);
  //   toggleCancel();
  // };

  const handleCusSend = () => {
    setSelectedRecord(record?.id);
    toggleCusSend();
  };

  const handleApprove = () => {
    setSelectedRecord(record?.id);
    record?.status === QUOTATION_STATUS.WAITING_APPROVAL ? toggleApproveInternal() : toggleApprove();
  };

  const handleReject = () => {
    setSelectedRecord(record?.id);
    toggleReject();
  };

  const disabledUpdate = record?.status !== QUOTATION_STATUS.NEW && record?.status !== QUOTATION_STATUS.NEED_UPDATE && record?.status !== QUOTATION_STATUS.REJECTED;
  const disabledDelete = record?.status === QUOTATION_STATUS.CUSTOMER_APPROVED || record?.status === QUOTATION_STATUS.REJECTED;
  // const disabledCancel =
  //   record?.status === QUOTATION_STATUS.SENT ||
  //   record?.status === QUOTATION_STATUS.CUSTOMER_APPROVED ||
  //   record?.status === QUOTATION_STATUS.REJECTED ||
  //   record?.status === QUOTATION_STATUS.CANCELLED ||
  //   record?.status === QUOTATION_STATUS.APPROVED;
  const disabledCusSend = record?.status === QUOTATION_STATUS.SENT || record?.status !== QUOTATION_STATUS.APPROVED;
  const disabledInApprove = !(
    record?.status === QUOTATION_STATUS.NEED_UPDATE ||
    record?.status === QUOTATION_STATUS.REJECTED ||
    record?.status === QUOTATION_STATUS.NEW
  );
  const disabledApprove = !(record?.status === QUOTATION_STATUS.SENT || record?.status === QUOTATION_STATUS.WAITING_APPROVAL);
  const disabledReject =
    record?.status === QUOTATION_STATUS.REJECTED ||
    !(record?.status === QUOTATION_STATUS.WAITING_APPROVAL || record?.status === QUOTATION_STATUS.SENT);

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
        <DropdownItem onClick={handleDetail}>
          <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="update" />}>
            Chi tiết
          </ButtonIcon>
        </DropdownItem>
        <AuthGuard permissionKey='PRICE_LIST.EDIT'>
          <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
            <ButtonIcon
              className="update"
              icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
            >
              Cập nhật
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleApprove} disabled={disabledApprove}>
            <ButtonIcon
              className="approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
            >
              Xét duyệt
            </ButtonIcon>
          </DropdownItem>
          </AuthGuard>
          <AuthGuard permissionKey='PRICE_LIST.CREATE'>
          <DropdownItem onClick={handleInApprove} disabled={disabledInApprove}>
            <ButtonIcon
              className="pending"
              icon={<img className="pointer" src="content/images/vuesax/linear/directbox-notif.svg" alt="pending" />}
            >
              Gửi duyệt
            </ButtonIcon>
          </DropdownItem>

          </AuthGuard>
          <AuthGuard permissionKey='PRICE_LIST.EDIT'>
          <DropdownItem onClick={handleReject} disabled={disabledReject}>
            <ButtonIcon
              className="reject"
              icon={<img className="pointer" src="content/images/vuesax/linear/folder-cross.svg" alt="reject" />}
            >
              Từ chối
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleCusSend} disabled={disabledCusSend}>
            <ButtonIcon
              className="sent"
              icon={<img className="pointer" src="content/images/vuesax/linear/sms-tracking.svg" alt="sent" />}

            >
              Gửi KH
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleDelete} disabled={disabledDelete}>
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>

        </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
