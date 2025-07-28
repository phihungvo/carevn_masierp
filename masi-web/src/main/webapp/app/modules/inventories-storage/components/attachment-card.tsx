import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { useGetSupplierContracts } from 'app/hooks/use-supplier-contract';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { InventoriesSchema } from 'app/validation/inventories.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { Card } from 'reactstrap';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import useProductionCommand from 'app/hooks/use-production-command';
const { useGetProductionCommandsQuery } = useProductionCommand;
interface IAttachmentCardProps {
  title: string;
  id: 'invoiceId' | 'purchaseContractId' | 'productionId';
}

const AttachmentCard = ({ title, id }: IAttachmentCardProps) => {
  const { setAttachment } = useContext(InventoriesStorageContext);
  const { watch, resetField } = useFormContext<InventoriesSchema>();
  const supplierId = watch('customerId');
  const invoice = watch('invoice');

  const { data: supplierContracts } = useGetSupplierContracts({
    'supplierId.equals': supplierId,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { data: productionCommands } = useGetProductionCommandsQuery();

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  const getNamePurchaseContract = () => {
    const selected = supplierContracts?.data?.find(
      contract => contract.id === watch(id),
    );
    if (selected) return `${selected?.contractCode} - ${selected?.note ?? ''}`;
    return '';
  };

  return (
    <Card className="attachment-card">
      <Flex direction="column" justify="space-between" gap={8}>
        <Flex
          justify="space-between"
          align="center"
          style={{ minHeight: '32px' }}
        >
          <span className="attachment-card-title">{title}</span>
          {id !== 'invoiceId' && (
            <ButtonV2
              type="button"
              variant="fill"
              color="blue"
              onClick={() => setAttachment({ isOpen: true, id })}
              disabled={disabled}
            >
              Thêm
            </ButtonV2>
          )}
        </Flex>

        <Flex justify="space-between" align="center">
          {id === 'invoiceId' && invoice?.id && (
            <span className="attachment-card-link">{`${invoice?.invoiceNo}`}</span>
          )}

          {id === 'purchaseContractId' && (
            <span className="attachment-card-link">
              {supplierContracts?.data?.length > 0 && getNamePurchaseContract()}
            </span>
          )}

          {id === 'purchaseContractId' &&
            supplierContracts?.data?.length > 0 &&
            getNamePurchaseContract() && (
              <ButtonDelete
                type="button"
                variant="text"
                onClick={() => resetField(id)}
                disabled={disabled}
              />
            )}

          {id === 'productionId' && (
            <span className="attachment-card-link">
              {watch(id) &&
                productionCommands?.data?.find(
                  command => command.id === watch(id),
                )?.name}
            </span>
          )}
        </Flex>
      </Flex>
    </Card>
  );
};

export default AttachmentCard;
