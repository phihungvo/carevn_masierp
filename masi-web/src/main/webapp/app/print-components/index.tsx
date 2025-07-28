import { useLocation } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { AdvanceRequestPrint } from './advance-request';
import { PaymentRequestPrint } from './payment-request';
import { RefundRequestPrint } from './refund-request';
import { PAYMENT_REQUEST_TYPE } from 'app/shared/model/enumerations/payment-request';
import PurchaseProposalPrint from 'app/modules/PurchaseProposal/Print/PurchaseProposalPrint';
import { InventoriesExportPrint } from 'app/modules/inventories-storage-export/components/InventoriesExportPrint/inventories-export-print';
import InventoriesPrint from 'app/modules/inventories-storage/components/InventoriesPrint/inventories-print';
import LiquidationPrint from 'app/modules/liquidation/component/liquidation-print/liquidation-print';
import CancelTestingPrint from 'app/modules/production-quality/components/print/cancel-testing-print';
import SupplierContractPrint from 'app/modules/supplier-contracts/components/SupplierContractPrint/supplier-contract-print';
import StockTakingPrint from 'app/modules/stocktaking/components/StocktakingPrint';

export type PrintType =
  | `${PAYMENT_REQUEST_TYPE}`
  | 'PURCHASE_PROPOSAL'
  | 'INVENTORIES_EXPORT'
  | 'INVENTORIES'
  | 'LIQUIDATION'
  | 'PRODUCTION_QUALITY_CANCEL_TESTING'
  | 'STOCKTAKING'
  | 'SUPPLIER_CONTRACTS';

export const PrintComponents = () => {
  const location = useLocation();
  const [searchParams] = useSearchParams();
  const printId = searchParams.get('printId');
  const printType = searchParams.get('printType') as PrintType;

  const renderPrintUI = (key: PrintType) => {
    switch (key) {
      case PAYMENT_REQUEST_TYPE.PAYMENT as string:
        return <PaymentRequestPrint id={printId} />;
      case PAYMENT_REQUEST_TYPE.ADVANCEMENT as string:
        return <AdvanceRequestPrint id={printId} />;
      case PAYMENT_REQUEST_TYPE.REIMBURSEMENT as string:
        return <RefundRequestPrint id={printId} />;
      case 'PURCHASE_PROPOSAL':
        return <PurchaseProposalPrint id={printId} />;
      case 'INVENTORIES_EXPORT':
        return <InventoriesExportPrint id={printId} />;
      case 'INVENTORIES':
        return <InventoriesPrint id={printId} />;
      case 'LIQUIDATION':
        return <LiquidationPrint id={printId} />;
      case 'PRODUCTION_QUALITY_CANCEL_TESTING':
        return <CancelTestingPrint id={printId} />;
      case 'SUPPLIER_CONTRACTS':
        return <SupplierContractPrint id={printId} />;
      case 'STOCKTAKING':
        return <StockTakingPrint id={printId} />;
      default:
        break;
    }
  };

  return (
    <section className="print-section">{renderPrintUI(printType)}</section>
  );
};
