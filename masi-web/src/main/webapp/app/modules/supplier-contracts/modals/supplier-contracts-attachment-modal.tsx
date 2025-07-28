import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import { SupplierContractsSchema } from 'app/validation/supplier-contracts.validation';
import { useContext } from 'react';
import { useFormContext } from 'react-hook-form';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';

const { useIncomingInvoices } = useIncomingInvoice;
const { useGetSuppliesRequests } = useSuppliesRequest;

const mapTitle = (id: 'invoiceId' | 'suppliesRequestId') => {
  switch (id) {
    case 'invoiceId':
      return 'Hoá đơn đầu vào';
    case 'suppliesRequestId':
      return 'Đề xuất mua hàng';
    default:
      return '';
  }
};

const SupplierContractsAttachmentModal = () => {
  const { attachment, setAttachment } = useContext(SupplierContractsContext);
  const { control, resetField, watch } =
    useFormContext<SupplierContractsSchema>();
  const supplierId = watch('supplierId');

  const { data: incomingInvoices } = useIncomingInvoices({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: supplierRequests } = useGetSuppliesRequests({
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
      {attachment?.id === 'suppliesRequestId' && (
        <FormSelect
          control={control}
          name="suppliesRequestId"
          label="Đề xuất mua hàng"
          options={supplierRequests?.data?.map(e => ({
            label: `${e.requestNumber} ${e?.note ? '- ' + e.note : ''}`,
            value: e.id,
          }))}
          placeholder="Chọn"
          isClearable={false}
        />
      )}

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
    </Modal>
  );
};

export default SupplierContractsAttachmentModal;
