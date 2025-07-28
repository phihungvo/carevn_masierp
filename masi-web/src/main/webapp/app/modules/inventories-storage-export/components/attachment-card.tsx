import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { Card } from 'reactstrap';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const { useOrdersQuery } = useOrders;

interface IAttachmentCardProps {
  title: string;
  id: 'orderId' | 'paymentRequestId';
}

const AttachmentCard = ({ title, id }: IAttachmentCardProps) => {
  const { setAttachment } = useContext(InventoriesExportStorageContext);
  const { watch, resetField } = useFormContext<InventoriesExportSchema>();

  const { data: orders } = useOrdersQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  return (
    <Card className="attachment-card">
      <Flex direction="column" justify="space-between" gap={8}>
        <Flex justify="space-between" align="center">
          <span className="attachment-card-title">{title}</span>
          {id === 'orderId' && (
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
          {id === 'orderId' && (
            <span className="attachment-card-link">
              {orders?.data?.length > 0 &&
                orders?.data?.find(command => command.id === watch(id))
                  ?.orderCode}
            </span>
          )}

          {id === 'orderId' && orders?.data?.length > 0 && (
            <ButtonDelete
              type="button"
              variant="text"
              onClick={() => resetField(id)}
              disabled={disabled}
            />
          )}
        </Flex>
      </Flex>
    </Card>
  );
};

export default AttachmentCard;
