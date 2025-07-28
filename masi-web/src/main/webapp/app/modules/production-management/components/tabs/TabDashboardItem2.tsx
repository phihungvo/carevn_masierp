import Flex from 'app/components/flex/flex';
import { useNavigate } from 'react-router';
import './style.scss';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

interface TabDashboardItem2Props {
  styleIcon?: '3' | '4';
  disabledDirect?: boolean;
  directUrl?: string;
  onClickTab?: () => void;
  titleHeader?: string;
  title?: string;
  valueTitle?: string;
  title2?: string;
  valueTitle2?: string;
}
export const TabDashboardItem2 = (props: TabDashboardItem2Props) => {
  const {
    styleIcon = '1',
    disabledDirect = false,
    directUrl = '#',
    onClickTab,
    titleHeader,
    title,
    valueTitle,
    title2,
    valueTitle2,
  } = props;
  const navigate = useNavigate();

  return (
    <>
      <Flex
        direction="column"
        className="tab-dashboard_item2"
        justify="space-between"
      >
        <Flex className="tab-dashboard_item2_top" justify="space-between">
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
        <Flex direction="row" className="tab-dashboard_item2_bottom" gap={16}>
          <Flex direction="row" className="tab-dashboard_item2_left" gap={44}>
            <img
              src={`content/images/vuesax/linear/production-dashboard-icon-${styleIcon}.svg`}
              alt={`icon${styleIcon}`}
            />
            <Flex direction="column">
              <span className="text_title">{title}</span>
              <Tooltip label={`${valueTitle}`} target={`whsName-${1}`}>
                <EllipsisParagraph
                  text={`${valueTitle}`}
                  id={`whsName-${1}`}
                  className="text_content"
                />
              </Tooltip>
            </Flex>
          </Flex>
          <Flex direction="column" className="tab-dashboard_item2_right">
            <span className="text_title">{title2}</span>
            <span className="text_content">{valueTitle2}</span>
          </Flex>
        </Flex>
      </Flex>
    </>
  );
};
