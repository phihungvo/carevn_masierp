import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { IContract } from 'app/shared/model/contract.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleRestore: () => void;
  record: IContract;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleRestore, record, setSelectedRecord } = props;

  const handleRestore = () => {
    setSelectedRecord(record.id);
    toggleRestore();
  };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <DropdownItem onClick={handleRestore}>
            <ButtonIcon
              className="restore"
              icon={<img className="pointer" src="content/images/vuesax/linear/rotate-left.svg" alt="restore" />}
            >
              Phục hồi
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
