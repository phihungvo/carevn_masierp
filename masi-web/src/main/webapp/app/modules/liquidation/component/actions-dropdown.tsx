import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import AuthGuard from 'app/components/guards/auth-guard';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { RecordType } from 'app/components/table/table.d';
import { ILiquidationDto } from 'app/shared/model/liquidation.model';


interface IActionsDropdownProps {
  record: RecordType<ILiquidationDto>
  toggleUpdate: () => void;
  setSelectedRow: React.Dispatch<React.SetStateAction<string>>;
  toggleDetail: () => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const {
    record,
    toggleUpdate,
    setSelectedRow,
    toggleDetail
  } = props;

  const handleDetail = () => {
    setSelectedRow(record?.id);
    toggleDetail();
  }

  const handleUpdate = () => {
    toggleUpdate();
    setSelectedRow(record?.id);
  }

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <DropdownItem >
            <ButtonIcon
              className="propose-approve"
              icon={<img className="pointer" src="content/images/vuesax/bulk/printer.svg" alt="print" />}
            >
              In
            </ButtonIcon>
          </DropdownItem>
          <DropdownItem >
            <ButtonIcon
              className="approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
            >
              Duyệt
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem>
            <ButtonIcon
              className="cancel"
              icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="reject" />}
            >
              Từ chối
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
