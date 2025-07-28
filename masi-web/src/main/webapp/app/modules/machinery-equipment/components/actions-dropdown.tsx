import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { PATH } from 'app/constants/path';
import { IItem } from 'app/shared/model/item.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useNavigate } from 'react-router';
import {
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  UncontrolledDropdown,
} from 'reactstrap';

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  record: IItem;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    toggleUpdate,
    record,
    setSelectedRecord,
    toggleDispose,
    toggleActivate,
  } = props;

  const navigate = useNavigate();

  const handleDetail = () => {
    navigate(PATH.MACHINERY_EQUIPMENT_UPDATE.replace(':id', record.id));
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDispose = () => {
    setSelectedRecord(record?.id);
    toggleDispose();
  };

  const handleActivate = () => {
    setSelectedRecord(record?.id);
    toggleActivate();
  };

  const disabledDeactivate = !record?.isActive;
  const disabledActivate = record?.isActive;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more-v2.svg" alt="more" />
      </DropdownToggle>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
