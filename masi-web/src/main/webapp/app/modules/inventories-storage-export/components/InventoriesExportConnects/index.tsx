import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { Col, Row } from 'reactstrap';
import AttachmentCard from '../attachment-card';

export const InventoriesExportConnect = () => {
  return (
    <Flex direction="column" gap={16}>
      <Typography level={5}>Liên kết</Typography>
      <Row>
        <Col md={4}>
          <AttachmentCard title="Đơn hàng bán" id="orderId" />
        </Col>
      </Row>
    </Flex>
  );
};
