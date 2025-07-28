import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import useProductionCommand from 'app/hooks/use-production-command';
import { useGetSupplierContracts } from 'app/hooks/use-supplier-contract';
import { InventoriesSchema } from 'app/validation/inventories.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';

const { useIncomingInvoices } = useIncomingInvoice;
const { useGetProductionCommandsQuery } = useProductionCommand;

const mapTitle = (id: 'invoiceId' | 'purchaseContractId' | 'productionId') => {
  switch (id) {
    case 'invoiceId':
      return 'Hoá đơn đầu vào';
    case 'purchaseContractId':
      return 'Hợp đồng mua';
    case 'productionId':
      return 'Lệnh sản xuất';
    default:
      return '';
  }
};

const InventoriesAttachmentModal = () => {
  const { attachment, setAttachment } = useContext(InventoriesStorageContext);
  const { control, resetField, watch } = useFormContext<InventoriesSchema>();
  const supplierId = watch('customerId');

  const { data: incomingInvoices } = useIncomingInvoices({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: productionCommands } = useGetProductionCommandsQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
    statuses: [MANUFACTURE_ORDER_STATUS.COMPLETED],
  });
  const { data: supplierContracts } = useGetSupplierContracts({
    'supplierId.equals': supplierId,
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const toggleAttachment = () => {
    setAttachment({ isOpen: !attachment.isOpen, id: attachment?.id });
    // resetField(attachment?.id);
    // resetField(`${attachment?.id}`.replace('Id', 'Name') as any);
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
      onOk={() => setAttachment({ isOpen: false, id: attachment?.id })}
    >
      {attachment?.id === 'invoiceId' && (
        <FormSelect
          control={control}
          name="invoiceId"
          label="Hoá đơn đầu vào"
          options={incomingInvoices?.data?.map(incomingInvoice => ({
            label: incomingInvoice.invoiceNo,
            value: incomingInvoice.id,
          }))}
          placeholder="Chọn"
          isClearable={false}
        />
      )}

      {attachment?.id === 'purchaseContractId' && (
        <FormSelect
          control={control}
          name="purchaseContractId"
          label="Hợp đồng mua"
          options={supplierContracts?.data?.map(s => ({
            label: `${s.contractCode} - ${s.note ?? ''}`,
            value: s.id,
          }))}
          placeholder="Chọn"
          isClearable={false}
        />
      )}

      {attachment?.id === 'productionId' && (
        <FormSelect
          control={control}
          name="productionId"
          label="Lệnh sản xuất"
          options={productionCommands?.data?.map(productionCommand => ({
            label: productionCommand.name,
            value: productionCommand.id,
          }))}
          placeholder="Chọn"
          isClearable={false}
        />
      )}
    </Modal>
  );
};

export default InventoriesAttachmentModal;
