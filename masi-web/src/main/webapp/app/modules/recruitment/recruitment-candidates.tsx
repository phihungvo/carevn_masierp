import { Typography } from 'app/components/typography/typography';
import './recruitment.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import { useModalsRecruitmentCandidates } from 'app/hooks/use-modals-recruitment';
import RecruitmentResultSuccessModals from './modals/recruitment-result-success';
import RecruitmentResultModals from './modals/recruitment-result-modals';
import { IInterviewScheduleParams } from 'app/shared/model/recruitment.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import RecruitmentCandidatesTable from './recruitment-candidates-table';
import RecruitmentHeaderCandidates from './recruitment-header-candidates';
import RecruitmentDetailModalsCandidates from './modals/recruiment-detail-modals-candidates';
import RecruitmentFilterModalsCandidates from './modals/recruiment-filter-modals-candidate';
import RecruitmentScheduleUpdateModals from './modals/recruitment-schedule-update-modals';
import RecruitmentScheduleUpdateSuccessModals from './modals/recruitment-schedule-update-success-modals';

const RecruitmentCandidates = () => {
  const [
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openUpdateCandidates, toggleUpdateCandidates },
    { openUpdateCandidatesSuccess, toggleUpdateCandidatesSuccess },
  ] = useModalsRecruitmentCandidates();

  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);

  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IInterviewScheduleParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách ứng viên</Typography>
      <Card header={<RecruitmentHeaderCandidates setSearchText={setSearchText} toggleFilter={toggleFilter} />}>
        <RecruitmentCandidatesTable
          toggleDetail={toggleDetail}
          toggleUpdate={toggleUpdate}
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          toggleUpdateCandidates={toggleUpdateCandidates}
        />
      </Card>
      <RecruitmentFilterModalsCandidates isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      <RecruitmentDetailModalsCandidates isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />

      <RecruitmentResultModals
        isOpen={openUpdate}
        toggle={toggleUpdate}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
      <RecruitmentResultSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />

      <RecruitmentScheduleUpdateModals
        isOpen={openUpdateCandidates}
        toggle={toggleUpdateCandidates}
        toggleSuccess={toggleUpdateCandidatesSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />

      <RecruitmentScheduleUpdateSuccessModals isOpen={openUpdateCandidatesSuccess} toggle={toggleUpdateCandidatesSuccess} />
    </div>
  );
};

export default RecruitmentCandidates;
