import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IWarehouse } from 'app/shared/model/warehouse.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { DEFAULT_PAGE } from 'app/constants/common';
import { useGetInventoriesStorageItemsAllByWarehouse } from 'app/hooks/use-inventories';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
  record: IWarehouse;
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

  const { data: itemInventories, refetch } = useGetInventoriesStorageItemsAllByWarehouse(record.id, { page: DEFAULT_PAGE,size: 1,});
  const isDisabled = () => {
    return (record?.createBy == 'SYSTEM') || itemInventories?.data?.length > 0;
  }


  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
        <AuthGuard permissionKey='WAREHOUSES.EDIT'>
          <DropdownItem onClick={handleUpdate}>
            <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}>
              Cập nhật
            </ButtonIcon>
          </DropdownItem>
          {
            isDisabled() ? <></> : (
              <DropdownItem onClick={handleDelete}>
                <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
                  Xoá
                </ButtonIcon>
              </DropdownItem>
            )}
        </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
