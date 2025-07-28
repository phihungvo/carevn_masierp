import React, { useEffect } from 'react'

import Table from 'app/components/table/table'
import { DEFAULT_PAGE } from 'app/constants/common';
import { generateColumns } from './generate-columns'
import { useDebounce } from 'app/hooks/use-debounce';
import { LOGISTICS_MATERIAL_RPOPOSAL_STATUS } from 'app/shared/model/enumerations/logistics-material-proposal';
import { ILogisticsMaterialProposal, ILogisticsMaterialProposalParams } from 'app/shared/model/logistics-material-proposal'

interface ILogisticsMaterialProposalTableProps {
    setSelectedRecord: (id: string) => void,
    searchText: string;
    filter: ILogisticsMaterialProposalParams;
    setFilter: React.Dispatch<React.SetStateAction<ILogisticsMaterialProposalParams>>;
    toggleUpdate: () => void;
    toggleDetail: () => void;
    togglePropose: () => void;
    toggleApprove: () => void;
    toggleDelete: () => void;
    toggleCancel: () => void;
}

export const logisticsMaterialProposals: ILogisticsMaterialProposal[] = [
    {
        id: '1',
        numberVoucher: "VCH001",
        voucherDate: "2024-09-01",
        employeeId: "EMP123",
        workspaceId: "Warehouse",
        productName: "Steel Rod",
        units: "Kg",
        quantity: 500,
        unitPrice: 10.5,
        supplier: "Steel Supplier Co.",
        note: "Urgent delivery",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED
    },
    {
        id: '2',
        numberVoucher: "VCH002",
        voucherDate: "2024-09-02",
        employeeId: "EMP124",
        workspaceId: "Factory",
        productName: "Aluminum Sheet",
        units: "Sheets",
        quantity: 200,
        unitPrice: 15.0,
        supplier: "AluSupply Ltd.",
        note: "For construction project",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED
    },
    {
        id: '3',
        numberVoucher: "VCH003",
        voucherDate: "2024-09-03",
        employeeId: "EMP125",
        workspaceId: "Office",
        productName: "Printer Paper",
        units: "Packs",
        quantity: 100,
        unitPrice: 5.25,
        supplier: "OfficeSupplies Inc.",
        note: "Monthly stock",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING
    },
    {
        id: '4',
        numberVoucher: "VCH004",
        voucherDate: "2024-09-04",
        employeeId: "EMP126",
        workspaceId: "Warehouse",
        productName: "Cement Bags",
        units: "Bags",
        quantity: 1000,
        unitPrice: 7.75,
        supplier: "Cement Supplier Co.",
        note: "Bulk order",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW
    },
    {
        id: '5',
        numberVoucher: "VCH005",
        voucherDate: "2024-09-05",
        employeeId: "EMP127",
        workspaceId: "Factory",
        productName: "Copper Wire",
        units: "Rolls",
        quantity: 150,
        unitPrice: 12.0,
        supplier: "Copper Supply Ltd.",
        note: "High quality",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW
    },
    {
        id: '6',
        numberVoucher: "VCH006",
        voucherDate: "2024-09-06",
        employeeId: "EMP128",
        workspaceId: "Office",
        productName: "Ink Cartridges",
        units: "Pieces",
        quantity: 50,
        unitPrice: 30.0,
        supplier: "PrinterInk Co.",
        note: "For printers",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED
    },
    {
        id: '7',
        numberVoucher: "VCH007",
        voucherDate: "2024-09-07",
        employeeId: "EMP129",
        workspaceId: "Warehouse",
        productName: "Wood Planks",
        units: "Meters",
        quantity: 300,
        unitPrice: 8.5,
        supplier: "Woodworks Inc.",
        note: "Construction use",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL
    },
    {
        id: '8',
        numberVoucher: "VCH008",
        voucherDate: "2024-09-08",
        employeeId: "EMP130",
        workspaceId: "Factory",
        productName: "Plastic Sheets",
        units: "Rolls",
        quantity: 400,
        unitPrice: 6.0,
        supplier: "Plastic Supplies Co.",
        note: "Order for production",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW
    },
    {
        id: '9',
        numberVoucher: "VCH009",
        voucherDate: "2024-09-09",
        employeeId: "EMP131",
        workspaceId: "Warehouse",
        productName: "Nails",
        units: "Boxes",
        quantity: 5000,
        unitPrice: 2.5,
        supplier: "Hardware Supplier Co.",
        note: "For general usage",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL
    },
    {
        id: '10',
        numberVoucher: "VCH010",
        voucherDate: "2024-09-10",
        employeeId: "EMP132",
        workspaceId: "Office",
        productName: "Desk Chairs",
        units: "Pieces",
        quantity: 20,
        unitPrice: 45.0,
        supplier: "Furniture Co.",
        note: "For new employees",
        status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED
    },
];


export default function LogisticsMaterialProposalTable({ setSelectedRecord, searchText, filter, setFilter, toggleUpdate, toggleDetail, togglePropose, toggleApprove, toggleDelete, toggleCancel }: ILogisticsMaterialProposalTableProps) {

    const columns = generateColumns(setSelectedRecord, toggleUpdate, toggleDetail, togglePropose, toggleApprove, toggleDelete, toggleCancel)

    const search = useDebounce(searchText, 500);

    useEffect(() => {
        setFilter(prev => ({ ...prev, search, page: DEFAULT_PAGE }));
    }, [search]);

    // const totalCount = data?.totalRecord || 0;
    const totalCount = 0;
    const { page, size } = filter;

    return (
        <Table<ILogisticsMaterialProposal>
            dataSource={logisticsMaterialProposals}
            columns={columns}
            pagination={{
                page,
                size,
                totalCount,
                onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
            }}
        />
    )
}
