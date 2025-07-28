import { PATH } from 'app/constants/path';
import useNotification from 'app/hooks/use-notification';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { Dropdown, DropdownMenu, DropdownToggle } from 'reactstrap';
import { DateObject } from 'react-multi-date-picker';
import Badge from 'app/components/badge/badge';
import { IData } from 'app/shared/model/notification.model';
import { IQuotation } from 'app/shared/model/quotation.model';
import Flex from 'app/components/flex/flex';

const {
  useGetCountUnreadNotifications,
  useReadNotificationMutation,
  useMyNotificationsQuery,
} = useNotification;

const NotificationHeader = () => {
  const navigate = useNavigate();
  const [filter, setFilter] = useState({ page: 0, size: 5 });
  const [dataNotifications, setDataNotifications] = useState([]);
  const [isEnableNotification, setIsEnableNotification] = useState(false);
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const { data } = useMyNotificationsQuery(filter, isEnableNotification);
  const { data: countUnread } = useGetCountUnreadNotifications();
  const { mutate: readNotification } = useReadNotificationMutation();

  const menuRef = React.useRef<any>(null);

  const handleNavigate = (id: string, data: IData, entityId: string) => {
    const { action, entity, fromDate, toDate, customerId } = data;
    readNotification(id);
    setDataNotifications(
      dataNotifications.map(item =>
        item.id === id ? { ...item, read: true } : item,
      ),
    );

    if (action === 'BirthdayNotification') {
      const currentDate = new Date();
      const currentMonth = currentDate.getMonth() + 1;
      const filteredMonth = new DateObject(toDate).month.number;

      if (currentMonth === filteredMonth) currentDate.setUTCDate(1);
      const birthdayFrom: string =
        currentMonth === filteredMonth
          ? currentDate.toISOString().split('T')[0]
          : fromDate;
      navigate(
        `${PATH.CUSTOMERS}?birthdayFrom=${birthdayFrom}&birthdayTo=${toDate}`,
      );
    }

    if (action === 'RecruitmentRequestUpdated') {
      navigate(`${PATH.RECRUITMENT}?id=${entityId}`);
    }

    if (action === 'INTERNAL_APPROVED_QUOTATION') {
      const { id, company } = entity as IQuotation;
      if (company === 'KIM_LONG') {
        navigate(`${PATH.PRICE_LIST_DETAIL_KIM_LONG}`?.replace(':id', id));
      } else {
        navigate(`${PATH.PRICE_LIST_DETAIL_MMS}`?.replace(':id', id));
      }
    }

    if (action === 'ChangeCustomerOwner')
      navigate(`${PATH.CUSTOMERS}/?customerId=${customerId}`);

    toggle();
  };

  const toggle = () => setDropdownOpen(prevState => !prevState);

  const isFetchMore = filter?.page * filter?.size < data?.totalRecord;

  const handleFetchNextPage = () => {
    if (isFetchMore) {
      setFilter({ ...filter, page: filter.page + 1 });
    }
  };

  useEffect(() => {
    if (data) {
      setDataNotifications(prev => [...prev, ...data?.data]);
    }
  }, [data]);

  return (
    <Dropdown
      isOpen={dropdownOpen}
      toggle={toggle}
      nav
      inNavbar
      id={'notification-dropdown'}
    >
      <Badge
        onClick={() => {
          toggle();
          setIsEnableNotification(true);
        }}
        color="error"
        dot={false}
        className="position-absolute pointer left-0"
        children={countUnread}
      />
      <DropdownToggle
        onClick={() => setIsEnableNotification(true)}
        nav
        className="d-flex align-items-center"
      >
        <img
          src="content/images/vuesax/bulk/notification.svg"
          className="notification-icon"
          alt="notification"
        />
      </DropdownToggle>
      <DropdownMenu end>
        <div ref={menuRef} className="d-flex flex-column notification-style">
          <h5 className='notification-header' style={{ padding: '0 10px' }}>Thông báo</h5>
          {dataNotifications?.map(item => (
            <Flex
              key={item?.id}
              align="center"
              justify="space-between"
              className={`notification-item-wrapper ${
                item?.read ? 'read' : 'unread'
              } pointer`}
            >
              <div
                className={`notification-item`}
                onClick={() =>
                  handleNavigate(
                    item?.id,
                    item?.notification?.data,
                    item?.notification?.entityId,
                  )
                }
              >
                <span className="mr-2 notification-item-header">
                  {item?.notification?.title}
                </span>
                <div>{item?.notification?.content}</div>
              </div>
              {!item?.read && <div className="dot dot-primary" />}
            </Flex>
          ))}
          {isFetchMore && (
            <div
              className="notification-load-more"
              onClick={handleFetchNextPage}
            >
              Tải thêm
            </div>
          )}
        </div>
      </DropdownMenu>
    </Dropdown>
  );
};

export default NotificationHeader;
