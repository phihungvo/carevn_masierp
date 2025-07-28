import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import AdvanceInfo from './AdvanceInfo/AdvanceInfo';
import Attachments from './Attachments/Attachments';

const MachineryEquipmentSpecificationsForm = (props: any) => {
  const methods = useFormContext();
  const { control, setValue } = methods
  return (<>
     <Row>
        <Col md={6}>
          <AdvanceInfo name='attribute.specification.advanceInfo' />
        </Col>
        <Col md={6}>
          <Attachments name='attribute.specification.attachments' />
        </Col>
      </Row>
  </>)
};
export default MachineryEquipmentSpecificationsForm;
