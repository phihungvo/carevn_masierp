import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import { IOrder } from 'app/shared/model/order.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';

interface IOrdersHeader {
  setSearchText: (value: string) => void;
  toggleFilter: () => void;
  toggleCreate: () => void;
  toggleDelete: () => void;
  toggleUpdate: () => void;
  toggleCancel: () => void;
  selectedRows: IOrder[];
}

const OrdersHeader = (props: IOrdersHeader) => {
  const { setSearchText, toggleFilter, toggleCreate, toggleDelete, toggleUpdate, toggleCancel, selectedRows } = props;

  const disabledDelete =
    selectedRows?.length === 0 || selectedRows?.some(row => row?.status === ORDER_STATUS.APPROVED || row?.status === ORDER_STATUS.REJECTED);
  const disabledCancel =
    selectedRows?.length === 0 ||
    selectedRows?.some(
      row => row?.status === ORDER_STATUS.APPROVED || row?.status === ORDER_STATUS.REJECTED || row?.status === ORDER_STATUS.CANCELLED,
    );
  const disabledUpdate =
    selectedRows?.length !== 1 || selectedRows?.some(row => row?.status === ORDER_STATUS.APPROVED || row?.status === ORDER_STATUS.REJECTED);

  return (
    <div className="card-header-container">
      <div className="card-header-extra">
        <InputSearch
          className="card-header-extra"
          onChange={e => {
            setSearchText(e.target.value);
          }}
        />
      </div>
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
        <AuthGuard permissionKey='ORDERS.CREATE'>
          <Button color="primary" onClick={toggleCreate}>
            Tạo mới
          </Button>
          </AuthGuard>
      </div>
    </div>
  );
};

export default OrdersHeader;
