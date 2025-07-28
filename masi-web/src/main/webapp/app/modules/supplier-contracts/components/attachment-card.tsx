import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';
import { SupplierContractsSchema } from 'app/validation/supplier-contracts.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { Card } from 'reactstrap';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import AuthGuard from 'app/components/guards/auth-guard';

interface IAttachmentCardProps {
  title: string;
  id: 'invoiceId' | 'suppliesRequestId';
}
const { useGetSuppliesRequests } = useSuppliesRequest;

const AttachmentCard = ({ title, id }: IAttachmentCardProps) => {
  const { setAttachment } = useContext(SupplierContractsContext);
  const { watch, resetField } = useFormContext<SupplierContractsSchema>();
  const supplierId = watch('supplierId');
  const invoices = watch('invoices');
  const statusWatch = watch('status');

  const { data: supplierRequests } = useGetSuppliesRequests({
    'supplierId.equals': supplierId,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const disabled = false;
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.CANCELLED as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.APPROVED as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.COMPLETED as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.LIQUIDATED as string) ||
    // statusWatch === (SUPPLIER_CONTRACT_STATUS.EXPIRED as string);

  const getNameSupplierRequest = () => {
    const selected = supplierRequests?.data?.find(
      contract => contract.id === watch(id),
    );
    if (selected)
      return `${selected.requestNumber} ${
        selected?.note ? '- ' + selected.note : ''
      }`;
    return '';
  };

  return (
    <Card className="attachment-card">
      <Flex direction="column" justify="space-between" gap={8}>
        <Flex justify="space-between" align="center">
          <span className="attachment-card-title">{title}</span>
          {id !== 'invoiceId' && (
            <AuthGuard permissionKey='LOGISTICS_SUPPLIER_CONTRACTS.CREATE'>
            <ButtonV2
              type="button"
              variant="fill"
              color="blue"
              onClick={() => setAttachment({ isOpen: true, id })}
              disabled={disabled}
            >
              Thêm
            </ButtonV2>
            </AuthGuard>
          )}
        </Flex>

        <Flex justify="space-between" align="center">
          {id === 'invoiceId' && invoices?.length > 0 && (
            <span className="attachment-card-link">{`${invoices
              ?.map(x => x.invoiceNo)
              .join(', ')}`}</span>
          )}

          {id === 'suppliesRequestId' && (
            <span className="attachment-card-link">
              {supplierRequests?.data?.length > 0 && getNameSupplierRequest()}
            </span>
          )}

          {id === 'suppliesRequestId' &&
            watch(id) &&
            supplierRequests?.data?.length > 0 && (
              <AuthGuard permissionKey='LOGISTICS_SUPPLIER_CONTRACTS.EDIT'>
              <ButtonDelete
                type="button"
                variant="text"
                onClick={() => resetField(id)}
                disabled={disabled}
              />
              </AuthGuard>
            )}
        </Flex>
      </Flex>
    </Card>
  );
};

export default AttachmentCard;
