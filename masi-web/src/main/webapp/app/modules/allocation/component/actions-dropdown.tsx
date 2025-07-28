import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import AuthGuard from 'app/components/guards/auth-guard';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { RecordType } from 'app/components/table/table.d';
import { IAllocationDto } from 'app/shared/model/allocation.model';


interface IActionsDropdownProps {
  record: RecordType<IAllocationDto>
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
          <DropdownItem onClick={handleUpdate}>
            <ButtonIcon
              className="update"
              icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
            >
              Cập nhật
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem >
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>



          <DropdownItem >
            <ButtonIcon
              className="propose-approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/folder-2.svg" alt="propose-approve" />}
            >
              Gửi xử lý
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem >
            <ButtonIcon
              className="approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
            >
              Xử lý
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem>
            <ButtonIcon
              className="cancel"
              icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="cancel" />}
            >
              Hủy
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
