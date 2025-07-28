import './uniform-orders.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsUniformOrders } from 'app/hooks/use-modals-uniform';
import UniformOrdersHeader from './uniform-orders-header';
import UniformOrdersTable from './uniform-orders-table';
import UniformOrdersCreateModals from './modals/uniform-orders-create-modals';
import UniformOrdersFilterModals from './modals/uniform-orders-filter-modals';
import UniformOrdersDetailModals from './modals/uniform-orders-detail-modals';
import UniformOrdersCreateSuccessModals from './modals/uniform-orders-create-success-modals';
import UniformOrdersUpdateModals from './modals/uniform-orders-update-modals';
import UniformOrdersUpdateSuccessModals from './modals/uniform-orders-update-success-modals';
import UniformOrdersDeleteModals from './modals/uniform-orders-delete-modals';
import UniformOrdersDeleteSuccessModals from './modals/uniform-orders-delete-success-modals';
import UniformOrdersApproveModals from './modals/uniform-orders-approve-modals';
import UniformOrdersApproveSignModals from './modals/uniform-orders-approve-sign-modals';
import UniformOrdersApproveSuccessModals from './modals/uniform-orders-approve-success-modals';
import UniformOrdersRejectModals from './modals/uniform-orders-reject-modals';
import UniformOrdersRejectSuccessModals from './modals/uniform-orders-reject-success-modals';
import UniformOrdersCancelModals from './modals/uniform-orders-cancel-modals';
import UniformOrdersCancelSuccessModals from './modals/uniform-orders-cancel-success-modals';
import UniformOrdersStockModals from './modals/uniform-orders-stock-modals';
import UniformOrdersStockSuccessModals from './modals/uniform-orders-stock-success-modals';
import { IUniformOrderParams } from 'app/shared/model/uniform.model';

const UniformOrders = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSuccess, toggleApproveSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openStock, toggleStock },
    { openStockSuccess, toggleStockSuccess },
  ] = useModalsUniformOrders();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IUniformOrderParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    name: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Đơn mua đồng phục</Typography>

      <Card header={<UniformOrdersHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
        <UniformOrdersTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleDelete={toggleDelete}
          toggleApprove={toggleApprove}
          toggleCancel={toggleCancel}
          toggleStock={toggleStock}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
        />
      </Card>

      <UniformOrdersFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <UniformOrdersDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <UniformOrdersCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UniformOrdersCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />

      <UniformOrdersUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord || selectedRowKeys?.[0]}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <UniformOrdersUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <UniformOrdersDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <UniformOrdersDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <UniformOrdersApproveModals
        isOpen={openApprove}
        toggle={toggleApprove}
        toggleApprove={toggleApproveSign}
        toggleReject={toggleReject}
      />
      <UniformOrdersApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformOrdersApproveSuccessModals isOpen={openApproveSuccess} toggle={toggleApproveSuccess} />
      <UniformOrdersRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformOrdersRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
      <UniformOrdersCancelModals
        isOpen={openCancel}
        toggle={toggleCancel}
        toggleSuccess={toggleCancelSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />
      <UniformOrdersCancelSuccessModals isOpen={openCancelSuccess} toggle={toggleCancelSuccess} />
      <UniformOrdersStockModals
        isOpen={openStock}
        toggle={toggleStock}
        toggleSuccess={toggleStockSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <UniformOrdersStockSuccessModals isOpen={openStockSuccess} toggle={toggleStockSuccess} />
    </div>
  );
};

export default UniformOrders;
