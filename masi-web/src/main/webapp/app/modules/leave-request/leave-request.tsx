import React, { useState } from 'react';

import './leave-request.scss';
import Card from 'app/components/card/card';
import BtnDownload from 'app/components/btn-download/btn-download';
import { LeaveRequestTable } from './leave-request-table';
import { useBtnDownload } from 'app/hooks/use-btn-download';
import { LeaveRequestHeader } from './leave-request-components';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { AcceptRequestModal } from './modals/leave-request-accept-modals';
import { DeleteRequestModal } from './modals/leave-request-delete-modals';
import { RejectRequestModal } from './modals/leave-request-reject-modals';
import { useModalsLeaveRequest } from 'app/hooks/use-modals-leave-request';
import { FilterRequestModal, NoteRequestModal } from './modals/leave-request-modals';
import { ILeaveRequest, ILeaveRequestParams } from 'app/shared/model/leave-request.model';
import { CancelRequestModal, ErrorCancelRequestModal } from './modals/leave-request-cancel-modals';
import { RequestLeaveRequestModal, RequestSuccessModal } from './modals/leave-request-create-modals';
import { DownloadSuccessfulModals } from 'app/components/modals-download-successful/download-successful-modals';
import LeaveRequestDetailModals from 'app/modules/leave-request/modals/leave-request-detail-modals';
import { LeaveRequestNoticesModal } from './modals/leave-request-notices-modals';

const LeaveRequest = () => {
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ILeaveRequest[]>([]);
  const [selected, setSelected] = useState<{ [leaving_id: string]: ILeaveRequest }>({});
  const [textNotices, setTextNotices] = useState<string>('')
  const [filter, setFilter] = useState<ILeaveRequestParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    status: [],
    type: [],
    sort: ['created_at,desc']
  });

  const [
    { openLeaveRequest, toggleLeaveRequest },
    { openRequestSuccess, toggleRequestSuccess },
    { openCancelLeaveRequest, toggleCancelLeaveRequest },
    { openCancelError, toggleCancelError },
    { openDeleteRequest, toggleDeleteRequest },
    { openFilterRequest, toggleFilterRequest },
    { openNoteRequest, toggleNoteRequest },
    { openRejectRequest, toggleRejectRequest },
    { openAcceptRequest, toggleAcceptRequest },
    { openDetailRequest, toggleDetailRequest },
    { openNotices, toggleNotices },
  ] = useModalsLeaveRequest();

  const [{ openModalDownload, toggleModalDownload }, { openDownloadSuccessful, toggleDownloadSuccessful }] = useBtnDownload();

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách đơn nghỉ phép</Typography>
      <Card
        header={
          <LeaveRequestHeader
            toggleLeaveRequest={toggleLeaveRequest}
            toggleCancelLeaveRequest={toggleCancelLeaveRequest}
            toggleDeleteRequest={toggleDeleteRequest}
            toggleFilterRequest={toggleFilterRequest}
            toggleAcceptRequest={toggleAcceptRequest}
            toggleRejectRequest={toggleRejectRequest}
            selectedRows={selectedRows}
            toggleModalDownload={toggleModalDownload}
            setFilter={setFilter}
          />
        }
      >
        <LeaveRequestTable
          toggleCancelLeaveRequest={toggleCancelLeaveRequest}
          toggleDeleteRequest={toggleDeleteRequest}
          toggleAcceptRequest={toggleAcceptRequest}
          toggleRejectRequest={toggleRejectRequest}
          toggleDetailRequest={toggleDetailRequest}
          setSelectedRecord={setSelectedRecord}
          selectedRowKeys={selectedRowKeys}
          setSelectedRowKeys={setSelectedRowKeys}
          setSelectedRows={setSelectedRows}
          selected={selected}
          setSelected={setSelected}
          filter={filter}
          setFilter={setFilter}
        />
      </Card>

      {/* MODAL CREATE LEAVE REQUEST REQUEST */}
      <RequestLeaveRequestModal toggleSuccess={toggleRequestSuccess} isOpen={openLeaveRequest} toggle={toggleLeaveRequest} />

      {/* MODAL REQUEST SUCCESS */}
      <RequestSuccessModal isOpen={openRequestSuccess} toggle={toggleRequestSuccess} cancel={false} />

      {/* MODAL CANCEL LEAVE REQUEST REQUEST */}
      <CancelRequestModal
        isOpen={openCancelLeaveRequest}
        toggle={toggleCancelLeaveRequest}
        selectedRecord={selectedRecord}
        toggleError={toggleCancelError}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        toggleNotices={toggleNotices}
        setTextNotices={setTextNotices}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL CANCEL REQUEST ERROR */}
      <ErrorCancelRequestModal isOpen={openCancelError} toggle={toggleCancelError} cancel={false} />

      {/* DETAIL LEAVE REQUEST */}
      <LeaveRequestDetailModals isOpen={openDetailRequest} toggle={toggleDetailRequest} selectedRecord={selectedRecord} />

      {/* MODAL DELETE REQUEST */}
      <DeleteRequestModal
        isOpen={openDeleteRequest}
        toggle={toggleDeleteRequest}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL FILTER REQUEST */}
      <FilterRequestModal isOpen={openFilterRequest} toggle={toggleFilterRequest} setFilter={setFilter} />

      {/* MODAL ACCEPT REQUEST*/}
      <AcceptRequestModal
        isOpen={openAcceptRequest}
        toggle={toggleAcceptRequest}
        selectedRecord={selectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        setSelectedRecord={setSelectedRecord}
        selected={selected}
        toggleNotices={toggleNotices}
        setTextNotices={setTextNotices}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL REJECT REQUEST*/}
      <RejectRequestModal
        isOpen={openRejectRequest}
        toggle={toggleRejectRequest}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
        selected={selected}
        toggleNotices={toggleNotices}
        setTextNotices={setTextNotices}
        setSelectedRows={setSelectedRows}
      />

      {/* MODAL NOTE REQUEST */}
      <NoteRequestModal isOpen={openNoteRequest} toggle={toggleNoteRequest} />

      {/* MODAL download */}
      <BtnDownload isOpen={openModalDownload} toggle={toggleModalDownload} toggleDownloadSuccessful={toggleDownloadSuccessful} />

      <DownloadSuccessfulModals title="theo dõi chế độ nghỉ" isOpen={openDownloadSuccessful} toggleSuccess={toggleDownloadSuccessful} />

      <LeaveRequestNoticesModal
        isOpen={openNotices}
        toggle={toggleNotices}
        textNotices={textNotices}
      />
    </div>
  );
};

export default LeaveRequest;
