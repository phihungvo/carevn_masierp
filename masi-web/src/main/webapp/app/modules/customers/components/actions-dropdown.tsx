import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { ICustomer } from 'app/shared/model/customer.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDispose: () => void;
  toggleTransfer: () => void;
  record: ICustomer;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleDetail, toggleUpdate, toggleDispose, toggleTransfer, record, setSelectedRecord } = props;

  const handleDetail = () => {
    setSelectedRecord(record?.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record?.id);
    toggleUpdate();
  };

  const handleDispose = () => {
    setSelectedRecord(record?.id);
    toggleDispose();
  };

  const handleTransfer = () => {
    setSelectedRecord(record?.id);
    toggleTransfer();
  };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='CUSTOMERS.VIEW'>
            <DropdownItem onClick={handleDetail}>
              <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
                Chi tiết
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='CUSTOMERS.EDIT'>
            <DropdownItem onClick={handleUpdate}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="edit" />}>
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='CUSTOMERS.EDIT'>
            <DropdownItem onClick={handleDispose}>
              <ButtonIcon className="dispose" icon={<img className="pointer" src="content/images/vuesax/linear/slash.svg" alt="dispose" />}>
                Vô hiệu
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='CUSTOMERS.EDIT'>
            <DropdownItem onClick={handleTransfer}>
              <ButtonIcon
                className="transfer"
                icon={<img className="pointer" src="content/images/vuesax/linear/arrow-right-transfer.svg" alt="transfer" />}
              >
                Chuyển giao
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
