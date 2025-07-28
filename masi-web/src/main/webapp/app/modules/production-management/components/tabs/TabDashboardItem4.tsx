import Flex from 'app/components/flex/flex';
import { ManufactureOrderSchema } from 'app/validation/manufacture-order.validation';
import { useFormContext } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { Col, Row } from 'reactstrap';
import { enableDirectPackaging } from '../../production-ultis';
import './style.scss';

interface TabDashboardItem4Props {
  disabledDirect?: boolean;
  directUrl?: string;
  titleHeader?: string;
  onClickTab: () => void;
}
export const TabDashboardItem4 = (props: TabDashboardItem4Props) => {
  const { disabledDirect, directUrl = '#', titleHeader, onClickTab } = props;
  const navigate = useNavigate();

  const methods = useFormContext<ManufactureOrderSchema>();
  const { watch } = methods;

  const production = watch('production');
  const statusWatch = watch('status');

  const renderCls = (result?: boolean) => {
    if (enableDirectPackaging(statusWatch)) {
      if (result) return 'text_status-pass';
      return 'text_status-fail';
    }
    return '';
  };

  const renderText = (result?: boolean) => {
    if (enableDirectPackaging(statusWatch)) {
      if (result) return 'Đạt';
      return 'Không đạt';
    }
    return '';
  };

  const validateArr = (arr: { pass?: boolean }[]) => {
    const newSet = [...new Set(arr?.map(x => x.pass))];
    if (newSet?.length === 1 && newSet?.[0] === true) return true;
    return false;
  };

  return (
    <>
      <Flex direction="column" className="tab-dashboard_item4" gap={12}>
        <Flex className="tab-dashboard_item4_top" justify="space-between">
          <span className="text_title">{titleHeader}</span>
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
        <Row className="tab-dashboard_item4_bottom">
          <Col md={3} className="tab-dashboard_item4_bottom_item">
            <Flex className="tab-dashboard_item4_bottom_item_top" gap={6}>
              <img
                src={`content/images/vuesax/linear/production-dashboard-icon-7.svg`}
                alt={`icon7`}
              />
              <span className="text_title">Hoạt động nhà máy</span>
            </Flex>
            <Flex className="tab-dashboard_item4_bottom_item_bottom" gap={24}>
              <Flex direction="column">
                <span className="text_title">Lò đốt</span>{' '}
                <span
                  className={`text_status ${renderCls(
                    validateArr(production?.monitorMachineOperation?.furnace),
                  )}`}
                >
                  {renderText(
                    validateArr(production?.monitorMachineOperation?.furnace),
                  )}
                </span>
              </Flex>
              <Flex direction="column">
                <span className="text_title">Lò sấy</span>
                <span
                  className={`text_status ${renderCls(
                    production?.monitorMachineOperation?.dryingFurnace?.[0]
                      ?.pass,
                  )}`}
                >
                  {renderText(
                    production?.monitorMachineOperation?.dryingFurnace?.[0]
                      ?.pass,
                  )}
                </span>
              </Flex>
            </Flex>
          </Col>
          <Col md={4} className="tab-dashboard_item4_bottom_item">
            <Flex className="tab-dashboard_item4_bottom_item_top" gap={6}>
              <img
                src={`content/images/vuesax/linear/production-dashboard-icon-8.svg`}
                alt={`icon8`}
              />
              <span className="text_title">Giám sát hấp, sấy</span>
            </Flex>
            <Flex className="tab-dashboard_item4_bottom_item_bottom" gap={24}>
              <Flex direction="column">
                <span className="text_title">Nồi hấp</span>
                <span className={`text_status`}>
                  {enableDirectPackaging(statusWatch)
                    ? `${production?.steamDryingMonitoring?.steamer?.pressure} - ${production?.steamDryingMonitoring?.steamer?.temperature}`
                    : ''}
                </span>
              </Flex>
              <Flex direction="column">
                <span className="text_title">Bồn sấy 1</span>
                <span className={`text_status`}>
                  {enableDirectPackaging(statusWatch)
                    ? `${production?.steamDryingMonitoring?.dryer1?.pressure} - ${production?.steamDryingMonitoring?.dryer1?.temperature}`
                    : ''}
                </span>
              </Flex>
              <Flex direction="column">
                <span className="text_title">Bồn sấy 2</span>
                <span className={`text_status`}>
                  {enableDirectPackaging(statusWatch)
                    ? `${production?.steamDryingMonitoring?.dryer2?.pressure} - ${production?.steamDryingMonitoring?.dryer2?.temperature}`
                    : ''}
                </span>
              </Flex>
            </Flex>
          </Col>
          <Col md={5} className="tab-dashboard_item4_bottom_item">
            <Flex className="tab-dashboard_item4_bottom_item_top" gap={6}>
              <img
                src={`content/images/vuesax/linear/production-dashboard-icon-9.svg`}
                alt={`icon9`}
              />
              <span className="text_title">Nam châm, lưới</span>
            </Flex>
            <Flex className="tab-dashboard_item4_bottom_item_bottom" gap={24}>
              <Flex direction="column">
                <span className="text_title">Nam châm</span>
                <span
                  className={`text_status ${renderCls(
                    production?.checkMagnetGrid?.magnet?.[1]?.pass,
                  )}`}
                >
                  {renderText(production?.checkMagnetGrid?.magnet?.[1]?.pass)}
                </span>
              </Flex>
              <Flex direction="column">
                <span className="text_title">Lưới sàng 4mm</span>
                <span
                  className={`text_status ${renderCls(
                    production?.checkMagnetGrid?.floorGrid?.[1]?.pass,
                  )}`}
                >
                  {renderText(
                    production?.checkMagnetGrid?.floorGrid?.[1]?.pass,
                  )}
                </span>
              </Flex>
              <Flex direction="column">
                <span className="text_title">Lưới nghiền 3mm</span>
                <span
                  className={`text_status ${renderCls(
                    production?.checkMagnetGrid?.grindingGrid?.[1]?.pass,
                  )}`}
                >
                  {renderText(
                    production?.checkMagnetGrid?.grindingGrid?.[1]?.pass,
                  )}
                </span>
              </Flex>
            </Flex>
          </Col>
        </Row>
      </Flex>
    </>
  );
};
