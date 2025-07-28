import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import AdvanceInfo from './AdvanceInfo/AdvanceInfo';
import MaterialInfo from './MaterialInfo/MaterialInfo';

const MachineryEquipmentMaintenanceForm = (props: any) => {
  const methods = useFormContext();
  const { control, setValue } = methods
  return (<>
    <Row>
      <Col md={12}>
        <MaterialInfo name="attribute.maintenance.materials" />
      </Col>
    </Row>
    <Row style={{
      paddingTop: "1rem",
    }}>
      <Col md={6}>
        <FormInputV2
          control={control}
          type='textarea'
          rows={2}
          id="attribute.maintenance.mechanical"
          name="attribute.maintenance.mechanical"
          label="Nội dung bảo trì cơ"
          placeholder="Điền"
        />
      </Col>
      <Col md={6}>
        <FormInputV2
          control={control}
          type='textarea'
          rows={2}
          id="attribute.maintenance.electrical"
          name="attribute.maintenance.electrical"
          label="Nội dung bảo trì điện"
          placeholder="Điền"
        />
      </Col>
    </Row>
  </>)
};

export default MachineryEquipmentMaintenanceForm;
