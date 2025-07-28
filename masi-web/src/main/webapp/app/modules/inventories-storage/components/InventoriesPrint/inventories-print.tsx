import React from 'react';
import './style.scss';
import useEmployee from 'app/hooks/use-employee';
import { useGetInventoriesById } from 'app/hooks/use-inventories';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import dayjs from 'dayjs';
import { convertCurrency } from 'app/shared/util/format';
import { Checkbox } from 'antd';
import { FormGroup, Label } from 'reactstrap';
import Input from 'app/components/input/input';
import Flex from 'app/components/flex/flex';

const { useGetEmployeesQuery } = useEmployee;

interface InventoriesPrintProps {
  id: string;
}

const InventoriesPrint = ({ id }: InventoriesPrintProps) => {
  const { data } = useGetInventoriesById(id);
  const { data: employees } = useGetEmployeesQuery();

  const renderFooterItem = (empId: string, path: string, updatedAt: string) => {
    const empSelected = employees?.data?.find(x => x.id === empId);
    return (
      <div className="footer-item">
        <span className="footer-item-text">{empSelected?.workspace?.name}</span>
        <span className="footer-item-sign">
          {updatedAt ? <img src={FILE_UTIL + '/' + path} /> : ''}
        </span>
        <span className="footer-item-text">
          {empSelected?.employeeProfile?.fullName}
        </span>
        <span className="footer-item-text">
          {updatedAt
            ? `Ngày ${dayjs(updatedAt)?.format(DATE_FORMAT?.DATE)}`
            : ''}
        </span>
      </div>
    );
  };
  return (
    <section className="inventories-print-section">
      <div className="inventories-top">
        <div className="top-item-group">
          <img
            alt="logo"
            className="top-item-logo"
            src="content/images/masilogo.png"
          />
          <div className="top-item-contact">
            <span className="top-item-text-sub">Mã số tài liệu: BM.04.04</span>
            <span className="top-item-text-sub">Lần ban hành: 01</span>
            <span className="top-item-text-sub">
              Ngày ban hành: {dayjs().format(DATE_FORMAT.DATE)}
            </span>
            <span className="top-item-text-sub">Trang: 1/1</span>
          </div>
        </div>
      </div>
      <div className="inventories-header">
        <div className="header-item-center">
          <span className="header-item-title">
            PHIẾU THEO DÕI SẢN PHẨM NHẬP KHO
          </span>
        </div>
      </div>

      <div className="inventories-table">
        <div className="table-container">
          <table>
            {/* <colgroup>
              <col style={{ width: '30%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '20%' }} />
              <col style={{ width: '30%' }} />
            </colgroup> */}
            <thead>
              <tr>
                <th colSpan={3}>Tên sản phẩm:</th>
                <th colSpan={2}>Ngày sản xuất:</th>
                <th colSpan={2}>Ngày nhập kho:</th>
                <th>Ngày kiểm tra</th>
              </tr>
              <tr>
                <th>Mã lô</th>
                <th>Số lượng (kg)</th>
                <th>Ngày sử dụng</th>
                <th>SL Sử dụng</th>
                <th>SL Còn lại</th>
                <th>Mục đích</th>
                <th>Ghi chú</th>
                <th>KẾT QUẢ KIỂM TRA</th>
              </tr>
            </thead>
            <tbody>
              {data?.inventoriesDetails?.map((x, idx) => (
                <tr key={idx}>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td>
                    <Flex align="center" gap={4}>
                      <Input type="checkbox" />
                      <span className="mt-1">Đạt</span>
                    </Flex>
                    <Flex align="center" gap={4}>
                      <Input type="checkbox" />
                      <span className="mt-1">Không đạt</span>
                    </Flex>
                    <span>Chi tiết:</span>
                  </td>
                </tr>
              ))}
              <tr>
                <td
                  colSpan={7}
                  style={{
                    textAlign: 'left',
                  }}
                >
                  Ghi chú: (số pallet ?, số bao trên pallet ?)
                </td>
                <td>Nhân viên kiểm tra</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
};

export default InventoriesPrint;
