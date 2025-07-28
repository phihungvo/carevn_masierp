import ButtonIcon from 'app/components/button-icon/button-icon';
import { ICustomer } from 'app/shared/model/customer.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleTransfer: () => void;
  record: ICustomer;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleTransfer, record, setSelectedRecord } = props;

  const handleTransfer = () => {
    setSelectedRecord(record.id);
    toggleTransfer();
  };

  return (
    <UncontrolledDropdown className="actions-dropdown">
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu>
        <DropdownItem onClick={handleTransfer}>
          <ButtonIcon
            className="transfer"
            icon={<img className="pointer" src="content/images/vuesax/linear/arrow-right-transfer.svg" alt="transfer" />}
          >
            Chuyển giao
          </ButtonIcon>
        </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
