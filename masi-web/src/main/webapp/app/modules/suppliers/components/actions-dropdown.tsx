import {
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  UncontrolledDropdown,
} from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { ISupplier } from 'app/shared/model/supplier.model';
import { useNavigate } from 'react-router';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  record: ISupplier;
  setSelectedRecord: (id: string) => void;
  toggleDetail: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    toggleUpdate,
    toggleDelete,
    record,
    setSelectedRecord,
    toggleDetail,
    toggleDispose,
    toggleActivate,
  } = props;

  const navigate = useNavigate();

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
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
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='LOGISTICS_SUPPLIERS.EDIT'>
            <DropdownItem onClick={handleDispose} disabled={disabledDeactivate}>
              <ButtonIcon
                className="deactivate"
                icon={
                  <img
                    className="pointer"
                    src="content/images/vuesax/linear/slash.svg"
                  />
                }
              >
                Vô hiệu
              </ButtonIcon>
            </DropdownItem>
            <DropdownItem onClick={handleActivate} disabled={disabledActivate}>
              <ButtonIcon
                className="activate"
                icon={
                  <img
                    className="pointer"
                    src="content/images/vuesax/linear/profile-tick.svg"
                  />
                }
              >
                Kích hoạt
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
