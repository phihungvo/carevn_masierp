import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IUomGroup } from 'app/shared/model/uom.model';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
  record: IUomGroup;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleUpdate, toggleDelete, toggleDetail, record, setSelectedRecord } = props;

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

  return (
    <UncontrolledDropdown className="actions-dropdown">
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu>
        <DropdownItem onClick={handleDetail}>
          <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
            Chi tiết
          </ButtonIcon>
        </DropdownItem>

        <DropdownItem onClick={handleUpdate}>
          <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}>
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
