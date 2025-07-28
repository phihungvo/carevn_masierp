import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { PURCHASE_STATUS } from 'app/shared/model/enumerations/purchase.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IPurchase } from 'app/shared/model/purchase.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleRequest: () => void;
  toggleApprove: () => void;
  toggleStatus: () => void;
  record: IPurchase;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleDetail, toggleUpdate, toggleDelete, toggleRequest, toggleApprove, toggleStatus, record, setSelectedRecord } = props;

  const handleDetail = () => {
    setSelectedRecord(record?.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record?.id);
    record?.requestStatus === PURCHASE_STATUS.APPROVED || record?.requestStatus === PURCHASE_STATUS.DELIVERING
      ? toggleStatus()
      : toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record?.id);
    toggleDelete();
  };

  const handleRequest = () => {
    setSelectedRecord(record?.id);
    toggleRequest();
  };

  const handleApprove = () => {
    setSelectedRecord(record?.id);
    toggleApprove();
  };

  const disabledDelete = record?.requestStatus === PURCHASE_STATUS.APPROVED;
  const disabledPropose =
    record?.requestStatus === PURCHASE_STATUS.APPROVED ||
    record?.requestStatus === PURCHASE_STATUS.REJECTED ||
    record?.requestStatus !== PURCHASE_STATUS.NEW;
  const disabledApprove = record?.requestStatus !== PURCHASE_STATUS.WAITING_APPROVAL;

  return (
    <UncontrolledDropdown className="actions-dropdown">
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu>
          <DropdownItem onClick={handleDetail}>
            <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
              Chi tiết
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleUpdate}>
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

          <DropdownItem onClick={handleRequest} disabled={disabledPropose}>
            <ButtonIcon
              className="request"
              icon={<img className="pointer" src="content/images/vuesax/linear/message-edit.svg" alt="request" />}
            >
              Yêu cầu
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleDelete} disabled={disabledDelete}>
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
