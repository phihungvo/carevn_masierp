import { Typography } from 'app/components/typography/typography';
import './time-sheet-violation.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import TimeSheetViolationTable from './time-sheet-violation-table';
import TimeSheetViolationHeader from './time-sheet-violation-header';
import { ModalTimeSheetViolationFilter } from './modals/time-sheet-violation-filter-modals';
import {
  ModalCreateExplanationSuccess,
  ModalTimeSheetCreateTimeSheetExplanation,
} from './modals/time-sheet-violation-create-explanation-modals';
import { ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useParams } from 'react-router';

const TimeSheetViolation = () => {
  const { id } = useParams<{ id: string }>();
  const [isOpenModalFilter, setIsOpenModalFilter] = useState(false);
  const [isOpenModalCreateTimeSheetExplanation, setIsOpenModalCreateTimeSheetExplanation] = useState(false);
  const [isOpenCreateSuccess, setIsOpenCreateSuccess] = useState(false);
  const [filter, setFilter] = useState<ITimeKeepingViolationsParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    fromDate: '',
    toDate: '',
    type: [],
  });

  const toggleModalFilter = () => {
    setIsOpenModalFilter(prev => !prev);
  };

  const toggleModalCreateTimeSheetExplanation = () => {
    setIsOpenModalCreateTimeSheetExplanation(prev => !prev);
  };

  const toggleCreateSuccess = () => {
    setIsOpenCreateSuccess(prev => !prev);
  };

  return (
    <div className='page_container'>
      {id ? <Typography level={4}>Chi tiết vi phạm giải trình</Typography> : <Typography level={4}>Danh sách vi phạm chấm công</Typography>}

      <Card
        header={
          <TimeSheetViolationHeader
            toggleModalFilter={toggleModalFilter}
            toggleModalCreateTimeSheetExplanation={toggleModalCreateTimeSheetExplanation}
            filter={filter}
            setFilter={setFilter}
          />
        }
      >
        <TimeSheetViolationTable
          filter={filter}
          setFilter={setFilter}
        />
      </Card>

      <ModalTimeSheetViolationFilter isOpen={isOpenModalFilter} toggle={toggleModalFilter} setFilter={setFilter} />

      <ModalTimeSheetCreateTimeSheetExplanation
        isOpen={isOpenModalCreateTimeSheetExplanation}
        toggle={toggleModalCreateTimeSheetExplanation}
        filterParent={filter}
        toggleSuccess={toggleCreateSuccess}
      />

      <ModalCreateExplanationSuccess isOpen={isOpenCreateSuccess} toggle={toggleCreateSuccess} />
    </div>
  );
};

export default TimeSheetViolation;
