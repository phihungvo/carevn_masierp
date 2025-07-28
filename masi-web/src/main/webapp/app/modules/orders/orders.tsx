import React, { useState } from 'react';

import './orders.scss';
import OrdersTable from './orders-table';
import OrdersHeader from './orders-header';
import Card from 'app/components/card/card';
import OrdersFilterModals from './modals/orders-filter-modals';
import OrdersDetailModals from './modals/orders-detail-modals';
import OrdersCreateModals from './modals/orders-create-modals';
import OrdersUpdateModals from './modals/orders-update-modals';
import OrdersDeleteModals from './modals/orders-delete-modals';
import OrdersCancelModals from './modals/orders-cancel-modals';
import OrdersRejectModals from './modals/orders-reject-modals';
import OrdersProposeModals from './modals/orders-propose-modals';
import OrdersApproveModals from './modals/orders-approve-modals';
import OrdersApproveSignModals from './modals/orders-approve-sign-modals';
import OrdersDeleteSuccessModals from './modals/orders-delete-success-modals';
import OrdersRejectSuccessModals from './modals/orders-reject-success-modals';
import OrdersCancelSuccessModals from './modals/orders-cancel-success-modals';
import OrdersCreateSuccessModals from './modals/orders-create-success-modals';
import OrdersUpdateSuccessModals from './modals/orders-update-success-modals';
import OrdersProposeSuccessModals from './modals/orders-propose-success-modals';
import OrdersApproveSuccessModals from './modals/orders-approve-success-modals';
import { useModalsOrders } from 'app/hooks/use-modals-orders';
import { Typography } from 'app/components/typography/typography';
import { IOrder, IOrderParams } from 'app/shared/model/order.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { DownloadSuccessfulModals } from 'app/components/modals-download-successful/download-successful-modals';

const Orders = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openPropose, togglePropose },
    { openProposeSuccess, toggleProposeSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
  ] = useModalsOrders();

  const [openDownloadSuccessful, setOpenDownloadSuccessful] =
    useState<boolean>(false);
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<IOrder[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IOrderParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    searchString: '',
  });

  const toggleDownloadSuccessful = () =>
    setOpenDownloadSuccessful(prev => !prev);

  return (
    <div className="page_container">
      <Typography level={4}>Đơn đặt hàng</Typography>

      <Card
        header={
          <OrdersHeader
            setSearchText={setSearchText}
            toggleFilter={toggleFilter}
            toggleCreate={toggleCreate}
            toggleDelete={toggleDelete}
            toggleUpdate={toggleUpdate}
            toggleCancel={toggleCancel}
            selectedRows={selectedRows}
          />
        }
      >
        <OrdersTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          togglePropose={togglePropose}
          toggleApprove={toggleApprove}
          toggleCancel={toggleCancel}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          selectedRowKeys={selectedRowKeys}
          setSelectedRowKeys={setSelectedRowKeys}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
          toggleDownloadSuccessful={toggleDownloadSuccessful}
        />
      </Card>

      <OrdersFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />
      <OrdersDetailModals
        isOpen={openDetail}
        toggle={toggleDetail}
        selectedRecord={selectedRecord}
      />
      <OrdersCreateModals
        isOpen={openCreate}
        toggle={toggleCreate}
        toggleSuccess={toggleCreateSuccess}
      />
      <OrdersCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />
      <OrdersUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord || selectedRowKeys?.[0]}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <OrdersUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
      <OrdersDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <OrdersDeleteSuccessModals
        isOpen={openDeleteSuccess}
        toggle={toggleDeleteSuccess}
      />
      <OrdersProposeModals
        isOpen={openPropose}
        toggle={togglePropose}
        toggleSuccess={toggleProposeSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <OrdersProposeSuccessModals
        isOpen={openProposeSuccess}
        toggle={toggleProposeSuccess}
      />
      <OrdersApproveModals
        isOpen={openApprove}
        toggle={toggleApprove}
        toggleApprove={toggleApproveSign}
        toggleReject={toggleReject}
      />
      <OrdersApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <OrdersApproveSuccessModals
        isOpen={openApproveSuccess}
        toggle={toggleApproveSuccess}
      />
      <OrdersRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <OrdersRejectSuccessModals
        isOpen={openRejectSuccess}
        toggle={toggleRejectSuccess}
      />
      <OrdersCancelModals
        isOpen={openCancel}
        toggle={toggleCancel}
        toggleSuccess={toggleCancelSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <OrdersCancelSuccessModals
        isOpen={openCancelSuccess}
        toggle={toggleCancelSuccess}
      />

      <DownloadSuccessfulModals
        title="đơn hàng"
        isOpen={openDownloadSuccessful}
        toggleSuccess={toggleDownloadSuccessful}
      />
    </div>
  );
};

export default Orders;
