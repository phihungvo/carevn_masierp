import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { PATH } from 'app/constants/path';
import { ITransferAssets } from 'app/shared/model/transfer-assets.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useNavigate } from 'react-router';
import {
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  UncontrolledDropdown,
} from 'reactstrap';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';

interface IActionsDropdown {
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  record: ITransferAssets;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    record,
    setSelectedRecord,
    toggleDispose,
    toggleActivate,
  } = props;

  const navigate = useNavigate();

  const handleCancel = () => {
    setSelectedRecord(record?.id);
    toggleDispose();
  };

  const handleCompleted = () => {
    setSelectedRecord(record?.id);
    toggleActivate();
  };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more-v2.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          {record.status === 'NEW' ? (<>
            <AuthGuard permissionKey='ASSET_TRANSFER.EDIT'>
            <DropdownItem onClick={handleCompleted}>
              <ButtonIcon className="activate" icon={<img className="pointer" src="content/images/vuesax/linear/profile-tick.svg" alt="activate" />}>
                Hoàn thành
              </ButtonIcon>
            </DropdownItem>
            <DropdownItem onClick={handleCancel}>
              <ButtonV2
                id="btn-cancel"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/x-circle.svg"
                    alt="cancel"
                  />
                }
              >
                Huỷ
              </ButtonV2>
            </DropdownItem>
            </AuthGuard>
          </>) : (<></>)}
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
