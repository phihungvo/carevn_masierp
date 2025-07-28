import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { Col, Row } from 'reactstrap';
import AttachmentCard from '../attachment-card';

export const SupplierContractsConnect = () => {
  return (
    <Flex direction="column" gap={16}>
      <Typography level={5}>Liên kết</Typography>
      <Row>
        <Col md={4}>
          <AttachmentCard title="Đề xuất mua hàng" id="suppliesRequestId" />
        </Col>
        <Col md={4}>
          <AttachmentCard title="Hoá đơn đầu vào" id="invoiceId" />
        </Col>
      </Row>
    </Flex>
  );
};
