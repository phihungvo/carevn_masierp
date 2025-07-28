import Flex from 'app/components/flex/flex';
import { ReactNode } from 'react';
import { useNavigate } from 'react-router';
import { IconFail, IconPass } from '../icons';
import './style.scss';

interface TabDashboardItemProps {
  styleIcon?: '1' | '2';
  disabledDirect?: boolean;
  directUrl?: string;
  onClickTab?: () => void;
  title?: string;
  valueTitle?: string;
  topRightChildren: ReactNode;
  status?: 'PASS' | 'FAIL' | '';
}
export const TabDashboardItem = (props: TabDashboardItemProps) => {
  const {
    styleIcon = '1',
    disabledDirect = false,
    directUrl = '#',
    onClickTab,
    title,
    valueTitle,
    topRightChildren,
    status = '',
  } = props;
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="row" className="tab-dashboard_item">
        <Flex direction="row" className="tab-dashboard_item_left" gap={16}>
          <img
            src={`content/images/vuesax/linear/production-dashboard-icon-${styleIcon}.svg`}
            alt={`icon${styleIcon}`}
          />
          <Flex direction="column">
            <span className="text_title">{title}</span>
            <span className="text_content">{valueTitle}</span>
          </Flex>
        </Flex>
        <Flex direction="column" className="tab-dashboard_item_right" gap={8}>
          <span className="text_title">
            {status === 'PASS' && (
              <span className="pass">
                {IconPass()} {topRightChildren}
              </span>
            )}
            {status === 'FAIL' && (
              <span className="fail">
                {IconFail()} {topRightChildren}
              </span>
            )}
            {status === '' && <span>{topRightChildren}</span>}
          </span>
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
    </>
  );
};
