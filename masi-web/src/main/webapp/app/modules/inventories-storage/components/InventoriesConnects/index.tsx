import Flex from 'app/components/flex/flex';
import FormError from 'app/components/form/form-error';
import { Typography } from 'app/components/typography/typography';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { InventoriesSchema } from 'app/validation/inventories.validation';
import { useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';
import { Col, Row } from 'reactstrap';
import AttachmentCard from '../attachment-card';

export const InventoriesConnect = () => {
  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const { formState } = useFormContext<InventoriesSchema>();
  return (
    <Flex direction="column" gap={16}>
      <Typography level={5}>Liên kết</Typography>
      <Row>
        {warehouseImportType ===
          (InventoriesWarehouse.WAREHOUSE_COMMERCE_IMPORT as string) && (
          <Col md={4}>
            <AttachmentCard title="Hoá đơn đầu vào" id="invoiceId" />
          </Col>
        )}
        <Col md={4}>
          <AttachmentCard title="Hợp đồng mua" id="purchaseContractId" />
          {formState.errors?.purchaseContractId?.message && (
            <FormError
              message={formState.errors?.purchaseContractId?.message}
            />
          )}
        </Col>
       
          <Col md={4}>
            <AttachmentCard title="Lệnh SX" id="productionId" />
          </Col>
      </Row>
    </Flex>
  );
};
