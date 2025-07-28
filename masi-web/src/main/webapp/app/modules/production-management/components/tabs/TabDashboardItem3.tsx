import Flex from 'app/components/flex/flex';
import { useNavigate } from 'react-router';
import { Col, Row } from 'reactstrap';
import './style.scss';

interface TabDashboardItem3Props {
  disabledDirect?: boolean;
  directUrl?: string;
  onClickTab?: () => void;
  title?: string;
  valueTitle?: string;
  internal?: number[];
  external?: number[];
}
export const TabDashboardItem3 = (props: TabDashboardItem3Props) => {
  const {
    disabledDirect = false,
    directUrl = '#',
    onClickTab,
    title,
    valueTitle,
    internal,
    external,
  } = props;
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" className="tab-dashboard_item3" gap={16}>
        <Flex className="tab-dashboard_item3_top" justify="space-between">
          <Flex direction="row" className="tab-dashboard_item3_left" gap={16}>
            <Flex direction="column">
              <span className="text_title">{title}</span>
              <span className="text_content">
                % đạm áp dụng: <b>{valueTitle}</b>
              </span>
            </Flex>
          </Flex>
          <Flex
            direction="column"
            className="tab-dashboard_item3_right"
            gap={8}
          >
            <div
              className={`btn_detail ${disabledDirect ? 'btn_disabled' : ''}`}
              onClick={() => {
                if (!disabledDirect) {
                  navigate(directUrl);
                  onClickTab();
                }
              }}
            >
              Chi tiết
              <img
                src="content/images/vuesax/linear/arrow-right-blue.svg"
                alt="arrow-right-blue"
              />
            </div>
          </Flex>
        </Flex>
        <Flex className="tab-dashboard_item3_bottom" direction="column" gap={4}>
          <span className="tab-dashboard_item3_bottom_title">Nội bộ</span>
          <Row style={{ width: '100%' }}>
            <Col md={2}>
              <img
                src={`content/images/vuesax/linear/production-dashboard-icon-5.svg`}
                alt={`icon5`}
              />
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Độ ẩm</span>
              <span className="text_content">{internal?.[0]}</span>
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">TVN</span>
              <span className="text_content">{internal?.[1]}</span>
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Tro</span>
              <span className="text_content">{internal?.[2]}</span>
            </Col>
            <Col md={3} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Protein</span>
              <span className="text_content">{internal?.[3]}</span>
            </Col>
          </Row>
        </Flex>
        <Flex className="tab-dashboard_item3_bottom" direction="column" gap={4}>
          <span className="tab-dashboard_item3_bottom_title">Đối tác</span>
          <Row style={{ width: '100%' }}>
            <Col md={2}>
              <img
                src={`content/images/vuesax/linear/production-dashboard-icon-6.svg`}
                alt={`icon6`}
              />
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Độ ẩm</span>
              <span className="text_content">{external?.[0]}</span>
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">TVN</span>
              <span className="text_content">{external?.[1]}</span>
            </Col>
            <Col md={2} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Tro</span>
              <span className="text_content">{external?.[2]}</span>
            </Col>
            <Col md={3} className="tab-dashboard_item3_bottom_item">
              <span className="text_title">Protein</span>
              <span className="text_content">{external?.[3]}</span>
            </Col>
          </Row>
        </Flex>
      </Flex>
    </>
  );
};
