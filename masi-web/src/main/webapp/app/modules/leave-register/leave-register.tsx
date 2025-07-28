import React, { useState } from 'react';

import './leave-register.scss';
import Card from 'app/components/card/card';
import LeaveRegisterTable from './leave-register-table';
import LeaveRegisterHeader from './leave-register-header';
import BtnDownload from 'app/components/btn-download/btn-download';
import LeaveRegisterFilterModal from './modals/leave-register-filter-modal';
import LeaveRegisterRejectModals from './modals/leave-register-reject-modal';
import LeaveRegisterDeleteModals from './modals/leave-register-delete-modals';
import LeaveRegisterDetailModals from './modals/leave-register-detail-modals';
import LeaveRegisterCancelModals from './modals/leave-register-cancel-modals';
import LeaveRegisterProposeModals from './modals/leave-register-propose-modal';
import LeaveRegisterCreateSuccessModals from './modals/leave-register-create-success-modals';
import LeaveRegisterUpdateSuccessModals from './modals/leave-register-update-success-modals';
import LeaveRegisterRejectSuccessModals from './modals/leave-register-reject-success-modals';
import LeaveRegisterDeleteSuccessModals from './modals/leave-register-delete-success-modals';
import LeaveRegisterCancelSuccessModals from './modals/leave-register-cancel-success-modals';
import LeaveRegisterApproveSuccessModals from './modals/leave-register-approve-success-modals';
import LeaveRegisterProposeSuccessModals from './modals/leave-register-propose-success-modals';
import LeaveRegisterApproveModals from 'app/modules/leave-register/modals/leave-register-approve-modals';
import LeaveRegisterApproveSignModals from 'app/modules/leave-register/modals/leave-register-approve-sign-modal';
import { useBtnDownload } from 'app/hooks/use-btn-download';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsLeaveRegister } from 'app/hooks/use-modals-leave-register';
import { LeaveRegisterUpdateModal } from './modals/leave-register-update-modals';
import { LeaveRegisterCreateModal } from './modals/leave-register-create-modals';
import { ILeaveRegime, ILeaveRegimeParams } from 'app/shared/model/leave-regime.model';
import { DownloadSuccessfulModals } from 'app/components/modals-download-successful/download-successful-modals';

const LeaveRegister = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openApprove, toggleApprove },
    { openApproveSuccess, toggleApproveSuccess },
    { openCancel, toggleCancel },
    { openCancelSuccess, toggleCancelSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openPropose, togglePropose },
    { openProposeSuccess, toggleProposeSuccess },
    { openDetail, toggleDetail },
    { openApproveSign, toggleApproveSign },
  ] = useModalsLeaveRegister();

  const [{ openModalDownload, toggleModalDownload }, { openDownloadSuccessful, toggleDownloadSuccessful }] = useBtnDownload();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ILeaveRegime[]>([]);
  const [filter, setFilter] = useState<ILeaveRegimeParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Đăng ký nghỉ chế độ</Typography>

      <Card
        header={<LeaveRegisterHeader toggleCreate={toggleCreate} toggleFilter={toggleFilter} toggleModalDownload={toggleModalDownload} />}
      >
        <LeaveRegisterTable
          toggleApproveSign={toggleApproveSign}
          toggleCancel={toggleCancel}
          toggleDelete={toggleDelete}
          toggleReject={toggleReject}
          toggleUpdate={toggleUpdate}
          togglePropose={togglePropose}
          toggleDetail={toggleDetail}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          selectedRowKeys={selectedRowKeys}
          setSelectedRowKeys={setSelectedRowKeys}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
        />
      </Card>

      <LeaveRegisterFilterModal isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <LeaveRegisterDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <LeaveRegisterCreateModal isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <LeaveRegisterCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <LeaveRegisterUpdateModal
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <LeaveRegisterUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <LeaveRegisterProposeModals
        isOpen={openPropose}
        toggle={togglePropose}
        toggleSuccess={toggleProposeSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <LeaveRegisterProposeSuccessModals isOpen={openProposeSuccess} toggle={toggleProposeSuccess} />

      <LeaveRegisterApproveSuccessModals isOpen={openApproveSuccess} toggle={toggleApproveSuccess} />
      <LeaveRegisterRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <LeaveRegisterRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
      <LeaveRegisterDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <LeaveRegisterDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <LeaveRegisterCancelModals
        isOpen={openCancel}
        toggle={toggleCancel}
        toggleSuccess={toggleCancelSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <LeaveRegisterCancelSuccessModals isOpen={openCancelSuccess} toggle={toggleCancelSuccess} />
      <LeaveRegisterApproveModals
        isOpen={openApprove}
        toggle={toggleProposeSuccess}
        toggleApprove={toggleApprove}
        toggleReject={toggleRejectSuccess}
      />
      <LeaveRegisterApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <BtnDownload isOpen={openModalDownload} toggle={toggleModalDownload} toggleDownloadSuccessful={toggleDownloadSuccessful} />

      <DownloadSuccessfulModals title="theo dõi chế độ nghỉ" isOpen={openDownloadSuccessful} toggleSuccess={toggleDownloadSuccessful} />
    </div>
  );
};

export default LeaveRegister;
