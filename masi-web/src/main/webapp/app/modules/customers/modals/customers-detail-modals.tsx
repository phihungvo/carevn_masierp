import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import dayjs from 'dayjs';
import React from 'react';

const { useGetCustomerById } = useCustomers;
const { useGetEmployeesQuery } = useEmployee;

interface ICustomersDetailModals {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const CustomersDetailModals = (props: ICustomersDetailModals) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data: employees } = useGetEmployeesQuery();
  const { data } = useGetCustomerById(selectedRecord);

  const findOwner = employees?.data?.find(employee => employee?.id === data?.customerOwner);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      ancel={false}
      ok={false}
      className="modals-detail-customers"
      titleHeader='Chi tiết khách hàng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã KH">
            <Typography level="text" className="fw-bolder">
              {data?.customerCode}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tên công ty">
            <Typography level="text" className="fw-bolder">
              {data?.companyName}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="MST">
            <Typography level="text" className="fw-bolder">
              {data?.taxCode}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Địa chỉ">
            <Typography level="text" className="fw-bolder">
              {data?.address}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin liên hệ" size="large" bordered column={2}>
          <Descriptions.Item label="Tên người liên hệ">
            <Typography level="text" className="fw-bolder">
              {(data?.lastName || '') + ' ' + (data?.firstName || '')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Chức vụ">
            <Typography level="text" className="fw-bolder">
              {data?.position}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày sinh">
            <Typography level="text" className="fw-bolder">
              {data?.birthday ? dayjs(data?.birthday).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Số điện thoại">
            <Typography level="text" className="fw-bolder">
              {data?.phoneNumber}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Email">
            <Typography level="text" className="fw-bolder">
              {data?.email}
            </Typography>
          </Descriptions.Item>


        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thông tin khác" size="large" bordered column={2}>
          <Descriptions.Item label="Người phụ trách">
            <Typography level="text" className="fw-bolder">
              {(findOwner?.lastName || '') + ' ' + (findOwner?.firstName || '')}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ngày tạo">
            <Typography level="text" className="fw-bolder">
              {data?.contractSigned ? dayjs(data?.contractSigned).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Ghi chú">
            <Typography level="text" className="fw-bolder">
              {data?.note}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Thời gian hiệu lực" size="large" bordered column={2}>
          <Descriptions.Item label="Thời gian hiệu lực từ">
            <Typography level="text" className="fw-bolder">
              {data?.contractFrom ? dayjs(data?.contractFrom).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Thời gian hiệu lực đến">
            <Typography level="text" className="fw-bolder">
              {data?.contractTo ? dayjs(data?.contractTo).format(DATE_FORMAT.DATE) : ''}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </Modal>
  );
};

export default CustomersDetailModals;
