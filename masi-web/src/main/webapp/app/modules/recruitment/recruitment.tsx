import { Typography } from 'app/components/typography/typography';
import './recruitment.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import RecruitmentHeader from './recruitment-header';
import RecruitmentTable from './recruitment-table';
import { useModalsRecruitment } from 'app/hooks/use-modals-recruitment';
import RecruitmentDetailModals from './modals/recruiment-detail-modals';
import RecruitmentFilterModals from './modals/recruiment-filter-modals';
import RecruitmentCreateModals from './modals/recruitment-create-modals';
import RecruitmentCreateSuccessModals from './modals/recruitment-create-success-modals';
import RecruitmentUpdateModals from './modals/recruitment-update-modals';
import RecruitmentUpdateSuccessModals from './modals/recruitment-update-success-modals';
import RecruitmentDeleteModals from './modals/recruitment-delete-modals';
import RecruitmentDeleteSuccessModals from './modals/recruitment-delete-success-modals';
import RecruitmentApproveModals from './modals/recruitment-approve-modals';
import RecruitmentApproveSignModals from './modals/recruitment-approve-sign-modals';
import RecruitmentApproveSuccessModals from './modals/recruitment-approve-sign-success';
import RecruitmentRejectModals from './modals/recruitment-reject-modals';
import RecruitmentRejectSuccessModals from './modals/recruitment-reject-success';
import RecruitmentScheduleModals from './modals/recruitment-schedule-modals';
import RecruitmentScheduleSuccessModals from './modals/recruitment-schedule-success';
import { IRecruitment, IRecruitmentParams } from 'app/shared/model/recruitment.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import RecruitmentRenewModals from 'app/modules/recruitment/modals/recruitment-renew-modals';
import RecruitmentRenewSuccessModals from 'app/modules/recruitment/modals/recruitment-renew-success-modals';
import RecruitmentHistoryModals from 'app/modules/recruitment/modals/recruitment-history-modals';

const Recruitment = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openDetail, toggleDetail },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openApprove, toggleApprove },
    { openApproveSign, toggleApproveSign },
    { openApproveSignSuccess, toggleApproveSignSuccess },
    { openReject, toggleReject },
    { openRejectSuccess, toggleRejectSuccess },
    { openSchedule, toggleSchedule },
    { openScheduleSuccess, toggleScheduleSuccess },
    { openRenew, toggleRenew },
    { openRenewSuccess, toggleRenewSuccess },
    { openHistory, toggleHistory },
  ] = useModalsRecruitment();

  const [record, setRecord] = useState<IRecruitment>();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);

  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IRecruitmentParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Tuyển dụng</Typography>
      <Card header={<RecruitmentHeader setSearchText={setSearchText} toggleCreate={toggleCreate} toggleFilter={toggleFilter} />}>
        <RecruitmentTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          toggleApprove={toggleApprove}
          toggleSchedule={toggleSchedule}
          toggleDelete={toggleDelete}
          toggleReject={toggleReject}
          toggleRenew={toggleRenew}
          toggleHistory={toggleHistory}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          setRecord={setRecord}
        />
      </Card>
      <RecruitmentFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <RecruitmentDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
      <RecruitmentCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <RecruitmentCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <RecruitmentUpdateModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />
      <RecruitmentDeleteModals
        isOpen={openDelete}
        toggle={toggleDelete}
        toggleSuccess={toggleDeleteSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />
      <RecruitmentApproveModals isOpen={openApprove} toggle={toggleApprove} toggleApprove={toggleApproveSign} toggleReject={toggleReject} />
      <RecruitmentApproveSignModals
        isOpen={openApproveSign}
        toggle={toggleApproveSign}
        toggleSuccess={toggleApproveSignSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentApproveSuccessModals isOpen={openApproveSignSuccess} toggle={toggleApproveSignSuccess} />
      <RecruitmentRejectModals
        isOpen={openReject}
        toggle={toggleReject}
        toggleSuccess={toggleRejectSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />
      <RecruitmentScheduleModals
        isOpen={openSchedule}
        toggle={toggleSchedule}
        toggleSuccess={toggleScheduleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentScheduleSuccessModals isOpen={openScheduleSuccess} toggle={toggleScheduleSuccess} />

      <RecruitmentRenewModals
        isOpen={openRenew}
        toggle={toggleRenew}
        toggleSuccess={toggleRenewSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentRenewSuccessModals isOpen={openRenewSuccess} toggle={toggleRenewSuccess} />

      <RecruitmentHistoryModals isOpen={openHistory} toggle={toggleHistory} selectedRecord={selectedRecord} />
    </div>
  );
};

export default Recruitment;
