import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IUniformOrder } from 'app/shared/model/uniform.model';
import { UNIFORM_ORDER_STATUS } from 'app/shared/model/enumerations/uniform.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleApprove: () => void;
  toggleCancel: () => void;
  toggleStock: () => void;
  record: IUniformOrder;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleDetail, toggleUpdate, toggleDelete, toggleApprove, toggleCancel, toggleStock, record, setSelectedRecord } = props;

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApprove();
  };

  const handleCancel = () => {
    setSelectedRecord(record.id);
    toggleCancel();
  };

  const handleStock = () => {
    setSelectedRecord(record.id);
    toggleStock();
  };

  const disabledUpdate =
    record.status === UNIFORM_ORDER_STATUS.APPROVED ||
    record.status === UNIFORM_ORDER_STATUS.REJECTED ||
    record.status === UNIFORM_ORDER_STATUS.STOCKED ||
    record.status === UNIFORM_ORDER_STATUS.PROCESSING;
  const disabledApprove =
    record.status === UNIFORM_ORDER_STATUS.APPROVED ||
    record.status === UNIFORM_ORDER_STATUS.CANCELLED ||
    record.status === UNIFORM_ORDER_STATUS.STOCKED ||
    record.status === UNIFORM_ORDER_STATUS.REJECTED ||
    record.status === UNIFORM_ORDER_STATUS.PROCESSING;
  const disabledCancel =
    record.status === UNIFORM_ORDER_STATUS.CANCELLED ||
    record.status === UNIFORM_ORDER_STATUS.APPROVED ||
    record.status === UNIFORM_ORDER_STATUS.STOCKED ||
    record.status === UNIFORM_ORDER_STATUS.PROCESSING;
  const disabledDelete =
    record.status === UNIFORM_ORDER_STATUS.STOCKED ||
    record.status === UNIFORM_ORDER_STATUS.WAITING ||
    record.status === UNIFORM_ORDER_STATUS.REJECTED ||
    record.status === UNIFORM_ORDER_STATUS.PROCESSING;
  const disabledStock =
    record.status === UNIFORM_ORDER_STATUS.STOCKED ||
    (record.status !== UNIFORM_ORDER_STATUS.PROCESSING && record.status !== UNIFORM_ORDER_STATUS.APPROVED);

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='UNIFORM_ORDERS.EDIT'>
            <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
              <ButtonIcon
                className="update"
                icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
              >
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
            <DropdownItem onClick={handleStock} disabled={disabledStock}>
              <ButtonIcon
                className="approve"
                icon={<img className="pointer" src="content/images/vuesax/linear/clipboard-tick.svg" alt="stock" />}
              >
                Nhập kho
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
            <DropdownItem onClick={handleCancel} disabled={disabledCancel}>
              <ButtonIcon className="cancel" icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="cancel" />}>
                Huỷ
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
