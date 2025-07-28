import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { IIncomingInvoice } from 'app/shared/model/incoming-invoice.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  record: IIncomingInvoice;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleDetail, toggleUpdate, toggleDelete, togglePropose, toggleApprove, record, setSelectedRecord } = props;

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

  // const handlePropose = () => {
  //   setSelectedRecord(record.id);
  //   togglePropose();
  // };

  // const handleApprove = () => {
  //   setSelectedRecord(record.id);
  //   toggleApprove();
  // };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <DropdownItem onClick={handleDetail}>
            <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
              Chi tiết
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleUpdate} >
            <ButtonIcon
              className="update"
              icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
            >
              Cập nhật
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleDelete}>
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
