import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { iconPath } from 'app/shared/util/format';
import { ManufactureOrderSchema } from 'app/validation/manufacture-order.validation';
import { useFormContext } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { Col, Row } from 'reactstrap';
import { manufactureOrderBadgeMapping } from '../../production-mapping';
import './ProductionSummary.scss';

type Props = {
  headerTitle: string | React.ReactNode;
  badge?: React.ReactNode;
  summaries: { title: string; value: string }[];
};

const ProductionSummary = (props: Props) => {
  const { headerTitle, summaries, badge } = props;

  const navigate = useNavigate();

  const methods = useFormContext<ManufactureOrderSchema>();
  const { watch } = methods;

  const typeWatch = watch('typePage');
  const statusWatch = watch('status');

  const step = () => {
    switch (statusWatch) {
      case MANUFACTURE_ORDER_STATUS.NEW as string:
        return 0;
      case MANUFACTURE_ORDER_STATUS.PRODUCTION as string:
        return 1;
      case MANUFACTURE_ORDER_STATUS.PACKAGING as string:
        return 2;
      case MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string:
        return 3;
      case MANUFACTURE_ORDER_STATUS.SHIPPED as string:
        return 4;
      case MANUFACTURE_ORDER_STATUS.COMPLETED as string:
        return 5;
      default:
        return 0;
    }
  };

  const path =
    typeWatch === (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER as string)
      ? PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER
      : PATH.PRODUCTION_MANUFACTURE_ORDER_BY_STANDARD;

  return (
    <div className="summary-card">
      <Row>
        <Col md={1}>
          <ButtonV2
            left_section={<img src={iconPath('back.svg')} alt="alone" />}
            onClick={() => navigate(path)}
          />
        </Col>
        <Col md={9}>
          <Flex align="center" gap={16} style={{ marginBottom: '14px' }}>
            <Typography level={5}>{headerTitle}</Typography>
            {manufactureOrderBadgeMapping(
              statusWatch as MANUFACTURE_ORDER_STATUS,
            )}
          </Flex>
          {badge}
          <div className="summary-card__list-item">
            {summaries.map((summary, index) => (
              <div
                key={`production-summary-${index}`}
                className="summary-card__item"
              >
                <span className="summary-card__item--title">
                  {summary.title}:
                </span>
                <span className="summary-card__item--content">
                  {summary.value}
                </span>
              </div>
            ))}
          </div>
        </Col>
        <Col md={2} style={{ display: 'flex', alignItems: 'flex-end' }}>
          <div className="summary-progress">
            <div className="summary-progress_barOverflow">
              <div
                className={`summary-progress_barOverflow_bar step_${step()}`}
              />
            </div>
            <div className="summary-progress_info">
              <span className="summary-progress_text">Quy trình</span>
              <span className="summary-progress_step">{step()} / 5</span>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default ProductionSummary;
