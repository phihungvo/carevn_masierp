import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import TabContentItem from 'app/components/tabContent/tabContent';
import Table from 'app/components/table/table';
import Tabs from 'app/components/tabs/tabs';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import useSupplier from 'app/hooks/use-supplier';
import dayjs from 'dayjs';
import React from 'react';

interface ISupplierDetailModal {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord?: string | null;
}

const SupplierDetailModals = (props: ISupplierDetailModal) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { useGetSupplierById } = useSupplier;
  const { data: supplier } = useGetSupplierById(selectedRecord);
  return (
    <Modal isOpen={isOpen} toggle={toggle} cancel={false} ok={false} className="modals-detail-supplier" titleHeader="Chi tiết nhà cung cấp">
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item className="bold" label="Mã nhà cung cấp">
            <Typography level="text" className="fw-bolder">
              {supplier?.code}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên nhà cung cấp">
            <Typography level="text" className="fw-bolder">
              {supplier?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số điện thoại">
            <Typography level="text" className="fw-bolder">
              {supplier?.phone}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Descriptions bordered column={2}>
          <Descriptions.Item label="Địa chỉ">
            <Typography level="text" className="fw-bolder">
              {supplier?.address}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Descriptions bordered column={2}>
          <Descriptions.Item label="Địa chỉ cung cấp dịch vụ">
            <Typography level="text" className="fw-bolder">
              {supplier?.addressService}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Descriptions bordered column={2}>
          <Descriptions.Item label="Mã số thuế">
            <Typography level="text" className="fw-bolder">
              {supplier?.taxCode}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Tên viết tắt">
            <Typography level="text" className="fw-bolder">
              {supplier?.shortName}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Liên hệ">
            <Typography level="text" className="fw-bolder">
              {supplier?.contact}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Số điện thoại">
            <Typography level="text" className="fw-bolder">
              {supplier?.phone}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Email">
            <Typography level="text" className="fw-bolder">
              {supplier?.email}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Ngày sinh">
            <Typography level="text" className="fw-bolder">
              {dayjs(supplier?.birthday).format(DATE_FORMAT.DATE)}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Nhóm nhà cung cấp">
            <Typography level="text" className="fw-bolder">
              {supplier?.supplierGroup?.name}
            </Typography>
          </Descriptions.Item>
          <Descriptions.Item label="Hạn Thanh Toán">
            <Typography level="text" className="fw-bolder">
              {supplier?.paymentTermNumber}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Descriptions bordered column={2}>
          <Descriptions.Item label="Ghi chú">
            <Typography level="text" className="fw-bolder">
              {supplier?.note}
            </Typography>
          </Descriptions.Item>
        </Descriptions>
        <Tabs
          header={[
            { id: 'contract', title: 'Hợp đồng' },
            { id: 'contacts', title: 'Liên hệ' },
            { id: 'debtEmployees', title: 'Nhân viên quản lý công nợ' },
          ]}
        >
          <TabContentItem id="contract">
            <Table
              rowKey="id"
              columns={[
                { title: 'Số hợp đồng', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: 'Ngày ký', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: 'Ngày lập', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: 'Giá trị hợp đồng', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: '% VAT', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: 'Thuế VAT', dataIndex: 'name', render: (_, record) => record?.contractName },
                { title: 'Tình trạng', dataIndex: 'name', render: (_, record) => record?.status },
              ]}
              dataSource={supplier?.supplierContracts}
            />
          </TabContentItem>
          <TabContentItem id="contacts">
            <Table
              rowKey="id"
              columns={[
                { title: 'Loại', dataIndex: 'name', render: (_, record) => record?.contactType?.name },
                { title: 'Liên hệ', dataIndex: 'name', render: (_, record) => record?.contactInfo },
                { title: 'Chức vụ', dataIndex: 'name', render: (_, record) => record?.position },
                { title: 'Mobile', dataIndex: 'name', render: (_, record) => record?.phone },
                { title: 'Email', dataIndex: 'name', render: (_, record) => record?.email },
                { title: 'Ngày sinh', dataIndex: 'name', render: (_, record) => dayjs(record?.birthDate).format(DATE_FORMAT.DATE) },
              ]}
              dataSource={supplier?.contacts}
            />
          </TabContentItem>
          <TabContentItem id="debtEmployees">
            <Table
              rowKey="code"
              columns={[
                { title: 'Nhân viên', dataIndex: 'name', render: (_, record) => record?.code },
                { title: 'Tên nhân viên', dataIndex: 'name', render: (_, record) => record?.name },
              ]}
              dataSource={supplier?.debtEmployees}
            />
          </TabContentItem>
        </Tabs>
      </Flex>
    </Modal>
  );
};

export default SupplierDetailModals;
