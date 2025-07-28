
import { Col } from 'antd';
import WrapDate from 'app/components/wrap-date/WrapDate';
import WrapSelect from 'app/components/wrap-select/WrapSelect';
import { FormGroup, Row } from 'reactstrap';
import { liquidationStatusArr, liquidationStatusObj } from './constants/status';

interface ILiquidationFilter {
  handleQuery: (key: string | number | symbol) => (eventData: any) => void
}

const LiquidationFilter = (props: ILiquidationFilter) => {
  const {
    handleQuery
  } = props

  return (
    <Row>
      <FormGroup>
        <Row>
          <Col span={12}>
            <WrapDate 
              isExcludeHookForm
              name='createdDate.greaterThanOrEqual'
              label='Từ ngày'
              onDateChange={iso => handleQuery('liquidationDate.greaterThanOrEqual')(iso)}
            />
          </Col>
          <Col span={12}>
            <WrapDate 
              isExcludeHookForm
              name='createdDate.greaterThanOrEqual'
              label='Đến ngày'
              onDateChange={iso => handleQuery('liquidationDate.lessThanOrEqual')(iso)}
            />
          </Col>
        </Row>
        <Row>
          <WrapSelect
            label='Trạng thái'
            name='status.equals'
            isExcludeHookForm
            data={{
              arr: liquidationStatusArr,
              obj: liquidationStatusObj
            }}
            onSelectChange={value => handleQuery('status.equals')(value?.value)}
          />
        </Row>
      </FormGroup>
    </Row>
  );
};

export default LiquidationFilter;
