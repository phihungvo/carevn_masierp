
import {
  ALLOCATION_STATUS
} from 'app/shared/model/enumerations/allocation';
import { Col, FormGroup, Input, Label, Row } from 'reactstrap';

interface IAllocationFilter {
}

const AllocationFilter = (props: IAllocationFilter) => {

  return (
    <Row>
      <FormGroup>
        <Label>Trạng thái</Label>
        <Row>
          <Col md={4}>
            <FormGroup check>
              <Label check htmlFor={ALLOCATION_STATUS.UNPROCESSED}>
                Chưa xử lý
              </Label>
              <Input
                id={ALLOCATION_STATUS.UNPROCESSED}
                name="allocationStatus"
                type="checkbox"
                value={ALLOCATION_STATUS.UNPROCESSED}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check htmlFor={ALLOCATION_STATUS.WAITING_FOR_PROCESSING}>
                Chờ xử lý
              </Label>
              <Input
                id={ALLOCATION_STATUS.WAITING_FOR_PROCESSING}
                name="allocationStatus"
                type="checkbox"
                value={ALLOCATION_STATUS.WAITING_FOR_PROCESSING}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check htmlFor={ALLOCATION_STATUS.PROCESSED}>
                Đang xử lý
              </Label>
              <Input
                id={ALLOCATION_STATUS.PROCESSED}
                name="allocationStatus"
                type="checkbox"
                value={ALLOCATION_STATUS.PROCESSED}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check htmlFor={ALLOCATION_STATUS.CANCEL}>
                Đã hủy bỏ
              </Label>
              <Input
                id={ALLOCATION_STATUS.CANCEL}
                name="allocationStatus"
                type="checkbox"
                value={ALLOCATION_STATUS.CANCEL}
              />
            </FormGroup>
          </Col>
          <Col md={4}>
            <FormGroup check>
              <Label check htmlFor={ALLOCATION_STATUS.SUCCESS}>
                Thành công
              </Label>
              <Input
                id={ALLOCATION_STATUS.SUCCESS}
                name="allocationStatus"
                type="checkbox"
                value={ALLOCATION_STATUS.SUCCESS}
              />
            </FormGroup>
          </Col>
        </Row>
      </FormGroup>
    </Row>
  );
};

export default AllocationFilter;
