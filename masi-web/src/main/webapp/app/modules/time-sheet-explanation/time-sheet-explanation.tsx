import { Typography } from 'app/components/typography/typography';
import './time-sheet-explanation.scss';
import React, { useState } from 'react';
import Card from 'app/components/card/card';
import { TimeSheetExplanationHeader } from './time-sheet-explanation-components';
import { useModalsTimeSheetExplanation } from 'app/hooks/use-modals-explanation';
import { TimeSheetExplanationTable } from './time-sheet-explanation-table';
import { TimeSheetExplanationFilterModal } from './modals/time-sheet-explanation-filter-modals';
import { AcceptRequestModal, SuccessAcceptRequestModal } from './modals/time-sheet-explanation-accept-modals';
import { RejectRequestModal, SuccessRejectRequestModal } from './modals/time-sheet-explanation-reject-modals';
import { ErrorUpdateRequestModal, UpdateRequestModal, UpdateSuccessRequestModal } from './modals/time-sheet-explanation-update-modals';
import { CancelRequestModal, CancelRequestSuccessModal, ErrorCancelRequestModal } from './modals/time-sheet-explanation-cancel-modals';
import { DeleteRequestModal, DeleteRequestSuccessModal, ErrorDeleteRequestModal } from './modals/time-sheet-explanation-delete-modals';
import { ITimeKeepingExplanation, ITimeKeepingExplanationParams } from 'app/shared/model/time-keeping-explanation.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';

const TimeSheetExplanation = () => {
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ITimeKeepingExplanation[]>([]);
  const [filter, setFilter] = useState<ITimeKeepingExplanationParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    statuses: [],
    types: [],
  });
  const [searchText, setSearchText] = useState<string>('');

  const [
    { openFilter, toggleFilter },
    { openAcceptReq, toggleAcceptReq },
    { openAcceptSuccessReq, toggleAcceptSuccessReq },
    { openRejectReq, toggleRejectReq },
    { openRejectSuccessReq, toggleRejectSuccessReq },
    { openUpdateReq, toggleUpdateReq },
    { openErrorUpdateReq, toggleErrorUpdateReq },
    { openUpdateSuccessReq, toggleUpdateSuccessReq },
    { openCancelReq, toggleCancelReq },
    { openErrorCancelReq, toggleErrorCancelReq },
    { openSuccessCancelReq, toggleSuccessCancelReq },
    { openDeleteReq, toggleDeleteReq },
    { openErrorDeleteReq, toggleErrorDeleteReq },
    { openSuccessDeleteReq, toggleSuccessDeleteReq },
  ] = useModalsTimeSheetExplanation();

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách giải trình</Typography>
      <Card
        header={
          <TimeSheetExplanationHeader
            toggleFilter={toggleFilter}
            toggleAcceptReq={toggleAcceptReq}
            toggleRejectReq={toggleRejectReq}
            toggleCancelReq={toggleCancelReq}
            toggleDeleteReq={toggleDeleteReq}
            selectedRows={selectedRows}
            setSearchText={setSearchText}
          />
        }
      >
        <TimeSheetExplanationTable
          toggleUpdateReq={toggleUpdateReq}
          toggleAcceptReq={toggleAcceptReq}
          toggleRejectReq={toggleRejectReq}
          toggleCancelReq={toggleCancelReq}
          toggleDeleteReq={toggleDeleteReq}
          setSelectedRecord={setSelectedRecord}
          setSelectedRowKeys={setSelectedRowKeys}
          selectedRowKeys={selectedRowKeys}
          setSelectedRows={setSelectedRows}
          filter={filter}
          setFilter={setFilter}
          searchText={searchText}
        />
      </Card>

      {/* MODAL FILTER */}
      <TimeSheetExplanationFilterModal isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />

      {/* MODAL ACCEPT REQUEST */}
      <AcceptRequestModal
        isOpen={openAcceptReq}
        toggle={toggleAcceptReq}
        toggleSuccess={toggleAcceptSuccessReq}
        selectedRows={selectedRows}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL ACCEPT REQ SUCCESS */}
      <SuccessAcceptRequestModal isOpen={openAcceptSuccessReq} toggle={toggleAcceptSuccessReq} />

      {/* MODAL REJECT REQUEST */}
      <RejectRequestModal
        isOpen={openRejectReq}
        toggle={toggleRejectReq}
        toggleSuccess={toggleRejectSuccessReq}
        selectedRows={selectedRows}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL REJECT REQ SUCCESS */}
      <SuccessRejectRequestModal isOpen={openRejectSuccessReq} toggle={toggleRejectSuccessReq} />

      {/* MODAL UPDATE REQUEST */}
      <UpdateRequestModal
        isOpen={openUpdateReq}
        toggle={toggleUpdateReq}
        toggleSuccess={toggleUpdateSuccessReq}
        selectedRecord={selectedRecord}
        toggleError={toggleErrorUpdateReq}
        setSelectedRecord={setSelectedRecord}
      />

      {/* MODAL ERROR UPDATE REQUEST */}
      <ErrorUpdateRequestModal isOpen={openErrorUpdateReq} toggle={toggleErrorUpdateReq} />

      {/* MODAL SUCCESS UPDATE REQUEST */}
      <UpdateSuccessRequestModal isOpen={openUpdateSuccessReq} toggle={toggleUpdateSuccessReq} />

      {/* MODAL CANCEL REQUEST */}
      <CancelRequestModal
        isOpen={openCancelReq}
        toggle={toggleCancelReq}
        toggleError={toggleErrorCancelReq}
        toggleSuccess={toggleSuccessCancelReq}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL ERROR CANCEL REQUEST */}
      <ErrorCancelRequestModal isOpen={openErrorCancelReq} toggle={toggleErrorCancelReq} />

      {/* MODAL CANCEL REQ SUCCESS */}
      <CancelRequestSuccessModal isOpen={openSuccessCancelReq} toggle={toggleSuccessCancelReq} />

      {/* MODAL DELETE REQUEST  */}
      <DeleteRequestModal
        isOpen={openDeleteReq}
        toggle={toggleDeleteReq}
        toggleError={toggleErrorDeleteReq}
        toggleSuccess={toggleSuccessDeleteReq}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL ERROR DELETE REQUEST */}
      <ErrorDeleteRequestModal isOpen={openErrorDeleteReq} toggle={toggleErrorDeleteReq} />

      {/* MODAL DELETE REQ SUCCESS */}
      <DeleteRequestSuccessModal isOpen={openSuccessDeleteReq} toggle={toggleSuccessDeleteReq} />
    </div>
  );
};

export default TimeSheetExplanation;
