import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useOrders from 'app/hooks/use-orders';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const { useOrdersQuery, useGetOrderByIdQuery } = useOrders;

const mapTitle = (id: 'orderId' | 'paymentRequestId') => {
  switch (id) {
    case 'orderId':
      return 'Đơn hàng bán';
    case 'paymentRequestId':
      return 'Đề nghị thanh toán';
    default:
      return '';
  }
};

const InventoriesAttachmentModal = () => {
  const { attachment, setAttachment } = useContext(
    InventoriesExportStorageContext,
  );

  const { control, resetField, setValue, watch } =
    useFormContext<InventoriesExportSchema>();
  const customerIdWatch = watch('customerId');

  const { data: orders } = useOrdersQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
    customerId: customerIdWatch,
  });

  const toggleAttachment = () => {
    setAttachment({ isOpen: !attachment.isOpen, id: attachment?.id });
  };

  const onCancel = () => {
    toggleAttachment();
    resetField(attachment?.id);
    resetField(`${attachment?.id}`.replace('Id', 'Name') as any);
  };

  return (
    <Modal
      isOpen={attachment.isOpen}
      toggle={toggleAttachment}
      titleHeader={mapTitle(attachment?.id)}
      onCancel={onCancel}
      okText="Thêm"
      style={{ height: 400 }}
      onOk={() =>
        setAttachment({ isOpen: false, id: attachment?.id, data: attachment })
      }
    >
      {attachment?.id === 'orderId' && (
        <FormSelect
          control={control}
          name="orderId"
          label="Đơn hàng bán"
          options={orders?.data?.map(x => ({
            label: x.orderCode,
            value: x.id,
          }))}
          placeholder="Chọn"
          isClearable={false}
          onChanges={e => {
            const selected = orders?.data?.find(x => x.id === e);
            if (selected)
              setValue('customerId', selected?.contract?.customerId);
              setValue('shipperAddress', selected?.contract?.customer?.address);
              setValue('receiverName', selected?.contract?.customer?.firstName);
              setValue('receiverPhone', selected?.contract?.customer?.phoneNumber);
          }}
        />
      )}
    </Modal>
  );
};

export default InventoriesAttachmentModal;
