import React, { useState } from 'react'

import './logistics-material-proposal.scss'
import Card from 'app/components/card/card'
import LogisticsMaterialProposalTable from 'app/modules/logistics-material-proposal/logistics-material-proposal-table'
import LogisticsMaterialProposalHeader from 'app/modules/logistics-material-proposal/logistics-material-proposal-header'
import LogisticsMaterialProposeModals from 'app/modules/logistics-material-proposal/modals/logistics-material-propose-modals'
import LogisticsMaterialProposalCancelModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-cancel-modals'
import LogisticsMaterialProposalFilterModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-filter-modals'
import LogisticsMaterialProposalUpdateModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-update-modals'
import LogisticsMaterialProposalCreateModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-create-modals'
import LogisticsMaterialProposalDetailModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-detail-modals'
import LogisticsMaterialProposeSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-propose-success-modals'
import LogisticsMaterialProposalDeleteModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-delete-modals'
import LogisticsMaterialProposalRejectModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-reject-modals'
import LogisticsMaterialProposalApproveModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-approve-modals'
import LogisticsMaterialProposalApproveSignModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-approve-sign-modals'
import LogisticsMaterialProposalRejectSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-reject-success-modals'
import LogisticsMaterialProposalDeleteSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-delete-success-modals'
import LogisticsMaterialProposalUpdateSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-update-success-modals'
import LogisticsMaterialProposalCancelSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-cancel-success-modals'
import LogisticsMaterialProposalCreateSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-create-success-modals'
import LogisticsMaterialProposalApproveSignSuccessModals from 'app/modules/logistics-material-proposal/modals/logistics-material-proposal-approve-sign-success-modals'
import { Typography } from 'app/components/typography/typography'
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common'
import { ILogisticsMaterialProposalParams } from 'app/shared/model/logistics-material-proposal'
import { useModalsLogisticsMaterialProposal } from 'app/hooks/use-modals-logistics-material-proposal'

export default function LogisticsMaterialProposal() {

    const [
        { openFilter, toggleFilter },
        { openCreate, toggleCreate },
        { openCreateSuccess, toggleCreateSuccess },
        { openUpdate, toggleUpdate },
        { openUpdateSuccess, toggleUpdateSuccess },
        { openDetail, toggleDetail },
        { openPropose, togglePropose },
        { openProposeSuccess, toggleProposeSuccess },
        { openApprove, toggleApprove },
        { openApproveSign, toggleApproveSign },
        { openApproveSignSuccess, toggleApproveSignSuccess },
        { openReject, toggleReject },
        { openRejectSuccess, toggleRejectSuccess },
        { openDelete, toggleDelete },
        { openDeleteSuccess, toggleDeleteSuccess },
        { openCancel, toggleCancel },
        { openCancelSuccess, toggleCancelSuccess },
    ] = useModalsLogisticsMaterialProposal()

    const [searchText, setSearchText] = useState<string>('');
    const [selectedRecord, setSelectedRecord] = useState<string>('')
    const [filter, setFilter] = useState<ILogisticsMaterialProposalParams>({
        page: DEFAULT_PAGE,
        size: DEFAULT_PAGE_SIZE,
        search: '',
    })

    return (
        <>
            <Typography level={3}>Danh sách đề xuất vật liệu</Typography>

            <Card header={<LogisticsMaterialProposalHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
                <LogisticsMaterialProposalTable
                    setSelectedRecord={setSelectedRecord}
                    searchText={searchText} filter={filter}
                    setFilter={setFilter} toggleUpdate={toggleUpdate}
                    toggleDetail={toggleDetail}
                    togglePropose={togglePropose}
                    toggleApprove={toggleApprove}
                    toggleDelete={toggleDelete}
                    toggleCancel={toggleCancel}
                />
            </Card>

            <LogisticsMaterialProposalCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />

            <LogisticsMaterialProposalCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />

            <LogisticsMaterialProposalUpdateModals isOpen={openUpdate} toggle={toggleUpdate} toggleSuccess={toggleUpdateSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposalUpdateSuccessModals isOpen={openUpdateSuccess} toggle={toggleUpdateSuccess} />

            <LogisticsMaterialProposalFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />

            <LogisticsMaterialProposalDetailModals isOpen={openDetail} toggle={toggleDetail} />

            <LogisticsMaterialProposeModals isOpen={openPropose} toggle={togglePropose} toggleSuccess={toggleProposeSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposeSuccessModals isOpen={openProposeSuccess} toggle={toggleProposeSuccess} />

            <LogisticsMaterialProposalApproveModals isOpen={openApprove} toggle={toggleApprove} toggleApprove={toggleApproveSign} toggleReject={toggleReject} />

            <LogisticsMaterialProposalApproveSignModals isOpen={openApproveSign} toggle={toggleApproveSign} toggleSuccess={toggleApproveSignSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposalApproveSignSuccessModals isOpen={openApproveSignSuccess} toggle={toggleApproveSignSuccess} />

            <LogisticsMaterialProposalRejectModals isOpen={openReject} toggle={toggleReject} toggleSuccess={toggleRejectSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposalRejectSuccessModals isOpen={openRejectSuccess} toggle={toggleRejectSuccess} />

            <LogisticsMaterialProposalDeleteModals isOpen={openDelete} toggle={toggleDelete} toggleSuccess={toggleDeleteSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposalDeleteSuccessModals isOpen={openDeleteSuccess} toggle={toggleDeleteSuccess} />

            <LogisticsMaterialProposalCancelModals isOpen={openCancel} toggle={toggleCancel} toggleSuccess={toggleCancelSuccess} selectedRecord={selectedRecord} setSelectedRecord={setSelectedRecord} />

            <LogisticsMaterialProposalCancelSuccessModals isOpen={openCancelSuccess} toggle={toggleCancelSuccess} />

        </>
    )
}
