import PersonSelect from 'app/components/wrap-select/Person';
import WrapSelect from 'app/components/wrap-select/WrapSelect';
import { liquidationStatusArr, liquidationStatusObj } from 'app/modules/liquidation/constants/status';
import { Col, Row } from 'reactstrap';
import { Filter } from '../types/filter';

type Props = {
  handleQuery: (key: keyof Filter) => (eventData: any) => void
  query: Partial<Filter>
}

const DepreciationFilter = (props: Props) => {
  const { handleQuery, query } = props

  return (
    <Row>
      <Col md={12}>
        <WrapSelect
          label="Trạng thái"
          name="status.equals"
          isExcludeHookForm
          data={{
            arr: liquidationStatusArr,
            obj: liquidationStatusObj,
          }}
          onSelectChange={value => handleQuery('status.equals')(value?.value)}
        />
      </Col>
      <Col md={12}>
        <PersonSelect<Filter>
          label="Người tính"
          name="employeeId.equals"
          isExcludeHookForm
          onSelectChange={data => handleQuery('employeeId.equals')(data?.id)}
          value={query?.['employeeId.equals']}
        />
      </Col>
    </Row>
  );
};

export default DepreciationFilter;
