import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import useOrders from 'app/hooks/use-orders';
import AuthGuard from 'app/components/guards/auth-guard';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { useAppSelector } from 'app/config/store';
import { IOrder } from 'app/shared/model/order.model';
import { useDownloadPdf } from 'app/hooks/use-download';
import { orderEndpoints } from 'app/constants/endpoints';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

const { useGetOrderExport } = useOrders;

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleCancel: () => void;
  record: IOrder;
  setSelectedRecord: (id: string) => void;
  toggleDownloadSuccessful: () => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    togglePropose,
    toggleApprove,
    toggleCancel,
    record,
    setSelectedRecord,
    toggleDownloadSuccessful,
  } = props;

  const { trigger, data } = useGetOrderExport(record?.id, false, toggleDownloadSuccessful);
  const account = useAppSelector(state => state.authentication.account);

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  // const handleUpdate = () => {
  //   setSelectedRecord(record.id);
  //   toggleUpdate();
  // };

  // const handleDelete = () => {
  //   setSelectedRecord(record.id);
  //   toggleDelete();
  // };

  const handlePropose = () => {
    setSelectedRecord(record.id);
    togglePropose();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApprove();
  };

  const handlePrint = () => {
    trigger();
  };

  const handleDownload = () => {
    window.open(orderEndpoints.getOrderExport(record.id, false), '_blank');
  };

  const handleCancel = () => {
    setSelectedRecord(record.id);
    toggleCancel();
  };

  useDownloadPdf(data?.data, 'order', 'pdf');

  // const disabledUpdate = record?.status === ORDER_STATUS.APPROVED || record?.status === ORDER_STATUS.REJECTED;
  // const disabledDelete = record?.status === ORDER_STATUS.APPROVED || record?.status === ORDER_STATUS.REJECTED;
  const disabledCancel =
    record?.status === ORDER_STATUS.APPROVED || record?.status === ORDER_STATUS.REJECTED || record?.status === ORDER_STATUS.CANCELLED;
  const disabledPropose = record?.status !== ORDER_STATUS.NEW;
  const disabledApprove = record?.status !== ORDER_STATUS.WAITING_APPROVAL || record?.orderReviews?.find(item => item?.employeeId === account?.id)?.status === ORDER_STATUS.APPROVED;
  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
        <DropdownItem onClick={handleDetail}>
          <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
            Chi tiết
          </ButtonIcon>
        </DropdownItem>
        <AuthGuard permissionKey='ORDERS.EDIT'>
          <DropdownItem onClick={handleApprove} disabled={disabledApprove}>
            <ButtonIcon
              className="approve"
              icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
            >
              Xét duyệt
            </ButtonIcon>
          </DropdownItem>
        </ AuthGuard>
        <AuthGuard permissionKey='ORDERS.CREATE'>
          <DropdownItem onClick={handlePropose} disabled={disabledPropose}>
            <ButtonIcon
              className="propose"
              icon={<img className="pointer" src="content/images/vuesax/linear/directbox-notif.svg" alt="propose" />}
            >
              Yêu cầu xét duyệt
            </ButtonIcon>
          </DropdownItem>
        </ AuthGuard>
        <AuthGuard permissionKey='ORDERS.EXPORT'>
          <DropdownItem onClick={handlePrint}>
            <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/direct-inbox.svg" />}>
              Xuất đơn hàng
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleDownload}>
            <ButtonIcon className="print" icon={<img className="pointer" src="content/images/vuesax/linear/receive-square.svg" />}>
              In đơn hàng
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
        <AuthGuard permissionKey='ORDERS.EDIT'>
          <DropdownItem onClick={handleCancel} disabled={disabledCancel}>
            <ButtonIcon className="cancel" icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="cancel" />}>
              Huỷ
            </ButtonIcon>
          </DropdownItem>
        </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
