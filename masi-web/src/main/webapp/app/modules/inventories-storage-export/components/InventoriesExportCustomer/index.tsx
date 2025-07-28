import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import useSupplier from 'app/hooks/use-supplier';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetEnabledCustomers } = useCustomers;
const { useGetSuppliers } = useSupplier;

export const InventoriesExportCustomer = () => {
  const { control, watch, setValue } =
    useFormContext<InventoriesExportSchema>();

  const { data: customers } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { data: suppliers } = useGetSuppliers({ size: DEFAULT_PAGE_SIZE_NAX });

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.COMPLETED as string);

  return (
    <>
      <Typography level={5}>Nơi nhận</Typography>
      <Row>
        <Col md={3}>
          <FormSelect
            control={control}
            name="customerId"
            label="KH"
            placeholder="Mã - Tên NCC"
            options={[
              ...(customers?.data ?? []),
              ...(suppliers?.data ?? []),
            ]?.map(x => ({
              value: x?.id,
              label: `${x?.customerCode ?? x.code} - ${
                x?.companyName ?? x.name
              } `,
            }))}
            disabled={disabled}
            onChanges={() => {
              setValue('orderId', '');
              setValue('shipperAddress', '');
              setValue('receiverName', '');
              setValue('receiverPhone', '');
            }}
          />
        </Col>

        <Col md={3}>
          <FormInputV2
            control={control}
            name="shipper"
            label="KH giao"
            placeholder="Điền"
            disabled={disabled}
          />
        </Col>

        <Col md={6}>
          <FormInputV2
            control={control}
            name="shipperAddress"
            label="Địa chỉ"
            placeholder="Điền"
            disabled={disabled}
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            name="receiverName"
            label="Người nhận"
            placeholder="Điền"
            disabled={disabled}
          />
        </Col>

        <Col md={3}>
          <FormInputV2
            control={control}
            name="receiverPhone"
            label="Số điện thoại"
            placeholder="Điền"
            disabled={disabled}
          />
        </Col>
      </Row>
    </>
  );
};
